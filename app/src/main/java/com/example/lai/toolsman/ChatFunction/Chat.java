package com.example.lai.toolsman.ChatFunction;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lai.toolsman.R;
import com.example.lai.toolsman.UserInfo.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Chat extends AppCompatActivity {

    private ViewPager mViewPager;
    public SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;

    private EditText mSearchText;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    //private EditText mSearchField;
    //private ImageButton mSearchBtn;
    //private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mSearchText = (EditText) findViewById(R.id.searchText);
        mSearchBtn = (ImageButton) findViewById(R.id.searchBtn);

        mResultList = (RecyclerView) findViewById(R.id.resultList);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mViewPager = (ViewPager) findViewById(R.id.chatPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.chat_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchText.getText().toString();
                firebaseUserSearch(searchText);
            }
        });

    }

    public void firebaseUserSearch(String searchText) {

        Query firebaseSearchQuery = mUserDatabase.orderByChild("email").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.user,
                UsersViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {
                viewHolder.setDetail(getApplicationContext(), model.getEmail(), model.getStatus(), model.getImage());
            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetail(Context ctx, String userEmail, String userStatus, String userImage) {
            TextView user_email = (TextView) mView.findViewById(R.id.email_text);
            TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);

            user_email.setText(userEmail);
            user_status.setText(userStatus);
            Glide.with(ctx).load(userImage).into(user_image);
        }
    }

}
