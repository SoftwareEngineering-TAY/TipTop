package com.example.tiptop.History;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Adapters.TaskListAdapter;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.util.ArrayList;
import static com.example.tiptop.Database.Database.updateTaskListFromDB;

public class HistoryChildActivity extends AppCompatActivity implements DataChangeListener {

    private ListView historyList;
    private TaskListAdapter mTaskListAdapter;
    private ArrayList<Task> list;
    private ArrayList<String> listID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_child);
        initializeClassVariables();
        createListOfTask();
        notifyOnChange();
    }

    /**
     * This function initializes all the required fields from the relevant XML file
     */
    private void initializeClassVariables() {
        historyList = (ListView) findViewById(R.id.historyList);
    }

    /**
     * This function is responsible for creating a list.
     * The list will contain all the tasks in the Confirmed status
     */
    private void createListOfTask() {
        list = new ArrayList<>();
        listID = new ArrayList<>();
        mTaskListAdapter = new TaskListAdapter(getApplicationContext(),R.layout.row_task_history,list);
        historyList.setAdapter(mTaskListAdapter);
    }

    /**
     * The function is responsible for listening to a click on the history task list and if a
     * click was made to move a screen to a screen where the information of the task appears
     */
    private void crateClickEvent() {
        historyList.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent = new Intent(view.getContext(), TaskInfoHistoryActivity.class);
            intent.putExtra("task",list.get(i));
            intent.putExtra("taskID",listID.get(i));
            startActivity(intent);
        });
    }

    @Override
    public void notifyOnChange() {
        crateClickEvent();
        updateTaskListFromDB(list,listID,"Confirmed",mTaskListAdapter);
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
