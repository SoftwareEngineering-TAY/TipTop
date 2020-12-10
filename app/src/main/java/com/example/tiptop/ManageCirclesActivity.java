package com.example.tiptop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageCirclesActivity extends AppCompatActivity {

    private static final String TAG = "ManageCirclesActivity";
    private ListView circles_list;

    private FirebaseDatabase root;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_circles);

        initializeClassVariables();

        setCirclesList();
    }

    private void initializeClassVariables(){

        circles_list = (ListView)findViewById(R.id.circlesList);

        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    private void setCirclesList(){

        ArrayList<String> circlesArray = new ArrayList<>();
        ArrayList<String> familiesID = new ArrayList<>();

        ArrayAdapter circlesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, circlesArray);

        circles_list.setAdapter(circlesAdapter);

        String uid = mAuth.getCurrentUser().getUid();
        reference = root.getReference("UserFamilies").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                circlesArray.clear();
                familiesID.clear();

                for(DataSnapshot ds : snapshot.getChildren()){

                    circlesArray.add(ds.getValue().toString());
                    familiesID.add(ds.getKey());

                }
                circlesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        circles_list.setOnItemClickListener((adapterView,view,i,l) -> {

            Intent go_to_current_circle = new Intent(ManageCirclesActivity.this,CurrentCircleActivity.class);

            go_to_current_circle.putExtra("family_uid", familiesID.get(i));
            startActivity(go_to_current_circle);

        });

    }
}
