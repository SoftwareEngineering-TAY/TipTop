package com.example.tiptop.PoolTasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database2;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import static com.example.tiptop.Database.Database2.addTaskToDB;
import static com.example.tiptop.Database.Database2.getKeyForNewTask;
import static com.example.tiptop.Database.Database2.getRouteType;
import static com.example.tiptop.Database.Database2.setTaskDesctiption;
import static com.example.tiptop.Database.Database2.updateListOfChildFromDB;
import static com.example.tiptop.Database.Database2.uploadImage;

public class NewTaskActivity extends AppCompatActivity implements DataChangeListener {

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
    private com.google.android.material.textfield.TextInputLayout description;
    private ImageButton newImage;
    private Bitmap bitmap_image =null;
    private Uri uri_image = null;
    private static final int CAMERA_PHOTO = 1;
    private static final int GALLERY_PHOTO = 2;

    private ArrayList<String> allKeys;
    private ArrayList<String> allKids;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRouteType().equals("With bonuses")) {
            setContentView(R.layout.activity_new_task);
        }
        else {
            setContentView(R.layout.activity_new_task_no_bonus);
        }
        initializationFromXML();
        notifyOnChange();
    }

    private void initializationListOfChildren() {
        createList();
        crateClickEvent();
        updateListOfChildFromDB(allKeys,allKids, adapter);
    }


    private void crateClickEvent() {
        ListOfChildren.setOnItemClickListener((adapterView,view,i,l) -> {
            if(allKids.get(i).equals("Not Associated"))
            {
                keyKid=null;
            }
            else
            {
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
        uploadImage( key, uri_image , bitmap_image,"taskImage");
        toAddTask.setNameTask(NameOfTask.getEditText().getText().toString());
        toAddTask.setStartDate(StartDate);
        toAddTask.setEndDate(EndDate);
        toAddTask.setBelongsToUID(keyKid);
        if(keyKid == null)
            toAddTask.setStatus(Task.STATUS.NotAssociated);
        else
            toAddTask.setStatus(Task.STATUS.Associated);
        toAddTask.setDescription(description.getEditText().getText().toString());
        if(getRouteType().equals("With bonuses")) {
            long bp = Long.parseLong(BonusPoint.getEditText().getText().toString());
            toAddTask.setBonusScore(bp);
        }
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
                                String zeroMonth = "";
                                String zeroDay = "";
                                if (monthOfYear<9) zeroMonth = "0";
                                if (dayOfMonth < 10) zeroDay = "0";
                                StartDate  = year+"-"+ zeroMonth +(monthOfYear + 1)+"-"+ zeroDay + dayOfMonth;
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
                                String zeroMonth = "";
                                String zeroDay = "";
                                if (monthOfYear < 9) zeroMonth = "0";
                                if (dayOfMonth < 10) zeroDay = "0";
                                EndDate = year+"-"+ zeroMonth +(monthOfYear + 1)+"-"+ zeroDay + dayOfMonth;
                                EndDateTV.setText(EndDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void setNewImagwButton(){
        newImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Take photo from camera", "Choose photo from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(NewTaskActivity.this);
                builder.setTitle("Attach a photo");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals(options[0]))
                        {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_PHOTO);
                        }
                        else if (options[item].equals(options[1]))
                        {
                            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, GALLERY_PHOTO);
                        }
                        else if (options[item].equals(options[2])) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PHOTO) {
                Bundle extras = data.getExtras();
                bitmap_image = (Bitmap)extras.get("data");
                newImage.setImageBitmap(bitmap_image);
            }
            else if (requestCode == GALLERY_PHOTO) {
                uri_image = data.getData();
                newImage.setImageURI(uri_image);
            }
        }
    }


    private void initializationFromXML() {
        NameOfTask = findViewById(R.id.NameOfTask);
        if(getRouteType().equals("With bonuses")) {
            BonusPoint = findViewById(R.id.BonusPoint);
        }
        StartDateButton = (Button) findViewById(R.id.StartDateButton);
        EndDateButton = (Button)findViewById(R.id.EndDateButton);
        StartDateTV = (TextView) findViewById(R.id.StartDateTV);
        EndDateTV = (TextView)findViewById(R.id.EndDateTV);
        SubmitButton = (Button)findViewById(R.id.SubmitButton);
        ListOfChildren = (ListView)findViewById(R.id.ListOfChildren);
        description = findViewById(R.id.Description);
        newImage = (ImageButton) findViewById(R.id.AddPic);
    }

    @Override
    public void notifyOnChange() {
        setSelectStartDateButton();
        setSelectEndDateButton();
        initializationListOfChildren();
        setNewImagwButton();
        setFinishButton();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Database2.addListener(this);
    }

    @Override
    protected void onPause() {
        Database2.removeListener(this);
        super.onPause();
    }
}

