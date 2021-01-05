package com.example.tiptop.PoolTasks;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;

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

import static com.example.tiptop.Database.Database2.getCurrFamilyId;
import static com.example.tiptop.Database.Database2.setCurrFamilyId;
import static com.example.tiptop.Database.Database2.updateExpandableTaskListFromDB;
import static com.example.tiptop.Database.Database2.updateTaskListFromDB;

public class PoolTasksParentActivity extends AppCompatActivity implements DataChangeListener {

    private ListView UnassignedTasks;
    private ExpandableListView AssociatedTasks;
    private ArrayList<Task> ListUnassignedTasks;
    private ArrayList<String> ListUnassignedTaskId;
    private ArrayList<String> ListChildForTask;
    private HashMap<String,ArrayList<Task>> ListTaskGroups;
    private HashMap<String,ArrayList<String>> ListTaskID;
    private TaskListAdapter adapter;
    private TaskToChildExtendListAdapter childAdapter;
    private Button addTaskButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_parent);
        initializeClassVariables();
        createListOfTask();
        createExpandableListOfTask();
        notifyOnChange();
    }

    private void createExpandableListOfTask() {
        ListChildForTask = new ArrayList<>(); //list group
        ListTaskGroups = new HashMap<>(); //list child
        ListTaskID = new HashMap<>();//list of ID'S Tasks
        childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,ListTaskID,R.layout.row_task_with_bonus, TaskInfoParentActivity.class);
        AssociatedTasks.setAdapter(childAdapter);
    }

    private void initializeClassVariables() {
        UnassignedTasks = (ListView) findViewById(R.id.ListUnassignedTasks);
        AssociatedTasks = (ExpandableListView) findViewById(R.id.ListAssociatedTasks);
    }

    private void createListOfTask() {
        ListUnassignedTasks = new ArrayList<>();
        ListUnassignedTaskId = new ArrayList<>();
        adapter = new TaskListAdapter(getApplicationContext(),R.layout.row_task_with_bonus,ListUnassignedTasks);
        UnassignedTasks.setAdapter(adapter);
    }

    private void crateClickEvent() {
        UnassignedTasks.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent =new Intent(view.getContext(), TaskInfoParentActivity.class);
            ////Chang destination!!
            intent.putExtra("task",ListUnassignedTasks.get(i));
            intent.putExtra("taskID", ListUnassignedTaskId.get(i));
            startActivity(intent);
        });

    }

    private void addButtonFunc() {
        addTaskButton = (Button)findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),NewTaskActivity.class);
                startActivity(i);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void notifyOnChange() {
        updateExpandableTaskListFromDB(ListChildForTask,ListTaskGroups,ListTaskID,"Associated",childAdapter,365,false);
        crateClickEvent();
        updateTaskListFromDB(ListUnassignedTasks,ListUnassignedTaskId,"NotAssociated",adapter);
        addButtonFunc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Database2.addListener(this);
        setCurrFamilyId(getCurrFamilyId());
    }

    @Override
    protected void onPause() {
        Database2.removeListener(this);
        super.onPause();
    }
}
