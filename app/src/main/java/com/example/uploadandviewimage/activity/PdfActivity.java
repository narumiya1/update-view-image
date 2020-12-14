package com.example.uploadandviewimage.activity;

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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PdfActivity extends AppCompatActivity {

    PieChart pieChart, pieChart1;
    ImageView imageView ;
    Date dateTime;
    DateFormat dateFormat;
    int pageWidth = 1200;   Bitmap bitmap, scaleBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        Bundle bundle = getIntent().getExtras();
        GrainPie[] type = (GrainPie[]) bundle.get("DataSaya");
        GrainPie[] size =  (GrainPie[]) bundle.get("Size");

//        pieChart = findViewById(R.id.piechart1_pdf);
        pieChart1 = findViewById(R.id.piechart2pdf);
        imageView = findViewById(R.id.photo_pdf);

    }
}
