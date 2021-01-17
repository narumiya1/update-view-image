package com.example.uploadandviewimage.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.activity.FragmentActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText numberPhone, password;
    private Button login;
    private FirebaseAuth.AuthStateListener listener;
    private String getPhone, getPassword, phoneAuth;
    private ProgressBar progressBar;
    private TextView reg;
    DatabaseReference rootDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        numberPhone = findViewById(R.id.editTextPhoneLogin);
        password = findViewById(R.id.editTextPasswordLogin);
        login = findViewById(R.id.btn_login);
        reg = findViewById(R.id.textview_signup);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        rootDb = FirebaseDatabase.getInstance().getReference().child("accountz");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rootDb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        List<Accounts> bpfragmentTableList = new ArrayList<>();

                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                            getPhone = numberPhone.getText().toString();
                            getPassword = password.getText().toString();
                            Accounts bpfragmentTable = dataSnapshot1.getValue(Accounts.class);
                            String hp = bpfragmentTable.getPhoneNumbers();
                            String passwordAuth = bpfragmentTable.getPassword();
                            //try 11 1 21
                            if (hp.equals(getPhone) && passwordAuth.equals(getPassword) ) {
                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                                finish();

                            }else {
//                                Toast.makeText(LoginActivity.this, "Tidak Dapat Masuk, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                            bpfragmentTableList.add(bpfragmentTable);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

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
