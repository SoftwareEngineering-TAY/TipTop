package com.example.tiptop.Database;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.Objects.User;
import com.example.tiptop.Points.PointsParentActivity;
import com.example.tiptop.PoolTasks.TaskInfoParentActivity;
import com.example.tiptop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Date;
import java.util.HashMap;

import static java.time.temporal.ChronoUnit.DAYS;

public class Database {

    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static DatabaseReference reference = db.getReference();
    private static String userID;
    private static String currFamilyId;
    private static String permission;
    private static String TitleSpinnerOfBelongChild;
    private static long currPoint;

    public static final String USERS_ROOT = "Users", FAMILIES_ROOT = "Families", TASKS_ROOT = "Tasks", USERRFAMILIES_ROOT = "UserFamilies";

    public static boolean setStatus(String taskID, String Status) {
        try {
            reference.child("Tasks").child(currFamilyId).child(taskID).child("status").setValue(Status);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean setConfirmedDate(String taskID) {
        try {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH)+1;
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            String zeroMonth = "";
            String zeroDay = "";
            if (month<10) zeroMonth = "0";
            if (day < 10) zeroDay = "0";
            String currentDate = "" + year + "-" + zeroMonth + month + "-" +zeroDay + day;
            reference.child("Tasks").child(currFamilyId).child(taskID).child("confirmedDate").setValue(currentDate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean setbelongsToUID(String taskID, String uid) {
        try {
            if (uid != null)
                reference.child("Tasks").child(currFamilyId).child(taskID).child("status").setValue(uid);
            else
                reference.child("Tasks").child(currFamilyId).child(taskID).child("belongsToUID").removeValue();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean setFamilyName(String key, String familyName) {
        try {
            reference.child("Families").child(key).child("Family name").setValue(familyName);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean setUserToFamily(String familyKey, String userName) {
        try {
            reference.child("Families").child(familyKey).child(userID).setValue(userName);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean setUserToUserFamily(String familyKey, String familyName) {
        try {
            reference.child("UserFamilies").child(userID).child(familyKey).setValue(familyName);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean setUser(User userToAdd) {
        try {
            reference.child("Users").child(userID).setValue(userToAdd);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean updateTaskListFromDB(ArrayList<Task> toUpdate, ArrayList<String> idToUpdate, String status, ArrayAdapter<Task> mTaskListAdapter) {
        try {
            reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    toUpdate.clear();
                    idToUpdate.clear();
                    for (DataSnapshot Snapshot : snapshot.getChildren()) {
                        if (permission.equals("Parent") && Snapshot.child("status").getValue().equals(status)) {
                            Task toAdd = Snapshot.getValue(Task.class);
                            toUpdate.add(toAdd);
                            idToUpdate.add(Snapshot.getKey());
                        } else if (permission.equals("Child") && Snapshot.child("belongsToUID").getValue() != null &&
                                Snapshot.child("belongsToUID").getValue().equals(userID) && Snapshot.child("status").getValue().equals(status)) {
                            Task toAdd = Snapshot.getValue(Task.class);
                            toUpdate.add(toAdd);
                            idToUpdate.add(Snapshot.getKey());
                        }
                    }
                    mTaskListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean updateExpandableTaskListFromDB(ArrayList<String> ListChildForTask, HashMap<String, ArrayList<Task>> ListTaskGroups, HashMap<String, ArrayList<String>> ListTaskID, String status, TaskToChildExtendListAdapter childAdapter, int days, boolean endOrConfirmed) {
        try {
            reference.child("Families").child(currFamilyId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ListChildForTask.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String toAddChildren = (String) ds.getValue();
                        reference.child("Users").child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child("type").getValue() != null && snapshot.child("type").getValue().toString().equals("Child")) {
                                    ListChildForTask.add(toAddChildren);
                                    ArrayList<Task> toAdd = new ArrayList<>();
                                    ArrayList<String> toAddID = new ArrayList<>();
                                    reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            toAdd.clear();
                                            toAddID.clear();
                                            for (DataSnapshot ds1 : snapshot.getChildren()) {
                                                if (ds1.child("belongsToUID").getValue() != null && ds1.child("belongsToUID").getValue().equals(ds.getKey()) && ds1.child("status").getValue().equals(status)) {
                                                    String toCalc = endOrConfirmed? "confirmedDate" : "endDate";
                                                    long Days = DAYS.between(LocalDate.now(),LocalDate.parse(ds1.child(toCalc).getValue(String.class)));
                                                    Log.v("DAYS!!!!!!: ", String.valueOf(Days));
                                                    if (days >= Math.abs(Days)){
                                                        Task taskToAdd = ds1.getValue(Task.class);
                                                        toAdd.add(taskToAdd);
                                                        toAddID.add(ds1.getKey());
                                                    }
                                                }
                                            }
                                            ListTaskGroups.put(toAddChildren, toAdd);
                                            ListTaskID.put(toAddChildren, toAddID);
                                            System.out.println("ListChildForTask1" + ListChildForTask);
                                            System.out.println("ListTaskGroups1" + ListTaskGroups);
                                            childAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean initializationCurrFamilyIdAndPermission(ImageButton imageButton, Context context) {
        userID = mAuth.getCurrentUser().getUid();
        try {
            reference.child("Users").child(userID).child("currFamilyId").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currFamilyId = (String) snapshot.getValue();
                    updateHomePicture(imageButton,context);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference.child("Users").child(userID).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    permission = (String) snapshot.getValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
//            reference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Log.v("onDataChange", "**************");
//                    for (DataSnapshot ds : snapshot.getChildren()) {
//                        Log.v("onFor", "**************");
//                        if (ds.getKey().equals("currFamilyId")) {
//                            currFamilyId = (String) ds.getValue();
//                        }
//                        if (ds.getKey().equals("type")) {
//                            permission = (String) ds.getValue();
//                            Log.v("permision: ", permission);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean setCurrFamilyId(String CurrFamilyId) {
        try {
            currFamilyId = CurrFamilyId;
            reference.child("Users").child(userID).child("currFamilyId").setValue(CurrFamilyId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getCurrFamilyId() {
        return currFamilyId;
    }

    public static String getPermission() {
        return permission;
    }

    public static void createUserInFireBase(User user_to_add, String familyId, Uri uri, Bitmap bitmap) {
        String key = reference.child("Families").push().getKey();
        user_to_add.setCurrFamilyId(key);
        mAuth.createUserWithEmailAndPassword(user_to_add.getEmail(), user_to_add.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userID = mAuth.getCurrentUser().getUid();
                    setFamilyName(key, familyId);
                    setUserToFamily(key, user_to_add.getName());
                    setUserToUserFamily(key, familyId);
                    setUser(user_to_add);
                    uploadImage(key, uri, bitmap);
                }
            }
        });
    }

    public static void uploadImage(String family_key, Uri uri_image, Bitmap bitmap_image) {
        String path = "Families/" + family_key;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = storage.getReference(path);
        if (uri_image != null) {
            mStorageRef.putFile(uri_image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                    } else {
                    }
                }
            });
        } else if (bitmap_image != null) {
            ByteArrayOutputStream to_stream = new ByteArrayOutputStream();
            bitmap_image.compress(Bitmap.CompressFormat.JPEG, 100, to_stream);
            byte bytes[] = to_stream.toByteArray();
            mStorageRef.putBytes(bytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                    } else {

                    }
                }
            });
        }
    }

    public static void updateListOfChildFromDB(ArrayList<String> allKeys, ArrayList<String> allKids, ArrayAdapter adapter) {
        reference.child("Families").child(currFamilyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allKids.clear();
                allKeys.clear();
                allKids.add("Not Associated");
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String toAddChildren = (String) ds.getValue();
                    String toAddKey = (String) ds.getKey();
                    reference.child("Users").child(toAddKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.child("type").getValue() != null && snapshot.child("type").getValue().toString().equals("Child")) {
                                allKeys.add(toAddKey);
                                allKids.add(toAddChildren);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void addTaskToDB(String key, Task toAddTask) {
        reference.child("Tasks").child(currFamilyId).child(key).setValue(toAddTask);
    }

    public static String getKeyForNewTask() {
        return reference.child("Tasks").child(currFamilyId).push().getKey();
    }

    public static boolean setTaskDesctiption(String taskID, String descriptionToUpdate) {
        try {
            reference.child("Tasks").child(currFamilyId).child(taskID).child("description").setValue(descriptionToUpdate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean setTaskName(String taskID, String nameToUpdate) {
        try {
            reference.child("Tasks").child(currFamilyId).child(taskID).child("nameTask").setValue(nameToUpdate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean setTaskBonus(String taskID, int bonusToUpdate) {
        try {
            reference.child("Tasks").child(currFamilyId).child(taskID).child("bonusScore").setValue(bonusToUpdate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getAndSetTitleSpinnerOfBelongChild(String BelongsToUID) {
        reference.child("Users").child(BelongsToUID).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TitleSpinnerOfBelongChild = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return TitleSpinnerOfBelongChild;
    }

    public static void updateListOfFamilyFromDB(ArrayList<String> allKeys, ArrayList<String> allFamilies, ArrayAdapter adapter) {
        //Updating the spinner
        Log.d("user", "updateListOfFamilyFromDB: userID " + userID);
        reference.child("UserFamilies").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allFamilies.clear();
                allKeys.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String toAddFamiliy = (String) ds.getValue();
                    String toAddKey = ds.getKey();
                    allFamilies.add(toAddFamiliy);
                    allKeys.add(toAddKey);
                }
                Log.d("database:", "onDataChange:families: " + allFamilies);
                Log.d("database:", "onDataChange:families: " + allKeys);

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void setScreenViewByUser(TextView name, TextView email) {
        reference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key;
                String value;

                for (DataSnapshot ds : snapshot.getChildren()) {

                    key = ds.getKey();
                    value = ds.getValue().toString();

                    if (key.equals("name")) {
                        name.setText(value);
                    } else if (email != null && key.equals("email")) {
                        email.setText(value);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void setNumOfPoints(TextView numOfPoints, ArrayList<Integer>sumOfPoints) {
        reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    if (ds.child("belongsToUID").getValue().equals(userID) && ds.child("status").getValue().equals("Confirmed")) {
                        sumOfPoints.set(0,sumOfPoints.get(0)+Integer.parseInt(ds.child("bonusScore").getValue().toString()));
                    }
                }
                numOfPoints.setText(String.valueOf(sumOfPoints.get(0)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void addPointsToChild (Task conformedTask){
        reference.child("Users").child(conformedTask.getBelongsToUID()).child("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currPoint = snapshot.getValue(long.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        currPoint+=conformedTask.getBonusScore();
        reference.child("Users").child(conformedTask.getBelongsToUID()).child("points").setValue(currPoint);

    }

    public static void getPoints (TextView numOfPoints){
        reference.child("Users").child(userID).child("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numOfPoints.setText(String.valueOf(snapshot.getValue(long.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void setNamesAndScores(PointsParentActivity act){

        reference.child("Families").child(currFamilyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    key = ds.getKey();
                    if(!key.equals("Family name")){
                        reference.child("Users").child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                Log.v("user**********",user.toString());
                                if(user.getType().equals("Child")){
                                    act.setChildName(user.getName(), user.getPoints());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void updateHomePicture(ImageButton imageButton, Context context) {

        String path = "Families/" + getCurrFamilyId();

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
                    imageButton.setImageResource(R.drawable.new_family);
                }
            }
        });
    }

    public static void logout(){
        mAuth.signOut();
    }
}
