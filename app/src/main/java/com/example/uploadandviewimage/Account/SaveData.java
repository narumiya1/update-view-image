package com.example.uploadandviewimage.Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.activity.FragmentActivity;
import com.example.uploadandviewimage.model.Accounts;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SaveData extends AppCompatActivity {
    private EditText id, name, email, alamat;
    private Button btnInsert ;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        name = findViewById(R.id.et_nama_account);
        email = findViewById(R.id.et_email_account);
        alamat = findViewById(R.id.et_alamat_account);

        btnInsert = findViewById(R.id.bt_submit);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("account");

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId, userName, userEmail, userAlamat;
                userId = databaseReference.push().getKey();
                userName = name.getText().toString();
                userEmail = email.getText().toString();
                userAlamat= alamat.getText().toString();

                Accounts accounts = new Accounts(userId, userName, userEmail,userAlamat);
                databaseReference.child(userId).setValue(accounts);
                Intent intent = new Intent(SaveData.this, FragmentActivity.class);
                startActivity(intent);
            }

        });
    }
}
