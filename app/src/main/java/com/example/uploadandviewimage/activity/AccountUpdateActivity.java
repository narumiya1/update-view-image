package com.example.uploadandviewimage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.fragment.AccountFragment;
import com.example.uploadandviewimage.model.Accounts;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountUpdateActivity extends AppCompatActivity {
    Button update;
    EditText et_nama, et_email, et_alamat;
    TextView tv_id;
    String id, name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        tv_id = findViewById(R.id.tv_id);
        et_nama = findViewById(R.id.et_update_nama_account);
        et_email = findViewById(R.id.et_update_email_account);
        et_alamat = findViewById(R.id.et_update_alamat_account);
        update = findViewById(R.id.bt_update_submit);


        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        name = intent.getStringExtra("namez");
        tv_id.setText(id);
        et_nama.setText(name);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("account");
                String nameU, alamarU, emailU;
                nameU = et_nama.getText().toString();
                emailU  = et_email.getText().toString();
                alamarU = et_alamat.getText().toString();

                Bundle args = new Bundle();
                args.putString("v", String.valueOf(nameU));
                args.putString("w", String.valueOf(emailU));
                args.putString("x", String.valueOf(alamarU));
                Accounts accounts = new Accounts(id,nameU, emailU, alamarU);
                databaseReference.child(id).setValue(accounts);
                AccountFragment accountzs = new AccountFragment();
                accountzs.setArguments(args);
                Intent intent = new Intent(AccountUpdateActivity.this, FragmentActivity.class);
                startActivity(intent);


            }
        });
    }
}
