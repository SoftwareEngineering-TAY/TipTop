package com.example.tiptop.History;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.FollowUp.ApproveTaskActivity;
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

public class HistoryParentActivity extends AppCompatActivity {

    private Spinner filterSpinner;
    private EditText search;

    private ExpandableListView AssociatedTasks;

    private ArrayList<String> ListChildForTask;
    private HashMap<String,ArrayList<Task>> ListTaskGroups;
    private HashMap<String,ArrayList<String>> ListTaskID;

    private TaskToChildExtendListAdapter childAdapter;

    private FirebaseDatabase root;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String uid;
    private String currFamilyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initializeClassVariables();

        createExpandableListOfTask();
    }

    private void initializeClassVariables(){
        filterSpinner = findViewById(R.id.filterSpinner);
        search = findViewById(R.id.searchEditText);

        AssociatedTasks = (ExpandableListView) findViewById(R.id.ListHistoryTasks);

        root = FirebaseDatabase.getInstance();
        reference = root.getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        Bundle extras = getIntent().getExtras();
        currFamilyId = (String) extras.get("currFamilyId");
    }



    private void createExpandableListOfTask() {
        ListChildForTask = new ArrayList<>(); //list group
        ListTaskGroups = new HashMap<>(); //list child
        ListTaskID = new HashMap<>();//list of ID'S Tasks

        childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,ListTaskID,currFamilyId, ApproveTaskActivity.class);
        AssociatedTasks.setAdapter(childAdapter);
        Log.v("family id///////////: ",currFamilyId);

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
                                Log.v("toaddChildren*******",toAddChildren);
                                ArrayList<Task> toAdd = new ArrayList<>();
                                ArrayList<String> toAddID = new ArrayList<>();
                                reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        toAdd.clear();
                                        toAddID.clear();
                                        for (DataSnapshot ds1 : snapshot.getChildren() )
                                        {
                                            if(ds1.child("belongsToUID").getValue()!=null && ds1.child("belongsToUID").getValue().equals(ds.getKey()) && ds1.child("status").getValue().equals("Confirmed")){
                                                Task taskToAdd = ds1.getValue(Task.class);
                                                toAdd.add(taskToAdd);
                                                toAddID.add(ds1.getKey());
                                                Log.v("task confirm: ", taskToAdd.getNameTask());
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
