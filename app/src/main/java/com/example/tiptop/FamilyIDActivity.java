package com.example.tiptop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FamilyIDActivity extends AppCompatActivity {

    private User user_to_add;
    private Button sign_up;
    private com.google.android.material.textfield.TextInputLayout family_id;

    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    private static final String TAG = "FamilyIDActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_id);

        initializeClassVariables();

        Bundle extras = getIntent().getExtras();

        user_to_add = (User)extras.get("user");

        Log.d(TAG, "onCreate: got user from newclinet activity, " + user_to_add);

        set_sign_in_button();
    }

    private void initializeClassVariables(){
        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference = root.getReference();
        family_id = findViewById(R.id.enterFamilyId);
        sign_up = findViewById(R.id.signUp);
        user_to_add = new User();
    }

    private void set_sign_in_button(){

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createUserInFireBase();

                Intent go_login = new Intent(v.getContext(),LoginActivity.class);
                startActivity(go_login);
            }
        });
    }

    private void createUserInFireBase(){

        mAuth.createUserWithEmailAndPassword(user_to_add.getEmail(),user_to_add.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: Sign to Auth successed.");
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String uid = firebaseUser.getUid();
                    Log.d(TAG, "onComplete: From auth, Type:" + user_to_add.getType() + ",  uid:" + uid);
                    reference.child("users").child(family_id.getEditText().getText().toString()).child(user_to_add.getType()).child(uid).setValue(user_to_add);
                    Log.d(TAG, "onComplete: user have been auth and saved to database" + user_to_add.toString());
                }
                else{
                    //Auth went wrong the sign in failed.
                    Log.d(TAG, "onComplete: Auth failed" + task.getException().toString());
                }

            }
        });
    }

}
