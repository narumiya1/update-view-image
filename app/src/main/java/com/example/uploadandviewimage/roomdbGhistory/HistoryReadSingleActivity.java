package com.example.uploadandviewimage.roomdbGhistory;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.emredavarci.noty.Noty;
import com.example.uploadandviewimage.GrainHistory;
import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.activity.PdfActivity;
import com.example.uploadandviewimage.utils.AppUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class HistoryReadSingleActivity extends AppCompatActivity {

    GrainHistory history;
    private static DecimalFormat df = new DecimalFormat("###.#");
    TextView tv_item2, tv_item3, itemTimes, tv_sum_varietas, tv_sum_butir;
    FloatingActionMenu menu;
    FloatingActionButton fab_pdf_intents, fab_toast;
    Date dateTime;
    DateFormat dateFormat;
    int pageWidth = 1200;
    double e;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_single);


        tv_item3 = findViewById(R.id.tv_items3);
        itemTimes = findViewById(R.id.item_time);
        tv_item2 = findViewById(R.id.tv_items2);
        tv_sum_varietas = findViewById(R.id.tv_sum_varietas);
        tv_sum_butir = findViewById(R.id.tv_sum_butir);
        history = (GrainHistory) getIntent().getSerializableExtra("data");
        menu = findViewById(R.id.fab_popUps);
        fab_pdf_intents = findViewById(R.id.fab_pdf_intents);
        fab_toast = findViewById(R.id.toasts);
        fab_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Noty.init(HistoryReadSingleActivity.this, "PDF Sudah di simpan di Folder Download, Silahkan klik ok", rl,
                        Noty.WarningStyle.ACTION)
                        .setActionText("OK")
                        .setHeight(new RelativeLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT))
                        .setWarningBoxBgColor("#00559a")
                        .setWarningTappedColor("#ff3300")
                        .setWarningBoxMargins(40,0,40,0)
                        .setWarningBoxPosition(Noty.WarningPos.CENTER)
                        .setAnimation(Noty.RevealAnim.FADE_IN, Noty.DismissAnim.FADE_OUT, 400,400)
                        .show();
                */

//                Toasty.success(getApplicationContext(), "PDF saved succesfully at Download Folder!", Toast.LENGTH_SHORT, true).show();

            }
        });
        //pie chart
        PieChart pieChart, pieCharts;
        pieChart = findViewById(R.id.chart_type);
        pieCharts = findViewById(R.id.chart_sizec);
        pieChart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

        ArrayList<GrainPie> myTypes = history.getType();
        int count = myTypes.size();
        StringBuilder builder = new StringBuilder();
        StringBuilder builders = new StringBuilder();
        int x = 0;
        for (int i = 0; i < count; i++) {

            //chart typez
            GrainPie type = myTypes.get(i);
            float a = (float) type.getValue();
            int b = (int) Math.round(a);
            x = x+b;
            yvalues.add(new PieEntry((float) type.getValue(), type.getName(), i + 1));
            //text
            String s = type.getName();
            double c = (double) type.getPercent() * 100;
            df.setRoundingMode(RoundingMode.HALF_EVEN);
            e = Double.parseDouble(df.format(c));

//            double d = Double.parseDouble(df.format(c));
//            if(c % 1 >= 0.5) {
//                e = Double.parseDouble(df.format(c));
//                System.out.println(e);
//            }else if(c %1 <= 0.5) {
//                e = c;
//            }
            Log.d("Math Body c", "" + e);
            builder.append(s).append(" - ").append(b).append(" Butir / ").append(e).append(" %").append("\t\r\n");

        }
        Log.d("Math Body x", "" + x);
        tv_sum_varietas.setText(String.valueOf(x));
        tv_item2.setText(builder.toString());

        itemTimes.setText(AppUtils.getFormattedDateString(history.getDateTime()));


        PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.election_results));
        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        Description description = new Description();
        description.setText(getString(R.string.pie_chart));
        description.setTextColor(R.color.normalColor);
        pieChart.setDescription(description);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setHoleRadius(58f);
        pieChart.setEntryLabelColor(Color.BLACK);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.BLACK);

        //size chart
        ArrayList<GrainPie> mySize = history.getSize();
        pieCharts.setUsePercentValues(true);
        ArrayList<PieEntry> valuesz = new ArrayList<PieEntry>();
        int counts = mySize.size();
        int z = 0;
        for (int j = 0; j < counts; j++) {
            GrainPie sizc = mySize.get(j);
            float a = (float) sizc.getValue();
            int b = (int) Math.round(a);
            z=z+b;
            double c = (double) sizc.getPercent() * 100;
            df.setRoundingMode(RoundingMode.HALF_EVEN);
            e = Double.parseDouble(df.format(c));

//            double d = Double.parseDouble(df.format(c));
//            if(c % 1 >= 0.5) {
//                e = Double.parseDouble(df.format(Math.round(c)));
//                e = Double.parseDouble(df.format((c)));
//                double roundOff = (double) Math.round(c * 100.0) / 100.0;
//                Log.d("Math Body roundOff", "" + roundOff);
//
//                System.out.println(e);
//            }else if(c %1 <= 0.5) {
//                e = c;
//                e = Double.parseDouble(df.format((c)));
//
//            }
            valuesz.add(new PieEntry((float) sizc.getValue(), sizc.getName(), j + 1));
            //text
            String s = sizc.getName();
            builders.append(s).append(" - ").append(b).append(" Butir / ").append(String.valueOf(e)).append(" %").append("\t\r\n");
        }
        tv_sum_butir.setText(String.valueOf(z));

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
        pieCharts.setEntryLabelColor(Color.BLACK);

        dataSetz.setColors(ColorTemplate.MATERIAL_COLORS);
        dataz.setValueTextSize(13f);
        dataz.setValueTextColor(Color.BLACK);
        tv_item3.setText(builders.toString());
        fab_pdf_intents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfCreate(history);
            }
        });
    }

    private void pdfCreate(GrainHistory history) {
        dateTime = new Date();
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo
                = new PdfDocument.PageInfo.Builder(1200, 2500, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(70);
        canvas.drawText("Hasil Pengujian", pageWidth / 2, 200, titlePaint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(35f);
        paint.setTextAlign(Paint.Align.RIGHT);
        //change 590-340 to
        canvas.drawText(" ", pageWidth - 20, 250, paint);

        dateFormat = new SimpleDateFormat("dd/MM/yy");
        canvas.drawText("Tanggal: " + dateFormat.format(dateTime), pageWidth - 20, 300, paint);

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        canvas.drawText("Pukul: " + dateFormat.format(dateTime), pageWidth - 20, 350, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(20, 360, pageWidth - 20, 420, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("Butir", 100, 400, paint);
        canvas.drawText("Jumlah Butir", 250, 400, paint);
        canvas.drawText("Varietas", 500, 400, paint);
        canvas.drawText("Jumlah Varietas", 800, 400, paint);

        int y = 450;
        ArrayList<GrainPie> myTypes = history.getType();
        int count = myTypes.size();
        for (int i = 0; i < count; i++) {
            GrainPie type = myTypes.get(i);
            float a = (float) type.getValue();
            int b = (int) Math.round(a);

            String s = type.getName();
            double c = (double) type.getPercent() * 100;
            df.setRoundingMode(RoundingMode.HALF_EVEN);
            double d = Double.parseDouble(df.format(c));

            canvas.drawText(String.valueOf(s), 500, y, paint);
            canvas.drawText(String.valueOf(b) + " (" + d + ") %", 820, y, paint);
            y += 50;

        }

        int v = 450;
        ArrayList<GrainPie> mySize = history.getSize();
        int counts = mySize.size();
        for (int j = 0; j < counts; j++) {
            GrainPie sizc = mySize.get(j);
            float a = (float) sizc.getValue();
            int b = (int) Math.round(a);
            double c = (double) sizc.getPercent() * 100;
            df.setRoundingMode(RoundingMode.HALF_EVEN);
            double d = Double.parseDouble(df.format(c));

            String s = sizc.getName();
            canvas.drawText(String.valueOf(s), 100, v, paint);
            canvas.drawText(String.valueOf(b) + " (" + d + ")% ", 270, v, paint);
            v += 50;
        }

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.logdaun);
        paint.setColor(Color.RED);
        canvas.drawBitmap(b, 400, 700, paint);
//        canvas.drawText("Grain Vision Version 2.0.0", 600, 600, paint);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, stream);


        pdfDocument.finishPage(page);
        //pdf file name
        String mFileName = new SimpleDateFormat("yyyy_MMdd_HH:mm:ss",
                Locale.getDefault()).format(System.currentTimeMillis());
        //pdf file path
        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/" + mFileName + ".pdf");


        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
//        Toast.makeText(this, "PDF sudah dibuat di Folder download", Toast.LENGTH_LONG).show();
        Toasty.Config.getInstance()
                .allowQueue(false)
                .apply();
        Toasty.custom(getApplicationContext(), R.string.pdf_download, getResources().getDrawable(R.drawable.ic_baseline_check_box_24),
                android.R.color.black, android.R.color.holo_green_dark, Toasty.LENGTH_LONG, true, true).show();
        Toasty.Config.reset(); // Use this if you want to use the configuration above only once
    }

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, HistoryReadSingleActivity.class);
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
                try {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    String titlez = "Hasil Pengujian ' \n";
                    String titlezs = "Hasil Pengujian Size \n";
                    sendIntent.putExtra(Intent.EXTRA_TEXT, TextUtils.concat(titlez, tv_item2.getText().toString(), TextUtils.concat(titlezs, tv_item3.getText().toString())).toString());
                    String someValue = (String) TextUtils.concat(titlez, tv_item3.getText().toString(), tv_item2.getText().toString());

                    Intent shareIntent = Intent.createChooser(sendIntent, someValue);
                    startActivity(shareIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com" +
                            "/store/apps/details?id=indonesia.cianjur.developer.net.mobile_cbt_smk&hl=in")));
                }
                break;
        }
        return true;
    }
}



