package com.example.uploadandviewimage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uploadandviewimage.Account.SaveData;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.Account.AccountUpdateActivity;
import com.example.uploadandviewimage.auth.AuthActivity;
import com.example.uploadandviewimage.Account.Accounts;
import com.example.uploadandviewimage.auth.LoginActivity;
import com.example.uploadandviewimage.auth.Preference;
import com.example.uploadandviewimage.auth.Sesion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {
    TextView txtData, textEmail, txtUsername, txtAlamat, txtPasswrd,txtPasswrdRetype ;
    Button btnLogout, btn_insertdataacount, btn_updatedataacount ;
    FirebaseAuth mAuth ;
    DatabaseReference rootDb;
    String getEmail, getAlamatz, getUsernamez,id, getId, getPw, getPwRetype;
    String getPphoneNumbbr;
    Sesion session;
    public AccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();
//        phoneNumbbr = mAuth.getCurrentUser().getPhoneNumber();

        txtData = rootView.findViewById(R.id.tv_account_phone);
        txtUsername = (TextView)rootView.findViewById(R.id.nameOneTv);
        textEmail = rootView.findViewById(R.id.tvemail_account);
        txtAlamat = rootView.findViewById(R.id.tv_account_alamat);
        txtPasswrd = rootView.findViewById(R.id.tv_password_view);
        txtPasswrdRetype = rootView.findViewById(R.id.tv_password_view_retype);
        session = new Sesion(getContext());
//        txtData.setText(mAuth.getCurrentUser().getPhoneNumber());
        // logout
        btnLogout = rootView.findViewById(R.id.btn_lougout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getContext(), LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                startActivity(intent);
                session.logoutUser();

            }
        });
        // insert Data user
        btn_insertdataacount = rootView.findViewById(R.id.btn_insertdataacount);
        btn_insertdataacount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SaveData.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        // get Data from Insert using firebase data snapshot
        rootDb = FirebaseDatabase.getInstance().getReference().child("accounts");
        rootDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Accounts> bpfragmentTableList = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                    Accounts bpfragmentTable = dataSnapshot1.getValue(Accounts.class);
//                    id = mAuth.getCurrentUser().getUid();
                    getId = bpfragmentTable.getPhoneNumbers();
                    String getPhone;
                    getPhone = bpfragmentTable.getPhoneNumbers();
                    //try 11 1 21
                    if (getPhone.equals(getId)) {
                        getUsernamez = bpfragmentTable.getNama();
                        getEmail = bpfragmentTable.getEmail();
                        getAlamatz = bpfragmentTable.getAlamat();
                        getPw = bpfragmentTable.getPassword();
                        getPwRetype = bpfragmentTable.getRetypePassword();
                        getPphoneNumbbr = bpfragmentTable.getPhoneNumbers();


                        System.out.println(" founded" + getId + " " + id);
                        txtUsername.setText(getUsernamez);
                        txtAlamat.setText(getAlamatz);
                        textEmail.setText(getEmail);
                        txtData.setText(getPphoneNumbbr);
                        break;
                    }
                    bpfragmentTableList.add(bpfragmentTable);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_updatedataacount = rootView.findViewById(R.id.btn_updatedataacount);
        btn_updatedataacount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), AccountUpdateActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("namez", getUsernamez);
                intent.putExtra("email", getEmail);
                intent.putExtra("alamat", getAlamatz);
                intent.putExtra("phone", getPphoneNumbbr);
                intent.putExtra("psswrd", getAlamatz);
                intent.putExtra("psswrdretype", getAlamatz);

                startActivity(intent);

            }
        });


        return rootView;
    }

}
