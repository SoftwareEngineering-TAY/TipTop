package com.example.tiptop.PoolTasks;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import static com.example.tiptop.Database.Database.setStatus;
import static com.example.tiptop.Database.Database.setTaskBonus;
import static com.example.tiptop.Database.Database.setTaskDesctiption;
import static com.example.tiptop.Database.Database.setTaskName;
import static com.example.tiptop.Database.Database.setbelongsToUID;
import static com.example.tiptop.Database.Database.updateListOfChildFromDB;

public class TaskInfoParentActivity extends AppCompatActivity {

    private Task taskToShow;
    private String taskID;
    private com.google.android.material.textfield.TextInputLayout taskName;
    private Button taskNameButton;
    private com.google.android.material.textfield.TextInputLayout bonusScore;
    private Button bonusScoreButton;
    private ImageButton taskImage;
    private Spinner chooseChildSpinner;
    private com.google.android.material.textfield.TextInputLayout description;
    private Button descriptionButton;

    //Variables to be used for the spinner
    private ArrayList <String> allKeys;
    private ArrayList <String> allKids;
    private ArrayAdapter adapter;
    private String titleParentSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_parent);
        getExtraFromIntent();
        initializeClassVariables();
        getExtraFromIntent();
        updateInfo();
        updateNameOfTask();
        updateBonusScore();
        updateDescriptionButton();
        updateReAssociation();
    }

    private void getExtraFromIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            if(extras.get("task")!=null) {
                taskToShow = (Task) extras.get("task");
            }
            if(extras.get("taskID")!=null) {
                taskID = extras.getString("taskID");
            }
            else {
                Toast.makeText(this, " didn't pass", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void updateReAssociation() {
        //Initialize the 2 lists that will save all the last names and all the keys respectively.
        allKeys = new ArrayList<>();
        allKids = new ArrayList<>();

        //Connecting the list to the view
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allKids);
        chooseChildSpinner.setAdapter(adapter);

        //A function that retrieves all information from the database
        updateListOfChildFromDB(allKeys, allKids,adapter);

        //Defines the functionality of a last name click
        chooseChildSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                titleParentSpinner = allKids.get(position);
                if(position == 0) {
                    setStatus(taskID,"NotAssociated");
                    setbelongsToUID(taskID,null);
                }
                else {
                    setbelongsToUID(taskID,allKeys.get(position-1));
                    setStatus(taskID,"Associated");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void updateDescriptionButton() {
        descriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descriptionToUpdate = description.getEditText().getText().toString();
                setTaskDesctiption(taskID, descriptionToUpdate);
            }
        });
    }

    private void updateNameOfTask() {
        taskNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameToUpdate = taskName.getEditText().getText().toString();
                setTaskName(taskID, nameToUpdate);
            }
        });

    }

    private void updateBonusScore() {
        bonusScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bonusToUpdate =  Integer.parseInt(bonusScore.getEditText().getText().toString());
                setTaskBonus(taskID, bonusToUpdate);
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
            titleParentSpinner ="Not Associated";
        }
        else
        {
//            reference.child("Users").child(taskToShow.getBelongsToUID()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot ds : snapshot.getChildren()) {
//                        if (ds.getValue().equals("name") && ds.getValue() != null) {
//                            titleParentSpinner = ds.getValue().toString();
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }
        // need to update image!!
        description.setHint(taskToShow.getDescription());
    }

    private void initializeClassVariables(){
        taskName = findViewById(R.id.NameOfTask);
        taskNameButton = (Button) findViewById(R.id.UpdateName);
        bonusScore = findViewById(R.id.BonusPoints);
        bonusScoreButton = (Button) findViewById(R.id.UpdateBonus);
        taskImage = (ImageButton) findViewById(R.id.AddPic);
        chooseChildSpinner = (Spinner) findViewById(R.id.SpinnerChooseChild);
        description = findViewById(R.id.Description);
        descriptionButton = (Button) findViewById(R.id.DescriptionUpdate);

    }
}