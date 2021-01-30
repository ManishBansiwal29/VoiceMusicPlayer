package com.manish.facechat_videocall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PageRange;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private Button saveBtn;
    private EditText userNameET,userBioET;
    private ImageView profileImageView;

    private static int GalleryPick = 1;

    private Uri ImageUri;

    private StorageReference userProfileImgRef;

    private FirebaseAuth mAuth;

    private String currentUser;

    private String downloadUrl;

    private DatabaseReference userRef;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser().getUid();

        saveBtn = findViewById(R.id.save_settings);
        userNameET = findViewById(R.id.username_settings);
        userBioET = findViewById(R.id.bio_settings);
        profileImageView = findViewById(R.id.settings_profile_image);

        progressDialog = new ProgressDialog(this);

        userProfileImgRef = FirebaseStorage.getInstance().getReference().child("Profile Image");

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        retrieveInfo();
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(SettingsActivity.this,ContactsActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
        if (requestCode==GalleryPick && resultCode==RESULT_OK && data != null){
            ImageUri = data.getData();
            profileImageView.setImageURI(ImageUri);
        }
    }
    private void saveUserData() {
        final String getUserName = userNameET.getText().toString();
        final String getUserStatus = userBioET.getText().toString();

        if (ImageUri == null){
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(currentUser).hasChild("image")){
                        saveInfoOnly();
                    }else {
                        Toast.makeText(SettingsActivity.this , "Please select a image first" , Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if( getUserName.equals("")){
            Toast.makeText(this , "Enter user name" , Toast.LENGTH_SHORT).show();
        }else if(getUserStatus.equals("")){
            Toast.makeText(this , "Enter bio..." , Toast.LENGTH_SHORT).show();
        }else{

            progressDialog.setTitle("Account Settings");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            final StorageReference filePath = userProfileImgRef.child(currentUser);
            final UploadTask uploadTask = filePath.putFile(ImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    downloadUrl = filePath.getDownloadUrl().toString();
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        downloadUrl = task.getResult().toString();

                        final HashMap<String,Object> profileMap = new HashMap<>();
                        profileMap.put("uid",currentUser);
                        profileMap.put("name",getUserName);
                        profileMap.put("bio",getUserStatus);
                        profileMap.put("image",downloadUrl);

                        userRef.child(currentUser).updateChildren(profileMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(SettingsActivity.this,ContactsActivity.class);
                                            startActivity(intent);
                                            finish();
                                            progressDialog.dismiss();
                                            Toast.makeText(SettingsActivity.this , "Profile Settings has been updated." , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }

    private void saveInfoOnly() {
        final String getUserName = userNameET.getText().toString();
        final String getUserStatus = userBioET.getText().toString();

        if( getUserName.equals("")){
            Toast.makeText(this , "Enter user name" , Toast.LENGTH_SHORT).show();
        }else if(getUserStatus.equals("")){
            Toast.makeText(this , "Enter bio" , Toast.LENGTH_SHORT).show();
        }else{
            final HashMap<String,Object> profileMap = new HashMap<>();
            profileMap.put("uid",currentUser);
            profileMap.put("name",getUserName);
            profileMap.put("bio",getUserStatus);

            userRef.child(currentUser).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(SettingsActivity.this,ContactsActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this , "Profile Settings has been updated." , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void retrieveInfo(){
        userRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String imageDb = snapshot.child("image").getValue().toString();
                    String nameDb = snapshot.child("name").getValue().toString();
                    String bioDb = snapshot.child("bio").getValue().toString();

                    userNameET.setText(nameDb);
                    userBioET.setText(bioDb);

                    Picasso.get().load(imageDb).placeholder(R.drawable.profile_image).into(profileImageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
