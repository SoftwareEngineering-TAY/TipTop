package com.example.tiptop.Points;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.R;
import static com.example.tiptop.Database.Database.getPoints;

public class PointsChildActivity extends AppCompatActivity {

    private TextView numOfPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_chilld);
        numOfPoints = (TextView)findViewById(R.id.numOfPoints);
        getPoints(numOfPoints);
    }

}
