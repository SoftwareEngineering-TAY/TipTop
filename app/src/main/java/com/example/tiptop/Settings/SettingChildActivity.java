package com.example.tiptop.Settings;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.R;
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

}
