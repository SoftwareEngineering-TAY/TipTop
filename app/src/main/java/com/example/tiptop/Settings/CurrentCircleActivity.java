package com.example.tiptop.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.getFamilyName;

public class CurrentCircleActivity extends AppCompatActivity implements DataChangeListener {

    private TextView family_name;
    private ListView circle_information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_circle);
        initializeClassVariables();
        notifyOnChange();
    }

    /**
     * This function initializes all the required fields from the relevant XML file And of the class
     */
    private void initializeClassVariables(){
        family_name = (TextView)findViewById(R.id.familyName);
        circle_information = (ListView)findViewById(R.id.circleInformation);
        family_name.setText(getFamilyName());
    }

    /**
     * The function produces a list of choices between MembersActivity and ParentRightsActivity and
     * depending on the click moves the user to the correct activity
     */
    private void setCirclesList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CurrentCircleActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.CircleInformation));
        circle_information.setAdapter(adapter);
        circle_information.setOnItemClickListener((adapterView,view,i,l) -> {
            if(i==0) {
                Intent go_to_members = new Intent(CurrentCircleActivity.this, MembersActivity.class);
                startActivity(go_to_members);
            }
            else if(i==1){
                Intent go_to_parent_rights = new Intent(CurrentCircleActivity.this, ParentRightsActivity.class);
                startActivity(go_to_parent_rights);
            }
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



