package com.example.tiptop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    private FirebaseAuth mAuth;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private String currFamilyId;
    private String uid;
    private String permission;
    private TaskAdapter mTaskAdapter;
    private TaskToChildAdapter childAdapter;
    private ArrayList<Task> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_parent);

        initializeClassVariables();

        createExpandableListOfTask();

       // createListOfTask();

        //crateClickEvent();

       // updateListFromDB();
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
        childAdapter = new TaskToChildAdapter(ListChildForTask,ListTaskGroups,currFamilyId,ApproveActivity.class);
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
                                            if(ds1.child("belongsToUID").getValue()!=null&&ds1.child("belongsToUID").getValue().equals(ds.getKey()) && ds1.child("status").getValue().equals("WaitingForApproval")){
                                                Task taskToAdd = ds1.getValue(Task.class);
                                                toAdd.add(taskToAdd);
                                            }
                                        }
                                        ListTaskGroups.put(toAddChildren,toAdd);
                                        System.out.println("ListChildForTask1"+ListChildForTask);
                                        System.out.println("ListTaskGroups1"+ListTaskGroups);

                                        childAdapter = new TaskToChildAdapter(ListChildForTask,ListTaskGroups,currFamilyId,ApproveActivity.class);
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

    private void createListOfTask() {
        list = new ArrayList<>();
        mTaskAdapter = new TaskAdapter(getApplicationContext(),R.layout.row_task,list);
        followList.setAdapter(mTaskAdapter);
    }

    private void crateClickEvent() {
        followList.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent;
            if (permission.equals("Parent")){
                intent = new Intent(view.getContext(), ApproveActivity.class);
            }

            else if (permission.equals("Child")){
                intent = new Intent(view.getContext(), TaskInfoChildActivity.class);
            }
            else return;
            intent.putExtra("task",list.get(i));
            intent.putExtra("currFamilyId",currFamilyId);
            startActivity(intent);
        });
    }

    private void updateListFromDB() {
        reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot Snapshot : snapshot.getChildren()){
                    if (permission.equals("Parent")&& Snapshot.child("status").getValue().equals("WaitingForApproval")){
                        Task toAdd = Snapshot.getValue(Task.class);
                        list.add(toAdd);
                        Log.v("Add to list",toAdd.getNameTask());
                    }
                    else if (permission.equals("Child") && Snapshot.child("belongsToUID").getValue()!=null&&Snapshot.child("belongsToUID").getValue().equals(uid)&& Snapshot.child("status").getValue().equals("WaitingForApproval")){
                        Task toAdd = Snapshot.getValue(Task.class);
                        list.add(toAdd);
                        Log.v("Add to list",toAdd.getNameTask());
                    }
                }
                Log.v("Data changed","www");
                mTaskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
