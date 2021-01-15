package com.example.tiptop.Statistics;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import static com.example.tiptop.Database.Database.getPermission;
import static com.example.tiptop.Database.Database.initializeArraysFromDB;

public class StatisticsActivity extends AppCompatActivity implements DataChangeListener {

    private BarChart barChart;
    private PieChart pieChart;
    private ArrayList<BarEntry> visitors;
    private ArrayList<PieEntry> visitors2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        initializeClassVariables();
        notifyOnChange();
    }

    /**
     * This function initializes all the required fields from the relevant XML file And of the class
     */
    private void initializeClassVariables() {
        barChart = findViewById(R.id.BarChart);
        visitors = new ArrayList<>();
        pieChart = findViewById(R.id.PieChart);
        visitors2 = new ArrayList<>();
    }

    /**
     * all settings of the charts
     */
    private void setSettingsCharts() {
        BarDataSet barDataSet = new BarDataSet(visitors,"");
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        if(getPermission().equals("Parent")) barChart.getDescription().setText("Tasks per Child");
        else barChart.getDescription().setText("Bonus per Task");
        barChart.getDescription().setTextSize(14f);
        barChart.animateY(2000);

        PieDataSet pieDataSet = new PieDataSet(visitors2,"");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setText("Task - Days");
        pieChart.getDescription().setTextSize(14f);
        if(getPermission().equals("Parent")) pieChart.setCenterText("AVG Days to complete Task");
        else pieChart.setCenterText("Days to complete Task");
        pieChart.setCenterTextSize(18f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(16f);
        pieChart.animateXY(2000,2000);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void notifyOnChange() {
        initializeArraysFromDB(visitors,visitors2);
        setSettingsCharts();
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
