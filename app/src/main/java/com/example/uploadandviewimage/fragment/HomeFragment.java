package com.example.uploadandviewimage.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.location.Location;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uploadandviewimage.Account.Accounts;
import com.example.uploadandviewimage.ExampleAdapter;
import com.example.uploadandviewimage.GrainData;
import com.example.uploadandviewimage.GrainHistory;
import com.example.uploadandviewimage.GrainItem;
import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.NetworkClient;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.SecondActivity;
import com.example.uploadandviewimage.UploadApis;
import com.example.uploadandviewimage.activity.FragmentActivity;
import com.example.uploadandviewimage.activity.LocTrack;
import com.example.uploadandviewimage.activity.PdfActivity;
import com.example.uploadandviewimage.auth.LoginActivity;
import com.example.uploadandviewimage.auth.LoginNumber;
import com.example.uploadandviewimage.auth.Sesion;
import com.example.uploadandviewimage.auth.TokenInterceptor;
import com.example.uploadandviewimage.auth.User;
import com.example.uploadandviewimage.cookies.AddCookiesInterceptor;
import com.example.uploadandviewimage.cookies.JavaNetCookieJar;
import com.example.uploadandviewimage.cookies.ReceivedCookiesInterceptor;
import com.example.uploadandviewimage.location.GpsUtils;
import com.example.uploadandviewimage.roomdbGhistory.AppDatabase;
import com.example.uploadandviewimage.roomdbGhistory.GHistory;
import com.example.uploadandviewimage.roomdbGhistory.HistoryReadActivity;
import com.example.uploadandviewimage.utils.AppUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.net.CookieHandler;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment{

    public static final String API_KEY = "PMAK-6010c29f1b0e6b0034f81b57-6387c56febd52e92623f8fe195e60bc8d7";
    final Handler handler = new Handler(Looper.getMainLooper());
    ProgressDialog progressDialog;
    private AppDatabase db;
    PhotoView viewImage;
    Button btnRetry, chartf, pdf, hisdtory, view_history;
    TextView warningtext, no_data;
    int pageWidth = 1200;
    Date dateTime;
    ImageButton add_photo;
    FloatingActionMenu menu;
    FloatingActionButton fab_chart, fab_pdf, fab_pdf_intent, fab_view_history;
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_CODE_READ_GALLERY = 1;
    private static final int PERMISSION_CODE_OPEN_CAMERA = 2;

    private Uri image_uri;
    private String mImageFileLocation = "";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList <String> permissions = new ArrayList();
    FirebaseAuth mAuth ;
    String phoneNumberz ;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocTrack locationTrack;
    TextView longi,lati;
    Sesion session;
    Accounts accounts = new Accounts();
    private CountDownTimer countDownTimer;
    private TextView textViewTime;
    private ProgressBar progressBarCircle;
    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private HomeFragment.TimerStatus timerStatus = HomeFragment.TimerStatus.STOPPED;
    private long timeCountInMilliSeconds = 1 * 60000;
    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_home, container, false);
        //get view
        session = new Sesion(getContext());
        findViews(view);
        //check connection
        if (isConnected()) {
            Toast.makeText(view.getContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
        } else {
            btnRetry.setVisibility(View.VISIBLE);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnected()){
                        btnRetry.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(view.getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }
            });
            Toast.makeText(view.getContext(), "Check Connection and Try Again", Toast.LENGTH_LONG).show();
        }
        // migrate
        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "tbGrainHistory")
                .fallbackToDestructiveMigration()
                .build();
        //get location
        ceklocation();
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        locationTrack = new LocTrack(getContext());


        if (locationTrack.canGetLocation()) {


            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

            lati.setText("latitude" +latitude);
            longi.setText("longitudesz" +longitude);

            Toast.makeText(getActivity(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {

            locationTrack.showSettingsAlert();
        }

        fab_view_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HistoryReadActivity.class);
                startActivity(intent);
            }
        });
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
        progressDialog = new ProgressDialog(getActivity());
        menu.setVisibility(View.VISIBLE);

        return view;

    }

    private void findViews(View view) {
        warningtext = view.findViewById(R.id.tv_warn);
        no_data = view.findViewById(R.id.no_data);
        fab_pdf_intent =  view.findViewById(R.id.fab_pdf_intent);
        add_photo = view.findViewById(R.id.iv_add);
        btnRetry = view.findViewById(R.id.btnRetry);
        //viewImage=(ImageView)findViewById(R.id.viewImage);
        viewImage = (PhotoView) view.findViewById(R.id.viewImage);
        menu = view.findViewById(R.id.fab_popUp);
        fab_chart = view.findViewById(R.id.fab_chart);
        fab_pdf = view.findViewById(R.id.fab_pdf);
        fab_view_history = view.findViewById(R.id.fab_view_history);
        final TextView textTime = view.findViewById(R.id.text_time);
        textTime.setText(AppUtils.getFormattedDateString(AppUtils.getCurrentDateTime()));
        longi = view.findViewById(R.id.tv_long);
        lati = view.findViewById(R.id.tv_lang);
        mAuth = FirebaseAuth.getInstance();
        progressBarCircle = (ProgressBar)  view.findViewById(R.id.progressBarCircle);
        textViewTime = (TextView)  view.findViewById(R.id.textViewTime);
        startStops();
    }

    private void startStops() {
        if (timerStatus == HomeFragment.TimerStatus.STOPPED) {
            // call to initialize the timer values
            setTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // changing the timer status to started
            timerStatus = HomeFragment.TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();
        } else {
            // changing the timer status to stopped
            timerStatus = HomeFragment.TimerStatus.STOPPED;
            stopCountDownTimer();

        }
    }

    private void stopCountDownTimer() {
        countDownTimer.cancel();

    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                setProgressBarValues();
                // changing the timer status to stopped
                timerStatus = HomeFragment.TimerStatus.STOPPED;
//                Toast.makeText(getActivity(), "TIME OUT SILAHKAN LOGIN KEMBAILI", Toast.LENGTH_LONG).show();
                closeProgress();
                String jwtNull = "";
                session.setKeyApiJwt(jwtNull);
                session.setIsLogin(false);
                session.logoutUser();

                Log.d("Body jwtNull", "String jwtNull : "+jwtNull);
            }

        }.start();
        countDownTimer.start();
    }

    private void setProgressBarValues() {
        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }

    private void setTimerValues() {
        int minute = 30 ;
        int minuteDiemn = Integer.parseInt(getString(R.string.minutes));

        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = minuteDiemn * 60 * 1000;
        Log.d("Body Time Count", "String jwtNull : "+timeCountInMilliSeconds);

    }
    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }
    private boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;

    }

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
    private void ceklocation() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        locationTrack = new LocTrack(getActivity());

        if (locationTrack.canGetLocation()) {
            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();
            lati.setText("latitude" +latitude);
            longi.setText("longitudesz" +longitude);
            Toast.makeText(getContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {
            locationTrack.showSettingsAlert();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ContextCompat.checkSelfPermission(getContext(),permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions, grantResults );
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            break;
                        }
                    }

                }

                break;
        }

    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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

            }else if (requestCode ==14){

            }else if (requestCode == 19){

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

// tambahkan login web api disini
        // dicek apabila login berhasil jalankan uploadimage
        // 110.50.85.28:8200/account/login
        // http://110.50.85.28:8200/account/login
        // x-www-form-urlencoded
        // Phone : +6281907123427
        // Password : 1m4dm1n
        // post

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

    public void volleyPost(){
        String postUrl = "http://110.50.85.28:8200/Account/Login";
        JSONObject postData = new JSONObject();
        try {
            postData.put("Phone", "+628156055410");
            postData.put("Password", "123456");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        StringRequest jsonObjRequest = new StringRequest(

                Request.Method.POST,
                postUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Phone", "+628156055410");
                params.put("Password", "123456");
                return params;
            }

        };

        requestQueue.add(jsonObjRequest);

        //AppController.getInstance().addToRequestQueue(jsonObjRequest);
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//
//        JSONObject postData = new JSONObject();
//        try {
//            postData.put("Phone", "+628156055410");
//            postData.put("Password", "123456");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        //com.android.volley.Request
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                com.android.volley.Request.Method.POST,
//                postUrl,
//                postData,
//                new com.android.volley.Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        System.out.println(response);
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                });

        //requestQueue.add(jsonObjectRequest);

    }

    protected String jwt;

    private void uploadImage(Bitmap bitmap) {
        //20210130
        //volleyPost();

        String urlDomain = "http://110.50.85.28:8200";

//        jwt = "";
        //String phone = "+6281907123427";
        //String password = "1m4dm1n";
        String phone = new Sesion(getContext()).getPhone();
        Log.d("Body Phone", "String Phone : "+phone);
        String password =  new Sesion(getContext()).getPassword();
        Log.d("Body Password", "String Password : " +password);
//        session.setKeyApiJwt(jwt);
        String jwtKey =  new Sesion(getContext()).getKeyApiJwt();
        Log.d("Body jwtKeys", "String jwtKey : " +jwtKey);
        if (jwtKey.equals(jwt)){
            session.setIsLogin(false);
            session.logoutUser();
        }
//
//        RequestBody reqq1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(phone));
//        RequestBody reqq2 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(password));



//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                //Do something after 100ms


                File file = new File(mImageFileLocation);
                int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                try {
                    Log.d("Upload jwtKeys respons", "String respons jwtKey : " +jwtKey);
//                    new Sesion(getContext()).getKeyApiJwt();
//                    session.setKeyApiJwt(jwt);
                    //20210130
                    HttpLoggingInterceptor loggingInterceptor2 = new HttpLoggingInterceptor();
                    loggingInterceptor2.setLevel(HttpLoggingInterceptor.Level.BODY);

                    TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtKey);

                    OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                            .addInterceptor(new AddCookiesInterceptor(getActivity()))
                            .addInterceptor(new ReceivedCookiesInterceptor(getActivity()))
                            .addInterceptor(loggingInterceptor2)
                            .addInterceptor(tokenInterceptor)
                            .build();

                    Gson gson2 = new GsonBuilder().serializeNulls().create();

                    //Retrofit retrofit = NetworkClient.getRetrofit();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(urlDomain)
                            .addConverterFactory(GsonConverterFactory.create(gson2))
                            .client(okHttpClient2)
                            .build();

                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);
                    String name = "Rifqi";
                    double latitude = locationTrack.getLatitude();
                    int val1=(int) latitude;
                    double longitude = locationTrack.getLongitude();
                    int val2=(int) longitude;
                    RequestBody req1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(name)); //change to phone number
                    RequestBody req2 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
                    RequestBody req3 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
                    UploadApis uploadApis = retrofit.create(UploadApis.class);
                    Call call = uploadApis.uploadImage(parts, req1, req2, req3);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.code() == 200) {
                                Object obj = response.body();
                                GrainData grainData = (GrainData) response.body();
                                //String gson = new Gson().toJson(response.body());
                                //20201126
                                //get pie chart
                                //for GrainType and GrainSize
                                GrainPie[] type = grainData.getTypePie();
                                GrainPie[] size = grainData.getSizePie();
//                        GrainType grainType = new GrainType();
                                showProgress();
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
                                fab_pdf_intent.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent((Context) getActivity(), PdfActivity.class);
//                                Bundle setData = new Bundle();
                                        intent.putExtra("pdfType", type);
                                        intent.putExtra("pdfSize", size);
                                        startActivityForResult(intent, 19);
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
                                        //pdf file name
                                        String mFileName = new SimpleDateFormat("yyyy_MMdd_HHmmss",
                                                Locale.getDefault()).format(System.currentTimeMillis());
                                        //pdf file path
                                        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";
                                        File file = new File(Environment.getExternalStorageDirectory(),  "/" + mFileName + ".pdf");


                                        try {
                                            pdfDocument.writeTo(new FileOutputStream(file));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        pdfDocument.close();
                                        Toast.makeText(getContext(), "PDF sudah dibuat", Toast.LENGTH_LONG).show();

                                    }

                                });

                                LocalDate date2 = LocalDate.now();
                                Date date = new Date();
                                //type
                                for(int i=0; i<type.length; i++) {
                                    String name = type[i].getName();
                                    double val = type[i].getValue();
                                    double pct = type[i].getPercent();

                                    //call db model
                                    GHistory type2 = new GHistory();
                                    type2.setName(name);
                                    type2.setVal(val);
                                    type2.setPct(pct);
                                    type2.setDataType(1);
                                    type2.setCreatedAt(date);
                                    insertData(type2);
                                }
                                //size
                                for(int i=0; i<size.length; i++) {
                                    String name_size = size[i].getName();
                                    double val_size = size[i].getValue();
                                    double pct_size = size[i].getPercent();

                                    //call db model
                                    GHistory type2 = new GHistory();
                                    type2.setName(name_size);
                                    type2.setVal(val_size);
                                    type2.setPct(pct_size);
                                    type2.setDataType(2);
                                    type2.setCreatedAt(date);
                                    insertData(type2);
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent((Context) getActivity(), SecondActivity.class);
//                                Bundle setData = new Bundle();
                                        intent.putExtra("DataSaya", type);
                                        intent.putExtra("Size", size);
                                        startActivityForResult(intent, 10);
                                        String message = "";
                                        closeProgress();


                                    }
                                },1000);



                                //library pdf writter


                            } else {
                                Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                                Toast.makeText(getContext(), "Login Time Out, silahkan login kembali", Toast.LENGTH_LONG).show();
                                showDialogs();
                                closeProgress();
                                String jwtNull = "";
                                session.setKeyApiJwt(jwtNull);
                                session.setIsLogin(false);
                                session.logoutUser();
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            String message = "";
                            closeProgress();
                            String jwtNull = "";
                            session.setKeyApiJwt(jwtNull);
                            session.setIsLogin(false);
                            session.logoutUser();
                        }
                    });

                } catch (Exception e) {
                    String errMessage = e.getMessage();
                }

//            }

//        }, 500);
//        Call<ResponseBody> calls = uploadApiss.insertLogin(phone, password);

        /*
        jwt = "";
        calls.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    closeProgress();
                    Log.d("Body <>", "Response: "+response.body().toString());
                    //add delay
                    //String jwt = "";
                    ResponseBody responseBody = response.body();
                    try {
                        byte[] myByte = responseBody.bytes();
                        jwt = new String(myByte, StandardCharsets.UTF_8);
                        jwt = jwt.substring(1, jwt.length()-1);
                        //String strResponse = responseBody.string();
                        //String coba = "disini";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            closeProgress();

                            File file = new File(mImageFileLocation);
                            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                            try {
                                ResponseBody responseBody = response.body();
                                byte[] myByte = responseBody.bytes();
                                jwt = new String(myByte, StandardCharsets.UTF_8);
                                jwt = jwt.substring(1, jwt.length()-1);
                                Log.d("Upload jwtKeys respons", "String respons jwtKey : " +new Sesion(getContext()).getKeyApiJwt());
                                //20210130
                                HttpLoggingInterceptor loggingInterceptor2 = new HttpLoggingInterceptor();
                                loggingInterceptor2.setLevel(HttpLoggingInterceptor.Level.BODY);

                                TokenInterceptor tokenInterceptor = new TokenInterceptor(jwt);

                                OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                                        .addInterceptor(new AddCookiesInterceptor(getActivity()))
                                        .addInterceptor(new ReceivedCookiesInterceptor(getActivity()))
                                        .addInterceptor(loggingInterceptor2)
                                        .addInterceptor(tokenInterceptor)
                                        .build();

                                Gson gson2 = new GsonBuilder().serializeNulls().create();

                                //Retrofit retrofit = NetworkClient.getRetrofit();
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(urlDomain)
                                        .addConverterFactory(GsonConverterFactory.create(gson2))
                                        .client(okHttpClient2)
                                        .build();

                                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                                MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);
                                String name = "Rifqi";
                                double latitude = locationTrack.getLatitude();
                                int val1=(int) latitude;
                                double longitude = locationTrack.getLongitude();
                                int val2=(int) longitude;
                                RequestBody req1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(name)); //change to phone number
                                RequestBody req2 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
                                RequestBody req3 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
                                UploadApis uploadApis = retrofit.create(UploadApis.class);
                                Call call = uploadApis.uploadImage(parts, req1, req2, req3);
                                call.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        if (response.code() == 200) {
                                            Object obj = response.body();
                                            GrainData grainData = (GrainData) response.body();
                                            //String gson = new Gson().toJson(response.body());
                                            //20201126
                                            //get pie chart
                                            //for GrainType and GrainSize
                                            GrainPie[] type = grainData.getTypePie();
                                            GrainPie[] size = grainData.getSizePie();
//                        GrainType grainType = new GrainType();
                                            showProgress();
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
                                            fab_pdf_intent.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent((Context) getActivity(), PdfActivity.class);
//                                Bundle setData = new Bundle();
                                                    intent.putExtra("pdfType", type);
                                                    intent.putExtra("pdfSize", size);
                                                    startActivityForResult(intent, 19);
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
                                                    //pdf file name
                                                    String mFileName = new SimpleDateFormat("yyyy_MMdd_HHmmss",
                                                            Locale.getDefault()).format(System.currentTimeMillis());
                                                    //pdf file path
                                                    String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";
                                                    File file = new File(Environment.getExternalStorageDirectory(),  "/" + mFileName + ".pdf");


                                                    try {
                                                        pdfDocument.writeTo(new FileOutputStream(file));
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                    pdfDocument.close();
                                                    Toast.makeText(getContext(), "PDF sudah dibuat", Toast.LENGTH_LONG).show();

                                                }

                                            });

                                            LocalDate date2 = LocalDate.now();
                                            Date date = new Date();
                                            //type
                                            for(int i=0; i<type.length; i++) {
                                                String name = type[i].getName();
                                                double val = type[i].getValue();
                                                double pct = type[i].getPercent();

                                                //call db model
                                                GHistory type2 = new GHistory();
                                                type2.setName(name);
                                                type2.setVal(val);
                                                type2.setPct(pct);
                                                type2.setDataType(1);
                                                type2.setCreatedAt(date);
                                                insertData(type2);
                                            }
                                            //size
                                            for(int i=0; i<size.length; i++) {
                                                String name_size = size[i].getName();
                                                double val_size = size[i].getValue();
                                                double pct_size = size[i].getPercent();

                                                //call db model
                                                GHistory type2 = new GHistory();
                                                type2.setName(name_size);
                                                type2.setVal(val_size);
                                                type2.setPct(pct_size);
                                                type2.setDataType(2);
                                                type2.setCreatedAt(date);
                                                insertData(type2);
                                            }
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent((Context) getActivity(), SecondActivity.class);
//                                Bundle setData = new Bundle();
                                                    intent.putExtra("DataSaya", type);
                                                    intent.putExtra("Size", size);
                                                    startActivityForResult(intent, 10);
                                                    String message = "";
                                                    closeProgress();


                                                }
                                            },1000);



                                            //library pdf writter


                                        } else {
                                            Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                                            closeProgress();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call call, Throwable t) {
                                        String message = "";
                                        closeProgress();
                                    }
                                });

                            } catch (Exception e) {
                                String errMessage = e.getMessage();
                            }

                        }

                    }, 500);
                }else {

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                String message = "";
                Log.d("Body -->>", "Error: ");

            }
        });
        */

    }
    private void showDialogs() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Session Login telah habis")
                .setContentText("silahkan lakukan login kembali untuk menggunakan aplikasi")
                .setConfirmText("ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog
                                .setTitleText("!")
                                .setContentText("")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
//                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
                .show();
    }

    private void showProgress() {

        progressDialog.setMessage("Loading . . .");
        progressDialog.show();

    }

    private void closeProgress() {
        progressDialog.dismiss();
    }

    private void insertData(final GHistory type2) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistoryDao().insertGrain(type2);
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

