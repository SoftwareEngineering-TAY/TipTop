package com.example.tiptop.LogInAndSignUp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.Objects.User;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.createUserInFireBase;

public class CreateFamilyActivity extends AppCompatActivity implements DataChangeListener {

    private com.google.android.material.textfield.TextInputLayout familyName;
    private ImageButton newImage;
    private Spinner route_type;
    private Button finish;
    private TextView route_information;
    private String route_type_in_firebase;
    private User user_to_add;
    private Bitmap bitmap_image =null;
    private Uri uri_image = null;
    private static final int CAMERA_PHOTO = 1;
    private static final int GALLERY_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_family_id);
        Bundle extras = getIntent().getExtras();
        user_to_add = (User)extras.get("user");
        initializeClassVariables();
        notifyOnChange();
    }

    private void initializeClassVariables(){
        newImage = (ImageButton) findViewById(R.id.newImage);
        route_type = (Spinner) findViewById(R.id.route_type_spinner);
        finish = (Button) findViewById(R.id.finish);
        familyName = findViewById(R.id.familyName);
        route_information = (TextView) findViewById(R.id.route_information);
        bitmap_image = null;
        uri_image = null;
    }

    private void setRouteInformation(){
        route_information.setOnClickListener(new View.OnClickListener() {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(CreateFamilyActivity.this);
            @Override
            public void onClick(View v) {
                if(route_type_in_firebase.equals("With bonuses")){
                    dlgAlert.setCancelable(true);
                    dlgAlert.setMessage("In this route you can determine the value of each task and depending on the points the child has accumulated you can agree with the child on a gift or something else he wants.");
                    dlgAlert.setTitle("With bonuses:");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                        }
                    });
                }
                else if(route_type_in_firebase.equals("Without bonuses")) {
                    dlgAlert.setCancelable(true);
                    dlgAlert.setMessage("In this route no points will be given to the child for tasks he has done.");
                    dlgAlert.setTitle("Without bonuses");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                        }
                    });
                }
            }
        });
    }

    private void setRouteTypeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateFamilyActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Route));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        route_type.setAdapter(adapter);

        route_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                route_type_in_firebase = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setNewImagwButton(){
        newImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Take photo from camera", "Choose photo from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateFamilyActivity.this);
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

    private void setFinishButton(){
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserInFireBase(user_to_add, familyName.getEditText().getText().toString(),route_type_in_firebase, uri_image , bitmap_image, null);
                Intent go_login = new Intent(v.getContext(),LoginActivity.class);
                startActivity(go_login);
            }
        });
    }

    @Override
    public void notifyOnChange() {
        setNewImagwButton();
        setRouteTypeSpinner();
        setRouteInformation();
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
