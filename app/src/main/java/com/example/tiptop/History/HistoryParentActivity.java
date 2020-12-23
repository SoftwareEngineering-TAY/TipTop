package com.example.tiptop.History;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.FollowUp.ApproveTaskActivity;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.tiptop.Database.Database.updateExpandableTaskListFromDB;

public class HistoryParentActivity extends AppCompatActivity {

    private EditText search;
    private ExpandableListView AssociatedTasks;
    private ArrayList<String> ListChildForTask;
    private HashMap<String,ArrayList<Task>> ListTaskGroups;
    private HashMap<String,ArrayList<String>> ListTaskID;

    private TaskToChildExtendListAdapter childAdapter;
    private String currFamilyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initializeClassVariables();

        createExpandableListOfTask();

        updateExpandableTaskListFromDB(ListChildForTask,ListTaskGroups,ListTaskID,currFamilyId,"Confirmed",childAdapter);
    }

    private void initializeClassVariables(){
        search = findViewById(R.id.searchEditText);
        AssociatedTasks = (ExpandableListView) findViewById(R.id.ListHistoryTasks);

        Bundle extras = getIntent().getExtras();
        currFamilyId = (String) extras.get("currFamilyId");
    }

    private void createExpandableListOfTask() {
        ListChildForTask = new ArrayList<>(); //list group
        ListTaskGroups = new HashMap<>(); //list child
        ListTaskID = new HashMap<>();//list of ID'S Tasks

        childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,ListTaskID,currFamilyId, ApproveTaskActivity.class);
        AssociatedTasks.setAdapter(childAdapter);
    }

}
