package com.example.tiptop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    //xml variables
    private Button login;
    private TextView sign_up;
    private com.google.android.material.textfield.TextInputLayout email;
    private com.google.android.material.textfield.TextInputLayout password;

    //Fire base variable
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeClassVariables();

        setSignUpTextView();

        setLoginButton();
    }

    private void setLoginButton(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getEditText().getText().toString();
                String pass = password.getEditText().getText().toString();
                mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent go_home_screen = new Intent(v.getContext(),HomeActivity.class);
                            startActivity(go_home_screen);
                        }
                        else {
                            Log.d(TAG,task.getException().toString());
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
                            dlgAlert.setCancelable(true);
                            dlgAlert.setMessage("Wrong password or email");
                            dlgAlert.setTitle("Error Message");
                            dlgAlert.setPositiveButton("OK", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                            dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void setSignUpTextView(){
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_new_client = new Intent(v.getContext(),NewClientActivity.class);
                startActivity(go_new_client);
            }
        });
    }

    private void initializeClassVariables(){

        login = (Button)findViewById(R.id.login);
        sign_up = (TextView) findViewById(R.id.signUp);
        email =  findViewById(R.id.enterEmail);
        password = findViewById(R.id.enterPassword);
        mAuth = FirebaseAuth.getInstance();
    }
}
