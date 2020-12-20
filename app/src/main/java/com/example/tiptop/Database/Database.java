package com.example.tiptop.Database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class Database {

    private  static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static final String USERS_ROOT = "Users", FAMILIES_ROOT= "Families" , TASKS_ROOT= "Tasks",USERRFAMILIES_ROOT = "UserFamilies";


}
