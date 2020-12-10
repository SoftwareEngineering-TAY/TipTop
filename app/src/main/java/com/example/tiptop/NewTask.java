package com.example.tiptop;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class NewTask extends AppCompatActivity {

    private Task toAddTask = new Task();
    private com.google.android.material.textfield.TextInputLayout NameOfTask;
    private com.google.android.material.textfield.TextInputLayout BonusPoint;
    private Button StartDateButton;
    private Button EndDateButton;
    private TextView StartDateTV;
    private TextView EndDateTV;
    private Button SubmitButton;
    private String StartDate;
    private String EndDate;
    private static final String TAG = "NewActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        initializationFromXML();
        setSelectDateButton(StartDateButton, StartDateTV);
        setSelectDateButton(EndDateButton, EndDateTV);
        setFinishButton();

    }

    private void initializationTask() {
        toAddTask.setNameTask(NameOfTask.getEditText().getText().toString());
        long bp = Integer.parseInt(BonusPoint.getEditText().getText().toString());
        toAddTask.setBonusScore(bp);
    }

    private void setFinishButton() {
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializationTask();

            }
        });
    }

    private Void setSelectDateButton(Button button, TextView tv) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTask.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

    }



    private void initializationFromXML() {
        NameOfTask = findViewById(R.id.NameOfTask);
        BonusPoint = findViewById(R.id.BonusPoint);
        StartDateButton = (Button) findViewById(R.id.StartDateButton);
        EndDateButton = (Button)findViewById(R.id.EndDateButton);
        StartDateTV = (TextView) findViewById(R.id.StartDateTV);
        EndDateTV = (TextView)findViewById(R.id.EndDateTV);
        SubmitButton = (Button)findViewById(R.id.SubmitButton);
    }
}

