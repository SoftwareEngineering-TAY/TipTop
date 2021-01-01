package com.example.tiptop.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.setScreenViewByUser;

public class SettingParentActivity extends AppCompatActivity {

    private Button manage_circles;
    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        initializeClassVariables();

        setScreenViewByUser(name,email);

        setManageCirclesButton();
    }

    private void initializeClassVariables(){

        manage_circles = (Button)findViewById(R.id.manageCircles);
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

}
