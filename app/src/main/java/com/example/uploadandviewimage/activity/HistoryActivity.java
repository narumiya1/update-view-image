package com.example.uploadandviewimage.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.NetworkClient;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.UploadApis;
import com.example.uploadandviewimage.history.MyClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryActivity extends AppCompatActivity {
    TextView type1,jumlahtype_1, size1,jumlah_size1, tanggal ;
    Date dateTime;
    ImageButton add_photo;

    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        type1 =findViewById(R.id.history_type);

        jumlahtype_1 = findViewById(R.id.history_type_jumlah);
        size1 = findViewById(R.id.history_size1);
        jumlah_size1 = findViewById(R.id.history_size_jumlah1);

        tanggal = findViewById(R.id.tanggal);

        Bundle bundle = getIntent().getExtras();

        GrainPie[] type = (GrainPie[]) bundle.get("Type");

        dateTime = new Date();
        dateFormat = new SimpleDateFormat("dd-MM-yy-HH:mm:ss");
        tanggal.setText("Tanggal: " + dateFormat.format(dateTime));


        history(type);

        Retrofit retrofit = NetworkClient.getRetrofit();
        MyClient myClient = retrofit.create(MyClient.class);
        Call call = myClient.getMyApi().insertdata(type[1].getName().toString(), (int) type[0].getValue());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void history(GrainPie[] type) {
        type1.setText(type[2].getName());
        jumlahtype_1.setText(String.valueOf(type[2].getValue()));

    }


}
