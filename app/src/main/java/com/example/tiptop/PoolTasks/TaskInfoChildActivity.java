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
import com.example.tiptop.Database.Database2;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.tiptop.Database.Database2.getRouteType;
import static com.example.tiptop.Database.Database2.setStatus;
import static com.example.tiptop.Database.Database2.setTaskComment;
import static com.example.tiptop.Database.Database2.setTaskDesctiption;
import static com.example.tiptop.Database.Database2.updatePicture;
import static com.example.tiptop.Database.Database2.uploadImage;

public class TaskInfoChildActivity  extends AppCompatActivity implements DataChangeListener {

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
        if(getRouteType().equals("With bonuses"))
        {
            setContentView(R.layout.activity_task_info_child);
        }
        else
        {
            setContentView(R.layout.activity_task_info_child_no_bonus);
        }
        initializeClassVariables();
        notifyOnChange();
    }

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

    private void initializeClassVariables(){
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

    private void setTextInfo() {
        taskName.setText(taskToShow.getNameTask());
        description.setText(taskToShow.getDescription());
        if(getRouteType().equals("With bonuses")){
            bonusScore.setText(taskToShow.getBonusScore().toString());
        }

    }

    private void updateCommentButton() {
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentToUpdate = comment.getText().toString();
                setTaskComment(taskID, commentToUpdate);
                Toast.makeText(getApplicationContext(),"Update!", Toast.LENGTH_SHORT).show();
//                comment.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
            }
        });
    }

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
        Database2.addListener(this);
    }

    @Override
    protected void onPause() {
        Database2.removeListener(this);
        super.onPause();
    }
}