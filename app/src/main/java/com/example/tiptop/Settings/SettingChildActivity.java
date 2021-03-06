package com.example.tiptop.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.LogInAndSignUp.LoginActivity;
import com.example.tiptop.R;

import static com.example.tiptop.Database.Database.getName;
import static com.example.tiptop.Database.Database.logout;

public class SettingChildActivity extends AppCompatActivity implements DataChangeListener {

    private TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_children);
        initializeClassVariables();
        notifyOnChange();
    }

    /**
     * This function initializes all the required fields from the relevant XML file And of the class
     */
    private void initializeClassVariables(){
        name = (TextView)findViewById(R.id.name);
    }

    /**
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    /**
     * @param item
     * @return true if the item selected or false if not.
     */
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
        name.setText(getName());
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
