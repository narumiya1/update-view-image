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
import com.example.uploadandviewimage.fragment.AccountFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountUpdateActivity extends AppCompatActivity {
    Button update;
    EditText et_nama, et_email, et_alamat, et_password, et_password_retype;
    TextView tv_id;
    String id, name,email, alamat, pw, retype;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        tv_id = findViewById(R.id.tv_id);
        et_nama = findViewById(R.id.et_update_nama_account);
        et_email = findViewById(R.id.et_update_email_account);
        et_alamat = findViewById(R.id.et_update_alamat_account);
        update = findViewById(R.id.bt_update_submit);
        et_password = findViewById(R.id.et_update_password_account);
        et_password_retype=findViewById(R.id.et_update_retypepassowrd_account);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        id = mAuth.getCurrentUser().getPhoneNumber();
        name = intent.getStringExtra("namez");
        email = intent.getStringExtra("email");
        alamat = intent.getStringExtra("alamat");
        pw = intent.getStringExtra("psswrd");
        retype = intent.getStringExtra("psswrdretype");
        tv_id.setText(id);
        et_nama.setText(name);
        et_email.setText(email);
        et_alamat.setText(alamat);
        et_password.setText(pw);
        et_password_retype.setText(retype);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("accountz");
                String nameU, alamarU, emailU, phones, password, passwordRetype ;
                id = mAuth.getCurrentUser().getUid();
                nameU = et_nama.getText().toString();
                emailU  = et_email.getText().toString();
                alamarU = et_alamat.getText().toString();
                password = et_password.getText().toString();
                passwordRetype = et_password_retype.getText().toString();
                phones = mAuth.getCurrentUser().getPhoneNumber();
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordRetype)) {
                    if (password.equals(passwordRetype)) {
                        Toast.makeText(AccountUpdateActivity.this, " Sucessfully  ", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AccountUpdateActivity.this, "password doesnt match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Bundle args = new Bundle();
                args.putString("v", String.valueOf(nameU));
                args.putString("w", String.valueOf(emailU));
                args.putString("x", String.valueOf(alamarU));
                Accounts accounts = new Accounts(id,nameU, emailU, alamarU,phones, password, passwordRetype);
                databaseReference.child(id).setValue(accounts);
                AccountFragment accountzs = new AccountFragment();
                accountzs.setArguments(args);
                Intent intent = new Intent(AccountUpdateActivity.this, FragmentActivity.class);
                startActivity(intent);


            }
        });
    }
}
