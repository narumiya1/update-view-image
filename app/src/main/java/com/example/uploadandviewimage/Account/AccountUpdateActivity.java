package com.example.uploadandviewimage.Account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.activity.FragmentActivity;
import com.example.uploadandviewimage.auth.Preference;
import com.example.uploadandviewimage.auth.Sesion;
import com.example.uploadandviewimage.fragment.AccountFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class AccountUpdateActivity extends AppCompatActivity {
    Button update;
    EditText et_nama, et_email, et_alamat;
    TextView tv_id;
    String id, name,email, alamat,phone, pw, retype;
    FirebaseAuth mAuth;
    TextInputEditText et_password,et_password_retype;
    private Accounts accounts = new Accounts();
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                            //"(?=.*[0-9])" +         //at least 1 digit
                            //"(?=.*[a-z])" +         //at least 1 lower case letter
                            //"(?=.*[A-Z])" +         //at least 1 upper case letter
//                    "(?=.*[a-zA-Z])" +      //any letter
//                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                    "(?=\\S+$)" +           //no white spaces
                            ".{6,}"               //at least 4 characters
//                    "$"
            );
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
         accounts = (Accounts) intent.getSerializableExtra("user");
//        id = mAuth.getCurrentUser().getPhoneNumber();
        tv_id.setText(accounts.getId());
        et_nama.setText(accounts.getUsername());
        et_email.setText(accounts.getEmail());
        et_alamat.setText(accounts.getAddress());
        et_password.setText(accounts.getPassword());
        et_password_retype.setText(accounts.getPassword());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                String nameU, alamarU, emailU, phones, password, passwordRetype ;
//                id = mAuth.getCurrentUser().getUid();
                nameU = et_nama.getText().toString();
                emailU  = et_email.getText().toString();
                alamarU = et_alamat.getText().toString();
                password = et_password.getText().toString();
                passwordRetype = et_password_retype.getText().toString();
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordRetype)) {
                    if (!password.equals(passwordRetype)) {
                        Toast.makeText(AccountUpdateActivity.this, "password doesnt match", Toast.LENGTH_SHORT).show();
                        return;

                    } else  if (!PASSWORD_PATTERN.matcher(password).matches()&&!PASSWORD_PATTERN.matcher(passwordRetype).matches()){
                        et_password.setError("min 6");
                        return;
                    }else {
                        et_password.setError(null);
                        Toast.makeText(AccountUpdateActivity.this, "  ", Toast.LENGTH_SHORT).show();

                    }
                }

                if (emailU.isEmpty()){
                    et_email.setError("cant empty");
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(emailU).matches()){
                    et_email.setError("enter valid email");
                    return;
                }else {
                    et_email.setError(null);

                }

                DatabaseReference reference = databaseReference.child(new Sesion(AccountUpdateActivity.this).getPhone());
                reference.child("address").setValue(alamarU);
                reference.child("email").setValue(emailU);
                reference.child("password").setValue(password);
                reference.child("username").setValue(nameU);

//                Bundle args = new Bundle();
//                args.putString("v", String.valueOf(nameU));
//                args.putString("w", String.valueOf(emailU));
//                args.putString("x", String.valueOf(alamarU));
//                Accounts accounts = new Accounts(id,nameU, emailU, alamarU,phone, password);
//                databaseReference.child(phone).setValue(accounts);
//                AccountFragment accountzs = new AccountFragment();
//                accountzs.setArguments(args);
//                Intent intent = new Intent(AccountUpdateActivity.this, FragmentActivity.class);
//                startActivity(intent);
                finish();

            }
        });
    }
}
