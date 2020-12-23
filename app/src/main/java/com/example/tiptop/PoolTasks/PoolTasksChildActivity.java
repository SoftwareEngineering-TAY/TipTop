package com.example.tiptop.PoolTasks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Adapters.TaskListAdapter;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PoolTasksChildActivity extends AppCompatActivity {
    private ListView followList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private String currFamilyId;
    private String uid;
    private TaskListAdapter mTaskListAdapter;
    private ArrayList<Task> list;
    private ArrayList<String> listID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_child);

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
    }

    private void createListOfTask() {
        list = new ArrayList<>();
        listID = new ArrayList<>();
        mTaskListAdapter = new TaskListAdapter(getApplicationContext(),R.layout.row_task,list);
        followList.setAdapter(mTaskListAdapter);
    }

    private void crateClickEvent() {
        followList.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent = new Intent(view.getContext(), TaskInfoChildActivity.class);
            intent.putExtra("task",list.get(i));
            intent.putExtra("taskID",listID.get(i));
            intent.putExtra("currFamilyId",currFamilyId);
            startActivity(intent);
        });
    }

    private void updateListFromDB() {
        reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                listID.clear();
                for (DataSnapshot Snapshot : snapshot.getChildren()){
                    if (Snapshot.child("belongsToUID").getValue()!=null&&Snapshot.child("belongsToUID").getValue().equals(uid) && Snapshot.child("status").getValue().equals("Associated")){
                        Task toAdd = Snapshot.getValue(Task.class);
                        list.add(toAdd);
                        listID.add(Snapshot.getKey());
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
