package com.example.tiptop.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.example.tiptop.Objects.Task;
import com.example.tiptop.R;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

public class TaskListAdapter extends ArrayAdapter<Task> {

    Context mContext;
    int mLayoutResourceId;
    ArrayList<Task> mData = null;

    public TaskListAdapter(Context context, int resource, ArrayList<Task> data) {
        super(context, resource, data);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mData = data;
    }

    @Override
    public Task getItem(int position) {
        return super.getItem(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        if (task.getEndDate()!=null){
            long Days = LocalDate.now().until(LocalDate.parse(task.getEndDate()),DAYS);
            timeView.setText("days left : "+ Days);
        }
        else timeView.setText("days left : inf");

        //returning the row view (because this is called getView after all)
        return row;
    }
}



