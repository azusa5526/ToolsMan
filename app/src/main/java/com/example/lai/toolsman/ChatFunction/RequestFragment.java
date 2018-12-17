package com.example.lai.toolsman.ChatFunction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lai.toolsman.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */

public class RequestFragment extends Fragment {

    private RecyclerView mFriendRequestList;

    private DatabaseReference mFriendRequestDatabase;
    private DatabaseReference mUsersDatebase;
    private FirebaseAuth mAuth;

    private String mCurrentUserId;
    private View mMainView;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_request, container, false);

        mFriendRequestList = (RecyclerView) mMainView.findViewById(R.id.friendRequestList);
        mAuth = FirebaseAuth.getInstance();

        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("FriendRequest").child(mCurrentUserId);
        mFriendRequestDatabase.keepSynced(true);
        mUsersDatebase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatebase.keepSynced(true);


        mFriendRequestList.setHasFixedSize(true);
        mFriendRequestList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;

    }


    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Requsets, FriendRequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Requsets, FriendRequestViewHolder>(
                Requsets.class,
                R.layout.user_list_layout,
                FriendRequestViewHolder.class,
                mFriendRequestDatabase
        ) {
            @Override
            protected void populateViewHolder(final FriendRequestViewHolder friendRequestViewHolder, Requsets requsets, int i) {

                friendRequestViewHolder.setRequest(requsets.getRequestType());

                final String listUserId = getRef(i).getKey();

                mUsersDatebase.child(listUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userEmail = dataSnapshot.child("email").getValue().toString();
                        String userThumb = dataSnapshot.child("thumbImage").getValue().toString();

                        friendRequestViewHolder.setEamil(userEmail);
                        friendRequestViewHolder.setImage(userThumb, getContext());

                        friendRequestViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                profileIntent.putExtra("user_id", listUserId);
                                startActivity(profileIntent);

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        mFriendRequestList.setAdapter(firebaseRecyclerAdapter);

    }



    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FriendRequestViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setEamil(String email) {
            TextView userEmailView = mView.findViewById(R.id.emailText);
            userEmailView.setText(email);
        }

        public void setImage(String thumbImage, Context ctx) {
            ImageView userImageView = (ImageView) mView.findViewById(R.id.profileImage);
            Picasso.get().load(thumbImage).placeholder(R.drawable.defaultavatar).into(userImageView);
        }

        public void setRequest(String requestType) {
            TextView requestView = mView.findViewById(R.id.statusText);
            requestView.setText(requestType);
        }

    }

}