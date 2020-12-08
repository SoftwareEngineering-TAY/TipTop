package com.example.tiptop;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaskChildActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    ListView listOfTasks;
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String FamilyID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_child);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Tasks").child(FamilyID).getDatabase();
        listOfTasks=(ListView)findViewById(R.id.ListOfTasks);




    }



}
