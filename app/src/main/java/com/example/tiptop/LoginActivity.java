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

public class LoginActivity extends AppCompatActivity {

    private Button login = null;
    private TextView sign_up = null;
    private com.google.android.material.textfield.TextInputLayout email = null;
    private com.google.android.material.textfield.TextInputLayout password = null;

    private FirebaseAuth dbA = null;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.login);
        sign_up = (TextView) findViewById(R.id.signUp);
        email =  findViewById(R.id.enterEmail);
        password = findViewById(R.id.enterPassword);

        dbA = FirebaseAuth.getInstance();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),NewClientActivity.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getEditText().getText().toString();
                String pass = password.getEditText().getText().toString();
                dbA.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent i = new Intent(v.getContext(),HomeActivity.class);
                            startActivity(i);
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
}
