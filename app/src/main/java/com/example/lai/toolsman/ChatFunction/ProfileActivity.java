package com.example.lai.toolsman.ChatFunction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView mProfileImage;
    private TextView mProfileEmail, mProfileStatus, mProfileFriendConut;
    private Button mProfileSendRequestBtn, mProfileDeclineBtn;
    private ProgressDialog mProgressDialog;
    private TextView mScore;

    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendRequestDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;
    private DatabaseReference mRootRef;
    private FirebaseUser mCurrentUser;
    private Button Scorebtn;
    private String mCurrentState;
    int CurrentScore;
    int newScore=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String userId = getIntent().getStringExtra("user_id");

        final View ScoreInput = getLayoutInflater().inflate(R.layout.score,null);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("FriendRequest");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();



        mProfileImage = (CircleImageView) findViewById(R.id.profileImage);
        mProfileEmail = (TextView) findViewById(R.id.profileEmail);
        mProfileStatus = (TextView) findViewById(R.id.profileStatus);
        mProfileFriendConut = (TextView) findViewById(R.id.profileFriendsCount);
        mProfileSendRequestBtn = (Button) findViewById(R.id.profileSendRequestBtn);
        mProfileDeclineBtn = (Button) findViewById(R.id.profileDeclineRequestBtn);
        mScore=(TextView)findViewById(R.id.UserScore);


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
                CurrentScore=Integer.parseInt(dataSnapshot.child("Score").getValue().toString());

                mProfileEmail.setText(email);
                mProfileStatus.setText(status);
                mScore.setText("用戶評分:"+CurrentScore);

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

        Scorebtn=(Button)findViewById(R.id.Scorebtn);
        Scorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProfileActivity.this).setTitle("請輸入評分")
                        .setView(ScoreInput)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText input = (EditText)ScoreInput.findViewById(R.id.ScoreInput);
                                newScore = Integer.parseInt(input.getText().toString());
                                CurrentScore=(CurrentScore+newScore)/2;
                                mUsersDatabase.child("Score").setValue(CurrentScore);




                            }
                        }).show();
            }
        });
        mProfileSendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProfileSendRequestBtn.setEnabled(false);

                // Not friends state -------------------
                if(mCurrentState.equals("not_friends")) {

                    DatabaseReference newNotificationRef = mRootRef.child("Notifications").child(userId).push();
                    String newNotificationId = newNotificationRef.getKey();

                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put("from", mCurrentUser.getUid());
                    notificationData.put("type", "request");

                    Map requestMap = new HashMap();
                    requestMap.put("FriendRequest/" + mCurrentUser.getUid() + "/" + userId + "/requestType", "sent");
                    requestMap.put("FriendRequest/" + userId + "/" + mCurrentUser.getUid() + "/requestType", "received");
                    requestMap.put("Notifications/" + userId + "/" + newNotificationId, notificationData);

                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError != null) {
                                Toast.makeText(ProfileActivity.this, "This was some error in sending request", Toast.LENGTH_SHORT).show();
                            }

                            mProfileSendRequestBtn.setEnabled(true);
                            mCurrentState = "req_sent";
                            mProfileSendRequestBtn.setText("Cancel friend request");

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

                    Map friendsMap = new HashMap();
                    friendsMap.put("Friends/" + mCurrentUser.getUid() + "/" + userId + "/date", currentDate);
                    friendsMap.put("Friends/" +userId + "/" + mCurrentUser.getUid() + "/date", currentDate);

                    friendsMap.put("FriendRequest/" + mCurrentUser.getUid() + "/" + userId, null);
                    friendsMap.put("FriendRequest/" + userId + "/" + mCurrentUser.getUid(), null);

                    mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError == null) {
                                mProfileSendRequestBtn.setEnabled(true);
                                mCurrentState = "friends";
                                mProfileSendRequestBtn.setText("Unfriend this Person");

                                mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                                mProfileDeclineBtn.setEnabled(false);
                            } else {
                                String error = databaseError.getMessage();
                                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();

                            }
                        }

                    });

                }

                // Unfriend this person state -------------------
                if(mCurrentState.equals("friends")) {

                    Map unfriendMap = new HashMap();
                    unfriendMap.put("Friends/" + mCurrentUser.getUid() + "/" + userId, null);
                    unfriendMap.put("Friends/" +userId + "/" + mCurrentUser.getUid(), null);

                    mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError == null) {
                                mCurrentState = "not_friends";
                                mProfileSendRequestBtn.setText("Send friend request");

                                mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                                mProfileDeclineBtn.setEnabled(false);
                            } else {
                                String error = databaseError.getMessage();
                                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                            }

                            mProfileSendRequestBtn.setEnabled(true);
                        }

                    });


                }

            }
        });


        mProfileDeclineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map declineMap = new HashMap();

                declineMap.put("FriendRequest/" + mCurrentUser.getUid() + "/" + userId, null);
                declineMap.put("FriendRequest/" + userId + "/" + mCurrentUser.getUid(), null);

                mRootRef.updateChildren(declineMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        if(databaseError == null) {

                            mCurrentState = "not friends";
                            mProfileSendRequestBtn.setText("Send Friend Request");
                            Toast.makeText(ProfileActivity.this, "Decline the friend request successfully", Toast.LENGTH_LONG).show();

                            mProfileDeclineBtn.setVisibility(View.INVISIBLE);
                            mProfileDeclineBtn.setEnabled(false);
                        } else {
                            String error = databaseError.getMessage();
                            Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_LONG).show();
                        }

                        mProfileSendRequestBtn.setEnabled(true);
                    }
                });

            }
        });

    }
}
