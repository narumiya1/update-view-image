package com.example.uploadandviewimage;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private TextView total, tipe;
    Button share_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        total = (TextView) findViewById(R.id.total_size);
        tipe = findViewById(R.id.tipezs);
        share_btn=findViewById(R.id.share_btn);
        Bundle bundle = getIntent().getExtras();

        GrainPie[] type = (GrainPie[]) bundle.get("DataSaya");
        GrainPie[] size = (GrainPie[]) bundle.get("Size");

        drawChart(type);
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < type.length; j++) {
            float a = (float) type[j].getValue();
            int b = (int) Math.round(a);
            String c = (String.valueOf(type[j].getValue()));
            String d = (String.valueOf(type[j].getName()));
            builder.append(d).append(" - ").append(b).append(" ").append("\t\r\n");
//            tipe.setText(String.valueOf(a));
        }
        tipe.setText(builder.toString());

        StringBuilder builders = new StringBuilder();
        for (int k = 0; k < type.length; k++) {
            float a = (float) type[k].getValue();
            int b = (int) Math.round(a);
            String e = (String.valueOf(type[k].getValue()));
            String f = (String.valueOf(type[k].getName()));
            builders.append(f).append(" - ").append(b).append(" ").append("\t\r\n");
//            tipe.setText(String.valueOf(a));
        }
        total.setText(builders.toString());
        drwawChartSize(size);

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Jika Terdapat Google PlayStore pada Perangkat Android
                //Maka akan langsung terhubung dengan PlayStore Tersebut
                StringBuilder builderz = new StringBuilder();
                for (int k = 0; k < type.length; k++) {
                    float a = (float) type[k].getValue();
                    int b = (int) Math.round(a);
                    int c = (int) type[k].getValue();
                    String f = (String.valueOf(type[k].getValue()));
                    String e = (String.valueOf(type[k].getName()));
                    builderz.append("Tipe : ").append(e).append("\t\r, Jumlah : ").append(b).append(" ").append("\t\r\n");
                }

                StringBuilder builderx = new StringBuilder();
                for (int i = 0; i < size.length; i++) {
                    float a = (float) size[i].getValue();
                    int b = (int) Math.round(a);
                    String c = (String.valueOf(size[i].getValue()));
                    String d = (String.valueOf(size[i].getName()));
                    builderx.append("Size : ").append(d).append(" \t\r, Jumlah : ").append(b).append("\t\r\n");
                }
                String titlez = "Hasil Pengujian \n";
                String new_ln = "\n";
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, TextUtils.concat(builderz.toString(), TextUtils.concat(builderx.toString())));
                String someValue = (String) TextUtils.concat(titlez, TextUtils.concat(builderz.toString(), new_ln ,TextUtils.concat(builderx.toString() )));

                Intent shareIntent = Intent.createChooser(sendIntent, someValue);
                startActivity(shareIntent);
            }
        });
    }

    private void drwawChartSize(GrainPie[] size) {
        PieChart pieChart;
        pieChart = findViewById(R.id.chart2);
        pieChart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        StringBuilder builders = new StringBuilder();
        for (int i = 0; i < size.length; i++) {
            float a = (float) size[i].getValue();
            int b = (int) Math.round(a);
            yvalues.add(new PieEntry((float) size[i].getValue(), size[i].getName(), i + 1));
            String c = (String.valueOf(size[i].getValue()));
            String d = (String.valueOf(size[i].getName()));
            builders.append(d).append(" - ").append(b).append(" ").append("\t\r\n");
        }
        total.setText(builders);
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


    private void drawChart(GrainPie[] type) {
        PieChart pieChart;
        pieChart = findViewById(R.id.chart1);
        pieChart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        StringBuilder builders = new StringBuilder();

        for (int i = 0; i < type.length; i++) {
            float a = (float) type[i].getValue();
            int b = (int) Math.round(a);
            yvalues.add(new PieEntry((float) type[i].getValue(), type[i].getName(), i+1));
            String c = (String.valueOf(type[i].getValue()));
            String d = (String.valueOf(type[i].getName()));
            builders.append(d).append(" - ").append(b).append(" ").append("\t\r\n");

             }
        tipe.setText(builders);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Menginisialisasi MenuBar yang akan ditampilkan pada ActionBar/Toolbar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rate:
                //Menangani kejadian saat Tombol Rate Diklik
                try {
                    //Jika Terdapat Google PlayStore pada Perangkat Android
                    //Maka akan langsung terhubung dengan PlayStore Tersebut
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setType("text/plain");
//                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, download this app!");
                    String titlez = "Hasil Pengujian Tipe' \n";
                    String titlezs = "Hasil Pengujian Size \n";
                    sendIntent.putExtra(Intent.EXTRA_TEXT, TextUtils.concat(titlez,tipe.getText().toString(),TextUtils.concat(titlezs,total.getText().toString()) ).toString());
                   String someValue = (String) TextUtils.concat(titlez,total.getText().toString(), tipe.getText().toString());

                    Intent shareIntent = Intent.createChooser(sendIntent, someValue);
                    startActivity(shareIntent);
                } catch (ActivityNotFoundException ex) {
                    //Jika tidak, maka akan terhubung dengan Browser
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com" +
                            "/store/apps/details?id=indonesia.cianjur.developer.net.mobile_cbt_smk&hl=in")));
                }
                break;
        }
        return true;
    }
}

