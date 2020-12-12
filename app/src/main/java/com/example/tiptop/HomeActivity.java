package com.example.tiptop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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

    //Variables that will be used to link with the database
    private FirebaseDatabase root;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String uid;

    //Variables that will be used to store information coming from the Internet
    private String permission;
    private String currFamilyId;

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
        spinerActive();
        initializationImage();
        ActivateAllButtons();
    }

    /**
     * The function retrieves from the database the family he selected and his permission.
     * This information is needed for almost all pages in the application and therefore we will pass the information in intent.
     */
    private void initializationCurrFamilyIdAndPermission() {
        root = FirebaseDatabase.getInstance();
        reference = root.getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        reference.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren() )
                {
                    if(ds.getKey().equals("currFamilyId")){
                        currFamilyId = (String) ds.getValue();
                        spinnerTitle =  (String) ds.getValue();
                    }
                    if (ds.getKey().equals("type")){
                        permission = (String) ds.getValue();
                    }
                }
                Log.v("****permissionnnnnn****", permission);
                Log.v("****currFamilyId****", currFamilyId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * The function activates the spinner component, meaning it fills in the spinner all
     * the families to which the user belongs by retrieving what DB. And when you click on another last
     * name the currFamilyId changes in the title and in DB and this is what is transmitted on the Internet.
     */
    private void spinerActive() {
        //Set the SpinnerFamily Spinner
        SpinnerFamily = (Spinner)findViewById(R.id.SpinnerFamily);

        //Initialize the 2 lists that will save all the last names and all the keys respectively.
        allKeys = new ArrayList<>();
        allFamilies = new ArrayList<>();

        //A function that retrieves all information from the database
        getInfoFromDB();

        //Connecting the list to the view
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allFamilies);
        SpinnerFamily.setAdapter(adapter);

        //Defines the functionality of a last name click
        SpinnerFamily.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currFamilyId = allKeys.get(position);
                spinnerTitle = allFamilies.get(position);
                reference.child("Users").child(uid).child("currFamilyId").setValue(currFamilyId);
                Log.v("****currFamilyId****", currFamilyId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getInfoFromDB() {
        //Updating the spinner
        reference.child("UserFamilies").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allFamilies.clear();
                allKeys.clear();
                for (DataSnapshot ds : snapshot.getChildren() )
                {
                    String toAddFamiliy =(String) ds.getValue();
                    String toAddKey =ds.getKey();
                    System.out.println("ds.getKey()!!!!!!!!!!"+ds.getKey());
                    allFamilies.add(toAddFamiliy);
                    allKeys.add(toAddKey);
                }
                int pos=allKeys.indexOf(currFamilyId);
                if(pos>0)
                {
                    String Family=allFamilies.get(pos);
                    allFamilies.remove(Family);
                    allFamilies.add(0,Family);

                    allKeys.remove(currFamilyId);
                    allKeys.add(0,currFamilyId);
                }
                adapter.notifyDataSetChanged();
                System.out.println(allFamilies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    private void ActivateAllButtons() {
        //Moves to the settings activity
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = mAuth.getCurrentUser().getUid();
                reference = root.getReference("Users").child(uid).child("type");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                       String type =  snapshot.getValue().toString();
                       if(type.equals("Parent")){
                           Intent go_to_setting = new Intent(v.getContext(),SettingActivity.class);
                           startActivity(go_to_setting);
                       }
                       else if(type.equals("Child")){
                           Intent go_to_setting_children = new Intent(v.getContext(),SettingChildrenActivity.class);
                           startActivity(go_to_setting_children);
                       }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        //Moves to the profile activity
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ProfileActivity.class);
                startActivity(i);
            }
        });

        //Moves to the followUp activity
        followUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),FollowUpActivity.class);
                i.putExtra("currFamilyId", currFamilyId);
                i.putExtra("permission",permission);
                startActivity(i);
            }
        });

        //Moves to the Statistics activity
        Statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),StatisticsActivity.class);
                startActivity(i);
            }
        });

        //Moves to the chat activity
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ChatActivity.class);
                startActivity(i);
            }
        });

        //Moves to the Tasks activity
        Tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if (permission.equals("Parent")){
                    i = new Intent(v.getContext(), PoolTasksParentActivity.class);
                }

                else if (permission.equals("Child")){
                    i = new Intent(v.getContext(), PoolTaskChildActivity.class);
                }
                else return;
                i.putExtra("currFamilyId", currFamilyId);
                startActivity(i);
            }
        });

        //Moves to the history activity
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),HistoryActivity.class);
                startActivity(i);
            }
        });

        //Moves to the points activity
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),PointsActivity.class);
                startActivity(i);
            }
        });

        //Moves to the ImageButton activity
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewImagwButton();
                uploadImage(currFamilyId);
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

    private void uploadImage(String family_key){
        String path = "Families/" + family_key;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = storage.getReference(path);
        if(uriImage != null){
            mStorageRef.putFile(uriImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: uri upload went good");
                    }
                    else{
                        Log.d(TAG, "onComplete: uri upload failed.");
                    }
                }
            });
        }
        else if(bitmapImage != null){
            ByteArrayOutputStream to_stream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG,100, to_stream);
            byte bytes[] = to_stream.toByteArray();
            mStorageRef.putBytes(bytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: bitmap upload went good");
                    }
                    else{
                        Log.d(TAG, "onComplete: bitmap upload failed.");
                    }
                }
            });
        }
    }

    //need to ask yirat
    private void initializationImage() {
        String path = "Families/" + currFamilyId;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = storage.getReference(path);
        if (mStorageRef != null)
        {

        }

        else
        {
            imageButton.setImageResource(R.drawable.new_family);
        }

    }


}
