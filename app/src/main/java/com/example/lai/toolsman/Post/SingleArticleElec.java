package com.example.lai.toolsman.Post;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lai.toolsman.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleArticleElec extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference mDatabase;

    private ImageView mHeadsticker;
    private TextView mPoster;
    private TextView mTitle;
    private TextView mDesc;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_article_elec);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ArticleElec");

        mPost_key = getIntent().getExtras().getString("article_id");
        //Toast.makeText(SingleArticleElec.this, post_key, Toast.LENGTH_LONG).show();
        mPoster = findViewById(R.id.poster);
        mTitle = findViewById(R.id.title);
        mDesc = findViewById(R.id.desc);
        mImage = findViewById(R.id.image);


        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_poster = (String) dataSnapshot.child("username").getValue();

                mPoster.setText(post_poster);
                mTitle.setText(post_title);
                mDesc.setText(post_desc);
                Picasso.get().load(post_image).into(mImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
