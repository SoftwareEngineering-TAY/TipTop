package com.example.tiptop.Points;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.getPoints;

public class PointsChildActivity extends AppCompatActivity implements DataChangeListener {

    //Fields
    private TextView numOfPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_chilld);
        numOfPoints = (TextView)findViewById(R.id.numOfPoints);
        notifyOnChange();
    }

    @Override
    public void notifyOnChange() {
        getPoints(numOfPoints);
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
