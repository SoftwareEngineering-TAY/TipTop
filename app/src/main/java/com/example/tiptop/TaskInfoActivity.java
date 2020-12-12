package com.example.tiptop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TaskInfoActivity extends AppCompatActivity {

    private Task taskToShow;
    private String currFamilyId;

    private String infoParentSpinner;

    private com.google.android.material.textfield.TextInputLayout taskName;
    private Button updateTaskName;
    private com.google.android.material.textfield.TextInputLayout bonusScore;
    private Button updateBonusScore;
    private ImageButton taskImage;
    private Spinner chooseChild;
    private com.google.android.material.textfield.TextInputLayout Description;
    private Button updateDescription;

    private static final String TAG = "TaskInfoActivity";
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_parent);

        getExtraFromIntent();

        initializeClassVariables();
        getExtraFromIntent();
        updateInfo();

        updateNameOfTask();


    }

    private void getExtraFromIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            String currFamilyidTemp = extras.getString("currFamilyId");
            Log.d(TAG, "getExtraFromIntent: currfamilyid" + currFamilyidTemp);
            if(extras.get("task")!=null)
            {
                Log.d(TAG, "getExtraFromIntent:task to show: " + extras.get("task"));
                taskToShow = (Task) extras.get("task");
            }

            if(currFamilyidTemp!=null)
            {
                Log.d(TAG, "getExtraFromIntent: currfamilyid" + currFamilyidTemp);
                currFamilyId=currFamilyidTemp;
            }
            else
            {
                Toast.makeText(this, "currFamily didn't pass", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateNameOfTask() {
        updateTaskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameToUpdate = taskName.getEditText().getText().toString();
                System.out.println("currFamilyId: "+ currFamilyId );
                reference.child("Tasks").child(currFamilyId).child(taskToShow.getTaskId()).child("nameTask").setValue(nameToUpdate);
            }
        });

    }

    private void updateInfo() {
        taskName.setHint(taskToShow.getNameTask());
        bonusScore.setHint(taskToShow.getBonusScore().toString());
        //Update Current Child Spinner
        System.out.println("taskToShow.getBelongsToUID():"+taskToShow.getBelongsToUID());
        if(taskToShow.getBelongsToUID()==null)
        {
            infoParentSpinner ="Not Associated";
        }
        else
        {
            reference.child("Users").child(taskToShow.getBelongsToUID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.getValue().equals("name") && ds.getValue() != null) {
                            infoParentSpinner = ds.getValue().toString();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        // need to update image!!
        Description.setHint(taskToShow.getComment());
    }


    private void initializeClassVariables(){
        taskName = findViewById(R.id.NameOfTask);
        updateTaskName = (Button) findViewById(R.id.UpdateName);
        bonusScore = findViewById(R.id.BonusPoints);
        updateBonusScore = (Button) findViewById(R.id.UpdateBonus);
        taskImage = (ImageButton) findViewById(R.id.AddPic);
        chooseChild = (Spinner) findViewById(R.id.SpinnerChooseChild);
        Description = findViewById(R.id.Description);
        updateDescription = (Button) findViewById(R.id.DescriptionUpdate);

        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference = root.getReference();


    }
}
