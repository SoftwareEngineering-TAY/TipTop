package com.example.tiptop;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup);

        followList = (ListView) findViewById(R.id.followList);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        reference = root.getReference();
        uid = mAuth.getCurrentUser().getUid();


        ArrayList<String> list = new ArrayList<>();
        list.add("A");

        Bundle extras = getIntent().getExtras();
        currFamilyId = extras.getString("currFamilyId");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        followList.setAdapter(adapter);

        Task toAdd = new Task();
        toAdd.setStatus(Task.STATUS.NotAssociated);
        toAdd.setBelongsToUID(uid);
        toAdd.setBonusScore(20);
        toAdd.setComment("BlaBlaBla");
        toAdd.setNameTask("Wash The Car");


        followList.setOnItemClickListener((adapterView,view,i,l) -> {
            Log.v("PLACE",list.get(i));
            String key = reference.child("Tasks").child(currFamilyId).push().getKey();
            reference.child("Tasks").child(currFamilyId).child(key).setValue(toAdd);
        });

        reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot Snapshot : snapshot.getChildren()){
                    if (Snapshot.child("belongsToUID").getValue().equals(uid)){
                        String toAdd = Snapshot.child("nameTask").getValue().toString();
                        list.add(toAdd);
                        Log.v("Add to list",toAdd);
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
}
