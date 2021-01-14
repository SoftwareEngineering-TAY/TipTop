package com.example.tiptop.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.LogInAndSignUp.LoginActivity;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.logout;
import static com.example.tiptop.Database.Database.setScreenViewByUser;

public class SettingParentActivity extends AppCompatActivity implements DataChangeListener {

    private EditText manage_circles;
    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initializeClassVariables();
        notifyOnChange();
    }

    private void initializeClassVariables(){

        manage_circles = (EditText) findViewById(R.id.manageCircles);
        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
    }

    private void setManageCirclesButton(){
        manage_circles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_manage_circles = new Intent(v.getContext(),ManageCirclesActivity.class);
                startActivity(go_manage_circles);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.logoutItem){
            Intent log_out = new Intent(this, LoginActivity.class);
            logout();
            startActivity(log_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void notifyOnChange() {
        setScreenViewByUser(name,email);
        setManageCirclesButton();
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
