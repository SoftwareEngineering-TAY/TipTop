package com.example.tiptop.FollowUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Adapters.TaskListAdapter;
import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.PoolTasks.TaskInfoChildActivity;
import com.example.tiptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowUpParentActivity extends AppCompatActivity {

    private ListView followList;

    private ExpandableListView AssociatedTasks;

    private ArrayList<String> ListChildForTask;
    private HashMap<String,ArrayList<Task>> ListTaskGroups;

    private HashMap<String,ArrayList<String>> ListTaskID;

    private FirebaseAuth mAuth;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private String currFamilyId;
    private String uid;
    private String permission;
    private TaskListAdapter mTaskListAdapter;
    private TaskToChildExtendListAdapter childAdapter;
    private ArrayList<Task> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_parent);

        initializeClassVariables();

        createExpandableListOfTask();

    }

    private void initializeClassVariables() {
        followList = (ListView) findViewById(R.id.followList);
        AssociatedTasks = (ExpandableListView) findViewById(R.id.followExpandableList);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        reference = root.getReference();
        uid = mAuth.getCurrentUser().getUid();

        Bundle extras = getIntent().getExtras();
        currFamilyId = extras.getString("currFamilyId");
        permission = extras.getString("permission");
    }

    private void createExpandableListOfTask() {
        ListChildForTask = new ArrayList<>(); //list group
        ListTaskGroups = new HashMap<>(); //list child
        ListTaskID = new HashMap<>();//list of ID'S Tasks
        childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,ListTaskID,currFamilyId, ApproveTaskActivity.class);
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
                                ArrayList<String> toAddID = new ArrayList<>();
                                reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        toAdd.clear();
                                        toAddID.clear();
                                        for (DataSnapshot ds1 : snapshot.getChildren() )
                                        {
                                            if(ds1.child("belongsToUID").getValue()!=null&&ds1.child("belongsToUID").getValue().equals(ds.getKey()) && ds1.child("status").getValue().equals("WaitingForApproval")){
                                                Task taskToAdd = ds1.getValue(Task.class);
                                                toAdd.add(taskToAdd);
                                                toAddID.add(ds1.getKey());
                                            }
                                        }
                                        ListTaskGroups.put(toAddChildren,toAdd);
                                        ListTaskID.put(toAddChildren,toAddID);
                                        System.out.println("ListChildForTask1"+ListChildForTask);
                                        System.out.println("ListTaskGroups1"+ListTaskGroups);

                                        childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,ListTaskID,currFamilyId, ApproveTaskActivity.class);
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

}
