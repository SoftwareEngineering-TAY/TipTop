package com.example.tiptop;

import android.os.Bundle;
import android.widget.Button;
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

public class SettingChildrenActivity extends AppCompatActivity {

    private TextView name;
    private TextView email;

    private FirebaseDatabase root;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_children);

        initializeClassVariables();

        setScreenViewByUser();
    }

    private void initializeClassVariables(){

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
}
