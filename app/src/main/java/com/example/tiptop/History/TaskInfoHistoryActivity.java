package com.example.tiptop.History;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;

public class TaskInfoHistoryActivity extends AppCompatActivity {

    private Task taskToShow;
    private String taskID;
    private ImageView taskImage;
    private TextView taskName;
    private TextView taskBonus;
    private TextView taskApproval;
    private TextView taskComment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_history);

        getExtrasFromIntent();

        initializeClassVariables();

        setTextInfo();
    }

    private void getExtrasFromIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            if(extras.get("task")!=null){
                taskToShow = (Task) extras.get("task");
            }
            if(extras.get("taskID")!=null)
            {
                taskID = extras.getString("taskID");
            }
        }
    }

    private void initializeClassVariables(){
        taskImage = (ImageView)findViewById(R.id.taskImage);
        taskName = (TextView)findViewById(R.id.taskName);
        taskBonus = (TextView)findViewById(R.id.taskBonus);
        taskApproval = (TextView)findViewById(R.id.taskApproval);
        taskComment = (TextView)findViewById(R.id.taskComment);
    }

    private void setTextInfo() {
        taskName.setText(taskToShow.getNameTask());
        taskBonus.setText(taskToShow.getBonusScore().toString());
        taskApproval.setText(taskToShow.getConfirmedDate());
        taskComment.setText(taskToShow.getComment());
    }
}
