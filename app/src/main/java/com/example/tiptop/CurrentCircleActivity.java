package com.example.tiptop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentCircleActivity extends AppCompatActivity {

    private TextView family_name;
    private ListView circle_information;

    private String family_uid;

    private FirebaseDatabase root;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_current_circle);

        initializeClassVariables();

        setScreenViewByFamily();

        setCirclesList();

    }

    private void initializeClassVariables(){
        family_name = (TextView)findViewById(R.id.familyName);
        circle_information = (ListView)findViewById(R.id.circleInformation);

        root = FirebaseDatabase.getInstance();
    }

    private void setScreenViewByFamily(){
        Bundle extras = getIntent().getExtras();
        family_uid = (String) extras.get("family_uid");
        reference = root.getReference("Families").child(family_uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){

                    if(ds.getKey().equals("Family name")){
                        family_name.setText(ds.getValue()+" Circle");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCirclesList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CurrentCircleActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.CircleInformation));

        circle_information.setAdapter(adapter);

        circle_information.setOnItemClickListener((adapterView,view,i,l) -> {

            if(i==0) {
                Intent go_to_members = new Intent(CurrentCircleActivity.this, MembersActivity.class);

                go_to_members.putExtra("family_uid", family_uid);
                startActivity(go_to_members);
            }

        });
    }

}



