package com.example.lai.toolsman.ChatFunction;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lai.toolsman.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout, parent, false);
        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends  RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView emailText;

        public MessageViewHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.messageTextLayout);
            profileImage = (CircleImageView) itemView.findViewById(R.id.messageProfileLayout);
            emailText = (TextView) itemView.findViewById(R.id.eamilTextLayout);

        }
    }

    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        Messages c = mMessageList.get(i);

        String fromUser = c.getFrom();
        String messageType = c.getType();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUser);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                String image = dataSnapshot.child("thumbImage").getValue().toString();

                viewHolder.emailText.setText(email);

                Picasso.get().load(image).placeholder(R.drawable.defaultavatar).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(messageType.equals("text")) {

            viewHolder.messageText.setText(c.getMessage());

        } else {

            viewHolder.messageText.setVisibility(View.INVISIBLE);
        }

    }

    public int getItemCount() {
        return mMessageList.size();
    }

}
