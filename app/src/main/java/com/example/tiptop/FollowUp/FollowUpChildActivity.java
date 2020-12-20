package com.example.tiptop.FollowUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Adapters.TaskListAdapter;
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

public class FollowUpChildActivity extends AppCompatActivity {

    private ListView followList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private String currFamilyId;
    private String uid;
    private String permission;
    private TaskListAdapter mTaskListAdapter;
    private ArrayList<Task> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup);

        initializeClassVariables();

        createListOfTask();

        crateClickEvent();

        updateListFromDB();
    }

    private void initializeClassVariables() {
        followList = (ListView) findViewById(R.id.followList);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        reference = root.getReference();
        uid = mAuth.getCurrentUser().getUid();

        Bundle extras = getIntent().getExtras();
        currFamilyId = extras.getString("currFamilyId");
        permission = extras.getString("permission");
    }

    private void createListOfTask() {
        list = new ArrayList<>();
        mTaskListAdapter = new TaskListAdapter(getApplicationContext(),R.layout.row_task,list);
        followList.setAdapter(mTaskListAdapter);
    }

    private void crateClickEvent() {
        followList.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent;
            if (permission.equals("Parent")){
                intent = new Intent(view.getContext(), ApproveTaskActivity.class);
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
                mTaskListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
