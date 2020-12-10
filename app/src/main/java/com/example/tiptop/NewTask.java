package com.example.tiptop;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class NewTask extends AppCompatActivity {

    private Task toAddTask= new Task();
    private TextView NameOfTask;
    private TextView BonusPoint;
    private TextView StartDate;
    private TextView EndDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        initializationFromXML();
        initializationTask();

    }

    private void initializationTask() {
        toAddTask.setNameTask(NameOfTask.getText().toString());
        int bp = Integer.parseInt(BonusPoint.getText().toString());
        toAddTask.setBonusScore(bp);
        StartDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewTask.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        StartDate,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    private void initializationFromXML() {
        NameOfTask = (TextView)findViewById(R.id.NameOfTask);
        BonusPoint = (TextView)findViewById(R.id.BonusPoint);
        StartDate = (TextView)findViewById(R.id.StartDate);
        EndDate = (TextView)findViewById(R.id.EndDate);
    }
}
