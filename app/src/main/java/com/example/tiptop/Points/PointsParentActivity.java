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
import com.example.tiptop.Objects.User;
import com.example.tiptop.R;
import java.util.ArrayList;
import static com.example.tiptop.Database.Database.getListOfChilds;

public class PointsParentActivity extends AppCompatActivity implements DataChangeListener {

    private TableLayout pointsTable;
    private TableRow titlesRow;
    private ArrayList<User> Childs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_parent);
        pointsTable = (TableLayout) findViewById(R.id.pointsTable);
        titlesRow = (TableRow) findViewById(R.id.titlesRow);
        notifyOnChange();
    }

    /**
     * This function creates a table that has the child's name and its bonus points.
     */
    public void setChildInfo(){
        for (User child : Childs) {
            TableRow tr = new TableRow(this);
            TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            tableRowParams.setMargins(0, 80, 0, 0);
            tr.setLayoutParams(tableRowParams);

            TextView nameText = new TextView(this);
            nameText.setText(child.getName());
            nameText.setTextColor(Color.BLACK);
            nameText.setTextSize(18);
            nameText.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView scoreText = new TextView(this);
            scoreText.setText((int) child.getPoints());
            scoreText.setTextColor(Color.BLACK);
            scoreText.setTextSize(18);
            scoreText.setGravity(Gravity.CENTER_HORIZONTAL);

            tr.addView(nameText);
            tr.addView(scoreText);
            pointsTable.addView(tr);
        }
    }

    @Override
    public void notifyOnChange() {
        pointsTable.removeAllViews();
        pointsTable.addView(titlesRow);
        Childs = getListOfChilds();
        setChildInfo();
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
