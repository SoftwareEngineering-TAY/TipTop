package com.example.tiptop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
    private Button register = null;
    private FirebaseAuth dbA;
    private EditText email = null;
    private EditText password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        email = (EditText)findViewById(R.id.enterEmail);
        password = (EditText)findViewById(R.id.enterPassword);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),NewClientActivity.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==login){
                    String mail = email.getText().toString();
                    String pass = password.getText().toString();
                    dbA = FirebaseAuth.getInstance();
                    dbA.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Intent i = new Intent(v.getContext(),HomeActivity.class);
                                startActivity(i);
                            }
                            else {
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
            }
        });



    }
}
