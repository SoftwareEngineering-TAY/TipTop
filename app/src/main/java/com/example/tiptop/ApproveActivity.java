
package com.example.tiptop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ApproveActivity extends AppCompatActivity {

    private Task taskToShow;
    private String currFamilyId;

    private TextView task_name;
    private TextView bonus_score;
    private ImageView task_image;
    private TextView task_comment;
    private Button approve_task;
    private Button repeat_task;

    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

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
            String currFamilyidTemp = extras.getString("currFamilyId");
            if(extras.get("task")!=null)
            {
                taskToShow = (Task) extras.get("task");
            }
            if(currFamilyidTemp!=null)
            {
                currFamilyId=currFamilyidTemp;
            }
            else
            {
                Toast.makeText(this, "currFamily didn't pass, currFamily: "+currFamilyId, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeClassVariables(){
        task_name = (TextView)findViewById(R.id.TaskNameShow);
        bonus_score = (TextView)findViewById(R.id.BonusPointShow);
        task_image = (ImageView)findViewById(R.id.taskImage);
        task_comment = (TextView)findViewById(R.id.CommentShow);
        approve_task = (Button)findViewById(R.id.TaskApprove);
        repeat_task = (Button)findViewById(R.id.TaskRepeat);

        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference = root.getReference();

    }

    private void setTaskImage() {
      //  task_image.
        //get image!!
    }

    private void setRepeatButton() {
        repeat_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("Tasks").child(currFamilyId).child(taskToShow.getTaskId()).child("status").setValue("Associated");
                taskToShow.setStatus(Task.STATUS.Associated);
                //add intent!!!!!
            }
        });
    }

    private void setApproveButton() {
        approve_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("Tasks").child(currFamilyId).child(taskToShow.getTaskId()).child("status").setValue("Confirmed");
                taskToShow.setStatus(Task.STATUS.Confirmed);
                //add intent!!!!!
            }
        });
    }


    private void setTextInfo() {
        task_name.setText(taskToShow.getNameTask());
        bonus_score.setText(taskToShow.getBonusScore().toString());
        task_comment.setText(taskToShow.getComment());
    }


}
