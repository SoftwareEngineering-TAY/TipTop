package com.example.tiptop.Settings;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database2;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database2.createSwitchForEveryUser;
import static com.example.tiptop.Database.Database2.getFamilyName;

public class ParentRightsActivity extends AppCompatActivity implements DataChangeListener {

    private TextView family_name;
    private LinearLayout parent_layout;
    private LinearLayout child_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_rights);
        initializeClassVariables();
        notifyOnChange();
    }

    private void initializeClassVariables(){
        parent_layout = (LinearLayout)findViewById(R.id.parentMembers);
        child_layout = (LinearLayout)findViewById(R.id.childMembers);
        family_name = (TextView)findViewById(R.id.familyName);

        family_name.setText(getFamilyName()+" Circle");
    }

    @Override
    public void notifyOnChange() {
        createSwitchForEveryUser(ParentRightsActivity.this, parent_layout, child_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Database2.addListener(this);
    }

    @Override
    protected void onPause() {
        Database2.removeListener(this);
        super.onPause();
    }
}
