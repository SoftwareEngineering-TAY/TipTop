package com.example.tiptop.Adapters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import static android.os.Bundle.EMPTY;
import static androidx.core.content.ContextCompat.startActivity;
import static java.time.temporal.ChronoUnit.DAYS;

public class TaskToChildExtendListAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> ListChildForTask;
    private HashMap<String,ArrayList<Task>> ListTaskGroups;
    private HashMap<String,ArrayList<String>> ListTaskID;
    private Class dest;
    private int mLayoutResourceId;

    public TaskToChildExtendListAdapter(ArrayList<String> ListChildForTask , HashMap<String,ArrayList<Task>> ListTaskGroups,HashMap<String,ArrayList<String>> ListTaskID , int resource , Class dest){
        this.ListChildForTask = ListChildForTask;
        this.ListTaskGroups = ListTaskGroups;
        this.ListTaskID = ListTaskID;
        this.mLayoutResourceId = resource;
        this.dest = dest;
    }

    @Override
    public int getGroupCount() {
        return ListChildForTask.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ListTaskGroups.get(ListChildForTask.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return ListChildForTask.get(groupPosition);
    }

    @Override
    public Task getChild(int groupPosition, int childPosition) {
        return ListTaskGroups.get(ListChildForTask.get(groupPosition)).get(childPosition);
    }

    public String getTaskID(int groupPosition, int childPosition) {
        return ListTaskID.get(ListChildForTask.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_expandable_list_item_1,parent,false);
        TextView textView = convertView.findViewById(android.R.id.text1);
        String sGroup = String.valueOf(getGroup(groupPosition));
        textView.setText(sGroup);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.BLUE);
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View row;
        //inflate the layout for a single row
        row = LayoutInflater.from(parent.getContext()).inflate(mLayoutResourceId,parent,false);

        //get the data from the data array
        Task task = getChild(groupPosition,childPosition);
        //need to add cases for layout types!!!!!!!

        switch (mLayoutResourceId){

            case R.layout.row_task_with_bonus:
                TextView nameView = (TextView) row.findViewById(R.id.TaskNameRow);
                TextView bonusView = (TextView) row.findViewById(R.id.BonusPointRow);
                TextView statusView = (TextView) row.findViewById(R.id.StatusTaskRow);
                TextView timeView = (TextView) row.findViewById(R.id.TimeLeftRow);

                //setting the view to reflect the data we need to display
                nameView.setText(task.getNameTask());
                bonusView.setText("Bonus: " + task.getBonusScore());
                statusView.setText("Status: " + task.getStatus().toString());
                if (task.getEndDate()!=null){
                    long Days = LocalDate.now().until(LocalDate.parse(task.getEndDate()),DAYS);
                    timeView.setText("days left : "+ Days);
                }
                else timeView.setText("days left : inf");

                break;
            case R.layout.row_task_without_bonus:
                TextView nameViewWithoutBonus = (TextView) row.findViewById(R.id.TaskNameRow);
                TextView statusViewWithoutBonus = (TextView) row.findViewById(R.id.StatusTaskRow);
                TextView timeViewWithoutBonus= (TextView) row.findViewById(R.id.TimeLeftRow);

                //setting the view to reflect the data we need to display
                nameViewWithoutBonus.setText(task.getNameTask());
                statusViewWithoutBonus.setText("Status: " + task.getStatus().toString());
                if (task.getEndDate()!=null){
                    long Days = LocalDate.now().until(LocalDate.parse(task.getEndDate()),DAYS);
                    timeViewWithoutBonus.setText("days left : "+ Days);
                }
                else timeViewWithoutBonus.setText("days left : inf");

                break;

            case R.layout.row_task_history:
                //get a reference to the different view elements we wish to update
                TextView taskName = (TextView) row.findViewById(R.id.taskName);
                TextView ApprovalDate = (TextView) row.findViewById(R.id.taskApproval);

                taskName.setText(task.getNameTask());
                ApprovalDate.setText(task.getConfirmedDate());

                break;

        }


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(row.getContext(),dest );
                intent.putExtra("task",task);
                intent.putExtra("taskID",getTaskID(groupPosition,childPosition));
                startActivity(row.getContext(),intent,EMPTY);

            }
        });

        //returning the row view (because this is called getView after all)
        return row;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
