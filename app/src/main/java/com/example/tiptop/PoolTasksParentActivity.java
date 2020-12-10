package com.example.tiptop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    private ListView UnassignedTasks;
    private FirebaseAuth mAuth;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private String currFamilyid;
    private String uid;
    private ArrayList<Task> ListUnassignedTasks;
    private TaskAdapter adapter;

    private Button addTaskButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_parent);
        initializeClassVariables();
        getExtrasFromIntent();
        createListOfTask();
        crateClickEvent();
        updateListFromDB();

        addButtonFunc();


    }

    private void initializeClassVariables() {
        UnassignedTasks = (ListView) findViewById(R.id.ListUnassignedTasks);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        reference = root.getReference();
        uid = mAuth.getCurrentUser().getUid();


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

    private void createListOfTask() {
        ListUnassignedTasks = new ArrayList<>();
        adapter = new TaskAdapter(getApplicationContext(),R.layout.row_task,ListUnassignedTasks);
        UnassignedTasks.setAdapter(adapter);
    }

    private void crateClickEvent() {
        UnassignedTasks.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent =new Intent(view.getContext(), TaskInfoActivity.class);
            ////Chang destination!!
            intent.putExtra("task",ListUnassignedTasks.get(i));
            startActivity(intent);
        });
    }

    private void updateListFromDB() {
        reference.child("Tasks").child(currFamilyid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListUnassignedTasks.clear();
                for (DataSnapshot Snapshot : snapshot.getChildren()){
                    if (Snapshot.child("status").getValue().equals("NotAssociated")){
                        Task toAdd = Snapshot.getValue(Task.class);
                        ListUnassignedTasks.add(toAdd);
                        Log.v("Add to list",toAdd.getNameTask());
                    }
                }
                Log.v("Data changed","www");
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addButtonFunc() {
        addTaskButton = (Button)findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),NewTask.class);
                i.putExtra("currFamilyid",currFamilyid);
                startActivity(i);
            }
        });
    }
}
