package com.example.lai.toolsman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostWater extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private static final int GALLERY_REQUEST = 1;

    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitBtn;
    private ImageButton mSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_water);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ArticleWater");
        mProgress = new ProgressDialog(this);

        mPostTitle = (EditText) findViewById(R.id.titleField);
        mPostDesc = (EditText) findViewById(R.id.descField);
        mSubmitBtn = (Button) findViewById(R.id.PostWater);
        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
                Intent Toelec = new Intent();
                Toelec.setClass(PostWater.this,Water.class);
                startActivity(Toelec);
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
            Uri imageUri = data.getData();
            mSelectImage.setImageURI(imageUri);
        }
    }

    private void startPosting() {
        mProgress.setMessage("Posting");
        mProgress.show();

        String title_value = mPostTitle.getText().toString().trim();
        String desc_value = mPostDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(title_value) && !TextUtils.isEmpty(desc_value)) {
            DatabaseReference newPost = mDatabase.push();

            newPost.child("title").setValue(title_value);
            newPost.child("desc").setValue(desc_value);
        }

    }
}
