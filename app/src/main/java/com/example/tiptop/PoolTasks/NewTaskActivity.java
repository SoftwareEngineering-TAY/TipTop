package com.example.tiptop.PoolTasks;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewTaskActivity extends AppCompatActivity {

    private Task toAddTask;
    private com.google.android.material.textfield.TextInputLayout NameOfTask;
    private com.google.android.material.textfield.TextInputLayout BonusPoint;
    private Button StartDateButton;
    private Button EndDateButton;
    private TextView StartDateTV;
    private TextView EndDateTV;
    private Button SubmitButton;
    private ListView ListOfChildren;
    private String StartDate;
    private String EndDate;
    private String currFamilyid;
    private DatabaseReference reference;
    private String keyKid;
    private String key;

    private DatabaseReference databaseReference ;
    private FirebaseAuth mAuth;
    private String uid;

    private ArrayList<String> allKeys;
    private ArrayList<String> allKids;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        initializationFromXML();
        getExtrasFromIntent();
        setSelectStartDateButton();
        setSelectEndDateButton();
        System.out.println("allKids3333333333333333333333333333333333333!!!@#E$%^&^%%"+allKids);
        initializationListOfChildren();
        System.out.println("allKids4444444444444444444444444444444444444444!!!@#E$%^&^%%"+allKids);
        setFinishButton();
    }

    private void initializationListOfChildren() {
        initializeVariables();
        createList();
        crateClickEvent();
        updateListFromDB();

        System.out.println("allKids222222222222222222222222222!!!@#E$%^&^%%"+allKids);

    }

    private void updateListFromDB() {
        databaseReference.child("Families").child(currFamilyid).addValueEventListener(new ValueEventListener() {
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
                    databaseReference.child("Users").child(toAddKey).addValueEventListener(new ValueEventListener() {
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

    private void crateClickEvent() {
        ListOfChildren.setOnItemClickListener((adapterView,view,i,l) -> {
            System.out.println("ad mty!!!"+allKids);
            System.out.println("iiiiiiiiiiiiiiiiiiiiiiii"+ i);
            if(allKids.get(i).equals("Not Associated"))
            {
                keyKid=null;
            }
            else
            {
                System.out.println("allKeys))))))))))))))))))))))))))))))))))"+allKeys);
                keyKid = allKeys.get(i-1);
            }

        });
    }

    private void createList() {
        allKeys = new ArrayList<>();
        allKids =  new ArrayList<>();
        allKids.add("Not Associated");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allKids);
        ListOfChildren.setAdapter(adapter);
    }

    private void initializeVariables() {
         databaseReference = FirebaseDatabase.getInstance().getReference();
         mAuth = FirebaseAuth.getInstance();
         uid = mAuth.getCurrentUser().getUid();
    }


    private void initializationTask() {
        toAddTask = new Task();
        key = reference.child("Tasks").child(currFamilyid).push().getKey();
        toAddTask.setNameTask(NameOfTask.getEditText().getText().toString());
        long bp = Long.parseLong(BonusPoint.getEditText().getText().toString());
        toAddTask.setBonusScore(bp);
        toAddTask.setStartDate(StartDate);
        toAddTask.setEndDate(EndDate);
        toAddTask.setBelongsToUID(keyKid);
        if(keyKid == null)
            toAddTask.setStatus(Task.STATUS.NotAssociated);
        else
            toAddTask.setStatus(Task.STATUS.Associated);

    }
    private void addTaskToDB()
    {
        System.out.println("key!!!!!!!!!!!!!!!!!!!!!!!"+key);
        System.out.println("fid))))))))))  " + currFamilyid);
        reference.child("Tasks").child(currFamilyid).child(key).setValue(toAddTask);
    }

    private void setFinishButton() {
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializationTask();
                addTaskToDB();
                Intent i = new Intent(v.getContext(), PoolTasksParentActivity.class);
                i.putExtra("currFamilyId", currFamilyid);
                startActivity(i);
            }
        });
    }

    private void setSelectStartDateButton(){
        StartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                StartDate  = year+"-"+(monthOfYear + 1)+"-"+dayOfMonth;
                                System.out.println("StartDate****************************"+StartDate);
                                StartDateTV.setText(StartDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void setSelectEndDateButton() {
        EndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                EndDate = year+"-"+(monthOfYear + 1)+"-"+dayOfMonth;
                                EndDateTV.setText(EndDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void getExtrasFromIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            String currFamilyidTemp = extras.getString("currFamilyid");
            if(currFamilyidTemp!=null)
            {
                currFamilyid=currFamilyidTemp;
            }
            else
            {
                Toast.makeText(this, "currFamily didn't pass", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void initializationFromXML() {
        NameOfTask = findViewById(R.id.NameOfTask);
        BonusPoint = findViewById(R.id.BonusPoint);
        StartDateButton = (Button) findViewById(R.id.StartDateButton);
        EndDateButton = (Button)findViewById(R.id.EndDateButton);
        StartDateTV = (TextView) findViewById(R.id.StartDateTV);
        EndDateTV = (TextView)findViewById(R.id.EndDateTV);
        SubmitButton = (Button)findViewById(R.id.SubmitButton);
        ListOfChildren = (ListView)findViewById(R.id.ListOfChildren);
        reference = FirebaseDatabase.getInstance().getReference();
    }
}

