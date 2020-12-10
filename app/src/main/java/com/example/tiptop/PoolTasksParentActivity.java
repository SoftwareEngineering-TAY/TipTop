package com.example.tiptop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PoolTasksParentActivity extends AppCompatActivity {
    private String currFamilyid;
    private List <String> ListUnassignedTasks;
    private List <String> ListKeysUnassignedTasks;
    private DatabaseReference databaseReference;
    private ArrayAdapter adapter;
    private ListView UnassignedTasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_parent);
        initializeClassVariables();
        getExtrasFromIntent();
        getAllUnassignedTasksFromDB();
        showlistUnassignedTasks();
    }

    private void showlistUnassignedTasks() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ListUnassignedTasks);
        UnassignedTasks =(ListView) findViewById(R.id.ListUnassignedTasks);
        UnassignedTasks.setAdapter(adapter);
    }

    private void initializeClassVariables() {

    }

    private void getAllUnassignedTasksFromDB() {
        ListUnassignedTasks = new ArrayList<>();
        ListKeysUnassignedTasks = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Updating the spinner
        databaseReference.child("Tasks").child(currFamilyid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren() )
                {
                    if(ds.child("status").toString().equals("NotAssociated"))
                    {
                        ListUnassignedTasks.add(ds.child("nameTask").toString());
                        ListKeysUnassignedTasks.add(ds.getKey());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getExtrasFromIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            String currFamilyidTemp = extras.getString("currFamilyId");
            if(currFamilyidTemp!=null)
            {
                currFamilyid=currFamilyidTemp;
            }
            else
            {
                Toast.makeText(this, "currFamily didn't pass", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
