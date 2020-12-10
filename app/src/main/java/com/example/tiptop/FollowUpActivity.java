package com.example.tiptop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class FollowUpActivity extends AppCompatActivity {

    private ListView followList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private String currFamilyId;
    private String uid;
    private String permission;
    private TaskAdapter mTaskAdapter;
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
//        Log.v("Gettpermissionnn!!!!!!!", permission);
    }

    private void createListOfTask() {
        list = new ArrayList<>();
        mTaskAdapter = new TaskAdapter(getApplicationContext(),R.layout.row_task,list);
        followList.setAdapter(mTaskAdapter);
    }

    private void crateClickEvent() {
        followList.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent;
            Log.v("Chepermissionnn!!!!!!!", permission);
            if (permission.equals("Parent")){
                intent = new Intent(view.getContext(), ApproveActivity.class);
            }

            else if (permission.equals("Child")){
                intent = new Intent(view.getContext(), TaskInfoActivity.class);
            }
            else return;
            Log.v("Chepermissionnn!!!!!!!", permission);
            intent.putExtra("task",list.get(i));
            startActivity(intent);
        });
    }

    private void updateListFromDB() {
        reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot Snapshot : snapshot.getChildren()){
                    if (Snapshot.child("belongsToUID").getValue().equals(uid)){
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
