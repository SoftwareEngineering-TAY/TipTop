package com.example.tiptop.FollowUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database2;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;

import static com.example.tiptop.Database.Database2.addPointsToChild;
import static com.example.tiptop.Database.Database2.getRouteType;
import static com.example.tiptop.Database.Database2.setConfirmedDate;
import static com.example.tiptop.Database.Database2.setStatus;

public class ApproveTaskActivity extends AppCompatActivity implements DataChangeListener {

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
        if(getRouteType().equals("With bonuses"))
        {
            setContentView(R.layout.activity_task_approve);
        }
        else
        {
            setContentView(R.layout.activity_task_approve_no_bonus);
        }
        getExtrasFromIntent();
        initializeClassVariables();
        notifyOnChange();
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
        taskImage = (ImageView)findViewById(R.id.taskImage);
        taskComment = (TextView)findViewById(R.id.CommentShow);
        approveTask = (Button)findViewById(R.id.TaskApprove);
        repeat_task = (Button)findViewById(R.id.TaskRepeat);
        if(getRouteType().equals("With bonuses")) {
            bonusScore = (TextView) findViewById(R.id.BonusPointShow);
        }

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
                setConfirmedDate(taskID);
                if(getRouteType().equals("With bonuses")) {
                    addPointsToChild(taskToShow);
                }
                Intent i = new Intent(v.getContext(), FollowUpParentActivity.class);
                startActivity(i);
            }
        });
    }

    private void setTextInfo() {
        taskName.setText(taskToShow.getNameTask());
        if(getRouteType().equals("With bonuses")) {
            bonusScore.setText(taskToShow.getBonusScore().toString());
        }
        taskComment.setText(taskToShow.getComment());
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

    @Override
    public void notifyOnChange() {
        setTextInfo();
        setTaskImage();
        setApproveButton();
        setRepeatButton();
    }
}
