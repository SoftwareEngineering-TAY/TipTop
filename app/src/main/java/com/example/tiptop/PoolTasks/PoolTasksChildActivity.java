package com.example.tiptop.PoolTasks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Adapters.TaskListAdapter;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database2;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.util.ArrayList;

import static com.example.tiptop.Database.Database2.getRouteType;
import static com.example.tiptop.Database.Database2.updateTaskListFromDB;

public class PoolTasksChildActivity extends AppCompatActivity implements DataChangeListener {

    //Fields
    private ListView followList;
    private TaskListAdapter mTaskListAdapter;
    private ArrayList<Task> list;
    private ArrayList<String> listID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_child);
        initializationFromXML();
        createListOfTask();
        crateClickEvent();
        notifyOnChange();
    }

    /**
     * This function initializes all the required fields from the relevant XML file
     */
    private void initializationFromXML() {
        followList = (ListView) findViewById(R.id.followList);
    }

    /**
     * The function is responsible for creating the list of tasks of the child
     */
    private void createListOfTask() {
        list = new ArrayList<>();
        listID = new ArrayList<>();
        if(getRouteType().equals("With bonuses"))
        {
            mTaskListAdapter = new TaskListAdapter(getApplicationContext(),R.layout.row_task_with_bonus,list);
        }
        else
        {
            mTaskListAdapter = new TaskListAdapter(getApplicationContext(),R.layout.row_task_without_bonus,list);
        }
        followList.setAdapter(mTaskListAdapter);
    }

    /**
     * The function is responsible for listening to clicks on the tasks in the list and if one of the
     * tasks is clicked on the list then also on moving a screen to information about the task
     */
    private void crateClickEvent() {
        followList.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent = new Intent(view.getContext(), TaskInfoChildActivity.class);
            intent.putExtra("task",list.get(i));
            intent.putExtra("taskID",listID.get(i));
            startActivity(intent);
        });
    }

    @Override
    public void notifyOnChange() {
        updateTaskListFromDB(list,listID,"Associated",mTaskListAdapter);
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
