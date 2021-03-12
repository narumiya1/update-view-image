package com.example.uploadandviewimage.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.uploadandviewimage.Account.SaveData;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.Account.AccountUpdateActivity;
import com.example.uploadandviewimage.auth.AuthActivity;
import com.example.uploadandviewimage.Account.Accounts;
import com.example.uploadandviewimage.auth.LoginActivity;
import com.example.uploadandviewimage.auth.Preference;
import com.example.uploadandviewimage.auth.Sesion;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {
    TextView txtData, textEmail, txtUsername, txtAlamat, txtPasswrd, txtPasswrdRetype;
    Button btnLogout, btn_insertdataacount, btn_updatedataacount, mBtn_Save;
    FirebaseAuth mAuth;
    DatabaseReference rootDb;
    String getEmail, getAlamatz, getUsernamez, id, getId, getPw, getPwRetype;
    String getPphoneNumbbr;
    Sesion session;
    Accounts accounts = new Accounts();
    CircleImageView profile_image;
    private StorageReference mStorageReference;
    private FirebaseFirestore mFirestore;
    public static final int REQUEST = 1;
    public static final int FUCK_UP = 2;
    private Uri mainImageURI;

    public AccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();
//        phoneNumbbr = mAuth.getCurrentUser().getPhoneNumber();

        txtData = rootView.findViewById(R.id.tv_account_phone);
        txtUsername = (TextView) rootView.findViewById(R.id.nameOneTv);
        textEmail = rootView.findViewById(R.id.tvemail_account);
        txtAlamat = rootView.findViewById(R.id.tv_account_alamat);
        txtPasswrd = rootView.findViewById(R.id.tv_password_view);
        txtPasswrdRetype = rootView.findViewById(R.id.tv_password_view_retype);
        profile_image = rootView.findViewById(R.id.profile_image);
        mBtn_Save = rootView.findViewById(R.id.Btn_Save_Setup);
        mBtn_Save.setVisibility(View.GONE);
        session = new Sesion(getContext());
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();
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
                    Log.d("DATA CHANGEt", "onDataChange: " + dataSnapshot.getValue());
                    accounts = dataSnapshot.getValue(Accounts.class);
                    Log.d("DATA CHANGE -- ", "onDataChange: " + accounts.getAddress());
                    txtUsername.setText(accounts.getUsername());
                    txtAlamat.setText(accounts.getAddress());
                    textEmail.setText(accounts.getEmail());
                    txtData.setText(session.getPhone());

                    mFirestore.collection("Users").document(accounts.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {

                                if (task.getResult().exists()) {

                                    String image = task.getResult().getString("image");

                                    Glide.with(getActivity()).load(image).into(profile_image);


                                }
                            } else {

                                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                    profile_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            //LETS THE USER SEE THE PERMISION
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                //LETS THE USER CHOSE TO EXEXPT IT OR DENIE IT
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                    Toast.makeText(getActivity(), "Permision denied", Toast.LENGTH_SHORT).show();
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


                                } else {

                                    CHOOSEFOTO();


                                }


                            } else {

                                CHOOSEFOTO();

                            }

                        }
                    });

                    mBtn_Save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SAVE_NAME_AND_PHOTO();

                        }
                    });
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
                bundle.putSerializable("user", accounts);
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

    private void SAVE_NAME_AND_PHOTO() {

        final String user_name = accounts.getUsername();

        if (!TextUtils.isEmpty(user_name)) {

            final String user_id = accounts.getId();

            final StorageReference ImagesPath = mStorageReference.child("profile_images").child(user_id + ".jpg");


            ImagesPath.putFile(mainImageURI).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ImagesPath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {


                        Uri downUri = task.getResult();
                        downUri.toString();


                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("image", downUri.toString());
                        userMap.put("name", user_name);

                        Glide.with(getActivity()).load(downUri).into(profile_image);
                        mFirestore.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                        mBtn_Save.setVisibility(View.GONE);


                    } else {

                        String ERROR = task.getException().getMessage();
                        Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();

                    }
                }

            });
        } else {

            Toast.makeText(getActivity(), "Enter your name.", Toast.LENGTH_SHORT).show();

        }


    }

    private void CHOOSEFOTO() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST);

//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST) {


            if (resultCode == RESULT_OK) {

                mainImageURI = data.getData();


                profile_image.setImageURI(mainImageURI);
                mBtn_Save.setVisibility(View.VISIBLE);
            } else if (resultCode == FUCK_UP) {


                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();

            }


        }
    }

}