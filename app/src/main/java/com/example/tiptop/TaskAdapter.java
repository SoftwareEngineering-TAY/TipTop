package com.example.tiptop;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {

    Context mContext;
    int mLayoutResourceId;
    ArrayList<Task> mData = null;

    public TaskAdapter(Context context, int resource, ArrayList<Task> data) {
        super(context, resource, data);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mData = data;
    }

    @Override
    public Task getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        //inflate the layout for a single row
        LayoutInflater inflater = LayoutInflater.from(mContext);
        row = inflater.inflate(mLayoutResourceId,parent,false);

        //get a reference to the different view elements we wish to update
        TextView nameView = (TextView) row.findViewById(R.id.TaskNameRow);
        TextView bonusView = (TextView) row.findViewById(R.id.BonusPointRow);
        TextView statusView = (TextView) row.findViewById(R.id.StatusTaskRow);
        TextView timeView = (TextView) row.findViewById(R.id.TimeLeftRow);


        //get the data from the data array
        Task task = mData.get(position);

        //setting the view to reflect the data we need to display
        nameView.setText(task.getNameTask());
        bonusView.setText("Bonus: " + task.getBonusScore());
        statusView.setText("Status: " + task.getStatus().toString());
        timeView.setText("day left : inf");

        //returning the row view (because this is called getView after all)
        return row;
    }
}



