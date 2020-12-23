package com.example.tiptop.PoolTasks;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.util.ArrayList;
import java.util.Calendar;
import static com.example.tiptop.Database.Database.addTaskToDB;
import static com.example.tiptop.Database.Database.getKeyForNewTask;
import static com.example.tiptop.Database.Database.updateListOfChildFromDB;

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
    private String keyKid;
    private String key;

    private ArrayList<String> allKeys;
    private ArrayList<String> allKids;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        initializationFromXML();
        setSelectStartDateButton();
        setSelectEndDateButton();
        initializationListOfChildren();
        setFinishButton();
    }

    private void initializationListOfChildren() {
        createList();
        crateClickEvent();
        updateListOfChildFromDB(allKeys,allKids, adapter);
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

    private void initializationTask() {
        toAddTask = new Task();
        key = getKeyForNewTask();
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


    private void setFinishButton() {
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializationTask();
                addTaskToDB(key,toAddTask);
                Intent i = new Intent(v.getContext(), PoolTasksParentActivity.class);
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

    private void initializationFromXML() {
        NameOfTask = findViewById(R.id.NameOfTask);
        BonusPoint = findViewById(R.id.BonusPoint);
        StartDateButton = (Button) findViewById(R.id.StartDateButton);
        EndDateButton = (Button)findViewById(R.id.EndDateButton);
        StartDateTV = (TextView) findViewById(R.id.StartDateTV);
        EndDateTV = (TextView)findViewById(R.id.EndDateTV);
        SubmitButton = (Button)findViewById(R.id.SubmitButton);
        ListOfChildren = (ListView)findViewById(R.id.ListOfChildren);
    }
}

