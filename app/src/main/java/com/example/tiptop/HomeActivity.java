package com.example.tiptop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    //Variables that will contain all the buttons
    private Button setting = null;
    private Button profile = null;
    private ImageButton  imageButton;
    private Button followUp = null;
    private Button Statistics = null;
    private Button chat = null;
    private Button Tasks = null;
    private Button history = null;
    private Button points = null;
    private Spinner SpinnerFamily = null;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uid=null;
    private String currFamilyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Set the settings button
        setting = (Button)findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),SettingActivity.class);
                startActivity(i);
            }
        });

        //Set the profile button
        profile = (Button)findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ProfileActivity.class);
                startActivity(i);
            }
        });

        //Set the followUp button
        followUp = (Button)findViewById(R.id.followUp);
        followUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),FollowUpActivity.class);
                i.putExtra("currFamilyId", currFamilyId);
                startActivity(i);
            }
        });

        //Set the Statistics button
        Statistics = (Button)findViewById(R.id.statistics);
        Statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),StatisticsActivity.class);
                startActivity(i);
            }
        });

        //Set the chat button
        chat = (Button)findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ChatActivity.class);
                startActivity(i);
            }
        });

        //Set the Tasks button
        Tasks = (Button)findViewById(R.id.tasks);
        Tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), TasksParentActivity.class);
                startActivity(i);
            }
        });

        //Set the history button
        history = (Button)findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),HistoryActivity.class);
                startActivity(i);
            }
        });

        //Set the points button
        points = (Button)findViewById(R.id.points);
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),PointsActivity.class);
                startActivity(i);
            }
        });

        //Set the ImageButton button
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ImageActivity.class);
                startActivity(i);
            }
        });

        SpinnerFamily = (Spinner)findViewById(R.id.SpinnerFamily);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        ArrayList <String> allFamilies = new ArrayList<>();
        List <String> allKeys = new ArrayList<>();

        databaseReference.child("UserFamilies").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren() )
                {
                    String toAddFamiliy =(String) ds.getValue();
                    String toAddKey =(String) ds.getKey();
                    allFamilies.add(toAddFamiliy);
                    allKeys.add(toAddKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allFamilies);
        SpinnerFamily.setAdapter(adapter);

        SpinnerFamily.setOnItemClickListener((adapterView,view,i,l) -> {
            databaseReference.child("Users").child(uid).child("currFamilyId").setValue(allKeys.get(i));
            currFamilyId = allKeys.get(i);
        });
    }
}
