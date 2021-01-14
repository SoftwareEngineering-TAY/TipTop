package com.example.tiptop.History;

import android.os.Build;
import android.os.Bundle;
import android.widget.ExpandableListView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.tiptop.Database.Database.updateExpandableTaskListFromDB;

public class HistoryParentActivity extends AppCompatActivity implements DataChangeListener {

    private ExpandableListView AssociatedTasks;
    private ArrayList<String> ListChildForTask;
    private HashMap<String,ArrayList<Task>> ListTaskGroups;
    private HashMap<String,ArrayList<String>> ListTaskID;
    private TaskToChildExtendListAdapter childAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_parent);
        initializeClassVariables();
        createExpandableListOfTask();
        notifyOnChange();
    }

    /**
     * This function initializes all the required fields from the relevant XML file
     */
    private void initializeClassVariables(){
        AssociatedTasks = (ExpandableListView) findViewById(R.id.ListHistoryTasks);
    }

    /**
     *The function is responsible for creating the expanding list in which the names of all the
     * children and within each child's tab there is a list of all his tasks in status Confirmed
     */
    private void createExpandableListOfTask() {
        ListChildForTask = new ArrayList<>(); //list group
        ListTaskGroups = new HashMap<>(); //list child
        ListTaskID = new HashMap<>();//list of ID'S Tasks
        childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,ListTaskID,R.layout.row_task_history, TaskInfoHistoryActivity.class);
        AssociatedTasks.setAdapter(childAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void notifyOnChange() {
        updateExpandableTaskListFromDB(ListChildForTask,ListTaskGroups,ListTaskID,"Confirmed",childAdapter,10, true);
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
