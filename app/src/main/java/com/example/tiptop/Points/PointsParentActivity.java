package com.example.tiptop.Points;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database2;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.setNamesAndScores;

public class PointsParentActivity extends AppCompatActivity implements DataChangeListener {

    private TableLayout pointsTable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_parent);

        pointsTable = (TableLayout) findViewById(R.id.pointsTable);
    }

    public void setChildName(String name, Long score){

        TableRow tr = new TableRow(this);
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tableRowParams.setMargins(10, 50, 10, 10);
        tr.setLayoutParams(tableRowParams);

        TextView nameText = new TextView(this);
        nameText.setText(name);
        nameText.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView scoreText = new TextView(this);
        scoreText.setText(score.toString());
        scoreText.setGravity(Gravity.CENTER_HORIZONTAL);

        tr.addView(nameText);
        tr.addView(scoreText);
        pointsTable.addView(tr);
    }

    @Override
    public void notifyOnChange() {
        //setNamesAndScores(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Database2.addListener(this);
    }

    @Override
    protected void onPause() {
        Database2.removeListener(this);
        super.onPause();
    }
}
