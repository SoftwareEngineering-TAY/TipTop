package com.example.tiptop.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.R;
import java.util.ArrayList;
import static com.example.tiptop.Database.Database.updateListOfFamilyFromDB;

public class ManageCirclesActivity extends AppCompatActivity implements DataChangeListener {

    private ListView circles_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_circles);
        initializeClassVariables();
        notifyOnChange();
    }

    private void initializeClassVariables(){
        circles_list = (ListView)findViewById(R.id.circlesList);
    }

    private void setCirclesList(){
        ArrayList<String> circlesArray = new ArrayList<>();
        ArrayList<String> familiesID = new ArrayList<>();
        ArrayAdapter circlesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, circlesArray);
        circles_list.setAdapter(circlesAdapter);
        updateListOfFamilyFromDB(familiesID , circlesArray , circlesAdapter);
        circles_list.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent go_to_current_circle = new Intent(ManageCirclesActivity.this,CurrentCircleActivity.class);
            startActivity(go_to_current_circle);
        });
    }

    @Override
    public void notifyOnChange() {
        setCirclesList();
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
