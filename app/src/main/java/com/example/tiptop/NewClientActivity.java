package com.example.tiptop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class NewClientActivity extends AppCompatActivity{

    private static final String TAG = "NewClientActivity";

    private Button next;
    private TextView login;
    private com.google.android.material.textfield.TextInputLayout email;
    private com.google.android.material.textfield.TextInputLayout password;
    private com.google.android.material.textfield.TextInputLayout confirm_password ;
    private com.google.android.material.textfield.TextInputLayout birthday;
    private Spinner spin;
    private User user;

    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase stuff.
        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference = root.getReference();

        setContentView(R.layout.activity_new_client);
        next = (Button)findViewById(R.id.next);
        login = (TextView)findViewById(R.id.login);
        email = findViewById(R.id.newEmail);
        password = findViewById(R.id.newPassword);
        confirm_password = findViewById(R.id.confirmPassword);
        birthday = findViewById(R.id.birthday);
        spin = (Spinner) findViewById(R.id.spinner);
        user = new User();

        //initialize the spinner with adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewClientActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: From spinner item selected");
                switch (position) {
                    case 0:
                        user.setType("Parent");
                        break;
                    case 1:
                        user.setType("Child");
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //check validation of password with confirm password if equals continue else toast bad password and foucs on password.
                //first we want signup the user in the authentication.

                Log.d(TAG, "onClick: nxt button has been clicked");

                if(!validateEmail() || !validatePassword() || !validateConfirmPassword() || !validateBirthday()){
                    return;
                }

                String mail = email.getEditText().getText().toString();
                String pass = password.getEditText().getText().toString();
                String birth = birthday.getEditText().getText().toString();
                Log.d(TAG, "onClick: try to auth" + mail + "," + pass);

                // to create user at Auth.
                mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Sign to Auth successed.");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();
                            // String type =  what choosen from spinner. after it send to .child(user.getType).child(user.getUid)..
                            user.setEmail(mail);
                            user.setPassword(pass);
                            user.setBirthday(birth);
                            Log.d(TAG, "onComplete: From auth, Type:" + user.getType() + ",  uid:" + uid);
                            reference.child("users").child(user.getType()).child(uid).setValue(user);
                            Log.d(TAG, "onComplete: user have been auth and saved to database" + user.toString());
                        }
                        else{
                            //Auth went wrong the sign in failed.
                            Log.d(TAG, "onComplete: Auth failed" + task.getException().toString());
                        }

                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean validateEmail(){
        String mail = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(mail.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        }
        else if(!mail.matches(emailPattern)){
            email.setError("Invalid email address");
            return false;}
        else{
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String pass = password.getEditText().getText().toString();
        if(pass.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword(){
        String pass = password.getEditText().getText().toString();
        String cpass = confirm_password.getEditText().getText().toString();
        if(!pass.equals(cpass)){
            confirm_password.setError("Your passwords should be the same");
            return false;
        }
        else{
            confirm_password.setError(null);
            return true;
        }
    }

    private boolean validateBirthday(){
        String birth = birthday.getEditText().getText().toString();
        if(birth.isEmpty()){
            birthday.setError("Field cannot be empty");
            return false;
        }
        else{
            birthday.setError(null);
            return true;
        }
    }

}
