package com.example.uploadandviewimage.Account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.activity.FragmentActivity;
import com.example.uploadandviewimage.auth.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SaveData extends AppCompatActivity {
    private EditText nameA, email, alamat, passwor, retyope_password, mobilephone_reg;
    private Button btnInsert;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    private String phoneNumbber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        nameA = findViewById(R.id.et_insert_nama);
        email = findViewById(R.id.et_insert_email);
        alamat = findViewById(R.id.et_insert_alamat);
        passwor = findViewById(R.id.et_insert_password);
        retyope_password = findViewById(R.id.et_insert_password_retype);
        mobilephone_reg = findViewById(R.id.mobilephone_reg);
        mAuth = FirebaseAuth.getInstance();
        btnInsert = findViewById(R.id.textView_insert);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("accountz");

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId, userName, userEmail, userAlamat, mobilephone_sreg, passwordUser, retypePasswordUser;
                phoneNumbber = mAuth.getCurrentUser().getPhoneNumber();
                userId = mAuth.getCurrentUser().getUid();
                userName = nameA.getText().toString();
                userEmail = email.getText().toString();
                userAlamat = alamat.getText().toString();
                mobilephone_sreg = mAuth.getCurrentUser().getPhoneNumber();
                mobilephone_reg.setText(mobilephone_sreg);
                passwordUser = passwor.getText().toString();
                retypePasswordUser = retyope_password.getText().toString();
                if (!TextUtils.isEmpty(passwordUser) && !TextUtils.isEmpty(retypePasswordUser)) {
                    if (passwordUser.equals(retypePasswordUser)) {
                        Toast.makeText(SaveData.this, " Sucessfully  ", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(SaveData.this, "password doesnt match", Toast.LENGTH_SHORT).show();
//                        return;
                    }
                }
                Accounts accounts = new Accounts(userId, userName, userEmail, userAlamat, phoneNumbber, passwordUser, retypePasswordUser);
                databaseReference.child(userId).setValue(accounts);
                Intent intent = new Intent(SaveData.this, ProfileActivity.class);
                startActivity(intent);
            }

        });
    }
}
