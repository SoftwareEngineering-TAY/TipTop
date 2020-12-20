package com.example.tiptop.PoolTasks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaskInfoChildActivity  extends AppCompatActivity {

    private Task taskToShow;
    private String currFamilyId;

    private TextView task_name;
    private TextView bonus_score;
    private TextView description;
    private Button done_task;

    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_child);

        getExtraFromIntent();

        initializeClassVariables();

        setTextInfo();

        setDoneButton();
    }

    private void getExtraFromIntent() {
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
        description = (TextView)findViewById(R.id.DescriptionShow);
        done_task = (Button)findViewById(R.id.TaskDone);

        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference = root.getReference();

    }
    private void setTextInfo() {
        task_name.setText(taskToShow.getNameTask());
        bonus_score.setText(taskToShow.getBonusScore().toString());
        description.setText(taskToShow.getComment());
    }

    private void setDoneButton() {
        done_task.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                reference.child("Tasks").child(currFamilyId).child(taskToShow.getTaskId()).child("status").setValue("WaitingForApproval");
                taskToShow.setStatus(Task.STATUS.WaitingForApproval);
                Log.v("currFamilyId: ",currFamilyId);
                //add intent!!!!!
                Intent i = new Intent(v.getContext(), PoolTasksChildActivity.class);
                i.putExtra("currFamilyId", currFamilyId);
                startActivity(i);
            }
        });

    }




}