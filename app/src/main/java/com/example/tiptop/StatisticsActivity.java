package com.example.tiptop;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database2;
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

import static com.example.tiptop.Database.Database2.initializeArraysFromDB;

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



    private void initializeClassVariables() {
        barChart = findViewById(R.id.BarChart);
        visitors = new ArrayList<>();
        pieChart = findViewById(R.id.PieChart);
        visitors2 = new ArrayList<>();
    }

    private void setSettingsCharts() {
        BarDataSet barDataSet = new BarDataSet(visitors,"");
        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("count Tasks");
        barChart.animateY(2000);

        PieDataSet pieDataSet = new PieDataSet(visitors2,"");
        pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setText("Avg Time");
        pieChart.setCenterText("AVG Time");
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
        Database2.addListener(this);
    }

    @Override
    protected void onPause() {
        Database2.removeListener(this);
        super.onPause();
    }
}
