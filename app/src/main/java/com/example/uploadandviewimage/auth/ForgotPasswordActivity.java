package com.example.uploadandviewimage.auth;

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

import com.example.uploadandviewimage.Account.AccountUpdateActivity;
import com.example.uploadandviewimage.Account.Accounts;
import com.example.uploadandviewimage.Account.SaveData;
import com.example.uploadandviewimage.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_forgotpassword_account);

        update = findViewById(R.id.bt_update_submitforgot);
        et_password = findViewById(R.id.et_update_password_account_forgot);
        et_password_retype=findViewById(R.id.et_update_retypepassowrd_forgot);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        accounts = (Accounts) intent.getSerializableExtra("user");
        id = mAuth.getCurrentUser().getPhoneNumber();
    }
    private boolean validatePassword() {
        String passwordInput = et_password.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            et_password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            et_password.setError("Password too weak");
            return false;
        } else {
            et_password.setError(null);
            return true;
        }
    }
    public void buttonForgotPassword(View v){
        if (!validatePassword()) {
            return;
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        String nameU, alamarU, emailU, phones, password, passwordRetype ;
        id = mAuth.getCurrentUser().getPhoneNumber();
        password = et_password.getText().toString();
        passwordRetype = et_password_retype.getText().toString();
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordRetype)) {
            if (!password.equals(passwordRetype)) {
                Toast.makeText(ForgotPasswordActivity.this, "password doesnt match", Toast.LENGTH_SHORT).show();
                return;

            } else  if (!PASSWORD_PATTERN.matcher(password).matches()&&!PASSWORD_PATTERN.matcher(passwordRetype).matches()){
                et_password.setError("min 6");
                return;
            }else {
                et_password.setError(null);
                Toast.makeText(ForgotPasswordActivity.this, "  ", Toast.LENGTH_SHORT).show();

                Toast.makeText(ForgotPasswordActivity.this, "Silahkan login kmbali untuk melanjutkn", Toast.LENGTH_LONG).show();
            }
        }


        DatabaseReference reference = databaseReference.child(id);
        reference.child("password").setValue(password);

        finish();
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginNumber.class);
        startActivity(intent);

    }
}
