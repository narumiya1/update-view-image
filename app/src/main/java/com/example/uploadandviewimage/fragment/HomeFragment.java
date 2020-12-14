package com.example.uploadandviewimage.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.uploadandviewimage.ExampleAdapter;
import com.example.uploadandviewimage.GrainData;
import com.example.uploadandviewimage.GrainItem;
import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.GrainType;
import com.example.uploadandviewimage.MainActivity;
import com.example.uploadandviewimage.NetworkClient;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.SecondActivity;
import com.example.uploadandviewimage.UploadApis;
import com.example.uploadandviewimage.activity.HistoryActivity;
import com.example.uploadandviewimage.helper.AppzDatabase;
import com.example.uploadandviewimage.helper.RoomReadActivity;
import com.example.uploadandviewimage.helper.RoomReadSingleActivity;
import com.example.uploadandviewimage.helper.Type;
import com.example.uploadandviewimage.utils.AppUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {


    private AppzDatabase db;
    PhotoView viewImage;
    Button btn, chartf, pdf, hisdtory, view_history;
    TextView warningtext, no_data;
    int pageWidth = 1200;
    Date dateTime;
    ImageButton add_photo;
    FloatingActionMenu menu;
    FloatingActionButton fab_chart, fab_pdf, fab_history, fab_view_history;

    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_CODE_READ_GALLERY = 1;
    private static final int PERMISSION_CODE_OPEN_CAMERA = 2;

    private Uri image_uri;
    private String mImageFileLocation = "";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_home, container, false);
//        btn = view.findViewById(R.id.btnSelectPhotoFragment);
        warningtext = view.findViewById(R.id.tv_warn);
        no_data = view.findViewById(R.id.no_data);
        pdf = (Button) view.findViewById(R.id.btnpdf);
        add_photo = view.findViewById(R.id.iv_add);

        //viewImage=(ImageView)findViewById(R.id.viewImage);
        viewImage = (PhotoView) view.findViewById(R.id.viewImage);
        menu = view.findViewById(R.id.fab_popUp);
        fab_chart = view.findViewById(R.id.fab_chart);
        fab_pdf = view.findViewById(R.id.fab_pdf);
        fab_history = view.findViewById(R.id.historia);
        fab_view_history = view.findViewById(R.id.fab_view_history);
        final TextView textTime = view.findViewById(R.id.text_time);
        textTime.setText(AppUtils.getFormattedDateString(AppUtils.getCurrentDateTime()));

        fab_view_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RoomReadActivity.class);
                startActivity(intent);
            }
        });

//        db = Room.databaseBuilder(getActivity().getApplicationContext(), AppzDatabase.class, "tbType").build();
        // migrate
        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppzDatabase.class, "tbType")
                .fallbackToDestructiveMigration()
                .addMigrations(AppzDatabase.MIGRATION_3_4)
                .build();

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        mRecyclerView = view.findViewById(R.id.recyclerView_fragment);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        menu.setVisibility(View.VISIBLE);

        return view;

    }

/*
    @OnClick({R.id.btnSelectPhotoFragment, R.id.btnpdf, R.id.intent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSelectPhotoFragment:
                selectImage();

                break;
            case R.id.btnpdf:

                break;
            case R.id.intent:

                break;

        }
    }
    */

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ((ActivityCompat.checkSelfPermission(getContext(),
                                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) ||
                                (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE_OPEN_CAMERA);

                        } else {
                            //permission already granted
                            openCamera();
                        }
                    } else {
                        // system OS < Marshmallow
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        openCamera();
                    }
                    /*
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1); */
                } else if (options[item].equals("Choose from Gallery")) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                        } else {
                            // No explanation needed; request the permission
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_CODE_READ_GALLERY);
                        }
                    } else {
                        //permission already granted
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                        } else {
                            // No explanation needed; request the permission
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_CODE_READ_GALLERY);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        //image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        image_uri = FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".provider", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);

        startActivityForResult(cameraIntent, 1);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        boolean isEmulated = Environment.isExternalStorageEmulated();
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do something you want
                } else {
                    // permission denied
                    Toast.makeText(getContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(mImageFileLocation,
                            bitmapOptions);

                    rotateImage(setReducedImageSize());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    viewImage.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } */

                //viewImage.setImageURI(image_uri);

                /*
                Uri selectedImage = image_uri; //data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = mImageFileLocation; //c.getString(columnIndex);
                c.close();
                BitmapFactory.Options options = new BitmapFactory.Options ();
                options.inSampleSize=4; // InSampleSize = 4;

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath, options));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                viewImage.setImageBitmap(thumbnail);

                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    viewImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } */

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                mImageFileLocation = picturePath;

                c.close();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4; // InSampleSize = 4;
                /*
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath, options));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                viewImage.setImageBitmap(thumbnail); */

                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    viewImage.setImageBitmap(bitmap);

                    /*
                    //draw circle
                    Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Paint paint = new Paint();                          //define paint and paint color
                    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(Color.RED);
                    paint.setStyle(Paint.Style.STROKE);
                    //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
                    Canvas canvas =new Canvas(mutableBitmap);
                    canvas.drawCircle(50, 50, 50, paint);
                    //invalidate to update bitmap in imageview
                    viewImage.setImageBitmap(mutableBitmap);
                    viewImage.invalidate(); */


                    PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(viewImage);
                    photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    uploadImage(bitmap);
//                    Uri imageUri = data.getData();
//                    cropImage(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 10) {

            } else if (requestCode == 11) {

            }
        }
    }

    private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mImageFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        viewImage.setImageBitmap(rotateBitmap);

        uploadImage(rotateBitmap);
    }

    private Bitmap setReducedImageSize() {
        int targetImageViewWidth = viewImage.getWidth();
        int targetImageViewHeight = viewImage.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        //Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        //mPhotoCapturedImageView.setImageBitmap(photoReducedSizeBitmap);
        Bitmap compressedOri = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);

        Bitmap compressed = null;
        //save bitmap to byte array

        try {
            File file = new File(mImageFileLocation);
            FileOutputStream fOut = new FileOutputStream(file);
            compressedOri.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compressedOri;
    }

    private void uploadImage(Bitmap bitmap) {
        File file = new File(mImageFileLocation);
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
        try {
            Retrofit retrofit = NetworkClient.getRetrofit();

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);

            RequestBody someData = RequestBody.create(MediaType.parse("text/plain"), "This is a new Image");
            UploadApis uploadApis = retrofit.create(UploadApis.class);
            Call call = uploadApis.uploadImage(parts, someData);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.code() == 200) {
                        GrainData grainData = (GrainData) response.body();
                        //String gson = new Gson().toJson(response.body());
                        //20201126
                        //get pie chart
                        //for GrainType and GrainSize
                        GrainPie[] type = grainData.getTypePie();
                        GrainPie[] size = grainData.getSizePie();

                        mAdapter = new ExampleAdapter(grainData);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                        warningtext.setVisibility(View.GONE);
                        viewImage.setVisibility(View.VISIBLE);
                        fab_chart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent((Context) getActivity(), SecondActivity.class);
//                                Bundle setData = new Bundle();
                                intent.putExtra("DataSaya", type);
                                intent.putExtra("Size", size);
                                startActivityForResult(intent, 10);
                            }
                        });
                        fab_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dateTime = new Date();
                                GrainItem[] items = grainData.getItems();

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
                                canvas.drawText("Report", pageWidth / 2, 200, titlePaint);

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
                                canvas.drawText("Size", 200, 400, paint);
                                canvas.drawText("Type", 500, 400, paint);
                                canvas.drawText("Jumlah data", 800, 400, paint);


                                int y = 450;
                                for (int j = 0; j < items.length; j++) {
                                    canvas.drawText(String.valueOf(items[j].getGrainType().getName()), 500, y, paint);
                                    canvas.drawText(String.valueOf(items[j].getGrainSize().getName()), 200, y, paint);
                                    y += 50;
                                }


//                                   canvas.drawText(String.valueOf(items[2].getShape().getWidth()), 800, 1000, paint);


                                pdfDocument.finishPage(page);

                                File file = new File(Environment.getExternalStorageDirectory(), "/Pesanan.pdf");


                                try {
                                    pdfDocument.writeTo(new FileOutputStream(file));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                pdfDocument.close();
                                Toast.makeText(getContext(), "PDF sudah dibuat", Toast.LENGTH_LONG).show();

                            }

                        });

                        Date date = new Date();
                        for(int i=0; i<type.length; i++) {
                            String name = type[i].getName();

                            double jumlah = type[i].getPercent();

                            //call db model
                            Type type2 = new Type();

//                            for(int j=0; j<type.length; j++) {
//                                // Automatically saved to history
//
//                                if (type[i].getName() == type[0].getName() || type[j].getValue() == type[0].getValue()){
//                                    type2.setNamaType(type[0].getName());
//                                }else if(type[i].getName() == type[1].getName() || type[j].getValue() == type[1].getValue()){
//                                    type2.setNamaType(type[1].getName());
//                                }else if(type[i].getName() == type[2].getName()|| type[j].getValue() == type[2].getValue()){
//                                    type2.setNamaType(type[2].getName());
//                                }
//
//                            }
                            type2.setNamaType(name);
                            type2.setJumlahType(jumlah);
                            type2.setCreatedAt(date);
                            insertData(type2);

                        }
                        /*using button
                        chartf.setVisibility(View.VISIBLE);
                        chartf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent((Context) getActivity(), SecondActivity.class);
//                                Bundle setData = new Bundle();
                                intent.putExtra("DataSaya", type);
                                intent.putExtra("Size", size);
                                startActivityForResult(intent, 10);
                            }
                        });
                        */
                        String message = "";

                        /*using button
                        pdf.setVisibility(View.VISIBLE);
                        //pdf x & y
                        pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dateTime = new Date();
                                GrainItem[] items = grainData.getItems();

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
                                canvas.drawText("Report", pageWidth / 2, 200, titlePaint);

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
                                canvas.drawText("Size", 200, 400, paint);
                                canvas.drawText("Type", 500, 400, paint);
                                canvas.drawText("Jumlah data", 800, 400, paint);


                                int y = 450;
                                for (int j = 0; j < items.length; j++) {
                                    canvas.drawText(String.valueOf(items[j].getGrainType().getName()), 500, y, paint);
                                    canvas.drawText(String.valueOf(items[j].getGrainSize().getName()), 200, y, paint);
                                    y += 50;
                                }


//                                   canvas.drawText(String.valueOf(items[2].getShape().getWidth()), 800, 1000, paint);


                                pdfDocument.finishPage(page);

                                File file = new File(Environment.getExternalStorageDirectory(), "/Pesanan.pdf");


                                try {
                                    pdfDocument.writeTo(new FileOutputStream(file));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                pdfDocument.close();
                                Toast.makeText(getContext(), "PDF sudah dibuat", Toast.LENGTH_LONG).show();

                            }
                        });

                        */

                        /*use dao database & button

                        final Type type1 = (Type) getActivity().getIntent().getSerializableExtra("data");
                        hisdtory.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Type type2 = new Type();
                                String name = type[2].getName().toString();
//                                double jumlah = type[1].getValue();
                                //manual
                                double jumlah = 3;
                                type2.setNamaType(name);
                                type2.setJumlahType(jumlah);
                                insertData(type2);

                            }
                        });

                        */

                        //send data fragmrnt to activity
                        /*
                        hisdtory.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent((Context) getActivity(), HistoryActivity.class);
                                intent.putExtra("Type", type);
                                intent.putExtra("Size", size);
                                startActivityForResult(intent, 11);
                            }
                        });
                        /*

                         */
                        //fragment to fragment
                 /*    hisdtory.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle result = new Bundle();
                                result.putString("BundleKey_Home", "result");
                                getParentFragmentManager().setFragmentResult("BundleKey_Home", result);
                            }
                        });
                 */

                        //library pdf writter
                        /*
                        pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GrainItem[] items = grainData.getItems();
                                Document mDoc = new Document();
                                //pdf file name
                                String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                        Locale.getDefault()).format(System.currentTimeMillis());
                                //pdf file path
                                String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";
                                try {
                                    //create instance of PdfWriter class
                                    PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
                                    //open the document for writing
                                    mDoc.open();
                                    //get text from EditText i.e. mTextEt

                                    for (int i = 0; i < items.length; i++) {
//                                        String mText = items[0].getGrainSize().getName();

                                        String mTexta = items[i].getGrainType().getName();
                                        String mText = items[i].getGrainSize().getName();
                                        String mTextb = items[i].getGrainSize().getName();

                                        //add author of the document (optional)
                                        mDoc.addAuthor("ACKERMAN");

                                        //add paragraph to the document
                                        mDoc.add(new Paragraph(mTexta));
                                        Paragraph paragraph = new Paragraph();
                                        paragraph.setSpacingAfter(1);

                                        mDoc.add(new Paragraph(mText));
//
//                                        mDoc.add(new Paragraph(mTextb));

                                    }
                                    //close the document
                                    mDoc.close();
                                    //show message that file is saved, it will show file name and file path too
                                    Toast.makeText(getActivity(), mFileName + ".pdf\nis saved to\n" + mFilePath, Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    //if any thing goes wrong causing exception, get and show exception message
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        */
                    } else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    String message = "";
                }
            });

        } catch (Exception e) {
            String errMessage = e.getMessage();
        }

    }

    private void insertData(final Type type2) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.typeDAO().insertBarang(type2);
                return status;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Long status) {
                Toast.makeText(getActivity().getApplicationContext(), "status row " + status, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }


}

