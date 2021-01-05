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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database2;
import com.example.tiptop.LogInAndSignUp.CreateFamilyActivity;
import com.example.tiptop.LogInAndSignUp.HomeActivity;
import com.example.tiptop.LogInAndSignUp.LoginActivity;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import com.example.tiptop.Settings.SettingParentActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import static com.example.tiptop.Database.Database2.createUserInFireBase;
import static com.example.tiptop.Database.Database2.getAndSetTitleSpinnerOfBelongChild;
import static com.example.tiptop.Database.Database2.setStatus;
import static com.example.tiptop.Database.Database2.setTaskBonus;
import static com.example.tiptop.Database.Database2.setTaskDesctiption;
import static com.example.tiptop.Database.Database2.setTaskName;
import static com.example.tiptop.Database.Database2.setbelongsToUID;
import static com.example.tiptop.Database.Database2.updateListOfChildFromDB;
import static com.example.tiptop.Database.Database2.uploadImage;

public class TaskInfoParentActivity extends AppCompatActivity implements DataChangeListener {
    private String taskID;
    private com.google.android.material.textfield.TextInputLayout taskName;
    private Button taskNameButton;
    private com.google.android.material.textfield.TextInputLayout bonusScore;
    private Button bonusScoreButton;
    private ImageButton newImage;
    private Button ImageButtonUpdate;
    private Spinner chooseChildSpinner;
    private com.google.android.material.textfield.TextInputLayout description;
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
        setContentView(R.layout.activity_task_info_parent);
        getExtraFromIntent();
        initializeClassVariables();
        notifyOnChange();
        clickOnBack();
    }

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
                if(position > 0)
                {
                    setbelongsToUID(taskID,allKeys.get(position-1));
                    adapter.notifyDataSetChanged();
                    if(position == 1)
                    {
                        setStatus(taskID,"NotAssociated");
                    }
                    else
                    {
                        setStatus(taskID,"Associated");
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void updateDescriptionButton() {
        descriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descriptionToUpdate = description.getEditText().getText().toString();
                setTaskDesctiption(taskID, descriptionToUpdate);
                description.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
            }
        });
    }

    private void updateNameOfTask() {
        taskNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameToUpdate = taskName.getEditText().getText().toString();
                taskName.setHint(nameToUpdate);
                taskName.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                setTaskName(taskID, nameToUpdate);

            }
        });
    }

    private void updateBonusScore() {
        bonusScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bonusToUpdate =  Integer.parseInt(bonusScore.getEditText().getText().toString());
                String bonusHint = Integer.toString(bonusToUpdate);
                bonusScore.setHint(bonusHint);
                bonusScore.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                setTaskBonus(taskID, bonusToUpdate);
            }
        });
    }

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

    private void initializeClassVariables(){
        taskName = findViewById(R.id.NameOfTask);
        taskNameButton = (Button) findViewById(R.id.UpdateName);
        bonusScore = findViewById(R.id.BonusPoints);
        bonusScoreButton = (Button) findViewById(R.id.UpdateBonus);
        chooseChildSpinner = (Spinner) findViewById(R.id.SpinnerChooseChild);
        description = findViewById(R.id.Description);
        descriptionButton = (Button) findViewById(R.id.DescriptionUpdate);
        Back=(Button) findViewById(R.id.Back);
        newImage = (ImageButton) findViewById(R.id.AddPic);
        ImageButtonUpdate=(Button) findViewById(R.id.ImageButtonUpdate);
        allKeys = new ArrayList<>();
        allKids = new ArrayList<>();
    }

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

    private void updateImageButton(){
        ImageButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage( taskID, uri_image , bitmap_image,"taskImage");
            }
        });
    }

    @Override
    public void notifyOnChange() {
        getExtraFromIntent();
        updateNameOfTask();
        updateBonusScore();
        updateDescriptionButton();
        updateReAssociation();
        setNewImagwButton();
        updateImageButton();
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