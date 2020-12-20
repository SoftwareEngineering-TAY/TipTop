package com.example.tiptop.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingParentActivity extends AppCompatActivity {

    private Button manage_circles;
    private TextView name;
    private TextView email;

    private FirebaseDatabase root;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        initializeClassVariables();

        setScreenViewByUser();

        setManageCirclesButton();
    }

    private void initializeClassVariables(){

        manage_circles = (Button)findViewById(R.id.manageCircles);
        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);

        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void setScreenViewByUser(){
        String uid = mAuth.getCurrentUser().getUid();
        reference = root.getReference("Users").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key;
                String value;

                for(DataSnapshot ds : snapshot.getChildren()){

                    key = ds.getKey();
                    value = (String) ds.getValue();

                    if(key.equals("name")){
                        name.setText(value);
                    }

                    else if(key.equals("email")){
                        email.setText(value);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setManageCirclesButton(){
        manage_circles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_manage_circles = new Intent(v.getContext(),ManageCirclesActivity.class);
                startActivity(go_manage_circles);
            }
        });
    }

}
