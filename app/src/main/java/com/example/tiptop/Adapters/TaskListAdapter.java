package com.example.tiptop.Adapters;

import android.annotation.SuppressLint;
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

    private final Context mContext;
    private final int mLayoutResourceId;
    private final ArrayList<Task> mData;

    /**
     * default constructor
     */
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

    @SuppressLint({"ViewHolder", "NonConstantResourceId", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        /**
         * inflate the layout for a single row
         */
        LayoutInflater inflater = LayoutInflater.from(mContext);
        row = inflater.inflate(mLayoutResourceId, parent, false);

        /**
         * get the data from the data array
         */
        Task task = mData.get(position);

        switch (mLayoutResourceId) {

            case R.layout.row_task_with_bonus:
                /**
                 * get a reference to the different view elements we wish to update
                 */
                TextView nameView = (TextView) row.findViewById(R.id.TaskNameRow);
                TextView bonusView = (TextView) row.findViewById(R.id.BonusPointRow);
                TextView statusView = (TextView) row.findViewById(R.id.StatusTaskRow);
                TextView timeView = (TextView) row.findViewById(R.id.TimeLeftRow);

                /**
                 * setting the view to reflect the data we need to display
                 */
                nameView.setText(task.getNameTask());
                bonusView.setText("Bonus: " + task.getBonusScore());
                statusView.setText("Status: " + task.getStatus().toString());
                if (task.getEndDate() != null) {
                    long Days = LocalDate.now().until(LocalDate.parse(task.getEndDate()), DAYS);
                    timeView.setText("days left : " + Days);
                } else timeView.setText("days left : inf");

                break;

            case R.layout.row_task_without_bonus:
                /**
                 * get a reference to the different view elements we wish to update
                 */
                TextView nameViewWithoutBonus = (TextView) row.findViewById(R.id.TaskNameRow);
                TextView statusViewWithoutBonus = (TextView) row.findViewById(R.id.StatusTaskRow);
                TextView timeViewWithoutBonus = (TextView) row.findViewById(R.id.TimeLeftRow);

                /**
                 * setting the view to reflect the data we need to display
                 */
                nameViewWithoutBonus.setText(task.getNameTask());
                statusViewWithoutBonus.setText("Status: " + task.getStatus().toString());
                if (task.getEndDate() != null) {
                    long Days = LocalDate.now().until(LocalDate.parse(task.getEndDate()), DAYS);
                    timeViewWithoutBonus.setText("days left : " + Days);
                } else timeViewWithoutBonus.setText("days left : inf");
                break;

            case R.layout.row_task_history:
                /**
                 * get a reference to the different view elements we wish to update
                 */
                TextView taskName = (TextView) row.findViewById(R.id.taskName);
                TextView ApprovalDate = (TextView) row.findViewById(R.id.taskApproval);

                taskName.setText(task.getNameTask());
                ApprovalDate.setText(task.getConfirmedDate());

                break;
        }
        /**
         * returning the row view (because this is called getView after all)
         */
        return row;
    }
}



