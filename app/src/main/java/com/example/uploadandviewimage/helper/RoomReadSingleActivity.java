package com.example.uploadandviewimage.helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadandviewimage.GrainHistory;
import com.example.uploadandviewimage.GrainHistoryCollection;
import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.GrainType;
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

public class RoomReadSingleActivity extends AppCompatActivity {
    GrainTypeData type;
    GrainHistory history;
    public ArrayList<GrainTypeData> listHistoria;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_single);

        TextView tv_item1, tv_item2, tv_item3, tv_jumlah1, tv_jumlah2, tv_jumlah3, itemTimes;
        tv_item1=findViewById(R.id.tv_item1);
        itemTimes = findViewById(R.id.item_time);
        tv_jumlah1 = findViewById(R.id.tv_jumlah1);
        tv_item2=findViewById(R.id.tv_item2);
        history = (GrainHistory) getIntent().getSerializableExtra("data");

        //pie chart
        PieChart pieChart;
        pieChart = findViewById(R.id.chart_detail);
        pieChart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

        ArrayList<GrainPie> myTypes = history.getType();
        int count =myTypes.size();
        StringBuilder builder = new StringBuilder();
        //String[] arr = {type.getNamaType()};
        for (int i=0;i<count;i++) {
            //chart
            GrainPie type = myTypes.get(i);
            yvalues.add(new PieEntry((float)type.getValue(), type.getName(), i + 1));
            //text
            String s = type.getName();
            builder.append(s).append(" - ").append(type.getValue()).append("%").append("\t\r\n");
        }

        tv_item2.setText(builder.toString());


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

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, RoomReadSingleActivity.class);
    }
}



