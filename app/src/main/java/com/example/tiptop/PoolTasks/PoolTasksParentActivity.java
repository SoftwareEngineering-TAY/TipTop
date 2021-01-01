package com.example.tiptop.PoolTasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Adapters.TaskListAdapter;
import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.tiptop.Database.Database.updateExpandableTaskListFromDB;
import static com.example.tiptop.Database.Database.updateTaskListFromDB;

public class PoolTasksParentActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_parent);
        initializeClassVariables();
        createListOfTask();
        createExpandableListOfTask();
        updateExpandableTaskListFromDB(ListChildForTask,ListTaskGroups,ListTaskID,"Associated",childAdapter);
        crateClickEvent();
        updateTaskListFromDB(ListUnassignedTasks,ListUnassignedTaskId,"NotAssociated",adapter);
        addButtonFunc();
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
}
