package com.example.tiptop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TaskInfoActivity extends AppCompatActivity {

    private Task taskToShow;
    private String currFamilyId;



    private com.google.android.material.textfield.TextInputLayout taskName;
    private Button taskNameButton;
    private com.google.android.material.textfield.TextInputLayout bonusScore;
    private Button bonusScoreButton;
    private ImageButton taskImage;
    private Spinner chooseChildSpinner;
    private com.google.android.material.textfield.TextInputLayout description;
    private Button descriptionButton;

    private static final String TAG = "TaskInfoActivity";
    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private String uid;

    //Variables to be used for the spinner
    private List<String> allKeys;
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
    private void updateReAssociation() {

        //Initialize the 2 lists that will save all the last names and all the keys respectively.
        allKeys = new ArrayList<>();
        allKids = new ArrayList<>();

        //A function that retrieves all information from the database
        getInfoFromDB();

        //Connecting the list to the view
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allKids);
        chooseChildSpinner.setAdapter(adapter);

        //Defines the functionality of a last name click
        chooseChildSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                titleParentSpinner = allKids.get(position);
                if(position == 0)
                {
                    reference.child("Tasks").child(currFamilyId).child(taskToShow.getTaskId()).child("belongsToUID").removeValue();
                    reference.child("Tasks").child(currFamilyId).child(taskToShow.getTaskId()).child("status").setValue("NotAssociated");

                }
                else
                {
                    reference.child("Tasks").child(currFamilyId).child(taskToShow.getTaskId()).child("belongsToUID").setValue(allKeys.get(position-1));
                    reference.child("Tasks").child(currFamilyId).child(taskToShow.getTaskId()).child("status").setValue("Associated");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void getInfoFromDB() {
        reference.child("Families").child(currFamilyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allKids.clear();
                allKeys.clear();
                allKids.add("Not Associated");
                for (DataSnapshot ds : snapshot.getChildren() )
                {
                    String toAddChildren =(String) ds.getValue();
                    System.out.println("  String toAddChildren =(String) ds.getValue();"+toAddChildren);
                    String toAddKey =(String) ds.getKey();
                    reference.child("Users").child(toAddKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.child("type").getValue()!=null&&snapshot.child("type").getValue().toString().equals("Child"))
                            {
                                allKeys.add(toAddKey);
                                allKids.add(toAddChildren);
                            }
                            System.out.println("allKids.isEmpty())))))"+allKids.isEmpty());
                            adapter.notifyDataSetChanged();

                            System.out.println("allKids1111111111111111111!!!@#E$%^&^%%"+allKids);
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
                System.out.println(" adapter.notifyDataSetChanged();allKids1111111111111111111!!!@#E$%^&^%%"+allKids);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    private void updateDescriptionButton() {

        descriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descriptionToUpdate = description.getEditText().getText().toString();
                reference.child("Tasks").child(currFamilyId).child(taskToShow.getTaskId()).child("nameTask").setValue(descriptionToUpdate);

            }
        });
    }

    private void updateNameOfTask() {
        taskNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameToUpdate = taskName.getEditText().getText().toString();
                System.out.println("currFamilyId: "+ currFamilyId );
                reference.child("Tasks").child(currFamilyId).child(taskToShow.getTaskId()).child("nameTask").setValue(nameToUpdate);

            }
        });

    }

    private void updateBonusScore() {
        bonusScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bonusToUpdate =  Integer.parseInt(bonusScore.getEditText().getText().toString());
                reference.child("Tasks").child(currFamilyId).child(taskToShow.getTaskId()).child("bonusScore").setValue(bonusToUpdate);

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
            reference.child("Users").child(taskToShow.getBelongsToUID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.getValue().equals("name") && ds.getValue() != null) {
                            titleParentSpinner = ds.getValue().toString();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        // need to update image!!
        description.setHint(taskToShow.getComment());
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

        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference = root.getReference();
        uid = mAuth.getCurrentUser().getUid();


    }
}
