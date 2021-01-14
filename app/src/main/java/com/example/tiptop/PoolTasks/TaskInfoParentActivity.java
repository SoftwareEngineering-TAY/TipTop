package com.example.tiptop.PoolTasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.R;
import java.util.ArrayList;
import static com.example.tiptop.Database.Database.getRouteType;
import static com.example.tiptop.Database.Database.setStatus;
import static com.example.tiptop.Database.Database.setTaskBonus;
import static com.example.tiptop.Database.Database.setTaskDesctiption;
import static com.example.tiptop.Database.Database.setTaskName;
import static com.example.tiptop.Database.Database.setbelongsToUID;
import static com.example.tiptop.Database.Database.updateListOfChildFromDB;
import static com.example.tiptop.Database.Database.updatePicture;
import static com.example.tiptop.Database.Database.uploadImage;

public class TaskInfoParentActivity extends AppCompatActivity implements DataChangeListener {
    //Fields
    private String taskID;
    private EditText taskName;
    private Button taskNameButton;
    private EditText bonusScore;
    private Button bonusScoreButton;
    private ImageButton newImage;
    private Button ImageButtonUpdate;
    private Spinner chooseChildSpinner;
    private EditText description;
    private Button descriptionButton;
    private Button Back;
    private Bitmap bitmap_image =null;
    private Uri uri_image = null;
    private static final int CAMERA_PHOTO = 1;
    private static final int GALLERY_PHOTO = 2;

    //Variables to be used for the spinner
    private ArrayList <String> allKeys;
    private ArrayList <String> allKids;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRouteType().equals("With bonuses")) {
            setContentView(R.layout.activity_task_info_parent);
        }
        else {
            setContentView(R.layout.activity_task_info_parent_no_bonuses);
        }
        getExtraFromIntent();
        initializationFromXML();
        notifyOnChange();
        clickOnBack();
    }

    /**
     * This function initializes all the required fields from the relevant XML file
     */
    private void initializationFromXML(){
        taskName = (EditText) findViewById(R.id.NameOfTask);
        taskNameButton = (Button) findViewById(R.id.UpdateName);
        chooseChildSpinner = (Spinner) findViewById(R.id.SpinnerChooseChild);
        description = (EditText) findViewById(R.id.Description);
        descriptionButton = (Button) findViewById(R.id.DescriptionUpdate);
        Back=(Button) findViewById(R.id.Back);
        newImage = (ImageButton) findViewById(R.id.AddPic);
        ImageButtonUpdate=(Button) findViewById(R.id.ImageButtonUpdate);
        if(getRouteType().equals("With bonuses")) {
            bonusScore = (EditText) findViewById(R.id.BonusPoints);
            bonusScoreButton = (Button) findViewById(R.id.UpdateBonus);
        }
        allKeys = new ArrayList<>();
        allKids = new ArrayList<>();
    }

    /**
     * The function is responsible for retrieving information from the Intent
     */
    private void getExtraFromIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            if(extras.get("taskID")!=null) {
                taskID = extras.getString("taskID");
            }
            else {
                Toast.makeText(this, " didn't pass", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * The function is responsible for updating the re-assignment of the task
     */
    private void updateReAssociation() {
        //Connecting the list to the view
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allKids);
        chooseChildSpinner.setAdapter(adapter);

        //A function that retrieves all information from the database
        updateListOfChildFromDB(allKeys, allKids,adapter);
        allKeys.add(0,null);
        allKids.add(0,"Choose a child");

        //Defines the functionality of a last name click
        chooseChildSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    setbelongsToUID(taskID,allKeys.get(position-1));
                    adapter.notifyDataSetChanged();
                    if(position == 1) {
                        setStatus(taskID,"NotAssociated");

                    }
                    else {
                        setStatus(taskID,"Associated");
                    }
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"ReAssociation!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        adapter.notifyDataSetChanged();
    }

    /**
     * The function is responsible for updating the description
     */
    private void updateDescriptionButton() {
        descriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descriptionToUpdate = description.getText().toString();
                setTaskDesctiption(taskID, descriptionToUpdate);
                Toast.makeText(getApplicationContext(),"Update!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * The function is responsible for updating the task name
     */
    private void updateNameOfTask() {
        taskNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameToUpdate = taskName.getText().toString();
                taskName.setHint(nameToUpdate);
                setTaskName(taskID, nameToUpdate);
                Toast.makeText(getApplicationContext(),"Update!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * The function is responsible for updating the name of the bonus points
     */
    private void updateBonusScore() {
        bonusScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bonusToUpdate =  Integer.parseInt(bonusScore.getText().toString());
                String bonusHint = Integer.toString(bonusToUpdate);
                bonusScore.setHint(bonusHint);
                setTaskBonus(taskID, bonusToUpdate);
                Toast.makeText(getApplicationContext(),"Update!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * The function is responsible for returning to the screen where all the tasks are
     */
    private void clickOnBack()
    {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TaskInfoParentActivity.this, PoolTasksParentActivity.class);
                startActivity(i);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskInfoParentActivity.this);
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

    /**
     * The function is responsible for calling the function that is responsible for
     * updating the image in the database
     */
    private void updateImageButton(){
        ImageButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage( taskID, uri_image , bitmap_image,"taskImage");
                Toast.makeText(getApplicationContext(),"Update!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void notifyOnChange() {
        getExtraFromIntent();
        updatePicture(newImage,getApplicationContext(),taskID,"taskImage");
        updateNameOfTask();
        updateDescriptionButton();
        updateReAssociation();
        setNewImagwButton();
        updateImageButton();
        if(getRouteType().equals("With bonuses")) {
            updateBonusScore();
        }
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