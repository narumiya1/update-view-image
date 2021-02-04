package com.example.uploadandviewimage.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
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
import com.google.firebase.database.Query;
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
    Accounts accounts = new Accounts();
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

        btnLogout = rootView.findViewById(R.id.btn_lougout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
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
//        rootDb = FirebaseDatabase.getInstance().getReference().child("User");
//        rootDb.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                List<Accounts> bpfragmentTableList = new ArrayList<>();
//
//                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//
//                    Accounts bpfragmentTable = dataSnapshot1.getValue(Accounts.class);
////                    id = mAuth.getCurrentUser().getUid();
//                    getId = bpfragmentTable.getPhone();
//                    String getPhone;
//                    getPhone = bpfragmentTable.getPhone();
//                    //try 11 1 21
//                    if (getPhone.equals(getId)) {
//                        getUsernamez = bpfragmentTable.getUsername();
//                        getEmail = bpfragmentTable.getEmail();
//                        getAlamatz = bpfragmentTable.getAddress();
//                        getPw = bpfragmentTable.getPassword();
////                        getPwRetype = bpfragmentTable.getRetypePassword();
//                        getPphoneNumbbr = bpfragmentTable.getPhone();
//
//
//                        System.out.println(" founded" + getId + " " + id);
//                        txtUsername.setText(getUsernamez);
//                        txtAlamat.setText(getAlamatz);
//                        textEmail.setText(getEmail);
//                        txtData.setText(getPphoneNumbbr);
//                        break;
//                    }
//                    bpfragmentTableList.add(bpfragmentTable);
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        getPphoneNumbbr = session.getPhone();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Sesion session = new Sesion(getActivity());
        Query query = reference.child("User").child(getPphoneNumbbr);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("DATA CHANGEt", "onDataChange: "+dataSnapshot.getValue());
                    accounts = dataSnapshot.getValue(Accounts.class);
                    Log.d("DATA CHANGE -- ", "onDataChange: "+accounts.getAddress());
                    txtUsername.setText(accounts.getUsername());
                    txtAlamat.setText(accounts.getAddress());
                    textEmail.setText(accounts.getEmail());
                    txtData.setText(session.getPhone());
//                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                        // do with your result
//                        String name = dataSnapshot.child("Username").getValue(String.class);
//                        Log.d(name,"output");
//                        txtUsername.setText(name);
//                        Accounts bpfragmentTable = issue.getValue(Accounts.class);
////
//                        getUsernamez = bpfragmentTable.getUsername();
//                        getEmail = bpfragmentTable.getEmail();
//                        getAlamatz = bpfragmentTable.getAddress();
//                        getPw = bpfragmentTable.getPassword();
////                        getPwRetype = bpfragmentTable.getRetypePassword();
//                        getPphoneNumbbr = bpfragmentTable.getPhone();
//                        String id = bpfragmentTable.getId();
//                        getId = bpfragmentTable.getId();
//                        System.out.println(" founded" + getId + " " + id);
//
////                        bpfragmentTableList.add(bpfragmentTable);
//
//
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    Log.d("DATA CHANGEt", "onDataChange: "+dataSnapshot.getValue());
//                    Accounts accounts = dataSnapshot.getValue(Accounts.class);
//                    Log.d("DATA CHANGEt", "onDataChange: "+accounts.getAddress());
//                    txtUsername.setText(accounts.getUsername());
//                    txtAlamat.setText(accounts.getAddress());
//                    textEmail.setText(accounts.getEmail());
//                    txtData.setText(accounts.getPhone());
////                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
////                        // do with your result
////                        String name = dataSnapshot.child("Username").getValue(String.class);
////                        Log.d(name,"output");
////                        txtUsername.setText(name);
////                        Accounts bpfragmentTable = issue.getValue(Accounts.class);
//////
////                        getUsernamez = bpfragmentTable.getUsername();
////                        getEmail = bpfragmentTable.getEmail();
////                        getAlamatz = bpfragmentTable.getAddress();
////                        getPw = bpfragmentTable.getPassword();
//////                        getPwRetype = bpfragmentTable.getRetypePassword();
////                        getPphoneNumbbr = bpfragmentTable.getPhone();
////                        String id = bpfragmentTable.getId();
////                        getId = bpfragmentTable.getId();
////                        System.out.println(" founded" + getId + " " + id);
////
//////                        bpfragmentTableList.add(bpfragmentTable);
////
////
////                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        btn_updatedataacount = rootView.findViewById(R.id.btn_updatedataacount);
        btn_updatedataacount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), AccountUpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",accounts);
                intent.putExtras(bundle);
//                intent.putExtra("id",id);
//                intent.putExtra("namez", accounts.getUsername());
//                intent.putExtra("email", accounts.getEmail());
//                intent.putExtra("alamat", accounts.getAddress());
//                intent.putExtra("phone", accounts.getPhone());
//                intent.putExtra("psswrd", accounts.getPassword());
//                intent.putExtra("psswrdretype", accounts.getPassword());
//                intent.putExtra("id", getId);

                startActivity(intent);

            }
        });


        return rootView;
    }

}