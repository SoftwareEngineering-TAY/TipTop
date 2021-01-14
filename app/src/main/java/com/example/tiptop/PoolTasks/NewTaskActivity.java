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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.util.ArrayList;
import java.util.Calendar;
import static com.example.tiptop.Database.Database.addTaskToDB;
import static com.example.tiptop.Database.Database.getKeyForNewTask;
import static com.example.tiptop.Database.Database.getRouteType;
import static com.example.tiptop.Database.Database.updateListOfChildFromDB;
import static com.example.tiptop.Database.Database.uploadImage;

public class NewTaskActivity extends AppCompatActivity implements DataChangeListener {

    //Fields
    private Task toAddTask;
    private EditText NameOfTask;
    private EditText BonusPoint;
    private TextView StartDateTV;
    private TextView EndDateTV;
    private Button SubmitButton;
    private ListView ListOfChildren;
    private String StartDate;
    private String EndDate;
    private String keyKid;
    private String key;
    private EditText description;

    //Variables for the image
    private ImageButton newImage;
    private Bitmap bitmap_image =null;
    private Uri uri_image = null;
    private static final int CAMERA_PHOTO = 1;
    private static final int GALLERY_PHOTO = 2;

    //Variables for the list
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

    /**
     * The function is responsible for creating the list, Listening to the list clicks and updating the list
     */
    private void initializationListOfChildren() {
        createList();
        crateClickEvent();
        updateListOfChildFromDB(allKeys,allKids, adapter);
    }

    /**
     * This function initializes all the required fields from the relevant XML file.
     */
    private void initializationFromXML() {
        NameOfTask = (EditText) findViewById(R.id.NameOfTask);
        if(getRouteType().equals("With bonuses")) {
            BonusPoint = (EditText) findViewById(R.id.BonusPoint);
        }
        StartDateTV = (EditText) findViewById(R.id.StartDate);
        EndDateTV = (EditText)findViewById(R.id.EndDate);
        SubmitButton = (Button)findViewById(R.id.SubmitButton);
        ListOfChildren = (ListView)findViewById(R.id.ListOfChildren);
        description = (EditText) findViewById(R.id.Description);
        newImage = (ImageButton) findViewById(R.id.AddPic);
    }

    /**
     * A function responsible for listening to clicks on the child list. When we detect a click,
     * the selection in the keyKid field will be saved.
     */
    private void crateClickEvent() {
        ListOfChildren.setOnItemClickListener((adapterView,view,i,l) -> {
            if(allKids.get(i).equals("Not Associated")) {
                keyKid=null;
            }
            else {
                keyKid = allKeys.get(i-1);
            }
        });
    }

    /**
     * A function responsible for creating the child list that will be used to select the assignment of the task
     */
    private void createList() {
        allKeys = new ArrayList<>();
        allKids =  new ArrayList<>();
        allKids.add("Not Associated");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allKids);
        ListOfChildren.setAdapter(adapter);
    }

    /**
     * A function responsible for creating a new task object and filling in its fields according to what the user has entered
     */
    private void initializationTask() {
        toAddTask = new Task();
        key = getKeyForNewTask();
        uploadImage( key, uri_image , bitmap_image,"taskImage");
        toAddTask.setNameTask(NameOfTask.getText().toString());
        toAddTask.setStartDate(StartDate);
        toAddTask.setEndDate(EndDate);
        toAddTask.setBelongsToUID(keyKid);
        if(keyKid == null)
            toAddTask.setStatus(Task.STATUS.NotAssociated);
        else
            toAddTask.setStatus(Task.STATUS.Associated);
        toAddTask.setDescription(description.getText().toString());
        if(getRouteType().equals("With bonuses")) {
            long bp = Long.parseLong(BonusPoint.getText().toString());
            toAddTask.setBonusScore(bp);
        }
    }

    /**
     * A function responsible for calling a function that creates an object of a new task,
     * notifying the user that the task has been created and transferring the user back to the
     * activity in which he sees all the tasks.
     */
    private void setFinishButton() {
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializationTask();
                addTaskToDB(key,toAddTask);
                Toast.makeText(getApplicationContext(),"A new task has been created!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), PoolTasksParentActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * The function is responsible for opening the start date dialog
     */
    private void setSelectStartDateButton(){
        StartDateTV.setOnClickListener(new View.OnClickListener() {
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

    /**
     * The function is responsible for opening the end date dialog
     */
    private void setSelectEndDateButton() {
        EndDateTV.setOnClickListener(new View.OnClickListener() {
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

    /**
     * The function is responsible for saving the image
     */
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
                        if (options[item].equals(options[0])) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_PHOTO);
                        }
                        else if (options[item].equals(options[1])) {
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

    /**
     * The function is responsible for saving the image
     */
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
        Database.addListener(this);
    }

    @Override
    protected void onPause() {
        Database.removeListener(this);
        super.onPause();
    }
}

