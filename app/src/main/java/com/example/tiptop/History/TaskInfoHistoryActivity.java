package com.example.tiptop.History;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.getRouteType;

public class TaskInfoHistoryActivity extends AppCompatActivity {

    private Task taskToShow;
    private TextView taskName;
    private TextView taskBonus;
    private TextView taskApproval;
    private TextView taskComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRouteType().equals("With bonuses")) {
            setContentView(R.layout.activity_task_info_history);
        }
        else {
            setContentView(R.layout.activity_task_info_history_no_bonus);
        }
        getExtrasFromIntent();
        initializeClassVariables();
        setTextInfo();
    }

    private void getExtrasFromIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            if(extras.get("task")!=null){
                taskToShow = (Task) extras.get("task");
            }
        }
    }

    private void initializeClassVariables(){
        ImageView taskImage = (ImageView) findViewById(R.id.taskImage);
        taskName = (TextView)findViewById(R.id.taskName);
        taskApproval = (TextView)findViewById(R.id.taskApproval);
        taskComment = (TextView)findViewById(R.id.taskComment);
        if(getRouteType().equals("With bonuses")) {
            taskBonus = (TextView) findViewById(R.id.taskBonus);
        }
    }

    private void setTextInfo() {
        taskName.setText(taskToShow.getNameTask());
        taskApproval.setText(taskToShow.getConfirmedDate());
        taskComment.setText(taskToShow.getComment());
        if(getRouteType().equals("With bonuses")) {
            taskBonus.setText(taskToShow.getBonusScore().toString());
        }
    }
}
