package com.example.lai.toolsman.SearchUser;

import android.content.Context;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchUser extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mSearchField = (EditText) findViewById(R.id.searchField);
        mSearchBtn = (ImageButton) findViewById(R.id.searchBtn);
        mResultList = (RecyclerView) findViewById(R.id.resultList);

        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUserSearch();
            }
        });
    }

    public void firebaseUserSearch() {
        FirebaseRecyclerAdapter<Users, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.user_list_layout,
                UserViewHolder.class,
                mUserDatabase
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, Users model, int position) {
                viewHolder.setDetail(getApplicationContext(), model.getName(), model.getStatus(), model.getImage());


            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetail(Context ctx, String userName, String userStatus, String userImage) {
            TextView user_email = (TextView) mView.findViewById(R.id.emailText);
            TextView user_status = (TextView) mView.findViewById(R.id.statusText);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profileImage);

            user_email.setText(userName);
            user_status.setText(userStatus);
            Glide.with(ctx).load(userImage).into(user_image);

        }

    }


}
