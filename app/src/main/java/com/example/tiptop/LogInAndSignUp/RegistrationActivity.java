package com.example.tiptop.LogInAndSignUp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import com.example.tiptop.Objects.User;
import com.example.tiptop.R;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "NewClientActivity";

    //xml variables
    private Button next;
//    private Button select_date;
    private TextView login;
    private EditText birthday;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirm_password ;

    //Fire base variables
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);
        initializeClassVariables();
        setSelectDateButton();
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

    private void setSelectDateButton(){
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String zeroMonth = "";
                                String zeroDay = "";
                                if (monthOfYear<9) zeroMonth = "0";
                                if (dayOfMonth < 10) zeroDay = "0";
                                birthday.setText(year + "-" + zeroMonth + (monthOfYear + 1) + "-" +zeroDay + dayOfMonth);
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

                Log.d(TAG, "onClick: nxt button has been clicked");

                if(!validateName() || !validateEmail() || !validatePassword() || !validateConfirmPassword()){
                    return;
                }

                String user_name = name.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String birth = birthday.getText().toString();
                user.setName(user_name);
                user.setEmail(mail);
                user.setPassword(pass);
                user.setBirthday(birth);
                user.setType("Parent");

                Intent go_new_id_family = new Intent(RegistrationActivity.this, CreateFamilyActivity.class);
                go_new_id_family.putExtra("user",user);
                startActivity(go_new_id_family);
            }
        });
    }

    private void initializeClassVariables(){
        next = (Button)findViewById(R.id.next);
        login = (TextView)findViewById(R.id.login);
        name = findViewById(R.id.name);
        email = findViewById(R.id.newEmail);
        password = findViewById(R.id.newPassword);
        confirm_password = findViewById(R.id.confirmPassword);
        birthday = (EditText) findViewById(R.id.birthday);
//        select_date = (Button) findViewById(R.id.selectDate);
        user = new User();
    }

    private boolean validateName(){
        String user_name = name.getText().toString();
        if(user_name.isEmpty()){
            name.setError("Field cannot be empty");
            return false;
        }
        else{
            name.setError(null);
            return true;
        }
    }

    private boolean validateEmail(){
        String mail = email.getText().toString();
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
        String pass = password.getText().toString();
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
        String pass = password.getText().toString();
        String cpass = confirm_password.getText().toString();
        if(!pass.equals(cpass)){
            confirm_password.setError("Your passwords should be the same");
            return false;
        }
        else{
            confirm_password.setError(null);
            return true;
        }
    }

}
