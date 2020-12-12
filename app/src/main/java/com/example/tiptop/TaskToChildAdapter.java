package com.example.tiptop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Bundle.EMPTY;
import static androidx.core.content.ContextCompat.startActivity;

public class TaskToChildAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> ListChildForTask;
    private HashMap<String,ArrayList<Task>> ListTaskGroups;

    public TaskToChildAdapter(ArrayList<String> ListChildForTask , HashMap<String,ArrayList<Task>> ListTaskGroups){
        this.ListChildForTask = ListChildForTask;
        this.ListTaskGroups = ListTaskGroups;
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

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View row;
        //inflate the layout for a single row
        row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_task,parent,false);

        //get a reference to the different view elements we wish to update
        TextView nameView = (TextView) row.findViewById(R.id.TaskNameRow);
        TextView bonusView = (TextView) row.findViewById(R.id.BonusPointRow);
        TextView statusView = (TextView) row.findViewById(R.id.StatusTaskRow);
        TextView timeView = (TextView) row.findViewById(R.id.TimeLeftRow);

        //get the data from the data array
        Task task = getChild(groupPosition,childPosition);

        //setting the view to reflect the data we need to display
        nameView.setText(task.getNameTask());
        bonusView.setText("Bonus: " + task.getBonusScore());
        statusView.setText("Status: " + task.getStatus().toString());
        timeView.setText("day left : inf");

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(row.getContext(), TaskInfoActivity.class);
                intent.putExtra("task",task);
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
