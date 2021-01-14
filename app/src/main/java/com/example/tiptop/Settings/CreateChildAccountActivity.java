package com.example.tiptop.Settings;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.LogInAndSignUp.HomeActivity;
import com.example.tiptop.Objects.User;
import com.example.tiptop.R;
import java.util.Calendar;
import static com.example.tiptop.Database.Database2.createUserInFireBase;
import static com.example.tiptop.Database.Database2.getCurrFamilyId;
import static com.example.tiptop.Database.Database2.getFamilyName;
import static com.example.tiptop.Database.Database2.getRouteType;

public class CreateChildAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateChildAccountActivity";

    private Button next;
    private EditText birthday;
    private EditText name;
    private EditText username;
    private EditText password;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_child_account);
        initializeClassVariables();
        setSelectDateButton();
        setContinueButton();
    }

    private void initializeClassVariables(){
        next = (Button)findViewById(R.id.next);
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.newPassword);
        birthday = (EditText) findViewById(R.id.birthday);
        user = new User();
    }

    private void setSelectDateButton(){
        birthday.setOnClickListener(new View.OnClickListener() {
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
                                String zeroMonth = "";
                                String zeroDay = "";
                                if (month<9) zeroMonth = "0";
                                if (day < 10) zeroDay = "0";
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
                if(!validateName() || !validateUsername() || !validatePassword()){
                    return;
                }
                user.setName(name.getText().toString());
                user.setEmail(username.getText().toString() + "@mail.com");
                user.setPassword(password.getText().toString());
                user.setBirthday(birthday.getText().toString());
                user.setType("Child");

                createUserInFireBase(user,getFamilyName(),getRouteType(),null,null,getCurrFamilyId());

                Intent go_to_home = new Intent(CreateChildAccountActivity.this, HomeActivity.class);
                startActivity(go_to_home);
            }
        });
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

    private boolean validateUsername(){
        String mail = username.getText().toString();
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
}
