package com.example.tiptop.FollowUp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Adapters.TaskListAdapter;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.PoolTasks.TaskInfoChildActivity;
import com.example.tiptop.R;
import java.util.ArrayList;
import static com.example.tiptop.Database.Database.getPermission;
import static com.example.tiptop.Database.Database.getRouteType;
import static com.example.tiptop.Database.Database.updateTaskListFromDB;

public class FollowUpChildActivity extends AppCompatActivity implements DataChangeListener {

    //Fields
    private ListView followList;
    private TaskListAdapter mTaskListAdapter;
    private ArrayList<Task> list;
    private ArrayList<String> listID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup);
        initializeClassVariables();
        notifyOnChange();
    }

    /**
     * This function initializes all the required fields from the relevant XML file And class variables
     */
    private void initializeClassVariables() {
        followList = (ListView) findViewById(R.id.followList);
    }

    /**
     * The function creates the child's task's list
     */
    private void createListOfTask() {
        list = new ArrayList<>();
        listID = new ArrayList<>();
        if(getRouteType().equals("With bonuses")) {
            mTaskListAdapter = new TaskListAdapter(getApplicationContext(), R.layout.row_task_with_bonus, list);
        }
        else
        {
            mTaskListAdapter = new TaskListAdapter(getApplicationContext(), R.layout.row_task_without_bonus, list);
        }
        followList.setAdapter(mTaskListAdapter);
    }

    /**
     * A function responsible for listening to clicks on the task list. When we detect a click,
     * The user goes to a screen where there is a detail about the task that has been pressed
     */
    private void crateClickEvent() {
        followList.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent;
            if (getPermission().equals("Parent")){
                intent = new Intent(view.getContext(), ApproveTaskActivity.class);
            }

            else if (getPermission().equals("Child")){
                intent = new Intent(view.getContext(), TaskInfoChildActivity.class);
            }
            else return;
            intent.putExtra("task",list.get(i));
            intent.putExtra("taskID",listID.get(i));
            startActivity(intent);
        });
    }

    @Override
    public void notifyOnChange() {
        createListOfTask();
        crateClickEvent();
        updateTaskListFromDB(list,listID,"WaitingForApproval",mTaskListAdapter);
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
