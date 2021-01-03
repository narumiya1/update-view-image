package com.example.uploadandviewimage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.activity.AccountActivity;
import com.example.uploadandviewimage.activity.FragmentActivity;
import com.example.uploadandviewimage.auth.AuthActivity;
import com.example.uploadandviewimage.auth.ProfileActivity;
import com.example.uploadandviewimage.model.Accounts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {
    TextView txtData, textEmail, txtUsername, txtAlamat;
    Button btnLogout, btn_insertdataacount, btn_readdataacount ;
    FirebaseAuth mAuth ;
    DatabaseReference rootDb;
    String emailz, alamatz, usernamez;
    public AccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        /*
        txtData = rootView.findViewById(R.id.txtData);
        Bundle bundle = this.getArguments();
        String d = bundle.getString("key");
        txtData.setText(d);\
        */
        mAuth = FirebaseAuth.getInstance();
        txtData = rootView.findViewById(R.id.tv_account_phone);
        txtUsername = (TextView)rootView.findViewById(R.id.nameOneTv);
        textEmail = rootView.findViewById(R.id.tvemail_account);
        txtAlamat = rootView.findViewById(R.id.tv_account_alamat);
        txtData.setText(mAuth.getCurrentUser().getPhoneNumber());
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
        btn_insertdataacount = rootView.findViewById(R.id.btn_insertdataacount);

        btn_insertdataacount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btn_readdataacount = rootView.findViewById(R.id.btn_readdataacount);
        rootDb = FirebaseDatabase.getInstance().getReference().child("account");
//        btn_readdataacount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                rootDb.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
////                        if (snapshot.exists()) {
////                            String data = snapshot.getValue().toString();
////                            Accounts accounts = new Accounts();
////                            String x = accounts.getAlamat();
////                            txtData.setText(data);
////                            alamat.setText(x);
////                        }
//
//                        List<Accounts> bpfragmentTableList = new ArrayList<>();
//
//                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//
//                            Accounts bpfragmentTable = dataSnapshot1.getValue(Accounts.class);
//                            usernamez = bpfragmentTable.getNama();
//                            emailz = bpfragmentTable.getEmail();
//                            alamatz = bpfragmentTable.getAlamat();
//                            bpfragmentTableList.add(bpfragmentTable);
//                            txtUsername.setText(usernamez);
//                            txtAlamat.setText(alamatz);
//                            textEmail.setText(emailz);
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
        rootDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            String data = snapshot.getValue().toString();
//                            Accounts accounts = new Accounts();
//                            String x = accounts.getAlamat();
//                            txtData.setText(data);
//                            alamat.setText(x);
//                        }

                List<Accounts> bpfragmentTableList = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                    Accounts bpfragmentTable = dataSnapshot1.getValue(Accounts.class);
                    usernamez = bpfragmentTable.getNama();
                    emailz = bpfragmentTable.getEmail();
                    alamatz = bpfragmentTable.getAlamat();
                    bpfragmentTableList.add(bpfragmentTable);
                    txtUsername.setText(usernamez);
                    txtAlamat.setText(alamatz);
                    textEmail.setText(emailz);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Bundle bundle = this.getArguments();
//        if (getArguments() != null) {
//            String mParam1 = getArguments().getString("params");
//        }

//
//        if (bundle != null) {
//            String value1 = getArguments().getString("key1");
//            String value2 = getArguments().getString("key2");
//            value1=bundle.getString("key1", "a");
//            value2=bundle.getString("key2","b");
//
//        }

        return rootView;
    }

    public void setArguments(Bundle args){
        String v = args.getString("v");
        String w = args.getString("w");
        String x = args.getString("y");
//        alamat.setText(x);




    }
}
