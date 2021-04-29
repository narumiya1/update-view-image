package com.example.uploadandviewimage.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
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
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uploadandviewimage.Account.Accounts;
import com.example.uploadandviewimage.ExampleAdapter;
import com.example.uploadandviewimage.ExampleItem;
import com.example.uploadandviewimage.GrainData;
import com.example.uploadandviewimage.GrainItem;
import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.SecondActivity;
import com.example.uploadandviewimage.UploadApis;
import com.example.uploadandviewimage.activity.LocTrack;
import com.example.uploadandviewimage.activity.PdfActivity;
import com.example.uploadandviewimage.adapter.CustomRecyclerview;
import com.example.uploadandviewimage.auth.Sesion;
import com.example.uploadandviewimage.auth.TokenInterceptor;
import com.example.uploadandviewimage.cookies.AddCookiesInterceptor;
import com.example.uploadandviewimage.cookies.ReceivedCookiesInterceptor;
import com.example.uploadandviewimage.roomdbGhistory.AppDatabase;
import com.example.uploadandviewimage.roomdbGhistory.GHistory;
import com.example.uploadandviewimage.roomdbGhistory.Gindeks;
import com.example.uploadandviewimage.roomdbGhistory.Gitem;
import com.example.uploadandviewimage.roomdbGhistory.HistoryReadActivity;
import com.example.uploadandviewimage.utils.AppUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment{
    private String image;
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
    private ArrayList<Gitem> listGrainType;
    private RecyclerView mrRecyclerView;
    private CardView cardView;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList <String> permissions = new ArrayList();
    FirebaseAuth mAuth ;
    String phoneNumberz ;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocTrack locationTrack;
    TextView longi,lati, pdef, tv_grain2, tv_grainSum2, tv_grain3,tv_grainSum3 ,tv_grainSum ;
    String state_data[] = new String[5];
    String choosen,  grain_slected;
    Sesion session;
    Accounts accounts = new Accounts();
    private CountDownTimer countDownTimer;
    private TextView textViewTime;
    private ProgressBar progressBarCircle;
    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    Context _context;
    int PRIVATE_MODE = 0;
    private HomeFragment.TimerStatus timerStatus = HomeFragment.TimerStatus.STOPPED;
    private long timeCountInMilliSeconds = 1 * 60000;
    private String[] Item = {"Rice","Coffee","Cocho"};
    private Dialog dialogue ;
    ArrayList<String> arrayListz;
    int originalPositions,newPositions ;
    private Button dialogIntent;
    private Dialog dialog;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_home, container, false);
        //get view
        session = new Sesion(getContext());
        dialog = new Dialog(getActivity());
        dialogue = new Dialog(getActivity());
        mrRecyclerView = view.findViewById(R.id.recyclerView_fragmenth);
        cardView = view.findViewById(R.id.cv_maine);
        warningtext = view.findViewById(R.id.warning_frame);
        pdef=view.findViewById(R.id.pdef);
        tv_grain2 = view.findViewById(R.id.tv_grain2);
        tv_grainSum=view.findViewById(R.id.tv_grainSum);
        tv_grainSum2 =  view.findViewById(R.id.tv_grainSum2);
        tv_grain3 =  view.findViewById(R.id.tv_grain3);
        tv_grainSum3=view.findViewById(R.id.tv_grainSum3);
        pdef.setVisibility(View.GONE);
        findViews(view);
        //check connection
        menu = view.findViewById(R.id.fab_popUp);

        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_4_5)
                .build();

        //malam
        GHistory type2 = new GHistory();
        decodeBase64(mImageFileLocation);
        type2.getImage();
        type2.getJenis();
        List<String> statusImg = db.gHistoryDao().getImageStorage();
        statusImg.size();
        if(statusImg.size()==0&&statusImg.size()<0){
            Log.d("AAABL", "No file");

            warningtext.setVisibility(View.VISIBLE);

        }
        else if(statusImg.size()>0) {
            String json = db.gHistoryDao().selectJsonHistory();
            GrainData grainData2 = new Gson().fromJson(json, GrainData.class);
            listGrainType = new ArrayList<>();
            String jenis = db.gHistoryDao().getJenis();
            tv_grainSum.setText(jenis);
            Log.d("Body jsonn", "jsonn "+json);
            if (json.equals(null) && grainData2.equals(null)){
                Log.d("AAABL", "No file");
                Log.d("AAABL", "No file");
            }
            GrainItem[] items = grainData2.getItems();
            for (int j = 0; j < items.length; j++) {
                Log.d("Body items", "here is an value jsonArray"+items);
                Log.d("Body items", "here is an value items----------"+items.length);
                GrainItem item = items[j];
                ExampleItem grain = new ExampleItem(item);

                String name = grain.getName();
                String score = grain.getScore();
                Gitem gitem = new Gitem();
                gitem.setName(name);
                gitem.setScore(score);
                gitem.setId(j);

                listGrainType.add(gitem);
                //call db model
//            Gitems type2 = new Gitems();
//            type2.setName(name1);
//            type2.setScore(score);
//
//            insertItems(type2);
            }

            mrRecyclerView.setHasFixedSize(true);
            mrRecyclerView.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
            mLayoutManager = new LinearLayoutManager(getContext());
            mrRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new CustomRecyclerview(getActivity(), listGrainType );
            mrRecyclerView.setAdapter(mAdapter);
            mImageFileLocation =  statusImg.get(statusImg.size()-1);
            File imgFile = new File(mImageFileLocation);
            int length = (int) imgFile.length();
            Log.d("Upload length respons", "String respons length : " +length);
            if (imgFile.exists())
            {
                Bitmap bitmap ;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(mImageFileLocation,
                        bitmapOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, 200,200, true);

                encodeTobase64(bitmap);
//                decodeBase64(mImageFileLocation);
                viewImage = (PhotoView) view.findViewById(R.id.viewImage);
                viewImage.setVisibility(View.VISIBLE);
                viewImage.setImageBitmap(BitmapFactory.decodeFile(mImageFileLocation));

                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(viewImage);
                photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else
            {
                Log.d("AAABL", "No file");
            }
        }




        //22 02 21
        // ambil text json dri database
        // rubah class ke object grain data --> GrainData grainData2 = new Gson().fromJson(gson, GrainData.class);
        // dirubah ke format recyclerView
        // insert ke variabel listGrainType





        if (isConnected()) {
            Log.d("Body Internet Connected", "Internet Connected");
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
        db = Room.databaseBuilder(getActivity(),
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
            Log.d("Body latitude", "latitude val"+latitude);
            Log.d("Body longitudesz", "latitude val"+longitude);
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
//                selectGrain();
//                selectRadio();
                selectDialogue();
            }
        });
        mRecyclerView = view.findViewById(R.id.recyclerView_fragment);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        progressDialog = new ProgressDialog(getActivity());


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
    private void selectRadio(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View formsView = inflater.inflate(R.layout.radio_buttton, null, false);
        final RadioGroup lokasi = (RadioGroup) formsView.findViewById(R.id.lokasi);
//        int pilih = lokasi.getCheckedRadioButtonId();
//        RadioButton radio_grain = (RadioButton) formsView.findViewById(pilih);
        new AlertDialog.Builder(getActivity())
                .setView(formsView)
                .setTitle("SELECT GRAIN")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                int pilih = lokasi.getCheckedRadioButtonId();
                                RadioButton radio_grain = (RadioButton) formsView.findViewById(pilih);

                                if (radio_grain.isChecked()){
                                    grain_slected = radio_grain.getText().toString();
                                    Toast.makeText(getActivity(), grain_slected, Toast.LENGTH_LONG).show();
                                    Log.d("Body grain_slected", "grain_slected  : "+grain_slected+" grain_slected ");
                                    selectImage(grain_slected);
                                }else {
                                    Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_LONG).show();

                                }


                                dialog.cancel();
                            }
                        }).show();
    }
    private void selectGrain(){
        LayoutInflater inf = (LayoutInflater)  getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inf.inflate(R.layout.change_bike_state, null);

        AlertDialog.Builder changeState = new AlertDialog.Builder(getActivity());

        changeState.setTitle(  " Choose Grain " );
        changeState.setView(layout);

        state_data[0]= "Rice";
        state_data[1]="Corn";
        state_data[2]="Chocholate";
        state_data[3]="Peanuts";
        state_data[4]="Coffee Bean";

        Spinner mSpinner = (Spinner) layout.findViewById(R.id.bike_states);

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,state_data);
//        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.grainList));
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        //positive and negative button
        changeState.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose Graain")){
                    Toast.makeText(getActivity(), mSpinner.getSelectedItem().toString(),
                            Toast.LENGTH_LONG)
                            .show();
                }
                choosen = state_data[0];
                selectImage(choosen);
                Log.d("Body state_data", "state_data : "+choosen+"  "+state_data);

            }
        });
//        changeState.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });

        changeState.setView(layout);
        AlertDialog alert = changeState.create();

        alert.show();

    }
    private void selectDialogue() {
        dialog.setTitle("Contoh Custom Dialog");
        dialog.setContentView(R.layout.custom_design);
//        dialog = dialogue.findViewById(R.id.action_cancels);
        //spinners
        final Spinner list = dialog.findViewById(R.id.listItemz);
        //1 Arraylist
        arrayListz = new ArrayList<>();
        arrayListz.add("Roaster");
        arrayListz.add("Beans");
        arrayListz.add("Cupz");
        //String Arr 2
        String[] question1 = {"Q1","Q2","Q3","Q4"};
        String[] question2 = {"Q5","Q6","Q7","Q8"};
        ArrayList<String> aList = new ArrayList<String>(Arrays.asList(question1));
        aList.addAll(Arrays.asList(question2));
        //3 HashMap
        Map<String, Integer> countryISOCodeMapping = new HashMap<>();
        countryISOCodeMapping.put("Rice", 0);
        countryISOCodeMapping.put("Corns", 1);
        countryISOCodeMapping.put("Fruitz", 2);
        countryISOCodeMapping.put("Peanuts", 3);
        countryISOCodeMapping.put("Wheat", 4);
        Collection<Object> vals = Collections.singleton(countryISOCodeMapping.values());
        Object[] array = vals.toArray(new Object[vals.size()]);
        //4 Algortihmszc
        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_4_5)
                .build();

//        List<Integer> indx = db.gHistoryDao().getCountIdx();
//        int count = db.gHistoryDao().getCount();
        String [] myArrays = {"Rice", "Coffee Beans", "Corns","Fruitz","Buckwheats","Wheat","Peanuts","Sesame","Millet"};
        String temps = myArrays[originalPositions];
        myArrays[originalPositions] = myArrays[newPositions];
        myArrays[newPositions] = temps;
        System.out.println(Arrays.toString(myArrays));

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, myArrays);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        list.setAdapter(adapter);
        //buat function untuk membaca selected indeks dari grain type dari room database(angka/indeks)
        //membuat table room dataabase (1 kolom)
        int selection = db.gHistoryDao().selectIndeks() ;
        Log.d("Body selections ", " selection : "+selection+" ");
        list.setSelection(selection);
        Sesion sesions = new Sesion(getActivity());


        list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity()," "+adapter.getItem(i)+" choosen", Toast.LENGTH_SHORT).show();
                grain_slected = adapter.getItem(i).toString();
                sesions.setString(grain_slected);
                adapterView.setTag(grain_slected);
//                Log.d("Body grain_slected selection", "grain_slected : "+grain_slected+" + "+i+" z");
                Log.d("Body i", " i : "+i+" ");

                for (int x = 0; x < myArrays.length; x++) {
                    System.out.print(myArrays[x] +" "+ i +" ");
//                    Log.d("Body myArrays", "myArrays  : >> "+myArrays[x]+" << ");
                }
//                originalPositions = 0;
                // buat function untuk menyimpan selected indeks ke room database (angka/indeks)
                //newPositions = 5;
                Gindeks idx = new Gindeks();
                idx.setType(1);
                idx.setValue(i);
                Log.d("Body idx", " fl getidxs : "+  idx.getId()+" "+db.gHistoryDao().getCountIdx()+" n ");
//                Log.d("Body ListgetIdx", " getIdx : "+db.gHistoryDao().getCountIdx()+" "+indx+"");
                /*
                1. baca jumlah row pada table Gindeks
                2. jika jumlah row == 0 , insert dengan type = 1, value selected row
                3. jika jumlah > 0 lakukan update Where type == 1, value selected row
                */
//                updateSelectedGrain(idx);

                int rowGrainType = db.gHistoryDao().getCount();
//                int rowGrainType = count123.size();
                if(rowGrainType==0){
                    Log.d("AAABL", "idx "+rowGrainType);
                    insertIdx(idx);
                }
                else{
                     /* cara-1, dengan fungsi */
                    idx.setId(1);
                    idx.setType(1);
                    idx.setValue(i);
                    Log.d("AAVAIL", " i "+i);
                    updateSelectedGrain(idx);
                   /* cara-2 , dengan queriy
                    int val = idx.getValue();
                    int id = 1;
                    int type = idx.getType();
                    db.gHistoryDao().updateIndeks(id,type, val);
                    Log.d("AAVAIL", " idx "+idx.getValue());
                    */
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        dialogIntent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent((Context) getActivity(), SlidePanelActivity.class);
//                startActivity(intent);
//            }
//        });
        //button
        Button DialogButton = dialog.findViewById(R.id.action_oke);
        DialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //textView
        TextView gallery = dialog.findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        TextView cammera = dialog.findViewById(R.id.cammera);
        cammera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

            }
        });

        dialog.show();
    }

    private void selectImage(String state_data) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
        Log.d("Body state_data", "state_data preasstate_data : "+state_data.toString()+" ");
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
            /*
                else if (options[item].equals("Grain")) {
                    LayoutInflater inf = (LayoutInflater)  getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inf.inflate(R.layout.change_bike_state, null);

                    AlertDialog.Builder changeState = new AlertDialog.Builder(getActivity());

                    changeState.setTitle(  " Choose Grain " );
                    changeState.setView(layout);

                    String state_data[] = new String[5];

                    state_data[0]= "Rice";
                    state_data[1]="Corn";
                    state_data[2]="Chocholate";
                    state_data[3]="Peanuts";
                    state_data[4]="Coffee Bean";


                    Spinner mSpinner = (Spinner) layout.findViewById(R.id.bike_states);

//                    ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,     state_data);
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.grainList));
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    mSpinner.setAdapter(adapter);
                    //positive and negative button
                    changeState.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose Graain")){
                                Toast.makeText(getActivity(), mSpinner.getSelectedItem().toString(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                    changeState.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                          dialogInterface.dismiss();
                        }
                    });

                    changeState.setView(layout);
                    AlertDialog alert = changeState.create();

                    alert.show();
                }
                else if(options[item].equals("Radio")){
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    final View formsView = inflater.inflate(R.layout.radio_buttton, null, false);
                    final RadioGroup lokasi = (RadioGroup) formsView.findViewById(R.id.lokasi);

                    new AlertDialog.Builder(getActivity())
                            .setView(formsView)
                            .setTitle("PILIH LOKASI ANDA")
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @TargetApi(11)
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            int pilih = lokasi.getCheckedRadioButtonId();
                                            RadioButton radio_lokasi = (RadioButton) formsView.findViewById(pilih);

                                            String lokasi_anda = "Lokasi Anda : " + radio_lokasi.getText();
                                            Toast.makeText(getActivity(), lokasi_anda, Toast.LENGTH_LONG).show();
                                            dialog.cancel();
                                        }
                                    }).show();
                }
          */
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
            Log.d("Body Longitude", "longitude : "+longitude+""+latitude);
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

                Bitmap compressedOri = BitmapFactory.decodeFile(mImageFileLocation);

                Bitmap compressed = null;
                //save bitmap to byte array

                try {
                    File file = new File(mImageFileLocation);
                    int length = (int) file.length();
                    Log.d("Body length", "String length : "+length);
                    FileOutputStream fOut = new FileOutputStream(file);
                    compressedOri.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
//                    rotateImage(setReducedImageSize());
                    int length = (int) mImageFileLocation.length();
                    if (length<40000){
                        Log.d("Body Max Size", "5mb " +length);
                    }
                    Log.d("Body length", "length : "+length);
                    Bitmap converetdImage = getResizedBitmap(bitmap, 650);



                    viewImage.setImageBitmap(converetdImage);
                    encodeTobase64(converetdImage);
//                    decodeBase64(mImageFileLocation);
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
                      /*
                        cek jenis grain
                        if (beras = panggil function upload image beras)
                        else if (kopi = uploadImagekopi)
                        else if(gandum = uploadImageGandum)
                        dibuatkan upload image
                      */
                    dialog.dismiss();
                    if (grain_slected.equals("Rice")){
                        uploadImage(converetdImage, grain_slected);
                        Log.d("Body grain_slected", "grain_slected  : " +grain_slected);
                    }else if (grain_slected.equals("Coffee Beans")){
                        Toast toast=Toast.makeText(getActivity(), "Coffee Bean is cooming soon / Not Executed /  in Development" , Toast.LENGTH_LONG);
                        View view = toast.getView();
                        //Gets the actual oval background of the Toast then sets the colour filter
                        view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                        view.setBackgroundColor(Color.RED);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        Toast.makeText(getActivity(), "This is my Coffee message!",
                                Toast.LENGTH_LONG).show();
                        Log.d("Body grain_slected2", "grain_slected2  : " +grain_slected);
                    }else if (grain_slected.equals("Corns")){
                        Toast toasts=Toast.makeText(getActivity(), "Chocholate is cooming soon / Not Executed /  in Development" , Toast.LENGTH_LONG);
                        View view = toasts.getView();
                        view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                        view.setBackgroundColor(Color.GREEN);
                        toasts.setGravity(Gravity.CENTER, 0, 0);
                        toasts.show();
                    } else if (grain_slected.equals("Buckwheats")){
                        Toast.makeText(getActivity(), "Buckwheats On Development" , Toast.LENGTH_LONG).show();
                    }  else if (grain_slected.equals("Fruitz")){
                        Toast toast = Toast.makeText(getActivity(), "Fruit is cooming soon / Not Executed / in Development" , Toast.LENGTH_LONG);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                        view.setBackgroundColor(Color.YELLOW);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                         Toast.makeText(getActivity(), "On Development" , Toast.LENGTH_LONG);
                    }
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

    private Bitmap getResizedBitmap(Bitmap bitmap, int i) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.d("Upload Bitmap bitmap respons", "String Bitmap bitmap  : " +bitmap);
        float bitmapRatio = (float)width / (float) height;
        Log.d("Upload Bitmap bitmap respons", "String Bitmap bitmap  : " +bitmapRatio);
        if (bitmapRatio > 1) {
            width = i;
            height = (int) (width / bitmapRatio);
        } else {
            height = i;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);

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
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(viewImage);
        photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
        /*
        cek jenis grain
        if (beras = panggil function upload image beras)
        else if (kopi = uploadImagekopi)
        else if(gandum = uploadImageGandum)
        dibuatkan upload image
        */
        if (grain_slected.equals("Rice")){
            uploadImage(rotateBitmap, grain_slected);
            Log.d("Body choosen Chōsa Heidan", "String choosen : "+grain_slected);

        }
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
            int length = (int) file.length();
            Log.d("Body length", "String length : "+length);
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
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void uploadImage(Bitmap bitmap, String choosen) {
        //20210130
        //volleyPost();
        Log.d("Body choosen", "String choosen : " +choosen);
        mrRecyclerView.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
//        cardView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
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
        int length = (int) file.length();
        Log.d("Upload length respons", "String respons length : " +length);
         if (length>50000){

        }
//        Toast.makeText(getContext(), " File Size"+length, Toast.LENGTH_LONG).show();
        Log.d("Body Max Size", "File Size------------5mb " +length);
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
        Log.d("Body Max Size", "------------5mb " +file_size);

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

                        //insert room datbase gson, created_at
                        //  22 2 21
                        Date dates = new Date();
                        String gson = new Gson().toJson(grainData);

                        Gitem typeC = new Gitem();
                        typeC.setCreatedAt(dates);
                        typeC.setJson(gson);
                        insertItems(typeC);


                        /*
                        for (int i = 0; i < items.length; i++) {
                            Gitems type2 = new Gitems();

                            GrainItem item = items[i];
                            ExampleItem grain = new ExampleItem(item);

                            String name1 = grain.getName();
                            String score = grain.getScore();
                            String json = type2.getJson();

                            type2.setName(name1);
                            type2.setScore(score);
                            type2.setJson(gson);
                            type2.setCreatedAt(dates);
                            insertItems(type2);
                        }

                        */
                        //20 2 21
                        /*
                        GrainItem[] items = grainData.getItems();
                        for (int j = 0; j < items.length; j++) {
                            Log.d("Body items", "here is an value jsonArray"+items);
                            GrainItem item = items[j];
                            ExampleItem grain = new ExampleItem(item);
                            double val = items[j].getShape().getWidth();
                            double pct = items[j].getShape().getLeft();
                            double shpe = items[j].getShape().getTop();

                            String name1 = grain.getName();
                            String score = grain.getScore();

                            //call db model
                            Gitems type2 = new Gitems();
                            type2.setName(name1);
                            type2.setScore(score);

                            insertItems(type2);
                        }
                        */


                        grainData.getItems();

                        GrainPie[] type = grainData.getTypePie();
                        GrainPie[] size = grainData.getSizePie();

                        showProgress();

                        mRecyclerView.setVisibility(View.VISIBLE);
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
                        fab_view_history.setVisibility(View.GONE);
                        fab_pdf_intent.setVisibility(View.VISIBLE);
                        menu.setVisibility(View.VISIBLE);
                        pdef.setVisibility(View.VISIBLE);
                        fab_pdf_intent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent((Context) getActivity(), PdfActivity.class);
//                                Bundle setData = new Bundle();
                                intent.putExtra("pdfType", type);
                                intent.putExtra("pdfSize", size);
                                startActivityForResult(intent, 19);
                                Toasty.Config.getInstance()
//                        .setToastTypeface(Typeface.createFromAsset(getAssets(), "revans.otf"))
                                        .allowQueue(false)
                                        .apply();
                                Toasty.custom(getActivity(), R.string.pdf_download, getResources().getDrawable(R.drawable.ic_arrow_left),
                                        android.R.color.black, android.R.color.holo_green_dark, Toasty.LENGTH_LONG, true, true).show();
                                Toasty.Config.reset(); // Use this if you want to use the configuration above only once
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
                                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),  "/" + mFileName + ".pdf");


                                try {
                                    pdfDocument.writeTo(new FileOutputStream(file));




                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                pdfDocument.close();
//                                Toast.makeText(getContext(), "PDF sudah dibuat di Folder Download", Toast.LENGTH_LONG).show();
                                Toasty.Config.getInstance()
                                        .allowQueue(false)
                                        .apply();
                                Toasty.custom(getActivity(), R.string.pdf_download, getResources().getDrawable(R.drawable.ic_arrow_left),
                                        android.R.color.black, android.R.color.holo_green_dark, Toasty.LENGTH_LONG, true, true).show();
                            }

                        });

                        LocalDate date2 = LocalDate.now();
                        Date date = new Date();
                        //type
                        double x = 0;
                        for(int i=0; i<type.length; i++) {
                            String name = type[i].getName();
                            double val = type[i].getValue();
                            double pct = type[i].getPercent();
                            int b = (int) Math.round(val);
                            x = x+b;

                            //call db model
                            GHistory type2 = new GHistory();
                            type2.setName(name);
                            type2.setVal(val);
                            type2.setPct(pct);
                            type2.setDataType(1);
                            type2.setCreatedAt(date);
                            type2.setImage(mImageFileLocation);
                            insertData(type2);
                        }
                     Toast.makeText(getActivity(), "Jumlah Variabel = "+x+" ", Toast.LENGTH_LONG).show();
//                        tv_grain3.setVisibility(View.VISIBLE);
//                        tv_grainSum3.setVisibility(View.VISIBLE);
//                        tv_grainSum3.setText(String.valueOf(x));
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
                            type2.setImage(mImageFileLocation);
                            insertData(type2);
                        }
//                        String jenisz= "Ricee";
//                        GHistory typec = new GHistory();
//                        typec.setJenis(jenisz);
                        int y = (int) x;
//                        typec.setJumlah(y);
//                        Gjumlah type3 = new Gjumlah();
//                        type3.setJumlah(y);
//                        Log.d("Math Body setJumlah", "" + y);
//                        insertData(typec);
//                        Log.d("Math Body setJenis", "" + jenisz);

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
//                        Toast.makeText(getContext(), "Login Time Out, silahkan login kembali", Toast.LENGTH_LONG).show();
                             Toasty.Config.getInstance()
//                        .setToastTypeface(Typeface.createFromAsset(getAssets(), "revans.otf"))
                                        .allowQueue(false)
                                        .apply();
                                Toasty.custom(getActivity(), R.string.pdf_download, getResources().getDrawable(R.drawable.ic_arrow_left),
                                        android.R.color.black, android.R.color.holo_green_dark, Toasty.LENGTH_LONG, true, true).show();
                                Toasty.Config.reset(); // Use this if you want to use the configuration above only once

                        closeProgress();
                        String jwtNull = "";
                        session.setKeyApiJwt(jwtNull);
                        session.setIsLogin(false);
                        session.logoutUser();
                        Toasty.Config.getInstance()
//                        .setToastTypeface(Typeface.createFromAsset(getAssets(), "revans.otf"))
                                .allowQueue(false)
                                .apply();
                        Toasty.custom(getActivity(), R.string.loginkembali, getResources().getDrawable(R.drawable.ic_arrow_left),
                                android.R.color.black, android.R.color.holo_green_dark, Toasty.LENGTH_LONG, true, true).show();
                        Toasty.Config.reset(); // Use this if you want to use the configuration above only once
//                        showDialogs();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    String message = "";
                    closeProgress();
                    String jwtNull = "";
//                    Toast.makeText(getActivity(), "Status Login Time Out, silahkan login kembali", Toast.LENGTH_LONG).show();
                    Toasty.Config.getInstance()
//                        .setToastTypeface(Typeface.createFromAsset(getAssets(), "revans.otf"))
                            .allowQueue(false)
                            .apply();
                    Toasty.custom(getActivity(), R.string.loginkembali, getResources().getDrawable(R.drawable.ic_baseline_close_24),
                            android.R.color.black, android.R.color.holo_green_dark, Toasty.LENGTH_LONG, true, true).show();
                    Toasty.Config.reset(); // Use this if you want to use the configuration above only once

//                    ???????
                    session.setKeyApiJwt(jwtNull);
                    session.setIsLogin(false);
                    session.setFirstTimeLaunch(false);
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
    private void updateSelectedGrain(Gindeks idx) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistoryDao().updateGrainSelected(idx);
                return status;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Long status) {
//                Toast.makeText(getActivity().getApplicationContext(), "status row " + status, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), "history row added sucessfully" + status, Toast.LENGTH_SHORT).show();
                Log.d("Upload history row added sucessfullys", "String status  : " +status);
            }
        }.execute();
    }

    private void deleteRowGrain(Gindeks idx) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistoryDao().updateGrainSelected(idx);
                return status;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Long status) {
//                Toast.makeText(getActivity().getApplicationContext(), "status row " + status, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), "history row added sucessfully" + status, Toast.LENGTH_SHORT).show();
                Log.d("Upload history row added sucessfullys", "String status  : " +status);
            }
        }.execute();
    }
    private void insertIdx(Gindeks idx) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistoryDao().insertIdx(idx);
                return status;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Long status) {
//                Toast.makeText(getActivity().getApplicationContext(), "status row " + status, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), "history row added sucessfully" + status, Toast.LENGTH_SHORT).show();
                Log.d("Upload history row added sucessfullys", "String status  : " +status);
            }
        }.execute();
    }
    private void insertItems(Gitem type2) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistoryDao().insertAllItem(type2);
                return status;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Long status) {
//                Toast.makeText(getActivity().getApplicationContext(), "status row " + status, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), "history row added sucessfully" + status, Toast.LENGTH_SHORT).show();
                Log.d("Upload history row added sucessfullys", "String status  : " +status);
            }
        }.execute();
    }

    private void showDialogs() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Session Login telah habis")
                .setContentText("silahkan lakukan login kembali untuk menggunakan aplikasi")
                .setConfirmText(" ")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog
                                .setTitleText("!")
                                .setContentText("")
                                .setConfirmText(" ")
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
//                Toast.makeText(getActivity().getApplicationContext(), "status row " + status, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity().getApplicationContext(), "history row added sucessfully" + status, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

//    20 2 21

//    private void insertItems(final GrainItem item) {
//        new AsyncTask<Void, Void, ArrayList<GrainItem>>() {
//            @Override
//            protected ArrayList<GrainItem> doInBackground(Void... voids) {
//                ArrayList<GrainItem> status = db.gHistoryDao().insertAllItem(item);
//                return status;
//            }
//
//            @Override
//            protected void onPostExecute(ArrayList<GrainItem> grainItems) {
//                super.onPostExecute(grainItems);
//
//            }
//
//        }.execute();
//    }

     /*

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Body onDestroyView", "String onDestroyView : ");
        String urlDomain = "http://110.50.85.28:8200";
        String jwtKey =  new Sesion(getContext()).getKeyApiJwt();
        File imgFile = new File(mImageFileLocation);
        if (imgFile.exists() || mImageFileLocation!="") {
            decodeBase64(mImageFileLocation);
            File file = new File(mImageFileLocation);
            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
            try {
                Log.d("Upload jwtKeys respons", "String respons jwtKey : " +jwtKey);
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

                            //for GrainType and GrainSize
                            GrainPie[] type = grainData.getTypePie();
                            GrainPie[] size = grainData.getSizePie();

                            showProgress();
                            mAdapter = new ExampleAdapter(grainData);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                            warningtext.setVisibility(View.GONE);
                            viewImage.setVisibility(View.VISIBLE);

                            LocalDate date2 = LocalDate.now();
                            Date date = new Date();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    closeProgress();


                                }
                            },1000);



                        } else {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Login Time Out, silahkan login kembali", Toast.LENGTH_LONG).show();
                            showDialogs();
                            closeProgress();
                            String jwtNull = "";
                            session.setKeyApiJwt(jwtNull);
                            session.setIsLogin(false);
                            session.logoutUser();
                            showDialogs();
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
                        showDialogs();
                    }
                });

            } catch (Exception e) {
                String errMessage = e.getMessage();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Body onDetach", "String onDetach : ");
        String urlDomain = "http://110.50.85.28:8200";
        String jwtKey =  new Sesion(getContext()).getKeyApiJwt();
        File imgFile = new File(mImageFileLocation);
        if (imgFile.exists() || mImageFileLocation!="") {
            decodeBase64(mImageFileLocation);
            File file = new File(mImageFileLocation);
            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
            try {
                Log.d("Upload jwtKeys respons", "String respons jwtKey : " +jwtKey);
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

                            //for GrainType and GrainSize
                            GrainPie[] type = grainData.getTypePie();
                            GrainPie[] size = grainData.getSizePie();

                            showProgress();
                            mAdapter = new ExampleAdapter(grainData);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                            warningtext.setVisibility(View.GONE);
                            viewImage.setVisibility(View.VISIBLE);

                            LocalDate date2 = LocalDate.now();
                            Date date = new Date();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    closeProgress();


                                }
                            },1000);



                        } else {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Login Time Out, silahkan login kembali", Toast.LENGTH_LONG).show();
                            showDialogs();
                            closeProgress();
                            String jwtNull = "";
                            session.setKeyApiJwt(jwtNull);
                            session.setIsLogin(false);
                            session.logoutUser();
                            showDialogs();
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
                        showDialogs();
                    }
                });

            } catch (Exception e) {
                String errMessage = e.getMessage();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Body onDetach", "String onDetach : ");
        String urlDomain = "http://110.50.85.28:8200";
        String jwtKey =  new Sesion(getContext()).getKeyApiJwt();
        File imgFile = new File(mImageFileLocation);
        if (imgFile.exists() || mImageFileLocation!="") {
            decodeBase64(mImageFileLocation);
            File file = new File(mImageFileLocation);
            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
            try {
                Log.d("Upload jwtKeys respons", "String respons jwtKey : " +jwtKey);
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

                            //for GrainType and GrainSize
                            GrainPie[] type = grainData.getTypePie();
                            GrainPie[] size = grainData.getSizePie();

                            showProgress();
                            mAdapter = new ExampleAdapter(grainData);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                            warningtext.setVisibility(View.GONE);
                            viewImage.setVisibility(View.VISIBLE);

                            LocalDate date2 = LocalDate.now();
                            Date date = new Date();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    closeProgress();


                                }
                            },1000);



                        } else {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Login Time Out, silahkan login kembali", Toast.LENGTH_LONG).show();
                            showDialogs();
                            closeProgress();
                            String jwtNull = "";
                            session.setKeyApiJwt(jwtNull);
                            session.setIsLogin(false);
                            session.logoutUser();
                            showDialogs();
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
                        showDialogs();
                    }
                });

            } catch (Exception e) {
                String errMessage = e.getMessage();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("Body onPause", "String onDetach : ");
        String urlDomain = "http://110.50.85.28:8200";
        String jwtKey =  new Sesion(getContext()).getKeyApiJwt();
        File imgFile = new File(mImageFileLocation);
        if (imgFile.exists() || mImageFileLocation!="") {
            decodeBase64(mImageFileLocation);
            File file = new File(mImageFileLocation);
            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
            try {
                Log.d("Upload jwtKeys respons", "String respons jwtKey : " +jwtKey);
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

                            //for GrainType and GrainSize
                            GrainPie[] type = grainData.getTypePie();
                            GrainPie[] size = grainData.getSizePie();

                            showProgress();
                            mAdapter = new ExampleAdapter(grainData);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                            warningtext.setVisibility(View.GONE);
                            viewImage.setVisibility(View.VISIBLE);

                            LocalDate date2 = LocalDate.now();
                            Date date = new Date();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    closeProgress();


                                }
                            },1000);



                        } else {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Login Time Out, silahkan login kembali", Toast.LENGTH_LONG).show();
                            showDialogs();
                            closeProgress();
                            String jwtNull = "";
                            session.setKeyApiJwt(jwtNull);
                            session.setIsLogin(false);
                            session.logoutUser();
                            showDialogs();
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
                        showDialogs();
                    }
                });

            } catch (Exception e) {
                String errMessage = e.getMessage();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        decodeBase64(mImageFileLocation);
        Log.d("Body onDestroy", "String onDestroy : ");
        String urlDomain = "http://110.50.85.28:8200";
        String jwtKey =  new Sesion(getContext()).getKeyApiJwt();
        File imgFile = new File(mImageFileLocation);
        if (imgFile.exists() || mImageFileLocation!="") {
            decodeBase64(mImageFileLocation);
            File file = new File(mImageFileLocation);
            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
            try {
                Log.d("Upload jwtKeys respons", "String respons jwtKey : " +jwtKey);
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

                            //for GrainType and GrainSize
                            GrainPie[] type = grainData.getTypePie();
                            GrainPie[] size = grainData.getSizePie();

                            showProgress();
                            mAdapter = new ExampleAdapter(grainData);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                            warningtext.setVisibility(View.GONE);
                            viewImage.setVisibility(View.VISIBLE);

                            LocalDate date2 = LocalDate.now();
                            Date date = new Date();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    closeProgress();


                                }
                            },1000);



                        } else {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Login Time Out, silahkan login kembali", Toast.LENGTH_LONG).show();
                            showDialogs();
                            closeProgress();
                            String jwtNull = "";
                            session.setKeyApiJwt(jwtNull);
                            session.setIsLogin(false);
                            session.logoutUser();
                            showDialogs();
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
                        showDialogs();
                    }
                });

            } catch (Exception e) {
                String errMessage = e.getMessage();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Body onStop", "String onStop : ");
        String urlDomain = "http://110.50.85.28:8200";
        String jwtKey =  new Sesion(getContext()).getKeyApiJwt();
        File imgFile = new File(mImageFileLocation);
        if (imgFile.exists() || mImageFileLocation!="") {
            decodeBase64(mImageFileLocation);
            File file = new File(mImageFileLocation);
            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
            try {
                Log.d("Upload jwtKeys respons", "String respons jwtKey : " +jwtKey);
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

                            //for GrainType and GrainSize
                            GrainPie[] type = grainData.getTypePie();
                            GrainPie[] size = grainData.getSizePie();

                            showProgress();
                            mAdapter = new ExampleAdapter(grainData);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                            warningtext.setVisibility(View.GONE);
                            viewImage.setVisibility(View.VISIBLE);

                            LocalDate date2 = LocalDate.now();
                            Date date = new Date();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    closeProgress();


                                }
                            },1000);



                        } else {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Login Time Out, silahkan login kembali", Toast.LENGTH_LONG).show();
                            showDialogs();
                            closeProgress();
                            String jwtNull = "";
                            session.setKeyApiJwt(jwtNull);
                            session.setIsLogin(false);
                            session.logoutUser();
                            showDialogs();
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
                        showDialogs();
                    }
                });

            } catch (Exception e) {
                String errMessage = e.getMessage();
            }
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Body onDetach", "String onDetach : ");
        String urlDomain = "http://110.50.85.28:8200";
        String jwtKey =  new Sesion(getContext()).getKeyApiJwt();
        File imgFile = new File(mImageFileLocation);
        if (imgFile.exists() || mImageFileLocation!="") {
            decodeBase64(mImageFileLocation);
            File file = new File(mImageFileLocation);
            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
            try {
                Log.d("Upload jwtKeys respons", "String respons jwtKey : " +jwtKey);
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

                            //for GrainType and GrainSize
                            GrainPie[] type = grainData.getTypePie();
                            GrainPie[] size = grainData.getSizePie();

                            showProgress();
                            mAdapter = new ExampleAdapter(grainData);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                            warningtext.setVisibility(View.GONE);
                            viewImage.setVisibility(View.VISIBLE);

                            LocalDate date2 = LocalDate.now();
                            Date date = new Date();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    closeProgress();


                                }
                            },1000);



                        } else {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Login Time Out, silahkan login kembali", Toast.LENGTH_LONG).show();
                            showDialogs();
                            closeProgress();
                            String jwtNull = "";
                            session.setKeyApiJwt(jwtNull);
                            session.setIsLogin(false);
                            session.logoutUser();
                            showDialogs();
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
                        showDialogs();
                    }
                });

            } catch (Exception e) {
                String errMessage = e.getMessage();
            }
        }
    }

    */
}

