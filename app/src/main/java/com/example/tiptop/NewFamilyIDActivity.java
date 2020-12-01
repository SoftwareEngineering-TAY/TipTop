package com.example.tiptop;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NewFamilyIDActivity extends AppCompatActivity {

    private static final String TAG = "NewFamilyIDActivity";

    private com.google.android.material.textfield.TextInputLayout family_id;
    private ImageButton new_image;
    private Spinner route_type;
    private Button finish;
    private TextView route_information;
    private String route_type_in_firebase;
    private User user_to_add;

    private FirebaseDatabase root;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_family_id);

        new_image = (ImageButton) findViewById(R.id.newImage);
        route_type = (Spinner) findViewById(R.id.route_type_spinner);
        finish = (Button) findViewById(R.id.finish);
        family_id = findViewById(R.id.familyId);
        route_information = (TextView) findViewById(R.id.route_information);
        user_to_add = new User();
        root = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reference = root.getReference();

        Bundle extras = getIntent().getExtras();

        user_to_add = (User)extras.get("user");

        setNewImagwButton();

        setRouteTypeSpinner();

        setRouteInformation();

        setFinishButton();
    }

    private void setRouteInformation(){

        route_information.setOnClickListener(new View.OnClickListener() {

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(NewFamilyIDActivity.this);

            @Override
            public void onClick(View v) {
                if(route_type_in_firebase.equals("With bonuses")){
                    dlgAlert.setCancelable(true);
                    dlgAlert.setMessage("Explanation about With bonuses route...");
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
                    dlgAlert.setMessage("Explanation about Without bonuses route...");
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewFamilyIDActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Route));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        route_type.setAdapter(adapter);

        route_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: From spinner item selected");
                route_type_in_firebase = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setNewImagwButton(){
        new_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(NewFamilyIDActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo"))
                        {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 1);
                        }
                        else if (options[item].equals("Choose from Gallery"))
                        {
                            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                        }
                        else if (options[item].equals("Cancel")) {
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
            if (requestCode == 1) {
                Bundle extras = data.getExtras();
                Bitmap bit = (Bitmap)extras.get("data");
                new_image.setImageBitmap(bit);
            }
            else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                new_image.setImageURI(selectedImage);
            }
        }
    }

    private void setFinishButton(){

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createUserInFireBase();

                Intent go_login = new Intent(v.getContext(),LoginActivity.class);
                startActivity(go_login);
            }
        });
    }

    private void createUserInFireBase(){

        mAuth.createUserWithEmailAndPassword(user_to_add.getEmail(),user_to_add.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: Sign to Auth successed.");
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String uid = firebaseUser.getUid();
                    Log.d(TAG, "onComplete: From auth, Type:" + user_to_add.getType() + ",  uid:" + uid);
                    reference.child("users").child(family_id.getEditText().getText().toString()).child(user_to_add.getType()).child(uid).setValue(user_to_add);
                    reference.child("users").child(family_id.getEditText().getText().toString()).child("Route").setValue(route_type_in_firebase);
                    Log.d(TAG, "onComplete: user have been auth and saved to database" + user_to_add.toString());
                }
                else{
                    //Auth went wrong the sign in failed.
                    Log.d(TAG, "onComplete: Auth failed" + task.getException().toString());
                }

            }
        });
    }

}
