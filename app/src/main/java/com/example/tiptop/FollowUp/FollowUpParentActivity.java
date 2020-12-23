package com.example.tiptop.FollowUp;

import android.os.Bundle;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.tiptop.Database.Database.updateExpandableTaskListFromDB;

public class FollowUpParentActivity extends AppCompatActivity {

    private ExpandableListView AssociatedTasks;
    private ArrayList<String> ListChildForTask;
    private HashMap<String,ArrayList<Task>> ListTaskGroups;
    private HashMap<String,ArrayList<String>> ListTaskID;
    private String currFamilyId;
    private TaskToChildExtendListAdapter childAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_parent);

        initializeClassVariables();

        createExpandableListOfTask();

        updateExpandableTaskListFromDB(ListChildForTask,ListTaskGroups,ListTaskID,currFamilyId,"WaitingForApproval",childAdapter);

    }

    private void initializeClassVariables() {
        AssociatedTasks = (ExpandableListView) findViewById(R.id.followExpandableList);

        Bundle extras = getIntent().getExtras();
        currFamilyId = extras.getString("currFamilyId");
    }

    private void createExpandableListOfTask() {
        ListChildForTask = new ArrayList<>(); //list group
        ListTaskGroups = new HashMap<>(); //list child
        ListTaskID = new HashMap<>();//list of ID'S Tasks
        childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,ListTaskID,currFamilyId, ApproveTaskActivity.class);
        AssociatedTasks.setAdapter(childAdapter);


    }

}
