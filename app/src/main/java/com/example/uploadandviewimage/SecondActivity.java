package com.example.uploadandviewimage;

import android.graphics.Color;
import android.os.Bundle;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.PendingIntent.getActivity;


public class SecondActivity extends AppCompatActivity {
    private TextView total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        total = (TextView) findViewById(R.id.total);
        Bundle bundle = getIntent().getExtras();
        GrainPie[] type = (GrainPie[]) bundle.get("DataSaya");
        GrainPie[] size =  (GrainPie[]) bundle.get("Size");

        drawChart(type);
        drwawChartSize(size);
    }

    private void drwawChartSize(GrainPie[] size) {
        PieChart pieChart;
        pieChart = findViewById(R.id.chart2);
        pieChart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry((float)size[0].getValue(), size[0].getName(), 0));
        yvalues.add(new PieEntry((float)size[1].getValue(), size[1].getName(), 1));
        yvalues.add(new PieEntry((float)size[2].getValue(), size[2].getName(), 2));

        PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.election_results));
        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        Description description = new Description();
        description.setText(getString(R.string.pie_chart));
        pieChart.setDescription(description);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setHoleRadius(58f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.BLACK);
    }


    private void drawChart(GrainPie[] type ) {
        PieChart pieChart;
        pieChart = findViewById(R.id.chart1);
        pieChart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry((float)type[0].getValue(), type[0].getName(), 0));
        yvalues.add(new PieEntry((float)type[1].getValue(), type[1].getName(), 1));
        yvalues.add(new PieEntry((float)type[2].getValue(), type[2].getName(), 2));

        PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.election_results));
        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        Description description = new Description();
        description.setText(getString(R.string.pie_chart));
        pieChart.setDescription(description);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setHoleRadius(58f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.BLACK);

        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

    }

}

