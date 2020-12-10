package com.example.tiptop;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.Calendar;

public class NewTask extends AppCompatActivity {

    private Task toAddTask;
    private com.google.android.material.textfield.TextInputLayout NameOfTask;
    private com.google.android.material.textfield.TextInputLayout BonusPoint;
    private Button StartDateButton;
    private Button EndDateButton;
    private TextView StartDateTV;
    private TextView EndDateTV;
    private Button SubmitButton;
    private LocalDate StartDate;
    private LocalDate EndDate;
    private String currFamilyid;
    private DatabaseReference reference;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        initializationFromXML();
        getExtrasFromIntent();
        setSelectStartDateButton();
        setSelectEndDateButton();
        setFinishButton();
    }



    private void initializationTask() {
        toAddTask = new Task();
        toAddTask.setNameTask(NameOfTask.getEditText().getText().toString());
        long bp = Integer.parseInt(BonusPoint.getEditText().getText().toString());
        toAddTask.setBonusScore(bp);
        toAddTask.setStartDate(StartDate);
//        toAddTask.setEndDate(EndDate);

    }
    private void addTaskToDB()
    {
        String key = reference.child("Tasks").child(currFamilyid).push().getKey();
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTask.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                StartDate  = LocalDate.parse(year+"-"+(monthOfYear + 1)+"-"+dayOfMonth);
                                System.out.println("StartDate****************************"+StartDate);
                                StartDateTV.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTask.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                EndDate = LocalDate.parse(year+"-"+(monthOfYear + 1)+"-"+dayOfMonth);
                                EndDateTV.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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
        reference = FirebaseDatabase.getInstance().getReference();
    }
}

