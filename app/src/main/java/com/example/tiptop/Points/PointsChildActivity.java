package com.example.tiptop.Points;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.R;
import java.util.ArrayList;

import static com.example.tiptop.Database.Database.getPoints;
//import static com.example.tiptop.Database.Database.setNumOfPoints;

public class PointsChildActivity extends AppCompatActivity {

    private TextView numOfPoints;
    private long[] sumOfPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_chilld);
        numOfPoints = (TextView)findViewById(R.id.numOfPoints);
        sumOfPoints = new long[1];
        getPoints(sumOfPoints);
        numOfPoints.setText(String.valueOf(sumOfPoints[0]));
    }

}
