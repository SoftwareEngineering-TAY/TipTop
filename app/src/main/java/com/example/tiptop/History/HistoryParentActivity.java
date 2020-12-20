package com.example.tiptop.History;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Adapters.TaskToChildExtendListAdapter;
import com.example.tiptop.FollowUp.ApproveTaskActivity;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryParentActivity extends AppCompatActivity {

    private Spinner filterSpinner;
    private EditText search;
//    private TableLayout tasksTable;
//    private ListView taskName;
//    private ListView taskDate;

    private ExpandableListView AssociatedTasks;

    private ArrayList<String> ListChildForTask;
    private HashMap<String,ArrayList<Task>> ListTaskGroups;

    private TaskToChildExtendListAdapter childAdapter;

    public String spinnerChildTitle;

    private FirebaseDatabase root;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String uid;
    private String currFamilyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initializeClassVariables();

//        setFilterSpinner();

        createExpandableListOfTask();
//        setTasklists();
    }

    private void initializeClassVariables(){
        filterSpinner = findViewById(R.id.filterSpinner);
        search = findViewById(R.id.searchEditText);

        AssociatedTasks = (ExpandableListView) findViewById(R.id.ListHistoryTasks);


//        tasksTable = (TableLayout) findViewById(R.id.tasksTable);
//
//        taskName = findViewById(R.id.taskNameList);
//        taskDate = findViewById(R.id.taskDateList);

        root = FirebaseDatabase.getInstance();
        reference = root.getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        Bundle extras = getIntent().getExtras();
        currFamilyId = (String) extras.get("currFamilyId");
    }

//    private void setFilterSpinner(){
//
//        ArrayList<String> childrenID = new ArrayList<>();
//        ArrayList<String> allChildren = new ArrayList<>();
//
//        ArrayAdapter childrenAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_dropdown_item, allChildren);
//
//        filterSpinner.setAdapter(childrenAdapter);
//
//        reference = root.getReference().child("Families").child(currFamilyId);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                spinnerChildTitle =  "Children";
//                childrenID.clear();
//                allChildren.clear();
//
//                for (DataSnapshot ds : snapshot.getChildren() ) {
//
//                    if (!ds.getKey().equals("Family name")) {
//
//                        String userId = ds.getKey();
//
//                        reference = root.getReference().child("Users").child(userId).child("type");
//                        reference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                if (snapshot.getValue().equals("Child")) {
//                                    reference = root.getReference().child("Users").child(userId).child("name");
//                                    reference.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                            childrenID.add(userId);
//                                            allChildren.add(snapshot.getValue().toString());
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                    }
//                }
//
//                childrenAdapter.notifyDataSetChanged();
//                Log.v("allChildren!!!!!!!!!!!!",allChildren.toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//        //Connecting the list to the view
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allChildren);
//        filterSpinner.setAdapter(adapter);
//
//        //Defines the functionality of a last name click
//        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                spinnerChildTitle = allChildren.get(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//    }

//    private void setTasklists(){
//
//        tasksTable.setColumnStretchable(0,true);
//        tasksTable.setColumnStretchable(1,true);
//        TableRow newRow = new TableRow(HistoryActivity.this);
//        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
//        newRow.setLayoutParams(lp);
//        newRow.setPadding(20,20,20,20);
//
//        TextView taskDescription = new TextView(HistoryActivity.this);
//        taskDescription.setText("Task Description");
//
//        TextView taskFinishDate = new TextView(HistoryActivity.this);
//        taskFinishDate.setText("task Finish Date");
//
//        newRow.addView(taskDescription);
//        newRow.addView(taskFinishDate);
//
//        tasksTable.addView(newRow);
//
////        ArrayList<String> taskNameArray = new ArrayList<>();
////        ArrayList<String> taskIDArray = new ArrayList<>();
////        ArrayList<String> endDateArray = new ArrayList<>();
////
////        ArrayAdapter taskNameAdapter = new ArrayAdapter<String>(this,
////                android.R.layout.simple_list_item_1, taskNameArray);
////
////        ArrayAdapter taskDateAdapter = new ArrayAdapter<String>(this,
////                android.R.layout.simple_list_item_1, endDateArray);
////
////        taskName.setAdapter(taskNameAdapter);
////        taskDate.setAdapter(taskDateAdapter);
//
//        reference = root.getReference().child("Tasks").child(currFamilyId);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
////                taskNameArray.clear();
////                taskIDArray.clear();
////                endDateArray.clear();
//
//                for(DataSnapshot ds : snapshot.getChildren()){
//
//                    Task task  = ds.getValue(Task.class);
//
////                    if(task.getStatus().equals("Confirmed")){
//
//
//
//                    TableRow newRow = new TableRow(HistoryActivity.this);
//                    newRow.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast toast = Toast.makeText(HistoryActivity.this, "yayyy", Toast.LENGTH_LONG);
//                            toast.show();
//                        }
//                    });
//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
//                    newRow.setLayoutParams(lp);
//                    newRow.setPadding(20,20,20,20);
//
//                    TextView taskDescription = new TextView(HistoryActivity.this);
//                    taskDescription.setText(task.getNameTask());
//
//                    TextView taskFinishDate = new TextView(HistoryActivity.this);
//                    taskFinishDate.setText(task.getEndDate());
//
//                    newRow.addView(taskDescription);
//                    newRow.addView(taskFinishDate);
//
//
//                    tasksTable.addView(newRow);
//
//
//
//
//
//
////                    taskIDArray.add(ds.getKey());
////                    taskNameArray.add(task.getNameTask());
////                    endDateArray.add(task.getEndDate());
//
////                    }
//
//                }
////                taskNameAdapter.notifyDataSetChanged();
////                taskDateAdapter.notifyDataSetChanged();
////                Log.v("allTasks!!!!!!!!!!!!",taskNameArray.toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }


    private void createExpandableListOfTask() {
        ListChildForTask = new ArrayList<>(); //list group
        ListTaskGroups = new HashMap<>(); //list child
        childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,currFamilyId, ApproveTaskActivity.class);
        AssociatedTasks.setAdapter(childAdapter);
        Log.v("family id///////////: ",currFamilyId);

        reference.child("Families").child(currFamilyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListChildForTask.clear();
                for (DataSnapshot ds : snapshot.getChildren() )
                {
                    String toAddChildren =(String) ds.getValue();
                    reference.child("Users").child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("type").getValue()!=null && snapshot.child("type").getValue().toString().equals("Child"))
                            {
                                ListChildForTask.add(toAddChildren);
                                Log.v("toaddChildren*******",toAddChildren);
                                ArrayList<Task> toAdd = new ArrayList<>();
                                reference.child("Tasks").child(currFamilyId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        toAdd.clear();
                                        for (DataSnapshot ds1 : snapshot.getChildren() )
                                        {
                                            if(ds1.child("belongsToUID").getValue()!=null && ds1.child("belongsToUID").getValue().equals(ds.getKey()) && ds1.child("status").getValue().equals("Confirmed")){
                                                Task taskToAdd = ds1.getValue(Task.class);
                                                toAdd.add(taskToAdd);
                                                Log.v("task confirm: ", taskToAdd.getNameTask());
                                            }
                                        }
                                        ListTaskGroups.put(toAddChildren,toAdd);
                                        System.out.println("ListChildForTask1"+ListChildForTask);
                                        System.out.println("ListTaskGroups1"+ListTaskGroups);

                                        childAdapter = new TaskToChildExtendListAdapter(ListChildForTask,ListTaskGroups,currFamilyId, ApproveTaskActivity.class);
                                        AssociatedTasks.setAdapter(childAdapter);
                                        childAdapter.notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
