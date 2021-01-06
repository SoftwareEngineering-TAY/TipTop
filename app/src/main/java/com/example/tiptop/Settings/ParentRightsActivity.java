package com.example.tiptop.Settings;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Objects.User;
import com.example.tiptop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ParentRightsActivity extends AppCompatActivity {

    private static final String TAG = "ParentRightsActivity";

    private TextView family_name;
    private LinearLayout parent_layout;
    private LinearLayout child_layout;

    private String family_uid;

    private FirebaseDatabase root;
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_parent_rights);

        initializeClassVariables();

        setScreenViewByFamily();

    }

    private void initializeClassVariables(){
        parent_layout = (LinearLayout)findViewById(R.id.parentMembers);
        child_layout = (LinearLayout)findViewById(R.id.childMembers);
        family_name = (TextView)findViewById(R.id.familyName);

        root = FirebaseDatabase.getInstance();
    }

    private void setScreenViewByFamily(){
        Bundle extras = getIntent().getExtras();
        family_uid = (String) extras.get("family_uid");
        reference = root.getReference("Families").child(family_uid).child("Family name");
        reference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                family_name.setText(snapshot.getValue()+" Circle");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference = root.getReference("Families").child(family_uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String key;

                for(DataSnapshot ds : snapshot.getChildren()){

                    key = ds.getKey();

                    if(!key.equals("Family name") && !key.equals("Route Type")){

                        createSwitchForEveryUser(key);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createSwitchForEveryUser(String key){
        DatabaseReference childReference = root.getReference("Users").child(key);
        childReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                Switch user_switch=new Switch(ParentRightsActivity.this);
                user_switch.setGravity(Gravity.LEFT);
                user_switch.setText(user.getName());

                if(user.getType().equals("Parent")){
                    user_switch.setChecked(true);
                    parent_layout.addView(user_switch);
                }
                else if(user.getType().equals("Child")){
                    user_switch.setChecked(false);
                    child_layout.addView(user_switch);
                }

                user_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(user.getType().equals("Child")) {
                            child_layout.removeView(user_switch);
                            parent_layout.addView(user_switch);
                            user.setType("Parent");
                            childReference.child("type").setValue("Parent");
                        }
                        else if(user.getType().equals("Parent")){
                            parent_layout.removeView(user_switch);
                            child_layout.addView(user_switch);
                            user.setType("Child");
                            childReference.child("type").setValue("Child");
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
