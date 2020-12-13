package com.example.tiptop;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CreateChildAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateChildAccountActiv";

    private Button next;
    private Button select_date;
    private TextView birthday;
    private com.google.android.material.textfield.TextInputLayout name;
    private com.google.android.material.textfield.TextInputLayout username;
    private com.google.android.material.textfield.TextInputLayout password;

    private String family_uid;
    private String family_name;
    private User user;
    private User current_user;

    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_child_account);

        initializeClassVariables();

        setCurrentUser();

        setSelectDateButton();

        setContinueButton();

    }

    private void initializeClassVariables(){
        Bundle extras = getIntent().getExtras();
        family_uid = (String) extras.get("family_uid");
        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference = root.getReference();

        foundFamilyById();

        next = (Button)findViewById(R.id.next);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.newPassword);
        birthday = (TextView) findViewById(R.id.birthday);
        select_date = (Button) findViewById(R.id.selectDate);

        user = new User();
        current_user = new User();

    }

    private void setCurrentUser(){
        String uid = mAuth.getCurrentUser().getUid();
        reference = root.getReference().child("Users").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                current_user = snapshot.getValue(User.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSelectDateButton(){
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateChildAccountActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                birthday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void setContinueButton(){
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!validateName() || !validateUsername() || !validatePassword()){
                    return;
                }

                String user_name = name.getEditText().getText().toString();
                String uname = username.getEditText().getText().toString() + "@mail.com";
                String pass = password.getEditText().getText().toString();
                String birth = birthday.getText().toString();
                user.setName(user_name);
                user.setEmail(uname);
                user.setPassword(pass);
                user.setBirthday(birth);
                user.setType("Child");

                createUserInFireBase();

                signInCurrentUser();

            }
        });
    }

    private void createUserInFireBase(){

        user.setCurrFamilyId(family_uid);

        mAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String uid = task.getResult().getUser().getUid();
                    Log.d(TAG, "onComplete:uid: " + uid);

                    reference = root.getReference();

                    reference.child("Families").child(family_uid).child(uid).setValue(user.getName());
                    reference.child("UserFamilies").child(uid).child(family_uid).setValue(family_name);
                    reference.child("Users").child(uid).setValue(user);

                    Toast.makeText(getApplicationContext(),"Account created", Toast.LENGTH_SHORT).show();

                }
                else{

                    Toast.makeText(getApplicationContext(),"Account failed", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void signInCurrentUser(){

        mAuth.signOut();

        mAuth.signInWithEmailAndPassword(current_user.getEmail(),current_user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent go_to_home = new Intent(CreateChildAccountActivity.this, HomeActivity.class);
                    startActivity(go_to_home);
                }
                else {

                }
            }
        });
    }

    private void foundFamilyById(){

        reference = root.getReference("Families").child(family_uid).child("Family name");
        Log.d(TAG, "foundFamilyById: family uid:" + family_uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                family_name = snapshot.getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validateName(){
        String user_name = name.getEditText().getText().toString();
        if(user_name.isEmpty()){
            name.setError("Field cannot be empty");
            return false;
        }
        else{
            name.setError(null);
            return true;
        }
    }

    private boolean validateUsername(){
        String mail = username.getEditText().getText().toString();

        if(mail.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        }


        if(mail.contains(" ")){
            username.setError("Field cannot contains spaces");
            return false;
        }

        else{
            username.setError(null);
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
}
