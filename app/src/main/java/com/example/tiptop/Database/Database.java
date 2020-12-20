package com.example.tiptop.Database;

import androidx.annotation.NonNull;

import com.example.tiptop.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class Database {

    private  static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static DatabaseReference reference = db.getReference();

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

    public static boolean setUserToFamily(String familyKey, String userID ,String userName){
        try{
            reference.child("Families").child(familyKey).child(userID).setValue(userName);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean setUserToUserFamily( String userID ,String familyKey,String familyName){
        try{
            reference.child("UserFamilies").child(userID).child(familyKey).setValue(familyName);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean setUser(String userID, User userToAdd){
        try{
            reference.child("Users").child(userID).setValue(userToAdd);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }





}
