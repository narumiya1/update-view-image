package com.example.uploadandviewimage.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {
    TextView txtData, textEmail, txtUsername, txtAlamat;
    Button btnLogout, btn_insertdataacount, btn_updatedataacount ;
    FirebaseAuth mAuth ;
    DatabaseReference rootDb;
    String getEmail, getAlamatz, getUsernamez,id;
    String phoneNumbbr;
    public AccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();
        txtData = rootView.findViewById(R.id.tv_account_phone);
        txtUsername = (TextView)rootView.findViewById(R.id.nameOneTv);
        textEmail = rootView.findViewById(R.id.tvemail_account);
        txtAlamat = rootView.findViewById(R.id.tv_account_alamat);
        txtData.setText(mAuth.getCurrentUser().getPhoneNumber());
        // logout
        btnLogout = rootView.findViewById(R.id.btn_lougout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
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
        rootDb = FirebaseDatabase.getInstance().getReference().child("account");
        rootDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phoneNumbbr = mAuth.getCurrentUser().getPhoneNumber();

                List<Accounts> bpfragmentTableList = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                    Accounts bpfragmentTable = dataSnapshot1.getValue(Accounts.class);
                    getUsernamez = bpfragmentTable.getNama();
                    getEmail = bpfragmentTable.getEmail();
                    getAlamatz = bpfragmentTable.getAlamat();
                    id = bpfragmentTable.getId();
                    if (bpfragmentTable.getPhoneNumbers() == phoneNumbbr){
                        System.out.println(" founded" + bpfragmentTable.getPhoneNumbers());
                    }
                    bpfragmentTableList.add(bpfragmentTable);
                    txtUsername.setText(getUsernamez);
                    txtAlamat.setText(getAlamatz);
                    textEmail.setText(getEmail);


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
                startActivity(intent);

            }
        });


        return rootView;
    }

}
