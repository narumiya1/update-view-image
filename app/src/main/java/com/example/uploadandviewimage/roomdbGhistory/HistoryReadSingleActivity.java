package com.example.uploadandviewimage.roomdbGhistory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadandviewimage.GrainHistory;
import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.utils.AppUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HistoryReadSingleActivity extends AppCompatActivity {

    GrainHistory history;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_single);

        TextView  tv_item2, tv_item3,itemTimes;
        tv_item3=findViewById(R.id.tv_items3);
        itemTimes = findViewById(R.id.item_time);
        tv_item2=findViewById(R.id.tv_items2);
        history = (GrainHistory) getIntent().getSerializableExtra("data");

        //pie chart
        PieChart pieChart, pieCharts;
        pieChart = findViewById(R.id.chart_type);
        pieCharts = findViewById(R.id.chart_sizec);
        pieChart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

        ArrayList<GrainPie> myTypes = history.getType();
        int count =myTypes.size();
        StringBuilder builder = new StringBuilder();
        StringBuilder builders = new StringBuilder();
        for (int i=0;i<count;i++) {

            //chart typez
            GrainPie type = myTypes.get(i);
            float a = (float) type.getValue();
            int b = (int)Math.round(a);
            yvalues.add(new PieEntry((float)type.getValue(), type.getName(), i + 1));
            //text
            String s = type.getName();
            builder.append(s).append(" - ").append(b).append(" ").append("\t\r\n");
        }
        tv_item2.setText(builder.toString());

        itemTimes.setText(AppUtils.getFormattedDateString(history.getDateTime()));


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

        //size chart
        ArrayList<GrainPie> mySize = history.getSize();
        pieCharts.setUsePercentValues(true);
        ArrayList<PieEntry> valuesz = new ArrayList<PieEntry>();

        for (int j=0;j<count;j++) {
            GrainPie sizc = mySize.get(j);
            float a = (float) sizc.getValue();
            int b = (int)Math.round(a);
            valuesz.add(new PieEntry((float)sizc.getValue(), sizc.getName(), j + 1));
            //text
            String s = sizc.getName();
            builders.append(s).append(" - ").append(b).append(" ").append("\t\r\n");
        }

        PieDataSet dataSetz = new PieDataSet(valuesz, getString(R.string.election_results));
        PieData dataz = new PieData(dataSetz);

        dataz.setValueFormatter(new PercentFormatter());
        pieCharts.setData(dataz);
        Description descriptionz = new Description();
        descriptionz.setText(getString(R.string.pie_chart));
        pieCharts.setDescription(descriptionz);
        pieCharts.setDrawHoleEnabled(true);
        pieCharts.setTransparentCircleRadius(58f);
        pieCharts.setHoleRadius(58f);
        dataSetz.setColors(ColorTemplate.MATERIAL_COLORS);
        dataz.setValueTextSize(13f);
        dataz.setValueTextColor(Color.BLACK);
        tv_item3.setText(builders.toString());

    }

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, HistoryReadSingleActivity.class);
    }
}



