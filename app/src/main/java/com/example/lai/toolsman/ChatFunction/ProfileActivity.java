package com.example.lai.toolsman.ChatFunction;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.lai.toolsman.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView mProfileImage;
    private TextView mProfileEmail, mProfileStatus, mProfileFriendConut;
    private Button mProfileSendRequestBtn;

    private DatabaseReference mUsersDatabase;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userId = getIntent().getStringExtra("user_id");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        mProfileImage = (CircleImageView) findViewById(R.id.profileImage);
        mProfileEmail = (TextView) findViewById(R.id.profileEmail);
        mProfileStatus = (TextView) findViewById(R.id.profileStatus);
        mProfileFriendConut = (TextView) findViewById(R.id.profileFriendsCount);
        mProfileSendRequestBtn = (Button) findViewById(R.id.profileSendRequestBtn);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading user data");
        mProgressDialog.setMessage("Please wait while we load user data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mProfileEmail.setText(email);
                mProfileStatus.setText(status);
                Picasso.get().load(image).placeholder(R.drawable.defaultavatar).into(mProfileImage);
                mProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
