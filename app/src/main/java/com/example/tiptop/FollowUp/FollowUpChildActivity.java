package com.example.tiptop.FollowUp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Adapters.TaskListAdapter;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.PoolTasks.TaskInfoChildActivity;
import com.example.tiptop.R;
import java.util.ArrayList;
import static com.example.tiptop.Database.Database.updateTaskListFromDB;

public class FollowUpChildActivity extends AppCompatActivity {

    private ListView followList;
    private String currFamilyId;
    private String permission;
    private TaskListAdapter mTaskListAdapter;
    private ArrayList<Task> list;
    private ArrayList<String> listID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup);

        initializeClassVariables();

        createListOfTask();

        crateClickEvent();

        updateTaskListFromDB(list,listID,currFamilyId,permission,"WaitingForApproval",mTaskListAdapter);
    }

    private void initializeClassVariables() {
        followList = (ListView) findViewById(R.id.followList);

        Bundle extras = getIntent().getExtras();
        currFamilyId = extras.getString("currFamilyId");
        permission = extras.getString("permission");
    }

    private void createListOfTask() {
        list = new ArrayList<>();
        listID = new ArrayList<>();
        mTaskListAdapter = new TaskListAdapter(getApplicationContext(),R.layout.row_task,list);
        followList.setAdapter(mTaskListAdapter);
    }

    private void crateClickEvent() {
        followList.setOnItemClickListener((adapterView,view,i,l) -> {
            Intent intent;
            if (permission.equals("Parent")){
                intent = new Intent(view.getContext(), ApproveTaskActivity.class);
            }

            else if (permission.equals("Child")){
                intent = new Intent(view.getContext(), TaskInfoChildActivity.class);
            }
            else return;
            intent.putExtra("task",list.get(i));
            intent.putExtra("taskID",listID.get(i));
            intent.putExtra("currFamilyId",currFamilyId);
            startActivity(intent);
        });
    }
}
