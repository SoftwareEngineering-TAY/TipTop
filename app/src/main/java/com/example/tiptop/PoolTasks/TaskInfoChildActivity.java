package com.example.tiptop.PoolTasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database2;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database2.setStatus;

public class TaskInfoChildActivity  extends AppCompatActivity implements DataChangeListener {

    private Task taskToShow;
    private String taskID;
    private TextView taskName;
    private TextView bonusScore;
    private TextView comment;
    private Button doneTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_child);
        getExtraFromIntent();
        initializeClassVariables();
        notifyOnChange();
    }

    private void getExtraFromIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            if(extras.get("task")!=null) {
                taskToShow = (Task) extras.get("task");
            }
            if(extras.get("taskID")!=null) {
                taskID = extras.getString("taskID");
            }
            else {
                Toast.makeText(this, "currFamily didn't pass, currFamily: ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeClassVariables(){
        taskName = (TextView)findViewById(R.id.TaskNameShow);
        bonusScore = (TextView)findViewById(R.id.BonusPointShow);
        comment = (TextView)findViewById(R.id.DescriptionShow);
        doneTask = (Button)findViewById(R.id.TaskDone);

    }
    private void setTextInfo() {
        taskName.setText(taskToShow.getNameTask());
        bonusScore.setText(taskToShow.getBonusScore().toString());
        comment.setText(taskToShow.getComment());
    }

    private void setDoneButton() {
        doneTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setStatus(taskID,"WaitingForApproval");
                Intent i = new Intent(v.getContext(), PoolTasksChildActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void notifyOnChange() {
        setTextInfo();
        setDoneButton();
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