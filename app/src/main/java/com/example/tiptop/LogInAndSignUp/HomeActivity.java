package com.example.tiptop.LogInAndSignUp;

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
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.ChatActivity;
import com.example.tiptop.FollowUp.FollowUpChildActivity;
import com.example.tiptop.FollowUp.FollowUpParentActivity;
import com.example.tiptop.History.HistoryParentActivity;
import com.example.tiptop.PointsActivity;
import com.example.tiptop.PoolTasks.PoolTasksChildActivity;
import com.example.tiptop.PoolTasks.PoolTasksParentActivity;
import com.example.tiptop.ProfileActivity;
import com.example.tiptop.R;
import com.example.tiptop.Settings.SettingChildActivity;
import com.example.tiptop.Settings.SettingParentActivity;
import com.example.tiptop.StatisticsActivity;
import java.util.ArrayList;
import java.util.List;
import static com.example.tiptop.Database.Database.getCurrFamilyId;
import static com.example.tiptop.Database.Database.getPermission;
import static com.example.tiptop.Database.Database.initializationCurrFamilyIdAndPermission;
import static com.example.tiptop.Database.Database.setCurrFamilyId;
import static com.example.tiptop.Database.Database.uploadImage;

public class HomeActivity extends AppCompatActivity  {

    //Variables that will contain all the buttons
    private Button setting;
    private Button profile;
    private ImageButton imageButton;
    private Button followUp;
    private Button Statistics;
    private Button chat ;
    private Button Tasks;
    private Button history;
    private Button points;
    private Spinner SpinnerFamily;


    //Variables to be used for the spinner
    private List <String> allKeys;
    private ArrayList <String> allFamilies;
    private ArrayAdapter adapter;
    public String spinnerTitle;

    private Bitmap bitmapImage =null;
    private Uri uriImage = null;
    private static final int CAMERA_PHOTO = 1;
    private static final int GALLERY_PHOTO = 2;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializationFromXML();
        initializationCurrFamilyIdAndPermission();
        initializeClassVariables();
        spinerActive();
        ActivateAllButtons();
    }

    private void initializationFromXML() {
        //Set the settings button
        setting = (Button)findViewById(R.id.setting);
        //Set the profile button
        profile = (Button)findViewById(R.id.profile);
        //Set the followUp button
        followUp = (Button)findViewById(R.id.followUp);
        //Set the Statistics button
        Statistics = (Button)findViewById(R.id.statistics);
        //Set the chat button
        chat = (Button)findViewById(R.id.chat);
        //Set the Tasks button
        Tasks = (Button)findViewById(R.id.tasks);
        //Set the history button
        history = (Button)findViewById(R.id.history);
        //Set the points button
        points = (Button)findViewById(R.id.points);
        //Set the ImageButton button
        imageButton = (ImageButton)findViewById(R.id.imageButton);

    }

    private void initializeClassVariables() {
        spinnerTitle = getCurrFamilyId();

        //Set the SpinnerFamily Spinner
        SpinnerFamily = (Spinner)findViewById(R.id.SpinnerFamily);

        //Initialize the 2 lists that will save all the last names and all the keys respectively.
        allKeys = new ArrayList<>();
        allFamilies = new ArrayList<>();
    }


    /**
     * The function activates the spinner component, meaning it fills in the spinner all
     * the families to which the user belongs by retrieving what DB. And when you click on another last
     * name the currFamilyId changes in the title and in DB and this is what is transmitted on the Internet.
     */
    private void spinerActive() {

        //Connecting the list to the view
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allFamilies);
        SpinnerFamily.setAdapter(adapter);

        //Defines the functionality of a last name click
        SpinnerFamily.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerTitle = allFamilies.get(position);
                setCurrFamilyId(allKeys.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void ActivateAllButtons() {
        //Moves to the settings activity
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if(getPermission().equals("Parent")){
                    i = new Intent(v.getContext(), SettingParentActivity.class);
                }
                else if (getPermission().equals("Child")) {
                    i = new Intent(v.getContext(), SettingChildActivity.class);
                }
                else return;
                startActivity(i);
            }
        });

        //Moves to the profile activity
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfileActivity.class);
                startActivity(i);
            }
        });

        //Moves to the followUp activity
        followUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if(getPermission().equals("Parent")){
                    i = new Intent(v.getContext(), FollowUpParentActivity.class);
                }
                else if (getPermission().equals("Child")) {
                    i = new Intent(v.getContext(), FollowUpChildActivity.class);
                }
                else return;
                startActivity(i);
            }
        });

        //Moves to the Statistics activity
        Statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), StatisticsActivity.class);
                startActivity(i);
            }
        });

        //Moves to the chat activity
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ChatActivity.class);
                startActivity(i);
            }
        });

        //Moves to the Tasks activity
        Tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if (getPermission().equals("Parent")){
                    i = new Intent(v.getContext(), PoolTasksParentActivity.class);
                }

                else if (getPermission().equals("Child")){
                    i = new Intent(v.getContext(), PoolTasksChildActivity.class);
                }
                else return;
                startActivity(i);
            }
        });

        //Moves to the history activity
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), HistoryParentActivity.class);
                startActivity(i);
            }
        });

        //Moves to the points activity
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PointsActivity.class);
                startActivity(i);
            }
        });

        //Moves to the ImageButton activity
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewImagwButton();
                uploadImage(getCurrFamilyId(),uriImage,bitmapImage);
            }
        });
    }

    private void setNewImagwButton(){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Take photo from camera", "Choose photo from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
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
                bitmapImage = (Bitmap)extras.get("data");
                imageButton.setImageBitmap(bitmapImage);
            }
            else if (requestCode == GALLERY_PHOTO) {
                uriImage = data.getData();
                imageButton.setImageURI(uriImage);
            }
        }
    }



//    //need to ask yirat
//    private void initializationImage() {
//        String path = "Families/" + getCurrFamilyId();
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference mStorageRef = storage.getReference(path);
//        if (mStorageRef != null)
//        {
//
//        }
//
//        else
//        {
//            imageButton.setImageResource(R.drawable.new_family);
//        }
//
//    }
//

}
