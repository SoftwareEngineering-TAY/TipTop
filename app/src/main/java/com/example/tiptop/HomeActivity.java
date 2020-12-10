package com.example.tiptop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity  {

    //Variables that will contain all the buttons
    private Button setting;
    private Button profile;
    private ImageButton  imageButton;
    private Button followUp;
    private Button Statistics;
    private Button chat ;
    private Button Tasks;
    private Button history;
    private Button points;
    private Spinner SpinnerFamily;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String uid;
    private String currFamilyId;
    private List <String> allKeys;
    private ArrayList <String> allFamilies;
    private ArrayAdapter adapter;
    private String spinner_title=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeClassVariables(); // Initialize all variables in class

        //Moves to the settings activity
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),SettingActivity.class);
                startActivity(i);
            }
        });

        //Moves to the profile activity
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ProfileActivity.class);
                startActivity(i);
            }
        });

        //Moves to the followUp activity
        followUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),FollowUpActivity.class);
                i.putExtra("currFamilyId", currFamilyId);
                startActivity(i);
            }
        });

        //Moves to the Statistics activity
        Statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),StatisticsActivity.class);
                startActivity(i);
            }
        });

        //Moves to the chat activity
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ChatActivity.class);
                startActivity(i);
            }
        });

        //Moves to the Tasks activity
        Tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PoolTasksParentActivity.class);
                startActivity(i);
            }
        });

        //Moves to the history activity
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),HistoryActivity.class);
                startActivity(i);
            }
        });

        //Moves to the points activity
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),PointsActivity.class);
                startActivity(i);
            }
        });

        //Moves to the ImageButton activity
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ImageActivity.class);
                startActivity(i);
            }
        });



        //Updating the spinner
        databaseReference.child("UserFamilies").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allFamilies.clear();
                allKeys.clear();
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


        SpinnerFamily.setAdapter(adapter);

        SpinnerFamily.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();

                spinner_title=selectedItem;
                Log.v("click", allFamilies.get(position));
                currFamilyId =allKeys.get(position);


            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


    }

    private void initializeClassVariables() {
        //Set the settings button
        setting = (Button)findViewById(R.id.setting);
        //Set the profile button
        profile = (Button)findViewById(R.id.profile);
        //Set the followUp button
        followUp = (Button)findViewById(R.id.followUp);
        //Set the Statistics button
        Statistics = (Button)findViewById(R.id.statistics);
        //Set the chat button
        chat = (Button)findViewById(R.id.chat);
        //Set the Tasks button
        Tasks = (Button)findViewById(R.id.tasks);
        //Set the history button
        history = (Button)findViewById(R.id.history);
        //Set the points button
        points = (Button)findViewById(R.id.points);
        //Set the ImageButton button
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        allKeys = new ArrayList<>();
        allFamilies = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allFamilies);
        SpinnerFamily = (Spinner)findViewById(R.id.SpinnerFamily);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        databaseReference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren() )
                {
                    if(ds.getKey().equals("currFamilyId")){
                        currFamilyId = (String) ds.getValue();
                    }
                }
                Log.v("currFamilyId!!!!!!!", currFamilyId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        currFamilyId =allKeys.get(position);
//        spinner_title=allFamilies.get(position);
//        Log.v("click", allFamilies.get(position));
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
}
