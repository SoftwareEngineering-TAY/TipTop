package com.example.tiptop.Points;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.setNamesAndScores;

public class PointsParentActivity extends AppCompatActivity implements DataChangeListener {

    private TableLayout pointsTable;
    private TableRow titlesRow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_parent);

        pointsTable = (TableLayout) findViewById(R.id.pointsTable);
        titlesRow = (TableRow) findViewById(R.id.titlesRow);

        notifyOnChange();
    }

    public void setChildName(String name, Long score){

        TableRow tr = new TableRow(this);
        TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
        tableRowParams.setMargins(0, 80, 0,0 );
        tr.setLayoutParams(tableRowParams);

        TextView nameText = new TextView(this);
        nameText.setText(name);
        nameText.setTextColor(Color.BLACK);
        nameText.setTextSize(18);
        nameText.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView scoreText = new TextView(this);
        scoreText.setText(score.toString());
        scoreText.setTextColor(Color.BLACK);
        scoreText.setTextSize(18);
        scoreText.setGravity(Gravity.CENTER_HORIZONTAL);

        tr.addView(nameText);
        tr.addView(scoreText);
        pointsTable.addView(tr);
    }

    @Override
    public void notifyOnChange() {
        pointsTable.removeAllViews();
        pointsTable.addView(titlesRow);
        setNamesAndScores(this);
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
