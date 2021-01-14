package com.example.tiptop.LogInAndSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.login;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView sign_up;
    private com.google.android.material.textfield.TextInputLayout email;
    private com.google.android.material.textfield.TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeClassVariables();
        setSignUpTextView();
        setLoginButton();
    }

    /**
     * This function initializes all the required fields from the relevant XML file
     */
    private void initializeClassVariables(){
        login = (Button)findViewById(R.id.login);
        sign_up = (TextView) findViewById(R.id.signUp);
        email =  findViewById(R.id.enterEmail);
        password = findViewById(R.id.enterPassword);
    }

    private void setLoginButton(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getEditText().getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(!mail.matches(emailPattern)){
                    mail = mail+"@mail.com";
                }
                String pass = password.getEditText().getText().toString();
                login(mail,pass,v,LoginActivity.this);
            }
        });
    }

    private void setSignUpTextView(){
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_new_client = new Intent(v.getContext(), RegistrationActivity.class);
                startActivity(go_new_client);
            }
        });
    }

}
