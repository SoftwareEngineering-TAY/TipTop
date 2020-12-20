package com.example.tiptop.PoolTasks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Adapters.TaskListAdapter;
import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PoolTasksParentActivity extends AppCompatActivity {

    private ListView UnassignedTasks;
    private ExpandableListView AssociatedTasks;
    private FirebaseAuth mAuth;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private String currFamilyId;
    private String uid;

    private ArrayList<Task> ListUnassignedTasks;
    private ArrayList<String> ListChildForTask;
    private HashMap<String,ArrayList<Task>> ListTaskGroups;
    private static final String TAG = "PoolTasksParentActivity";
    private TaskListAdapter adapter;
    private TaskToChildExtendListAdapter childAdapter;

    private Button addTaskButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_parent);
        initializeClassVariables();
        getExtrasFromIntent();
        createListOfTask();

        createExpandableListOfTask();

        crateClickEvent();
        updateListFromDB();

        addButtonFunc();


    }

    private void createExpandableListOfTask() {
        ListChildForTask = new ArrayList<>(); //list group
        ListTaskGroups = new HashMap<>(); //list child
        childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,currFamilyId, TaskInfoParentActivity.class);
        AssociatedTasks.setAdapter(childAdapter);

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
                                reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        toAdd.clear();
                                        for (DataSnapshot ds1 : snapshot.getChildren() )
                                        {
                                            if(ds1.child("belongsToUID").getValue()!=null&&ds1.child("belongsToUID").getValue().equals(ds.getKey()) && ds1.child("status").getValue().equals("Associated")){
                                                Task taskToAdd = ds1.getValue(Task.class);
                                                toAdd.add(taskToAdd);
                                            }
                                        }
                                        ListTaskGroups.put(toAddChildren,toAdd);
                                        System.out.println("ListChildForTask1"+ListChildForTask);
                                        System.out.println("ListTaskGroups1"+ListTaskGroups);

                                        childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,currFamilyId, TaskInfoParentActivity.class);
                                        AssociatedTasks.setAdapter(childAdapter);
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

    private void initializeClassVariables() {
        UnassignedTasks = (ListView) findViewById(R.id.ListUnassignedTasks);
        AssociatedTasks = (ExpandableListView) findViewById(R.id.ListAssociatedTasks);

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
                currFamilyId =currFamilyidTemp;
            }
            else
            {
                Toast.makeText(this, "currFamily didn't pass", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createListOfTask() {
        ListUnassignedTasks = new ArrayList<>();
        adapter = new TaskListAdapter(getApplicationContext(),R.layout.row_task,ListUnassignedTasks);
        UnassignedTasks.setAdapter(adapter);
    }

    private void crateClickEvent() {

        UnassignedTasks.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent =new Intent(view.getContext(), TaskInfoParentActivity.class);
            ////Chang destination!!
            intent.putExtra("task",ListUnassignedTasks.get(i));
            intent.putExtra("currFamilyId", currFamilyId);
            Log.d(TAG, "crateClickEvent:task: " + ListUnassignedTasks.get(i));
            Log.d(TAG, "crateClickEvent: currFamily" + currFamilyId);
            startActivity(intent);
        });

    }

    private void updateListFromDB() {
        reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
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
                Intent i = new Intent(v.getContext(),NewTaskActivity.class);
                i.putExtra("currFamilyid", currFamilyId);
                startActivity(i);
            }
        });
    }
}
