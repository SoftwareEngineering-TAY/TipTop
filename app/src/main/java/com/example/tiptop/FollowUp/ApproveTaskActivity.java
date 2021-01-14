package com.example.tiptop.FollowUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;

import static com.example.tiptop.Database.Database.addPointsToChild;
import static com.example.tiptop.Database.Database.getRouteType;
import static com.example.tiptop.Database.Database.setConfirmedDate;
import static com.example.tiptop.Database.Database.setStatus;
import static com.example.tiptop.Database.Database.updateImageView;

public class ApproveTaskActivity extends AppCompatActivity implements DataChangeListener {

    //Fields
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

    /**
     * The function is responsible for retrieving information from the Intent
     */
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

    /**
     * This function initializes all the required fields from the relevant XML file And class variables
     */
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

    /**
     * The function is responsible for listening to the repeat button, if he is pressed to update the
     * status of the task back to "associated" and move in to the follow up screen
     */
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

    /**
     * The function is responsible for listening to the approve button, if he is pressed to update the
     * status of the task to "Confirmed" and move in to the follow up screen
     */
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

    /**
     * The function is responsible for displaying the name, comment and bonus if necessary in the activity
     */
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
        Database.addListener(this);
    }

    @Override
    protected void onPause() {
        Database.removeListener(this);
        super.onPause();
    }

    @Override
    public void notifyOnChange() {
        setTextInfo();
        updateImageView(taskImage, getApplicationContext(),taskID,"taskImage");
        setApproveButton();
        setRepeatButton();
    }
}
