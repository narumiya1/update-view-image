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
import com.example.uploadandviewimage.auth.LoginActivity;
import com.example.uploadandviewimage.auth.ProfileActivity;
import com.example.uploadandviewimage.auth.Sesion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class SaveData extends AppCompatActivity {
    private EditText nameA, email, alamat, passwor, retyope_password, mobilephone_reg;
    private Button btnInsert;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    private String phoneNumbber;
    Sesion sesion;
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
        setContentView(R.layout.layout_register);

        nameA = findViewById(R.id.et_insert_nama);
        email = findViewById(R.id.et_insert_email);
        alamat = findViewById(R.id.et_insert_alamat);
        passwor = findViewById(R.id.et_insert_password);
        retyope_password = findViewById(R.id.et_insert_password_retype);
        mobilephone_reg = findViewById(R.id.mobilephone_reg);
        mAuth = FirebaseAuth.getInstance();
        sesion = new Sesion(this);
        btnInsert = findViewById(R.id.textView_insert);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
    }

    private boolean validateEmail() {
        String emailInput = email.getText().toString().trim();
        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
    private boolean validateUsername() {
        String usernameInput = nameA.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            nameA.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 20) {
            nameA.setError("Username too long");
            return false;
        } else {
            nameA.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String passwordInput = passwor.getText().toString().trim();
        String passwordReType = retyope_password.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            passwor.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            passwor.setError("Password too weak");
            return false;
        } else if(!passwordInput.equals(passwordReType)){
//            retyope_password.setError("Password doesn't match");
            Toast.makeText(SaveData.this, "passwordd doesnt match", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            passwor.setError(null);
            return true;
        }
    }
    public void confirmInput(View v) {
        if (!validateEmail() | !validateUsername() | !validatePassword()) {
            return;
        }
        String input = "Email: " + email.getText().toString();
        input += "\n";
        input += "Username: " + nameA.getText().toString();
        input += "\n";
        input += "Password: " + passwor.getText().toString();
//        Toast.makeText(this, input, Toast.LENGTH_LONG).show();

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
//                sesion.setPhone(mobilephone_sreg);
//                sesion.setIsLogin(true);
        Accounts accounts = new Accounts(userId, userName, userEmail, userAlamat, mobilephone_sreg, passwordUser);
        databaseReference.child(mobilephone_sreg).setValue(accounts);
        Intent intent = new Intent(SaveData.this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(SaveData.this, "Silahkan login untuk melanjutkan", Toast.LENGTH_SHORT).show();

    }
}
