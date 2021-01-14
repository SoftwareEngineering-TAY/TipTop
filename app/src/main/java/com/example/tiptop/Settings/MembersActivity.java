package com.example.tiptop.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database2.setScreenViewByFamily;

public class MembersActivity extends AppCompatActivity {

    private TextView family_members;
    private ListView circle_members;
    private Button create_a_child_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        initializeClassVariables();
        setScreenViewByFamily(this, circle_members, family_members);
        setCreateAChildAccountButton();
    }

    private void initializeClassVariables(){
        family_members = (TextView)findViewById(R.id.familyMembers);
        circle_members = (ListView)findViewById(R.id.circleMembers);
        create_a_child_account = (Button)findViewById(R.id.inviteNewMember);
    }

    private void setCreateAChildAccountButton(){
        create_a_child_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_child_account = new Intent(MembersActivity.this,CreateChildAccountActivity.class);
                startActivity(go_to_child_account);
            }
        });
    }
}
