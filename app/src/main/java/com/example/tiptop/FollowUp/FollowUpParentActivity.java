package com.example.tiptop.FollowUp;

import android.os.Build;
import android.os.Bundle;
import android.widget.ExpandableListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Adapters.TaskListAdapter;
import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database2;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.tiptop.Database.Database2.getRouteType;
import static com.example.tiptop.Database.Database2.updateExpandableTaskListFromDB;

public class FollowUpParentActivity extends AppCompatActivity implements DataChangeListener {

    private ExpandableListView AssociatedTasks;
    private ArrayList<String> ListChildForTask;
    private HashMap<String,ArrayList<Task>> ListTaskGroups;
    private HashMap<String,ArrayList<String>> ListTaskID;
    private TaskToChildExtendListAdapter childAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_parent);
        initializeClassVariables();
        createExpandableListOfTask();
        notifyOnChange();
    }

    private void initializeClassVariables() {
        AssociatedTasks = (ExpandableListView) findViewById(R.id.followExpandableList);
    }

    private void createExpandableListOfTask() {
        ListChildForTask = new ArrayList<>(); //list group
        ListTaskGroups = new HashMap<>(); //list child
        ListTaskID = new HashMap<>();//list of ID'S Tasks
        if(getRouteType().equals("With bonuses")) {
            childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,ListTaskID,R.layout.row_task_with_bonus, ApproveTaskActivity.class);
        }
        else
        {
            childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,ListTaskID,R.layout.row_task_without_bonus, ApproveTaskActivity.class);
        }
        AssociatedTasks.setAdapter(childAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void notifyOnChange() {
        updateExpandableTaskListFromDB(ListChildForTask,ListTaskGroups,ListTaskID,"WaitingForApproval",childAdapter,365,false);
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
