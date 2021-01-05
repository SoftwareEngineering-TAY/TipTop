package com.example.tiptop.Points;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database2;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database2.getPoints;

public class PointsChildActivity extends AppCompatActivity implements DataChangeListener {

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
        Database2.addListener(this);
    }

    @Override
    protected void onPause() {
        Database2.removeListener(this);
        super.onPause();
    }
}
