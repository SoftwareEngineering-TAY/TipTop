package com.example.tiptop.PoolTasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.getRouteType;
import static com.example.tiptop.Database.Database.setStatus;
import static com.example.tiptop.Database.Database.setTaskComment;
import static com.example.tiptop.Database.Database.updatePicture;
import static com.example.tiptop.Database.Database.uploadImage;

public class TaskInfoChildActivity  extends AppCompatActivity implements DataChangeListener {

    //Fields
    private Task taskToShow;
    private String taskID;
    private TextView taskName;
    private TextView bonusScore;
    private TextView description;
    private Button doneTask;
    private ImageButton newImage;
    private Button ImageButtonUpdate;
    private Bitmap bitmap_image =null;
    private Uri uri_image = null;
    private EditText comment;
    private Button commentButton;
    private static final int CAMERA_PHOTO = 1;
    private static final int GALLERY_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRouteType().equals("With bonuses")) {
            setContentView(R.layout.activity_task_info_child);
        }
        else {
            setContentView(R.layout.activity_task_info_child_no_bonus);
        }
        initializationFromXML();
        notifyOnChange();
    }

    /**
     * This function initializes all the required fields from the relevant XML file
     */
    private void initializationFromXML(){
        taskName = (TextView)findViewById(R.id.TaskNameShow);
        description = (TextView)findViewById(R.id.DescriptionShow);
        doneTask = (Button)findViewById(R.id.TaskDone);
        newImage = (ImageButton) findViewById(R.id.AddPic);
        ImageButtonUpdate=(Button) findViewById(R.id.ImageButtonUpdate);
        comment = (EditText) findViewById(R.id.Comment);
        commentButton = (Button) findViewById(R.id.CommentUpdate);
        if(getRouteType().equals("With bonuses")){
            bonusScore = (TextView)findViewById(R.id.BonusPointShow);
        }
    }

    /**
     * The function is responsible for retrieving information from the Intent
     */
    private void getExtraFromIntent() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            if(extras.get("task")!=null) {
                taskToShow = (Task) extras.get("task");
            }
            if(extras.get("taskID")!=null) {
                taskID = extras.getString("taskID");
            }
            else {
                Toast.makeText(this, "currFamily didn't pass, currFamily: ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * The function is responsible for displaying the name, description and bonus if necessary in the activity
     */
    private void setTextInfo() {
        taskName.setText(taskToShow.getNameTask());
        description.setText(taskToShow.getDescription());
        if(getRouteType().equals("With bonuses")){
            bonusScore.setText(taskToShow.getBonusScore().toString());
        }

    }

    /**
     * The function is responsible for updating the child's comment
     */
    private void updateCommentButton() {
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentToUpdate = comment.getText().toString();
                setTaskComment(taskID, commentToUpdate);
                Toast.makeText(getApplicationContext(),"Update!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * The function is responsible for updating the status of the task and transferring it back
     * to the child's task screen
     */
    private void setDoneButton() {
        doneTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setStatus(taskID,"WaitingForApproval");
                Intent i = new Intent(v.getContext(), PoolTasksChildActivity.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskInfoChildActivity.this);
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
                uploadImage(taskID, uri_image , bitmap_image,"taskImage");
            }
        });
    }

    @Override
    public void notifyOnChange() {
        getExtraFromIntent();
        setTextInfo();
        updatePicture(newImage,getApplicationContext(),taskID,"taskImage");
        setDoneButton();
        setNewImagwButton();
        updateImageButton();
        updateCommentButton();
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