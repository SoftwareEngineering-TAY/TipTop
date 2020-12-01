package com.example.tiptop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

    //xml variables
    private Button next;
    private TextView login;
    private com.google.android.material.textfield.TextInputLayout email;
    private com.google.android.material.textfield.TextInputLayout password;
    private com.google.android.material.textfield.TextInputLayout confirm_password ;
    private com.google.android.material.textfield.TextInputLayout birthday;
    private Spinner type_spinner;

    //Fire base variables
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_client);

        initializeClassVariables();

        setSpinner();

        setContinueButton();

        setLoginButton();
    }

    private void setLoginButton(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_to_login_activity = new Intent(v.getContext(),LoginActivity.class);
                startActivity(back_to_login_activity);
            }
        });
    }

    private void setContinueButton(){
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: nxt button has been clicked");

                if(!validateEmail() || !validatePassword() || !validateConfirmPassword() || !validateBirthday()){
                    return;
                }

                String mail = email.getEditText().getText().toString();
                String pass = password.getEditText().getText().toString();
                String birth = birthday.getEditText().getText().toString();
                user.setEmail(mail);
                user.setPassword(pass);
                user.setBirthday(birth);

                Intent go_id_family = new Intent(v.getContext(), FamilyIDActivity.class);

                go_id_family.putExtra("user",user);

                startActivity(go_id_family);
            }
        });
    }

    private void setSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewClientActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Type));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(adapter);

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: From spinner item selected");
                user.setType((String) parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initializeClassVariables(){
        next = (Button)findViewById(R.id.next);
        login = (TextView)findViewById(R.id.login);
        email = findViewById(R.id.newEmail);
        password = findViewById(R.id.newPassword);
        confirm_password = findViewById(R.id.confirmPassword);
        birthday = findViewById(R.id.birthday);
        type_spinner = (Spinner) findViewById(R.id.type_spinner);
        user = new User();
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
