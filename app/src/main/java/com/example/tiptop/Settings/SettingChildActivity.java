package com.example.tiptop.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.LogInAndSignUp.HomeActivity;
import com.example.tiptop.LogInAndSignUp.LoginActivity;
import com.example.tiptop.R;

import static com.example.tiptop.Database.Database.getPermission;
import static com.example.tiptop.Database.Database.logout;
import static com.example.tiptop.Database.Database.setScreenViewByUser;

public class SettingChildActivity extends AppCompatActivity {

    private TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_children);

        initializeClassVariables();

        setScreenViewByUser(name, null);
    }

    private void initializeClassVariables(){
        name = (TextView)findViewById(R.id.name);
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

}
