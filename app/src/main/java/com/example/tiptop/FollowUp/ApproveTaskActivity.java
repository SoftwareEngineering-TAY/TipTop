package com.example.tiptop.FollowUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;

import static com.example.tiptop.Database.Database.addPointsToChild;
import static com.example.tiptop.Database.Database.setStatus;

public class ApproveTaskActivity extends AppCompatActivity {

    private Task taskToShow;
    private String taskID;
    private TextView taskName;
    private TextView bonusScore;
    private ImageView taskImage;
    private TextView taskComment;
    private Button approveTask;
    private Button repeat_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_approve);
        getExtrasFromIntent();
        initializeClassVariables();
        setTextInfo();
        setTaskImage();
        setApproveButton();
        setRepeatButton();
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
        taskName = (TextView)findViewById(R.id.TaskNameShow);
        bonusScore = (TextView)findViewById(R.id.BonusPointShow);
        taskImage = (ImageView)findViewById(R.id.taskImage);
        taskComment = (TextView)findViewById(R.id.CommentShow);
        approveTask = (Button)findViewById(R.id.TaskApprove);
        repeat_task = (Button)findViewById(R.id.TaskRepeat);

    }

    private void setTaskImage() {
      //  task_image.
        //get image!!
    }

    private void setRepeatButton() {
        repeat_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(taskID,"Associated");
                Intent i = new Intent(v.getContext(), FollowUpParentActivity.class);
                startActivity(i);
            }
        });
    }

    private void setApproveButton() {
        approveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(taskID,"Confirmed");
                addPointsToChild(taskToShow);
                Intent i = new Intent(v.getContext(), FollowUpParentActivity.class);
                startActivity(i);
            }
        });
    }

    private void setTextInfo() {
        taskName.setText(taskToShow.getNameTask());
        bonusScore.setText(taskToShow.getBonusScore().toString());
        taskComment.setText(taskToShow.getComment());
    }
}
