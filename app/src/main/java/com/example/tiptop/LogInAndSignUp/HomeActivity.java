package com.example.tiptop.LogInAndSignUp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Chat.ChatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.FollowUp.FollowUpChildActivity;
import com.example.tiptop.FollowUp.FollowUpParentActivity;
import com.example.tiptop.History.HistoryChildActivity;
import com.example.tiptop.History.HistoryParentActivity;
import com.example.tiptop.Points.PointsChildActivity;
import com.example.tiptop.Points.PointsParentActivity;
import com.example.tiptop.PoolTasks.PoolTasksChildActivity;
import com.example.tiptop.PoolTasks.PoolTasksParentActivity;
import com.example.tiptop.R;
import com.example.tiptop.Settings.SettingChildActivity;
import com.example.tiptop.Settings.SettingParentActivity;
import com.example.tiptop.Statistics.StatisticsActivity;
import java.util.ArrayList;
import static com.example.tiptop.Database.Database.getCurrFamilyId;
import static com.example.tiptop.Database.Database.getPermission;
import static com.example.tiptop.Database.Database.getRouteType;
import static com.example.tiptop.Database.Database.initializationCurrFamilyId;
import static com.example.tiptop.Database.Database.initializationPermission;
import static com.example.tiptop.Database.Database.initializationRouteType;
import static com.example.tiptop.Database.Database.initializationUserId;
import static com.example.tiptop.Database.Database.setCurrFamilyId;
import static com.example.tiptop.Database.Database.updateListOfFamilyFromDB;
import static com.example.tiptop.Database.Database.updatePicture;
import static com.example.tiptop.Database.Database.uploadImage;

public class HomeActivity extends AppCompatActivity implements DataChangeListener {

    //Variables that will contain all the buttons
    private ImageButton imageButton;
    private androidx.cardview.widget.CardView followUp;
    private androidx.cardview.widget.CardView Statistics;
    private androidx.cardview.widget.CardView chat;
    private androidx.cardview.widget.CardView Tasks;
    private androidx.cardview.widget.CardView history;
    private androidx.cardview.widget.CardView points;
    private Spinner SpinnerFamily;

    //Variables to be used for the spinner
    private ArrayList <String> allKeys;
    private ArrayList <String> allFamilies;
    public String spinnerTitle;

    private Bitmap bitmapImage =null;
    private Uri uriImage = null;
    private static final int CAMERA_PHOTO = 1;
    private static final int GALLERY_PHOTO = 2;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializationUserSettings();
        if(getRouteType().equals("With bonuses")) setContentView(R.layout.activity_home);
        else setContentView(R.layout.activity_home_no_bonus);
        initializationFromXML();
        initializeClassVariables();
        notifyOnChange();
    }

    private void initializationUserSettings() {
        initializationUserId();
        initializationCurrFamilyId();
        initializationPermission();
        initializationRouteType();
    }

    /**
     * This function initializes all the required fields from the relevant XML file
     */
    private void initializationFromXML() {
        //Set the followUp button
        followUp = findViewById(R.id.followUp);
        //Set the Statistics button
        Statistics = findViewById(R.id.statistics);
        //Set the chat button
        chat = findViewById(R.id.chat);
        //Set the Tasks button
        Tasks = findViewById(R.id.tasks);
        //Set the history button
        history = findViewById(R.id.history);
        //Set the ImageButton button
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        //Set the SpinnerFamily Spinner
        SpinnerFamily = (Spinner)findViewById(R.id.SpinnerFamily);
        if(getRouteType().equals("With bonuses")) {
            //Set the points button
            points = findViewById(R.id.points);
        }
    }

    /**
     * This function initializes all the fields class Unrelated to XML
     */
    private void initializeClassVariables() {
        spinnerTitle = getCurrFamilyId();
        //Initialize the 2 lists that will save all the last names and all the keys respectively.
        allKeys = new ArrayList<>();
        allFamilies = new ArrayList<>();
    }

    /**
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    /**
     * @param item
     * @return true if the item selected or false if not.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.settingItem){
            Intent i;
            if(getPermission().equals("Parent")){
                i = new Intent(HomeActivity.this, SettingParentActivity.class);
            }
            else {
                i = new Intent(HomeActivity.this, SettingChildActivity.class);
            }
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * The function activates the spinner component, meaning it fills in the spinner all
     * the families to which the user belongs by retrieving what DB. And when you click on another last
     * name the currFamilyId changes in the title and in DB and this is what is transmitted on the Internet.
     */
    private void spinerActive() {
        //Connecting the list to the view
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allFamilies);
        SpinnerFamily.setAdapter(adapter);
        updateListOfFamilyFromDB(allKeys,allFamilies, adapter);

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
                Intent i;
                if (getPermission().equals("Parent")){
                    i = new Intent(v.getContext(), HistoryParentActivity.class);
                }

                else if (getPermission().equals("Child")){
                    i = new Intent(v.getContext(), HistoryChildActivity.class);
                }
                else return;
                startActivity(i);
            }
        });

        if(getRouteType().equals("With bonuses")) {
            //Moves to the points activity
            points.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i;
                    if (getPermission().equals("Parent")) {
                        i = new Intent(v.getContext(), PointsParentActivity.class);
                    } else if (getPermission().equals("Child")) {
                        i = new Intent(v.getContext(), PointsChildActivity.class);
                    } else return;
                    startActivity(i);
                }
            });
        }

        //Moves to the ImageButton activity
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewImagwButton();
                uploadImage(getCurrFamilyId(),uriImage,bitmapImage,"Families");
            }
        });
    }

    /**
     * The function is responsible for saving the image
     */
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
                bitmapImage = (Bitmap)extras.get("data");
                imageButton.setImageBitmap(bitmapImage);
            }
            else if (requestCode == GALLERY_PHOTO) {
                uriImage = data.getData();
                imageButton.setImageURI(uriImage);
            }
        }
    }

    @Override
    public void notifyOnChange() {
        spinerActive();
        ActivateAllButtons();
        updatePicture(imageButton,getApplicationContext(),getCurrFamilyId(),"Families");
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
