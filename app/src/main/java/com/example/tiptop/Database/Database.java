package com.example.tiptop.Database;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Adapters.ChatAdapter;
import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.LogInAndSignUp.HomeActivity;
import com.example.tiptop.LogInAndSignUp.LoginActivity;
import com.example.tiptop.Objects.Message;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.Objects.User;
import com.example.tiptop.Points.PointsParentActivity;
import com.example.tiptop.R;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import static java.lang.System.currentTimeMillis;
import static java.time.temporal.ChronoUnit.DAYS;

public class Database extends AppCompatActivity implements ValueEventListener {

    private static DataSnapshot dataSnapshot;
    private static ArrayList<DataChangeListener> listeners;
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static String userID;
    private static String currFamilyId;
    private static String routeType;
    private static String permission;
    private static final int SPLASH_SCREEN = 1000;

    private static final DatabaseReference.CompletionListener completionListener = new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
//            if (error != null) {
//
//            } else {
//            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logo);
        reference.addValueEventListener(this);
        listeners = new ArrayList<>();

        /**
         * Loading the logo page
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent go_to_login = new Intent(Database.this, LoginActivity.class);
                startActivity(go_to_login);
                finish();
            }
        },SPLASH_SCREEN);

    }

    /**
     * Notify when change was made in the DB
     */
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        dataSnapshot = snapshot;
        notifyAllListeners();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    /**
     * Menage the list of listeners fot notify
     */
    public static void addListener(DataChangeListener listener){
        listeners.add(listener);
    }

    public static boolean removeListener(DataChangeListener listener){
        return listeners.remove(listener);
    }

    /**
     * notify the listeners by calling theirs notifyOnChange() method
     */
    private static void notifyAllListeners(){
        for (DataChangeListener listener : listeners) {
            listener.notifyOnChange();
        }
    }

    /**
     * initialize User Settings
     */
    public static void initializationUserId() {
        userID = mAuth.getCurrentUser().getUid();
    }

    public static void initializationCurrFamilyId() {
        currFamilyId = dataSnapshot.child("Users").child(userID).child("currFamilyId").getValue(String.class);
    }

    public static void initializationPermission() {
        permission = dataSnapshot.child("Users").child(userID).child("type").getValue(String.class);
    }

    public static void initializationRouteType() {
        routeType = dataSnapshot.child("Families").child(currFamilyId).child("Route Type").getValue(String.class);
    }

    /**
     * Getters - from DB
     */
    public static String getUserID() {
        return userID;
    }

    public static String getCurrFamilyId() {
        return currFamilyId;
    }

    public static String getPermission() {
        return permission;
    }

    public static String getName(){
        return dataSnapshot.child("Users").child(userID).child("name").getValue(String.class);
    }

    public static String getEmail() {
        return dataSnapshot.child("Users").child(userID).child("email").getValue(String.class);
    }

    public static String getFamilyName(){
        return dataSnapshot.child("Families").child(currFamilyId).child("Family name").getValue(String.class);
    }

    public static String getRouteType() {
        return routeType;
    }

    public static String getKeyForNewTask() {
        return reference.child("Tasks").child(currFamilyId).push().getKey();
    }

    public static String getKeyForNewMessage() {
        return reference.child("Chats").child(currFamilyId).push().getKey();
    }

    public static long getPoints (){
        return dataSnapshot.child("Users").child(userID).child("points").getValue(long.class);
    }

    public static ArrayList<User> getListOfChilds(){
        ArrayList<User> toReturn = new ArrayList<>();
        String key;
        Iterable<DataSnapshot> UsersInFamily = dataSnapshot.child("Families").child(currFamilyId).getChildren();
        for (DataSnapshot User : UsersInFamily) {
            key = User.getKey();
            if (!key.equals("Family name")&&!key.equals("Route Type")) {
                User user = dataSnapshot.child("Users").child(key).getValue(User.class);
                if (user.getType().equals("Child")) {
                    toReturn.add(user);
                }
            }
        }
        return toReturn;
    }

    public static void getScreenViewByFamily(Context context, ListView circle_members, TextView family_members){
        ArrayList<String> membersArray = new ArrayList<>();
        ArrayAdapter membersAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, membersArray);
        circle_members.setAdapter(membersAdapter);
        String key;
        String value;
        Iterable<DataSnapshot> UsersInFamily = dataSnapshot.child("Families").child(currFamilyId).getChildren();
        for (DataSnapshot User : UsersInFamily) {
            key = User.getKey();
            value = User.getValue(String.class);
            if (key.equals("Family name")){
                family_members.setText(value+"'s members");
            }
            else if(!key.equals("Route Type")){
                membersArray.add(value);
            }
        }
        membersAdapter.notifyDataSetChanged();
    }

    /**
     * Setters - to DB
     */
    public static void setStatus(String taskID, String Status) {
        reference.child("Tasks").child(currFamilyId).child(taskID).child("status").setValue(Status);
    }

    public static void setConfirmedDate(String taskID) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String zeroMonth = "";
        String zeroDay = "";
        if (month<10) zeroMonth = "0";
        if (day < 10) zeroDay = "0";
        String currentDate = "" + year + "-" + zeroMonth + month + "-" +zeroDay + day;
        reference.child("Tasks").child(currFamilyId).child(taskID).child("confirmedDate").setValue(currentDate);
    }

    public static void setbelongsToUID(String taskID, String uid) {
        if (uid != null)
            reference.child("Tasks").child(currFamilyId).child(taskID).child("belongsToUID").setValue(uid);
        else
            reference.child("Tasks").child(currFamilyId).child(taskID).child("belongsToUID").removeValue();
    }

    public static void createUserInFireBase(User user_to_add, String familyName,String routeType, Uri uri, Bitmap bitmap , String familyID) {
        String key;
        if (familyID.equals(null)) key = reference.child("Families").push().getKey();
        else key = familyID;
        user_to_add.setCurrFamilyId(key);
        mAuth.createUserWithEmailAndPassword(user_to_add.getEmail(), user_to_add.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (familyID.equals(null)) userID = task.getResult().getUser().getUid();
                    setFamilyName(key, familyName);
                    setRouteType(key, routeType);
                    setUserToFamily(key, user_to_add.getName());
                    setUserToUserFamily(key, familyName);
                    setUser(user_to_add);
                    if (!uri.equals(null)) uploadImage(key, uri, bitmap,"Families");
                }
            }
        });
    }

    public static void setFamilyName(String key, String familyName) {
        reference.child("Families").child(key).child("Family name").setValue(familyName);
    }

    public static void setRouteType(String key, String routeType) {
        reference.child("Families").child(key).child("Route Type").setValue(routeType);
    }

    public static void setUserToFamily(String familyKey, String userName) {
        reference.child("Families").child(familyKey).child(userID).setValue(userName);
    }

    public static void setUserToUserFamily(String familyKey, String familyName) {
        reference.child("UserFamilies").child(userID).child(familyKey).setValue(familyName);
    }

    public static void setUser(User userToAdd) {
        reference.child("Users").child(userID).setValue(userToAdd);
    }

    public static void setCurrFamilyId(String CurrFamilyId) {
        currFamilyId = CurrFamilyId;
        reference.child("Users").child(userID).child("currFamilyId").setValue(CurrFamilyId);
    }

    public static void addTaskToDB(String key, Task toAddTask) {
        reference.child("Tasks").child(currFamilyId).child(key).setValue(toAddTask);
    }

    public static void setTaskDesctiption(String taskID, String descriptionToUpdate) {
        reference.child("Tasks").child(currFamilyId).child(taskID).child("description").setValue(descriptionToUpdate);
    }

    public static void setTaskComment(String taskID, String commentToUpdate) {
        reference.child("Tasks").child(currFamilyId).child(taskID).child("comment").setValue(commentToUpdate);
    }

    public static void setTaskName(String taskID, String nameToUpdate) {
        reference.child("Tasks").child(currFamilyId).child(taskID).child("nameTask").setValue(nameToUpdate);
    }

    public static void setTaskBonus(String taskID, int bonusToUpdate) {
        reference.child("Tasks").child(currFamilyId).child(taskID).child("bonusScore").setValue(bonusToUpdate);
    }

    public static void addPointsToChild (Task conformedTask){
        long currPoint = dataSnapshot.child("Users").child(conformedTask.getBelongsToUID()).child("points").getValue(long.class) + conformedTask.getBonusScore();
        reference.child("Users").child(conformedTask.getBelongsToUID()).child("points").setValue(currPoint);
    }

    public static void sendMessage(String texkMsg) {
        Message toAdd =new Message(getName(),userID,texkMsg, currentTimeMillis());
        String key = getKeyForNewMessage();
        reference.child("Chats").child(currFamilyId).child(key).setValue(toAdd);
    }



    /**
     * Update Lists - from DB
     */
    public static void updateListOfChildFromDB(ArrayList<String> allKeys, ArrayList<String> allKids, ArrayAdapter adapter) {
        allKids.clear();
        allKeys.clear();
        allKids.add("Not Associated");
        Iterable<DataSnapshot> UsersInFamily = dataSnapshot.child("Families").child(currFamilyId).getChildren();
        for (DataSnapshot User : UsersInFamily) {
            String toAddChildren = (String) User.getValue();
            String toAddKey = (String) User.getKey();
            if (!User.getKey().equals("Family name")&&!User.getKey().equals("Route Type")) {
                if (dataSnapshot.child("Users").child(User.getKey()).child("type").getValue().toString().equals("Child")) {
                    allKeys.add(toAddKey);
                    allKids.add(toAddChildren);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    public static void updateListOfFamilyFromDB(ArrayList<String> allKeys, ArrayList<String> allFamilies, ArrayAdapter adapter) {
        allFamilies.clear();
        allKeys.clear();
        Iterable<DataSnapshot> FamilysOfUsers = dataSnapshot.child("UserFamilies").child(userID).getChildren();
        for(DataSnapshot Family :FamilysOfUsers){
            allFamilies.add((String) Family.getValue());
            allKeys.add(Family.getKey());
        }
        int pos = allKeys.indexOf(currFamilyId);
        if (pos > 0) {
            String Family = allFamilies.get(pos);
            allFamilies.remove(Family);
            allFamilies.add(0, Family);
            allKeys.remove(currFamilyId);
            allKeys.add(0, currFamilyId);
        }
        adapter.notifyDataSetChanged();
    }

    public static void updateChatListFromDB(ArrayList<Message> messages, ChatAdapter mAdapter){
        messages.clear();
        Iterable<DataSnapshot> Messages = dataSnapshot.child("Chats").child(currFamilyId).getChildren();
        for(DataSnapshot Msg : Messages){
            Message toAdd = Msg.getValue(Message.class);
            messages.add(toAdd);
        }
        mAdapter.notifyDataSetChanged();
    }

    //for the statistics
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void initializeArraysFromDB(ArrayList<BarEntry> visitors, ArrayList<PieEntry> visitors2) {
        visitors.clear();
        visitors2.clear();
        if(permission.equals("Parent")){
            int countChild =0;
            Iterable<DataSnapshot> UsersInFamily = dataSnapshot.child("Families").child(currFamilyId).getChildren();
            for (DataSnapshot User : UsersInFamily) {
                if (!User.getKey().equals("Family name") && !User.getKey().equals("Route Type")) {
                    String toAddChildren = (String) User.getValue();
                    String toAddKey = (String) User.getKey();
                    if (dataSnapshot.child("Users").child(User.getKey()).child("type").getValue().toString().equals("Child")) {
                        countChild++;
                        int sumTimes=0;
                        int countTasks=0;
                        Iterable<DataSnapshot> Tasks = dataSnapshot.child("Tasks").child(currFamilyId).getChildren();
                        for (DataSnapshot Task : Tasks) {
                            if (Task.child("status").getValue().equals("Confirmed") && Task.child("belongsToUID").getValue().equals(toAddKey)) {
                                Task toAdd = Task.getValue(Task.class);
                                countTasks++;
                                sumTimes += LocalDate.parse(toAdd.getStartDate()).until(LocalDate.parse(toAdd.getConfirmedDate()),DAYS);

                            }
                        }
                        visitors.add(new BarEntry(countChild,countTasks));
                        visitors2.add(new PieEntry(sumTimes/countTasks,toAddChildren));
                    }
                }
            }
        }
        else{
            Iterable<DataSnapshot> Tasks = dataSnapshot.child("Tasks").child(currFamilyId).getChildren();
            int CountTask = 0;
            int Time=0;
            for (DataSnapshot Task : Tasks) {
                if (Task.child("status").getValue().equals("Confirmed") && Task.child("belongsToUID").getValue().equals(userID)) {
                    Task toAdd = Task.getValue(Task.class);
                    Time = (int) LocalDate.parse(toAdd.getStartDate()).until(LocalDate.parse(toAdd.getConfirmedDate()),DAYS);
                    visitors.add(new BarEntry(++CountTask,toAdd.getBonusScore()));
                    visitors2.add(new PieEntry(Time,toAdd.getNameTask()));
                }
            }
        }
    }

    public static void updateTaskListFromDB(ArrayList<Task> toUpdate, ArrayList<String> idToUpdate, String status, ArrayAdapter<Task> mTaskListAdapter) {
        toUpdate.clear();
        idToUpdate.clear();
        Iterable<DataSnapshot> Tasks = dataSnapshot.child("Tasks").child(currFamilyId).getChildren();
        for (DataSnapshot Task : Tasks){
            if (permission.equals("Parent") && Task.child("status").getValue().equals(status)) {
                Task toAdd = Task.getValue(Task.class);
                toUpdate.add(toAdd);
                idToUpdate.add(Task.getKey());
            } else if (permission.equals("Child") && Task.child("belongsToUID").getValue() != null &&
                    Task.child("belongsToUID").getValue().equals(userID) && Task.child("status").getValue().equals(status)) {
                Task toAdd = Task.getValue(Task.class);
                toUpdate.add(toAdd);
                idToUpdate.add(Task.getKey());
            }
        }
        mTaskListAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void updateExpandableTaskListFromDB(ArrayList<String> ListChildForTask, HashMap<String, ArrayList<Task>> ListTaskGroups, HashMap<String, ArrayList<String>> ListTaskID, String status, TaskToChildExtendListAdapter childAdapter, int days, boolean endOrConfirmed) {
        ListChildForTask.clear();
        Iterable<DataSnapshot> UsersInFamily = dataSnapshot.child("Families").child(currFamilyId).getChildren();
        for (DataSnapshot User : UsersInFamily) {
            String toAddChildren = (String) User.getValue();
            if (!User.getKey().equals("Family name")&&!User.getKey().equals("Route Type")) {
                if (dataSnapshot.child("Users").child(User.getKey()).child("type").getValue().toString().equals("Child")) {
                    ListChildForTask.add(toAddChildren);
                    ArrayList<Task> toAdd = new ArrayList<>();
                    ArrayList<String> toAddID = new ArrayList<>();
                    Iterable<DataSnapshot> Tasks = dataSnapshot.child("Tasks").child(currFamilyId).getChildren();
                    for (DataSnapshot Task : Tasks) {
                        if (Task.child("belongsToUID").getValue() != null && Task.child("belongsToUID").getValue().equals(User.getKey()) && Task.child("status").getValue().equals(status)) {
                            String toCalc = endOrConfirmed ? "confirmedDate" : "endDate";
                            long Days = DAYS.between(LocalDate.now(), LocalDate.parse(Task.child(toCalc).getValue(String.class)));
                            if (days >= Math.abs(Days)) {
                                Task taskToAdd = Task.getValue(Task.class);
                                toAdd.add(taskToAdd);
                                toAddID.add(Task.getKey());
                            }
                        }
                    }
                    ListTaskGroups.put(toAddChildren, toAdd);
                    ListTaskID.put(toAddChildren, toAddID);
                    childAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * deal with Images
     */
    public static void uploadImage(String family_key, Uri uri_image, Bitmap bitmap_image, String folder) {
        String path =folder+"/" + family_key;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = storage.getReference(path);
        if (uri_image != null) {
            mStorageRef.putFile(uri_image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> task) {
//                    if (task.isSuccessful()) {
//                    } else {
//                    }
                }
            });
        } else if (bitmap_image != null) {
            ByteArrayOutputStream to_stream = new ByteArrayOutputStream();
            bitmap_image.compress(Bitmap.CompressFormat.JPEG, 100, to_stream);
            byte[] bytes = to_stream.toByteArray();
            mStorageRef.putBytes(bytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> task) {
//                    if (task.isSuccessful()) {
//                    } else {
//                    }
                }
            });
        }
    }

    public static void updatePicture(ImageButton imageButton, Context context,String key,String folder) {

        String path = folder+"/" + key;

        storage.getReference(path).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri imageUri = task.getResult();
                    Picasso.get()
                            .load(imageUri)
                            .fit()
                            .centerCrop()
                            .into(imageButton);
                }
                else {
                    if(folder.equals("Families"))
                    {
                        imageButton.setImageResource(R.drawable.new_family);
                    }
                }
            }
        });
    }

    public static void updateImageView(ImageView imageView, Context context,String key,String folder) {

        String path = folder+"/" + key;

        storage.getReference(path).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri imageUri = task.getResult();
                    Picasso.get()
                            .load(imageUri)
                            .fit()
                            .centerCrop()
                            .into(imageView);

                }
                else {
                    Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Create the switches to change permissions to family members
     */
    public static void createSwitchForEveryUser(Context context, LinearLayout parentSwitch ,LinearLayout childSwitch){
        String key;
        Iterable<DataSnapshot> UsersInFamily = dataSnapshot.child("Families").child(currFamilyId).getChildren();
        for (DataSnapshot User : UsersInFamily) {
            key = User.getKey();
            if (!key.equals("Family name")&&!key.equals("Route Type")) {
                User user = dataSnapshot.child("Users").child(key).getValue(User.class);

                Switch user_switch=new Switch(context);
                user_switch.setGravity(Gravity.LEFT);
                user_switch.setText(user.getName());

                if(user.getType().equals("Parent")){
                    user_switch.setChecked(true);
                    parentSwitch.addView(user_switch);
                }
                else if(user.getType().equals("Child")){
                    user_switch.setChecked(false);
                    childSwitch.addView(user_switch);
                }
                DatabaseReference childReference = reference.child("Users").child(key);
                user_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(user.getType().equals("Child")) {
                            childSwitch.removeView(user_switch);
                            parentSwitch.addView(user_switch);
                            user.setType("Parent");
                            childReference.child("type").setValue("Parent");
                        }
                        else if(user.getType().equals("Parent")){
                            parentSwitch.removeView(user_switch);
                            childSwitch.addView(user_switch);
                            user.setType("Child");
                            childReference.child("type").setValue("Child");
                        }
                    }
                });
            }
        }
    }

    /**
     * Login & Logout
     */
    public static void login(String mail, String pass, View v , Context context){
        mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent go_home_screen = new Intent(v.getContext(), HomeActivity.class);
                    context.startActivity(go_home_screen);
                }
                else {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                    dlgAlert.setCancelable(true);
                    dlgAlert.setMessage("Wrong password or email");
                    dlgAlert.setTitle("Error Message");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                        }
                    });
                }
            }
        });
    }

    public static void logout(){
        mAuth.signOut();
    }
}
