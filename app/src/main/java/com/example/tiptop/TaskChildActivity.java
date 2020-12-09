package com.example.tiptop;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaskChildActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    ListView listOfTasks;
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String FamilyID="";
    private ArrayAdapter arrAdapter;
    String [] allElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_child);

        initializeClassVariables();
        setAdapterFunc();

    }

    private void setAdapterFunc() {
        //link between listOfTasks and arrAdapter
        if(listOfTasks!=null) {
            listOfTasks.setAdapter(arrAdapter);
        }

        listOfTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    private void initializeClassVariables() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        listOfTasks=(ListView)findViewById(R.id.ListOfTasks);
        arrAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allElement);
    }


}
