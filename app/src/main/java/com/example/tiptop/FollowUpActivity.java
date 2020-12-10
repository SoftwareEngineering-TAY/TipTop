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
    private TaskAdapter mTaskAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup);

        followList = (ListView) findViewById(R.id.followList);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        reference = root.getReference();
        uid = mAuth.getCurrentUser().getUid();

        Bundle extras = getIntent().getExtras();
        currFamilyId = extras.getString("currFamilyId");

        ArrayList<Task> list = new ArrayList<>();
        Task toAdd = new Task();
        toAdd.setStatus(Task.STATUS.WaitingForApproval);
        toAdd.setBelongsToUID(uid);
        toAdd.setBonusScore((long) 20);
        toAdd.setComment("BlaBlaBla");
        toAdd.setNameTask("Wash The Car");
        list.add(toAdd);

        String key = reference.child("Tasks").child(currFamilyId).push().getKey();
        reference.child("Tasks").child(currFamilyId).child(key).setValue(toAdd);

        mTaskAdapter = new TaskAdapter(getApplicationContext(),R.layout.row_task,list);

        followList.setAdapter(mTaskAdapter);




        followList.setOnItemClickListener((adapterView,view,i,l) -> {
            Log.v("PLACE",list.get(i).getNameTask());

            Intent intent = new Intent(view.getContext(), TaskInfoActivity.class);
            intent.putExtra("task",list.get(i));
            startActivity(intent);

//            String key = reference.child("Tasks").child(currFamilyId).push().getKey();
//            reference.child("Tasks").child(currFamilyId).child(key).setValue(toAdd);
        });

        reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
            @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                for (DataSnapshot Snapshot : snapshot.getChildren()){
                    if (Snapshot.child("belongsToUID").getValue().equals(uid)){
                        Task toAdd = Snapshot.getValue(Task.class);
//                        Task toAdd = new Task();
//                        toAdd.setNameTask(Snapshot.child("nameTask").getValue().toString());
//                        toAdd.setBonusScore((Long)Snapshot.child("bonusScore").getValue());
//
//                        toAdd.setStatus((Task.STATUS)Snapshot.child("status").getValue());
//
//                        toAdd.setBelongsToUID((String) Snapshot.child("belongsToUID").getValue());
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
