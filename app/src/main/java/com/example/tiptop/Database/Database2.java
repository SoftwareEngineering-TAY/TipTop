package com.example.tiptop.Database;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Database2  extends android.app.Application implements ValueEventListener {

    private static DataSnapshot dataSnapshot;
    private static ArrayList<DataChangeListener> listeners;
    private static DatabaseReference reference = FirebaseDatabase.getInstance().getReference();;
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static String userID = mAuth.getCurrentUser().getUid();
    private static String currFamilyId;
    private static String permission;
    private static String TitleSpinnerOfBelongChild;


    @Override
    public void onCreate() {
        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        reference.addValueEventListener(this);
        listeners = new ArrayList<>();

        super.onCreate();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        dataSnapshot = snapshot;
        notifyAllListeners();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    public static void addListener(DataChangeListener listener){
        listeners.add(listener);
    }

    public static boolean removeListener(DataChangeListener listener){
        return listeners.remove(listener);
    }

    private static void notifyAllListeners(){
        for (DataChangeListener listener : listeners) {
            listener.notifyOnChange();
        }
    }



    public static String getCurrFamilyId() {
        return currFamilyId;
    }

    public static String getPermission() {
        return permission;
    }

    public static String getKeyForNewTask() {
        return reference.child("Tasks").child(currFamilyId).push().getKey();
    }

    public static void getPoints (long []sum ){
        sum[0] = (long)dataSnapshot.child("Users").child(userID).child("points").getValue();
    }

    public static void setStatus(String taskID, String Status) {
        reference.child("Tasks").child(currFamilyId).child(taskID).child("status").setValue(Status);
    }

    public static void setbelongsToUID(String taskID, String uid) {
        if (uid != null)
            reference.child("Tasks").child(currFamilyId).child(taskID).child("status").setValue(uid);
        else
            reference.child("Tasks").child(currFamilyId).child(taskID).child("belongsToUID").removeValue();
    }

    public static void setFamilyName(String key, String familyName) {
        reference.child("Families").child(key).child("Family name").setValue(familyName);
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

    public static void setTaskName(String taskID, String nameToUpdate) {
        reference.child("Tasks").child(currFamilyId).child(taskID).child("nameTask").setValue(nameToUpdate);
    }

    public static void setTaskBonus(String taskID, int bonusToUpdate) {
        reference.child("Tasks").child(currFamilyId).child(taskID).child("bonusScore").setValue(bonusToUpdate);
    }

    public static String getAndSetTitleSpinnerOfBelongChild(String BelongsToUID) {
        TitleSpinnerOfBelongChild = dataSnapshot.child("Users").child(BelongsToUID).child("name").getValue().toString();
        return TitleSpinnerOfBelongChild;
    }

    public static void addPointsToChild (Task conformedTask){
        DataSnapshot Child = dataSnapshot.child("Users").child(conformedTask.getBelongsToUID());
        Child.child("points").getRef().setValue((long)Child.child("points").getValue() + conformedTask.getBonusScore());
    }

    public static void updateListOfChildFromDB(ArrayList<String> allKeys, ArrayList<String> allKids, ArrayAdapter adapter) {
        allKids.clear();
        allKeys.clear();
        allKids.add("Not Associated");
        Iterable<DataSnapshot> UsersInFamily = dataSnapshot.child("Families").child(currFamilyId).getChildren();
        for (DataSnapshot User : UsersInFamily) {
            String toAddChildren = (String) User.getValue();
            String toAddKey = (String) User.getKey();
            if (dataSnapshot.child("Users").child(User.getKey()).child("type").getValue().toString().equals("Child")){
                allKeys.add(toAddKey);
                allKids.add(toAddChildren);
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

    public static void updateExpandableTaskListFromDB(ArrayList<String> ListChildForTask, HashMap<String, ArrayList<Task>> ListTaskGroups, HashMap<String, ArrayList<String>> ListTaskID, String status, TaskToChildExtendListAdapter childAdapter) {
        ListChildForTask.clear();
        Iterable<DataSnapshot> UsersInFamily = dataSnapshot.child("Families").child(currFamilyId).getChildren();
        for (DataSnapshot User : UsersInFamily) {
            String toAddChildren = (String) User.getValue();
            if (dataSnapshot.child("Users").child(User.getKey()).child("type").getValue().toString().equals("Child")){
                ListChildForTask.add(toAddChildren);
                ArrayList<Task> toAdd = new ArrayList<>();
                ArrayList<String> toAddID = new ArrayList<>();
                Iterable<DataSnapshot> Tasks = dataSnapshot.child("Tasks").child(currFamilyId).getChildren();
                for (DataSnapshot Task : Tasks){
                    if (Task.child("belongsToUID").getValue() != null && Task.child("belongsToUID").getValue().equals(User.getKey()) && Task.child("status").getValue().equals(status)) {
                        Task taskToAdd = Task.getValue(Task.class);
                        toAdd.add(taskToAdd);
                        toAddID.add(Task.getKey());
                    }
                }
                ListTaskGroups.put(toAddChildren, toAdd);
                ListTaskID.put(toAddChildren, toAddID);
                childAdapter.notifyDataSetChanged();
            }
        }
    }

    public static void initializationCurrFamilyIdAndPermission() {
        currFamilyId = (String) dataSnapshot.child("Users").child(userID).child("currFamilyId").getValue();
        permission = (String) dataSnapshot.child("Users").child(userID).child("type").getValue();
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

    public static void createUserInFireBase(User user_to_add, String familyId, Uri uri, Bitmap bitmap) {
        String key = reference.child("Families").push().getKey();
        user_to_add.setCurrFamilyId(key);
        mAuth.createUserWithEmailAndPassword(user_to_add.getEmail(), user_to_add.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setFamilyName(key, familyId);
                    setUserToFamily(key, user_to_add.getName());
                    setUserToUserFamily(key, familyId);
                    setUser(user_to_add);
                    uploadImage(key, uri, bitmap);
                }
            }
        });
    }

    public static void setScreenViewByUser(TextView name, TextView email) {
        name.setText((String) dataSnapshot.child("Users").child(userID).child("name").getValue());
        if (email != null)
            email.setText((String) dataSnapshot.child("Users").child(userID).child("email").getValue());
    }
}
