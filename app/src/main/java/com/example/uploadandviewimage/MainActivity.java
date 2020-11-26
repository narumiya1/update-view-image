package com.example.uploadandviewimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    //ImageView viewImage;
    PhotoView viewImage;
    Button b, intent;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private  static  final int PERMISSION_CODE_READ_GALLERY = 1;
    private  static  final int PERMISSION_CODE_OPEN_CAMERA = 2;

    private Uri image_uri;
    private String mImageFileLocation = "";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b=(Button)findViewById(R.id.btnSelectPhoto);
        intent = findViewById(R.id.intent);
        //viewImage=(ImageView)findViewById(R.id.viewImage);
        viewImage=(PhotoView)findViewById(R.id.viewImage);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        //initializeGrid();
        /*
        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        exampleList.add(new ExampleItem("Sakahayang1", 222.2323, 534.823));
        exampleList.add(new ExampleItem("Sakahayang2", 122.434, 125.8));
        exampleList.add(new ExampleItem("Sakahayang3", 432.434, 325.8));
        exampleList.add(new ExampleItem("Sakahayang4", 543.434, 545.8));
        exampleList.add(new ExampleItem("Sakahayang5", 982.434, 532.8)); */

        //String contoh = exampleList.get(2).getClassification();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mAdapter = new ExampleAdapter(exampleList);
        //mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permision = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permision, PERMISSION_CODE_OPEN_CAMERA);
                        } else {
                            //permission already granted
                            openCamera();
                        }
                    }  else {
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
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                        } else {
                            // No explanation needed; request the permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_CODE_READ_GALLERY);
                        }
                    } else {
                        //permission already granted
                        Intent intent = new   Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                    /*
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permision = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //requestPermissions(permision, PERMISSION_CODE_READ_GALLERY);
                        ActivityCompat.requestPermissions(MainActivity.this, permision,                                 PERMISSION_CODE_READ_GALLERY);
                    } else {
                        //permission already granted
                        Intent intent = new   Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } */


                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
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
        image_uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);

        startActivityForResult(cameraIntent, 1);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        boolean isEmulated = Environment.isExternalStorageEmulated();

        /*
        int CAMERA_PERMISSION_REQUEST_CODE = 2;
        int result = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this.getApplicationContext(), "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_PERMISSION_REQUEST_CODE);
            }
        } */

        //File storageDirectory = getFilesDir();
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File storageDirectory = Environment.getDataDirectory();

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();


        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
            {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do something you want
                } else {
                    // permission denied
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(mImageFileLocation,
                            bitmapOptions);
                    //viewImage.setImageBitmap(bitmap);

                    rotateImage(setReducedImageSize());

                    /*
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
                    } */
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
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                mImageFileLocation = picturePath;

                c.close();
                BitmapFactory.Options options = new BitmapFactory.Options ();
                options.inSampleSize=4; // InSampleSize = 4;
                /*
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath, options));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                viewImage.setImageBitmap(thumbnail); */

                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
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

                    uploadImage(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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

        int scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
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
        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));

        //ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        /* byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } */


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
                        GrainData grainData = (GrainData)response.body();
                        //String gson = new Gson().toJson(response.body());
                        //20201126
                        //get pie chart
                        //for GrainType and GrainSize
                        GrainPie[] type = grainData.getTypePie();
                        GrainPie[] size = grainData.getSizePie();

                        mAdapter = new ExampleAdapter(grainData);
                        mRecyclerView.setAdapter(mAdapter);

                        intent.setVisibility(View.VISIBLE);
                        intent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//                                intent.putStringArrayListExtra("contactRecords_list", type);
                                startActivity(intent);
                            }
                        });

                        String message = "";
                    } else {
                        Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_LONG).show();
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

    private void initializeGrid(){
        TableLayout ll = (TableLayout) findViewById(R.id.displayLinear);


        for (int i = 0; i <40; i++) {

            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            //checkBox = new CheckBox(this);
            TextView tv = new TextView(this);
            tv.setText("dafasf");
            row.addView(tv);
            Button addBtn = new Button(this);
            addBtn.setText("ssafasfsa");
            row.addView(addBtn);
            //addBtn.setImageResource(R.drawable.add);
            //minusBtn = new ImageButton(this);
            //minusBtn.setImageResource(R.drawable.minus);
            //qty = new TextView(this);


            //qty.setText("10");
            //row.addView(checkBox);
            //row.addView(minusBtn);
            //row.addView(qty);
            //row.addView(addBtn);
            ll.addView(row, i);
        }
    }
}