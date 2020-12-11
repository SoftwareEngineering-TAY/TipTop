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

    private FirebaseDatabase root;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    private String uid;
    private String permission;
    private String currFamilyId;
    private List <String> allKeys;
    private ArrayList <String> allFamilies;
    private ArrayAdapter adapter;
    public String spinnerTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializationFromXML();
        initializationCurrFamilyId();
        spinerActive();
        ActivateAllButtons();
    }

    private void initializationCurrFamilyId() {
        root = FirebaseDatabase.getInstance();
        reference = root.getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        reference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren() )
                {
                    if(ds.getKey().equals("currFamilyId")){
                        currFamilyId = (String) ds.getValue();
                        spinnerTitle =  (String) ds.getValue();
                    }
                    if (ds.getKey().equals("type")){
                        permission = (String) ds.getValue();
                    }
                }
                Log.v("permissionnnnnn!!!!!!!", permission);
                Log.v("currFamilyId!!!!!!!", currFamilyId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void spinerActive() {
        //Set the SpinnerFamily Spinner
        SpinnerFamily = (Spinner)findViewById(R.id.SpinnerFamily);
        allKeys = new ArrayList<>();
        allFamilies = new ArrayList<>();
        getInfoFromDB();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allFamilies);
        SpinnerFamily.setAdapter(adapter);
        SpinnerFamily.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currFamilyId = allKeys.get(position);
                spinnerTitle = allFamilies.get(position);
                System.out.println(" currFamilyId = allKeys.get(position);"+ allKeys.get(position)+"position: "+position);
                System.out.println(allKeys);
                Log.v("currFamilyId666666", currFamilyId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getInfoFromDB() {
        //Updating the spinner
        reference.child("UserFamilies").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allFamilies.clear();
                allKeys.clear();
                for (DataSnapshot ds : snapshot.getChildren() )
                {
                    String toAddFamiliy =(String) ds.getValue();
                    String toAddKey =ds.getKey();
                    System.out.println("ds.getKey()!!!!!!!!!!"+ds.getKey());
                    allFamilies.add(toAddFamiliy);
                    allKeys.add(toAddKey);
                }
                adapter.notifyDataSetChanged();
                System.out.println(allFamilies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializationFromXML() {
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
      
    }

    private void ActivateAllButtons() {
        //Moves to the settings activity
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = mAuth.getCurrentUser().getUid();
                reference = root.getReference("Users").child(uid).child("type");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                       String type =  snapshot.getValue().toString();
                       if(type.equals("Parent")){
                           Intent go_to_setting = new Intent(v.getContext(),SettingActivity.class);
                           startActivity(go_to_setting);
                       }
                       else if(type.equals("Child")){
                           Intent go_to_setting_children = new Intent(v.getContext(),SettingChildrenActivity.class);
                           startActivity(go_to_setting_children);
                       }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
                i.putExtra("permission",permission);
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
                Intent i;
                if (permission.equals("Parent")){
                    i = new Intent(v.getContext(), PoolTasksParentActivity.class);
                }

                else if (permission.equals("Child")){
                    i = new Intent(v.getContext(), PoolTaskChildActivity.class);
                }
                else return;
                i.putExtra("currFamilyId", currFamilyId);
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
    }
}
