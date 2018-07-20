package com.example.lai.toolsman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettings extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private CircleImageView mUserImage;
    private TextView mUserEmail;
    private TextView mUserStatus;
    private Button mChangeStateBtn;
    private Button mChangeImageBtn;

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
    }


    @Override
    public void onBackPressed() {
        Intent drawerIntent= new Intent(AccountSettings.this, drawer.class);
        startActivity(drawerIntent);
    }
}
