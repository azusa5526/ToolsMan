package com.example.lai.toolsman;

import android.accounts.Account;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettings extends AppCompatActivity {

    //Firebase
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference mImageStorage;

    private CircleImageView mUserImage;
    private TextView mUserEmail;
    private TextView mUserStatus;
    private Button mChangeStateBtn;
    private Button mChangeImageBtn;

    private static final int GALLERY_PICK = 1;
    private static final int MAX_LENGTH = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        getSupportActionBar().hide();

        mUserImage = (CircleImageView) findViewById(R.id.displayImage);
        mUserEmail = (TextView) findViewById(R.id.displayEmail);
        mUserStatus = (TextView) findViewById(R.id.displayStatus);
        mChangeStateBtn = (Button) findViewById(R.id.changeState);
        mChangeImageBtn = (Button) findViewById(R.id.changeImage);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = mCurrentUser.getUid();

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mUserEmail.setText(email);
                mUserStatus.setText(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mChangeStateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statusIntent = new Intent(AccountSettings.this, StatusActivity.class);
                startActivity(statusIntent);
            }
        });

        mChangeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);


                /*
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AccountSettings.this);
                        */
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)   //keep it square
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                StorageReference filePath = mImageStorage.child("User_profile_images").child(random() + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(AccountSettings.this, "Working", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AccountSettings.this, "Error in uploading", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    public void onBackPressed() {
        Intent drawerIntent= new Intent(AccountSettings.this, drawer.class);
        startActivity(drawerIntent);
    }
}
