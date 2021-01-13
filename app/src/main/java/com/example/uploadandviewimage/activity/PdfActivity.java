package com.example.uploadandviewimage.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadandviewimage.GrainHistory;
import com.example.uploadandviewimage.GrainItem;
import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PdfActivity extends AppCompatActivity {

    PieChart pieChart, pieChart1;
    ImageView imageView;
    Date dateTime;
    DateFormat dateFormat;
    int pageWidth = 1200;
    Bitmap bitmap, scaleBitmap;
//    GrainHistory history;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        Bundle bundle = getIntent().getExtras();
        GrainPie[] type = (GrainPie[]) bundle.get("pdfType");
        GrainPie[] size = (GrainPie[]) bundle.get("pdfSize");
//        history = (GrainHistory) getIntent().getSerializableExtra("pdfType");
//        history = (GrainHistory) getIntent().getSerializableExtra("pdfSize");


//        pieChart = findViewById(R.id.piechart1_pdf);
        pieChart1 = findViewById(R.id.piechart2pdf);
        imageView = findViewById(R.id.photo_pdf);
        pdfCreate(type, size);


        Intent intent = new Intent(PdfActivity.this, FragmentActivity.class);
        startActivity(intent);

    }

    private void pdfCreate(GrainPie[] type,GrainPie[] size ) {


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
        canvas.drawText("No : " + "232425", pageWidth - 20, 250, paint);

        dateFormat = new SimpleDateFormat("dd/MM/yy");
        canvas.drawText("Tanggal: " + dateFormat.format(dateTime), pageWidth - 20, 300, paint);

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        canvas.drawText("Pukul: " + dateFormat.format(dateTime), pageWidth - 20, 350, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(20, 360, pageWidth - 20, 420, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("Size", 100, 400, paint);
        canvas.drawText("Jumlah Size", 250, 400, paint);
        canvas.drawText("Type", 500, 400, paint);
        canvas.drawText("Jumlah Type", 800, 400, paint);


        int y = 450;
        for (int j = 0; j < type.length; j++) {
            float a = (float) type[j].getValue();
            int b = (int)Math.round(a);
            canvas.drawText(String.valueOf(type[j].getName()), 500, y, paint);
            canvas.drawText(String.valueOf(b), 820, y, paint);
            y += 50;
        }

        int v = 450;
        for (int j = 0; j < size.length; j++) {
            float a = (float) size[j].getValue();
            int b = (int)Math.round(a);
            canvas.drawText(String.valueOf(size[j].getName()), 100, v, paint);
            canvas.drawText(String.valueOf(b), 270, v, paint);
            v += 50;
        }


        PieChart pieChart;
        pieChart = findViewById(R.id.piechart2pdf);
        pieChart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        for (int i = 0 ; i <type.length ; i++) {
            yvalues.add(new PieEntry((float) type[i].getValue(), type[i].getName(), i+1));
        }
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

//                                   canvas.drawText(String.valueOf(items[2].getShape().getWidth()), 800, 1000, paint);

       // 07 draw picture chart
        Bitmap b=BitmapFactory.decodeResource(getResources(), R.drawable.grainvision2020);
        paint.setColor(Color.RED);
        canvas.drawBitmap(b, 400, 700, paint);
        // chart to pdf
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, stream);



        // 07 draw picture chart

        pdfDocument.finishPage(page);
        //pdf file name
        String mFileName = new SimpleDateFormat("yyyy_MMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        //pdf file path
        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";
        File file = new File(Environment.getExternalStorageDirectory(), "/" + mFileName + ".pdf");


        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
        Toast.makeText(this, "PDF sudah dibuat", Toast.LENGTH_LONG).show();


        Intent intent = new Intent(PdfActivity.this, FragmentActivity.class);
        startActivity(intent);

    }


}

