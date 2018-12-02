package com.example.lai.toolsman.ChatFunction;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.lai.toolsman.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String mChatUser;
    private Toolbar mChatToolbar;

    private DatabaseReference mRootRef;
    private DatabaseReference mUserRef;
    private FirebaseAuth mAuth;

    private TextView mTitleView;
    private TextView mLastSeenView;
    private CircleImageView mProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        mChatToolbar = (Toolbar) findViewById(R.id.toolbar);    //some name mistake here > chatAppBar??


        setSupportActionBar(mChatToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mChatUser = getIntent().getStringExtra("user_id");
        String userEamil = getIntent().getStringExtra("user_email");

        //getSupportActionBar().setTitle(userEamil);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.chat_custom_bar, null);

        actionBar.setCustomView(actionBarView);

        // ----------Custom action bar item

        mTitleView = (TextView) findViewById(R.id.customBarTitle);
        mLastSeenView = (TextView) findViewById(R.id.customBarSeen);
        mProfileImage = (CircleImageView) findViewById(R.id.customBarImg);

        mTitleView.setText(userEamil);

        mRootRef.child("Users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String image = dataSnapshot.child("image").toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onStart() {

        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null) {

        } else {
            mUserRef.child("online").setValue(true);
        }
    }

    public void onStop() {

        super.onStop();
        mUserRef.child("online").setValue(false);
    }
}
