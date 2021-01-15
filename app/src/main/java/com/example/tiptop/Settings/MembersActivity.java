package com.example.tiptop.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.getScreenViewByFamily;

public class MembersActivity extends AppCompatActivity implements DataChangeListener {

    private TextView family_members;
    private ListView circle_members;
    private Button create_a_child_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        initializeClassVariables();
        notifyOnChange();
    }

    /**
     * This function initializes all the required fields from the relevant XML file And of the class
     */
    private void initializeClassVariables(){
        family_members = (TextView)findViewById(R.id.familyMembers);
        circle_members = (ListView)findViewById(R.id.circleMembers);
        create_a_child_account = (Button)findViewById(R.id.inviteNewMember);
    }

    /**
     * The function listens to the click on the button Create a new child and is responsible for
     * transferring the user to the correct screen.
     */
    private void setCreateAChildAccountButton(){
        create_a_child_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_child_account = new Intent(MembersActivity.this,CreateChildAccountActivity.class);
                startActivity(go_to_child_account);
            }
        });
    }

    @Override
    public void notifyOnChange() {
        getScreenViewByFamily(this, circle_members, family_members);
        setCreateAChildAccountButton();
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
