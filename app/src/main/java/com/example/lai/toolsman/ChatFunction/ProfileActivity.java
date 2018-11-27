package com.example.lai.toolsman.ChatFunction;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView mProfileImage;
    private TextView mProfileEmail, mProfileStatus, mProfileFriendConut;
    private Button mProfileSendRequestBtn, mProfileDeclineBtn;
    private ProgressDialog mProgressDialog;

    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendRequestDatabase;
    private DatabaseReference mFriendDatabase;
    private FirebaseUser mCurrentUser;

    private String mCurrentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String userId = getIntent().getStringExtra("user_id");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("FriendRequest");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mProfileImage = (CircleImageView) findViewById(R.id.profileImage);
        mProfileEmail = (TextView) findViewById(R.id.profileEmail);
        mProfileStatus = (TextView) findViewById(R.id.profileStatus);
        mProfileFriendConut = (TextView) findViewById(R.id.profileFriendsCount);
        mProfileSendRequestBtn = (Button) findViewById(R.id.profileSendRequestBtn);
        mProfileDeclineBtn = (Button) findViewById(R.id.profileDeclineRequestBtn);

        mCurrentState = "not_friends";

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading user data");
        mProgressDialog.setMessage("Please wait while we load user data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mProfileDeclineBtn.setVisibility(View.INVISIBLE);
        mProfileDeclineBtn.setEnabled(false);

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mProfileEmail.setText(email);
                mProfileStatus.setText(status);
                Picasso.get().load(image).placeholder(R.drawable.defaultavatar).into(mProfileImage);


                // Friends list / request feature -------------------
                mFriendRequestDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(userId)) {
                            String requestType = dataSnapshot.child(userId).child("requestType").getValue().toString();

                            if(requestType.equals("received")) {
                                mCurrentState = "req_received";
                                mProfileSendRequestBtn.setText("Accept friend request");

                                mProfileDeclineBtn.setVisibility(View.VISIBLE);
                                mProfileDeclineBtn.setEnabled(true);

                            } else if(requestType.equals("sent")) {
                                mCurrentState = "req_sent";
                                mProfileSendRequestBtn.setText("Cancel friend request");

                                mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                                mProfileDeclineBtn.setEnabled(false);
                            }

                            mProgressDialog.dismiss();

                        } else {
                            mFriendDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.hasChild(userId)) {
                                        mCurrentState = "friends";
                                        mProfileSendRequestBtn.setText("Unfriend this Person");

                                        mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                                        mProfileDeclineBtn.setEnabled(false);
                                    }

                                    mProgressDialog.dismiss();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    mProgressDialog.dismiss();

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProfileSendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Not friends state -------------------
                if(mCurrentState.equals("not_friends")) {
                    mFriendRequestDatabase.child(mCurrentUser.getUid()).child(userId).child("requestType").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            mProfileSendRequestBtn.setEnabled(false);

                            if(task.isSuccessful()) {
                                mFriendRequestDatabase.child(userId).child(mCurrentUser.getUid()).child("requestType").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mProfileSendRequestBtn.setEnabled(true);
                                        mCurrentState = "req_sent";
                                        mProfileSendRequestBtn.setText("Cancel friend request");

                                        mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                                        mProfileDeclineBtn.setEnabled(false);

                                        //Toast.makeText(ProfileActivity.this, "Request sent successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed sending request.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                // Cancel request state -------------------
                if(mCurrentState.equals("req_sent")) {
                    mFriendRequestDatabase.child(mCurrentUser.getUid()).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendRequestDatabase.child(userId).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProfileSendRequestBtn.setEnabled(true);
                                    mCurrentState = "not_friends";
                                    mProfileSendRequestBtn.setText("Send friend request");

                                    mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                                    mProfileDeclineBtn.setEnabled(false);
                                }
                            });
                        }
                    });

                }

                // Request received state -------------------
                if(mCurrentState.equals("req_received")) {
                    final String currentDate = DateFormat.getDateInstance().format(new Date());
                    mFriendDatabase.child(mCurrentUser.getUid()).child(userId).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendDatabase.child(userId).child(mCurrentUser.getUid()).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                            mFriendRequestDatabase.child(mCurrentUser.getUid()).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                        public void onSuccess(Void aVoid) {
                                            mFriendRequestDatabase.child(userId).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mProfileSendRequestBtn.setEnabled(true);
                                                    mCurrentState = "friends";
                                                    mProfileSendRequestBtn.setText("Unfriend this Person");

                                                    mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                                                    mProfileDeclineBtn.setEnabled(false);
                                                }
                                            });
                                        }
                                    });
                                }

                            });
                        }
                    });

                }

                // Unfriend this person state -------------------
                if(mCurrentState.equals("friends")) {
                    mFriendDatabase.child(mCurrentUser.getUid()).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendDatabase.child(userId).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProfileSendRequestBtn.setEnabled(true);
                                    mCurrentState = "not_friends";
                                    mProfileSendRequestBtn.setText("Send friend request");

                                    mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                                    mProfileDeclineBtn.setEnabled(false);
                                }
                            });
                        }
                    });
                }

            }
        });

    }
}
