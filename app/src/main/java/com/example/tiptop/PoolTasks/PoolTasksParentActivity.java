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
import com.example.tiptop.Database.Database;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.tiptop.Database.Database.getCurrFamilyId;
import static com.example.tiptop.Database.Database.getRouteType;
import static com.example.tiptop.Database.Database.setCurrFamilyId;
import static com.example.tiptop.Database.Database.updateExpandableTaskListFromDB;
import static com.example.tiptop.Database.Database.updateTaskListFromDB;

public class PoolTasksParentActivity extends AppCompatActivity implements DataChangeListener {

    //Fields
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
        initializationFromXML();
        createListOfTask();
        createExpandableListOfTask();
        notifyOnChange();
    }

    /**
     * This function initializes all the required fields from the relevant XML file
     */
    private void initializationFromXML() {
        UnassignedTasks = (ListView) findViewById(R.id.ListUnassignedTasks);
        AssociatedTasks = (ExpandableListView) findViewById(R.id.ListAssociatedTasks);
        addTaskButton = (Button)findViewById(R.id.addTaskButton);
    }

    /**
     *The function is responsible for creating the expanding list in which the names of all the
     * children and within each child's tab there is a list of all his tasks in status Associated
     */
    private void createExpandableListOfTask() {
        ListChildForTask = new ArrayList<>(); //list group
        ListTaskGroups = new HashMap<>(); //list child
        ListTaskID = new HashMap<>();//list of ID'S Tasks
        if(getRouteType().equals("With bonuses")) {
            childAdapter = new TaskToChildExtendListAdapter(ListChildForTask, ListTaskGroups, ListTaskID, R.layout.row_task_with_bonus, TaskInfoParentActivity.class);
        }
        else {
            childAdapter = new TaskToChildExtendListAdapter(ListChildForTask, ListTaskGroups, ListTaskID, R.layout.row_task_without_bonus, TaskInfoParentActivity.class);
        }
        AssociatedTasks.setAdapter(childAdapter);
    }

    /**
     * The function is responsible for creating the list of tasks that have not been assigned to any child, ie in Not associated status
     */
    private void createListOfTask() {
        ListUnassignedTasks = new ArrayList<>();
        ListUnassignedTaskId = new ArrayList<>();
        if(getRouteType().equals("With bonuses")) {
            adapter = new TaskListAdapter(getApplicationContext(), R.layout.row_task_with_bonus, ListUnassignedTasks);
        }
        else {
            adapter = new TaskListAdapter(getApplicationContext(), R.layout.row_task_without_bonus, ListUnassignedTasks);
        }
        UnassignedTasks.setAdapter(adapter);
    }

    /**
     * The function is responsible for listening to a click on the unassigned task list and if a
     * click was made to move a screen to a screen where the information of the task appears
     */
    private void crateClickEvent() {
        UnassignedTasks.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent =new Intent(view.getContext(), TaskInfoParentActivity.class);
            ////Chang destination!!
            intent.putExtra("task",ListUnassignedTasks.get(i));
            intent.putExtra("taskID", ListUnassignedTaskId.get(i));
            startActivity(intent);
        });

    }

    /**
     * The function is responsible for move to the activity of creating a new task
     */
    private void addButtonFunc() {
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
        Database.addListener(this);
        setCurrFamilyId(getCurrFamilyId());
    }

    @Override
    protected void onPause() {
        Database.removeListener(this);
        super.onPause();
    }
}
