package com.example.tiptop;

        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ListView;
        import android.widget.Spinner;
        import android.widget.TextView;

        import androidx.appcompat.app.AppCompatActivity;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

public class TaskInfoActivity extends AppCompatActivity {

    private Task task_to_show;

    private TextView task_name;
    private TextView bonus_score;
    private Button done_task;

    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        Bundle extras = getIntent().getExtras();

        task_to_show = (Task) extras.get("task");

        initializeClassVariables();

        setTextInfo();

        setDoneButton();
    }

    private void setDoneButton() {
        done_task.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                task_to_show.setStatus(Task.STATUS.WaitingForApproval);
                //add intent!!!!!
            }
        });

    }

    private void setTextInfo() {
        task_name.setText(task_to_show.getNameTask());
        bonus_score.setText(task_to_show.getBonusScore().toString());
    }

    private void initializeClassVariables(){
        task_name = (TextView)findViewById(R.id.TaskNameShow);
        bonus_score = (TextView)findViewById(R.id.BonusPointShow);
        done_task = (Button)findViewById(R.id.TaskDone);

        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference = root.getReference();

    }
}
