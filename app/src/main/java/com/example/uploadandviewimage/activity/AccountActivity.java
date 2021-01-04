package com.example.uploadandviewimage.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.fragment.AccountFragment;
import com.example.uploadandviewimage.model.Accounts;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {
    private EditText name, email,alamat ;
    private String id;
    private Button submit;
    private DatabaseReference databaseReference;
    AccountFragment fragment = new AccountFragment();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        name = findViewById(R.id.et_nama_account);
        email = findViewById(R.id.et_email_account);
        alamat = findViewById(R.id.et_alamat_account);
        submit = findViewById(R.id.bt_submit);
/*
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final String userEnterName = name.getText().toString();
        final String userEnterEmail = email.getText().toString();
        final String userEnterAlamat = alamat.getText().toString();
        reference.child("account").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String nameDb = snapshot.child(userEnterName).child("name").getValue(String.class);
                    String emailDb = snapshot.child(userEnterEmail).child("email").getValue(String.class);
                    String alamatDb = snapshot.child(userEnterAlamat).child("alamat").getValue(String.class);

                    Bundle args = new Bundle();
                    args.putString("v",nameDb);
                    args.putString("w",emailDb);
                    args.putString("x",alamatDb);
                    AccountFragment fragment = new AccountFragment();
                    fragment.setArguments(args);

                    Intent intent = new Intent(AccountActivity.this, FragmentActivity.class);
                    startActivity(intent);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
        //2 1 21 back pres
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount()==0) finish();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id= databaseReference.push().getKey();
                if (!isEmpty(name.getText().toString()) && !isEmpty(email.getText().toString()) && !isEmpty(alamat.getText().toString())){
                    submitAccount(new Accounts(id,name.getText().toString(), email.getText().toString(), alamat.getText().toString()));
                }else {
                    Snackbar.make(findViewById(R.id.bt_submit), "Data tidak boleh kosong", Snackbar.LENGTH_LONG).show();

                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            name.getWindowToken(), 0);
                }
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.account_menu);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }

    }

    public interface IOnBackPressed {
        /**
         * If you return true the back press will not be taken into account, otherwise the activity will act naturally
         * @return true if your processing has priority if not false
         */
        boolean onBackPressed();
    }

    private void submitAccount(Accounts account) {

        databaseReference.child("account").push().setValue(account).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                name.setText("");
//                email.setText("");
//                alamat.setText("");

//                Bundle bundle = new Bundle();
//                String myMessage = "Stackoverflow is cool!";
//                bundle.putString("message1", String.valueOf(name));
//                bundle.putString("message2", String.valueOf(email));
//                bundle.putString("message3", String.valueOf(alamat));
//                AccountFragment fragInfo = new AccountFragment();
//                fragInfo.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().replace(R.id.frl_main, fragInfo);
//                getSupportFragmentManager().beginTransaction().commit();

                Bundle args = new Bundle();
                args.putString("v", String.valueOf(name.getText().toString()));
                args.putString("w", String.valueOf(email.getText().toString()));
                args.putString("x", String.valueOf(alamat.getText().toString()));
AccountFragment accounts = new AccountFragment();
                accounts.setArguments(args);
                Intent intent = new Intent(AccountActivity.this, FragmentActivity.class);
                startActivity(intent);

                Snackbar.make(findViewById(R.id.bt_submit), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    public boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

}
