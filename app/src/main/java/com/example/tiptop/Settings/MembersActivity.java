package com.example.tiptop.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MembersActivity extends AppCompatActivity {

    private TextView family_members;
    private ListView circle_members;
    private Button create_a_child_account;

    private String family_uid;

    private FirebaseDatabase root;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_members);

        initializeClassVariables();

        setScreenViewByFamily();

        setCreateAChildAccountButton();

    }

    private void initializeClassVariables(){
        family_members = (TextView)findViewById(R.id.familyMembers);
        circle_members = (ListView)findViewById(R.id.circleMembers);
        create_a_child_account = (Button)findViewById(R.id.inviteNewMember);

        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void setScreenViewByFamily(){
        ArrayList<String> membersArray = new ArrayList<>();

        ArrayAdapter membersAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, membersArray);

        circle_members.setAdapter(membersAdapter);

        Bundle extras = getIntent().getExtras();
        family_uid = (String) extras.get("family_uid");


        reference = root.getReference("Families").child(family_uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key;
                String value;

                for(DataSnapshot ds : snapshot.getChildren()){

                    key = ds.getKey();
                    value = (String) ds.getValue();

                    if(key.equals("Family name")){
                        family_members.setText(value+"'s members");
                    }

                    else{
                        membersArray.add(value);
                    }
                }
                membersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCreateAChildAccountButton(){
        create_a_child_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_child_account = new Intent(MembersActivity.this,CreateChildAccountActivity.class);

                go_to_child_account.putExtra("family_uid", family_uid);
                Log.v("****family_uid1****",family_uid);
                startActivity(go_to_child_account);
            }
        });
    }
}
