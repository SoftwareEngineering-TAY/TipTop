package com.example.tiptop.Database;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.Objects.User;
import com.example.tiptop.PoolTasks.TaskInfoParentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    private  static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static DatabaseReference reference = db.getReference();
    private static String userID = mAuth.getCurrentUser().getUid();

    public static final String USERS_ROOT = "Users", FAMILIES_ROOT= "Families" , TASKS_ROOT= "Tasks",USERRFAMILIES_ROOT = "UserFamilies";

    public static boolean setStatus(String currFamilyId, String taskID, String Status){
        try{
            reference.child("Tasks").child(currFamilyId).child(taskID).child("status").setValue(Status);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean setFamilyName(String key, String familyName){
        try{
            reference.child("Families").child(key).child("Family name").setValue(familyName);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean setUserToFamily(String familyKey ,String userName){
        try{
            reference.child("Families").child(familyKey).child(userID).setValue(userName);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean setUserToUserFamily(String familyKey,String familyName){
        try{
            reference.child("UserFamilies").child(userID).child(familyKey).setValue(familyName);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean setUser(User userToAdd){
        try{
            reference.child("Users").child(userID).setValue(userToAdd);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }


    public static boolean updateTaskListFromDB(ArrayList<Task> toUpdate,ArrayList<String>idToUpdate, String currFamilyId, String permission, String status, ArrayAdapter<Task> mTaskListAdapter ) {
        try{
            reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    toUpdate.clear();
                    idToUpdate.clear();
                    for (DataSnapshot Snapshot : snapshot.getChildren()){
                        if (permission.equals("Parent")&& Snapshot.child("status").getValue().equals(status)){
                            Task toAdd = Snapshot.getValue(Task.class);
                            toUpdate.add(toAdd);
                            idToUpdate.add(Snapshot.getKey());
                        }
                        else if (permission.equals("Child") && Snapshot.child("belongsToUID").getValue()!=null &&
                                Snapshot.child("belongsToUID").getValue().equals(userID)&& Snapshot.child("status").getValue().equals(status)){
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
        }
        catch (Exception e){
            return false;
        }
        return true;
    }


    public static boolean updateExpandableTaskListFromDB(ArrayList<String> ListChildForTask, HashMap<String,ArrayList<Task>> ListTaskGroups,HashMap<String,ArrayList<String>> ListTaskID, String currFamilyId, String status, TaskToChildExtendListAdapter childAdapter) {
        try{
            reference.child("Families").child(currFamilyId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ListChildForTask.clear();
                    for (DataSnapshot ds : snapshot.getChildren() )
                    {
                        String toAddChildren =(String) ds.getValue();
                        reference.child("Users").child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("type").getValue()!=null && snapshot.child("type").getValue().toString().equals("Child"))
                                {
                                    ListChildForTask.add(toAddChildren);
                                    ArrayList<Task> toAdd = new ArrayList<>();
                                    ArrayList<String> toAddID = new ArrayList<>();
                                    reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            toAdd.clear();
                                            toAddID.clear();
                                            for (DataSnapshot ds1 : snapshot.getChildren() )
                                            {
                                                if(ds1.child("belongsToUID").getValue()!=null&&ds1.child("belongsToUID").getValue().equals(ds.getKey()) && ds1.child("status").getValue().equals(status)){
                                                    Task taskToAdd = ds1.getValue(Task.class);
                                                    toAdd.add(taskToAdd);
                                                    toAddID.add(ds1.getKey());
                                                }
                                            }
                                            ListTaskGroups.put(toAddChildren,toAdd);
                                            ListTaskID.put(toAddChildren,toAddID);
                                            System.out.println("ListChildForTask1"+ListChildForTask);
                                            System.out.println("ListTaskGroups1"+ListTaskGroups);
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
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

//    public static boolean initializationCurrFamilyIdAndPermission() {
//        try{
//            String currFamilyId;
//            reference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot ds : snapshot.getChildren() )
//                    {
//                        if(ds.getKey().equals("currFamilyId")){
//                            currFamilyId = (String) ds.getValue();
//                            spinnerTitle =  (String) ds.getValue();
//                        }
//                        if (ds.getKey().equals("type")){
//                            permission = (String) ds.getValue();
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        catch (Exception e){
//            return false;
//        }
//        return true;
//
//    }

}
