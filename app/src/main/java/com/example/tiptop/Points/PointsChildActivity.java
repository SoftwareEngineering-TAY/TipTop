package com.example.tiptop.Points;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.R;
import java.util.ArrayList;
import static com.example.tiptop.Database.Database.setNumOfPoints;

public class PointsChildActivity extends AppCompatActivity {

    private TextView numOfPoints;

    private ArrayList<Integer> sumOfPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_chilld);

        initializeClassVariables();
        setNumOfPoints(numOfPoints,sumOfPoints);
    }

    private void initializeClassVariables(){

        numOfPoints = (TextView)findViewById(R.id.numOfPoints);

        sumOfPoints = new ArrayList<>();
        sumOfPoints.add(0);
    }
}
