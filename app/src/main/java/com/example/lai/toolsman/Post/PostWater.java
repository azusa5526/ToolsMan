package com.example.lai.toolsman.Post;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lai.toolsman.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class PostWater extends AppCompatActivity {

    private Toolbar PostWaterBar;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    String id="一般使用者";
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitBtn;
    private ImageButton mSelectImage;
    private Uri mImageUri = null;
    String AccountName;
    private  DatabaseReference userid;
    private DatabaseReference historyDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_water);
        Intent GetName = getIntent();
        AccountName = GetName.getStringExtra("AccountName");

        PostWaterBar = findViewById(R.id.post_air_bar);
        setSupportActionBar(PostWaterBar);
        getSupportActionBar().setTitle("Add New Post");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ArticleWater");
        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("User_Water").child(mCurrentUser.getUid());

        String currentUser=mCurrentUser.getUid();
        userid=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);
        userid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                id=dataSnapshot.child("id").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        String currentHistoryUser = mCurrentUser.getUid();
        historyDatabase = FirebaseDatabase.getInstance().getReference().child("History").child(currentHistoryUser);

        mPostTitle = (EditText) findViewById(R.id.titleField);
        mPostDesc = (EditText) findViewById(R.id.descField);
        mSubmitBtn = (Button) findViewById(R.id.Submit);
        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);
        mStorage = FirebaseStorage.getInstance().getReference();

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
        mSelectImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }
    }

    private void startPosting() {
        mProgress.setMessage("Posting");

        final String title_value = mPostTitle.getText().toString().trim();
        final String desc_value = mPostDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(title_value) && !TextUtils.isEmpty(desc_value) && mImageUri != null) {
            StorageReference filepath = mStorage.child("Water").child(mImageUri.getLastPathSegment());
            mProgress.show();

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUriForHistory = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newHistory = historyDatabase.push();

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newHistory.child("type").setValue("水務類");
                            newHistory.child("title").setValue(title_value);
                            newHistory.child("desc").setValue(desc_value);
                            newHistory.child("image").setValue(downloadUriForHistory.toString());
                            newHistory.child("uid").setValue(mCurrentUser.getUid());
                            newHistory.child("username").setValue(AccountName);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUri = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = mDatabase.push();

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("title").setValue(title_value);
                            newPost.child("desc").setValue(desc_value);
                            newPost.child("image").setValue(downloadUri.toString());
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("select").setValue("false");



                            newPost.child("username").setValue(AccountName+"("+id+")").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(PostWater.this, "貼文成功", Toast.LENGTH_LONG).show();
                                        //Intent goBack = new Intent(PostWater.this,Water.class);
                                        //goBack.putExtra("AccountName",AccountName);
                                        //startActivity(goBack);
                                        onRestart();
                                        finish();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mProgress.dismiss();
                }
            });

        } else {
            Toast.makeText(PostWater.this,"Title and Desc can't be null", Toast.LENGTH_LONG).show();
        }

    }


}
