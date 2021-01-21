package com.example.uploadandviewimage.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadandviewimage.Account.Accounts;
import com.example.uploadandviewimage.Account.SaveData;
import com.example.uploadandviewimage.NetworkClient;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.UploadApis;
import com.example.uploadandviewimage.activity.FragmentActivity;
import com.example.uploadandviewimage.fragment.AccountFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;

public class LoginActivity extends AppCompatActivity {

    private EditText numberPhone, password;
    private Button login;
    private FirebaseAuth.AuthStateListener listener;
    private String getPhone, getPassword, phoneAuth;
    private ProgressBar progressBar;
    private TextView reg;
    DatabaseReference rootDb;
    private FirebaseAuth mAuth;
    Sesion session;
    public static final int REQUEST_CODE = 1;
    boolean status = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        mAuth = FirebaseAuth.getInstance();
        session = new Sesion(this);
        numberPhone = findViewById(R.id.editTextPhoneLogin);
        password = findViewById(R.id.editTextPasswordLogin);
        login = findViewById(R.id.btn_login);
        reg = findViewById(R.id.textview_signup);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //auth here
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LoginActivity.this, AuthActivity.class));
                finish();
            }
        });

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //Mengecek Keberadaan User
//        listener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                //Mengecek apakah ada user yang sudah login / belum logout
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if(user != null){
//                    //Jika ada, maka halaman akan langsung berpidah pada MainActivity
//                    startActivity(new Intent(LoginActivity.this, FragmentActivity.class));
//                    finish();
//                }
//            }
//        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == login) {
                    userLogin();
                }
            }
        });
//        rootDb = FirebaseDatabase.getInstance().getReference().child("accountz");
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                rootDb.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//
//                        List<Accounts> bpfragmentTableList = new ArrayList<>();
//
//                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//
//                            getPhone = numberPhone.getText().toString();
//                            getPassword = password.getText().toString();
//                            Accounts bpfragmentTable = dataSnapshot1.getValue(Accounts.class);
//                            String hp = bpfragmentTable.getPhoneNumbers();
//                            String passwordAuth = bpfragmentTable.getPassword();
//                            //try 11 1 21
//                            if (hp.equals(getPhone) && passwordAuth.equals(getPassword) ) {
//                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
//                                finish();
//
//                            }else {
//                                Toast.makeText(LoginActivity.this, "Tidak Dapat Masuk, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
//                                progressBar.setVisibility(View.GONE);
//                            }
//                            bpfragmentTableList.add(bpfragmentTable);
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//
//                });
//            }
//        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String input1 = numberPhone.getText().toString();
                        String input2 = password.getText().toString();

                        if (dataSnapshot.child(input1).exists()) {
                            if (dataSnapshot.child(input1).child("password").getValue(String.class).equals(input2)) {

//                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                intent.putExtra(input1, "numberPhone");
                                startActivityForResult(intent, REQUEST_CODE);

                            } else {
                                Toast.makeText(LoginActivity.this, "Kata sandi salah", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Data belum terdaftar", Toast.LENGTH_SHORT).show();
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("edttext", input1);
                        AccountFragment fragobj = new AccountFragment();
                        fragobj.setArguments(bundle);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void userLogin() {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e("DEBUG", "Pause of ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("DEBUG", "Stop of ");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("DEBUG", "Destroy of ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("DEBUG", "  Resume of ");
    }

    private void openMain() {
        if (session.isLoggedIn()) {
            Intent i = null;
            if (status) {

                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }

    //    private void loginUserAccount(){
//        auth.signInWithEmailAndPassword(getPhone, getPassword)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        //Mengecek status keberhasilan saat login
//                        if(task.isSuccessful()){
//                            startActivity(new Intent(LoginActivity.this, FragmentActivity.class));
//                            finish();
//                        }else {
//                            Toast.makeText(LoginActivity.this, "Tidak Dapat Masuk, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//
//                });
//    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//
//
//            case R.id.btn_login:
//                //Mendapatkan dat yang diinputkan User
//                getPhone = numberPhone.getText().toString();
//                getPassword = password.getText().toString();
//
//                //Mengecek apakah email dan sandi kosong atau tidak
//                if(TextUtils.isEmpty(getPhone) || TextUtils.isEmpty(getPassword)){
//                    Toast.makeText(this, "Email atau Sandi Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
//                }else{
//                    loginUserAccount();
//                    progressBar.setVisibility(View.VISIBLE);
//                }
//                break;
//        }
//    }


}
