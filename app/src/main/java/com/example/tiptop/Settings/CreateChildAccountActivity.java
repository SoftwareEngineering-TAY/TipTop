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
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.LogInAndSignUp.HomeActivity;
import com.example.tiptop.Objects.User;
import com.example.tiptop.R;
import java.util.Calendar;
import static com.example.tiptop.Database.Database.createUserInFireBase;
import static com.example.tiptop.Database.Database.getCurrFamilyId;
import static com.example.tiptop.Database.Database.getFamilyName;
import static com.example.tiptop.Database.Database.getRouteType;

public class CreateChildAccountActivity extends AppCompatActivity implements DataChangeListener {

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
        notifyOnChange();
    }

    /**
     * This function initializes all the required fields from the relevant XML file And of the class
     */
    private void initializeClassVariables(){
        next = (Button)findViewById(R.id.next);
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.newPassword);
        birthday = (EditText) findViewById(R.id.birthday);
        user = new User();
    }

    /**
     * The function is responsible for opening the birthday date dialog
     */
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

    /**
     * The function listens to the click of a continue button and saves all the information entered
     * about the user within a user object, then saves it in the database and moves the user back to
     * the home screen.
     */
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

    /**
     * The function verifies that a name has been entered and that this field is not empty
     * @return True if the name not empty and false if the name is empty
     */
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

    /**
     * The function verifies that a username has been entered and in good format and that this field is not empty
     * @return True if the username not empty and false if the username is empty or bad format
     */
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

    /**
     * The function verifies that a password has been entered and that this field is not empty
     * @return True if the password not empty and false if  the password is empty
     */
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

    @Override
    public void notifyOnChange() {
        setSelectDateButton();
        setContinueButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Database.addListener(this);
    }

    @Override
    protected void onPause() {
        Database.removeListener(this);
        super.onPause();
    }
}
