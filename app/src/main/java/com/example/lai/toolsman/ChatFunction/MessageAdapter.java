package com.example.lai.toolsman.ChatFunction;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lai.toolsman.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;

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

            emailText = (TextView) itemView.findViewById(R.id.eamilTextLayout);
            messageText = (TextView) itemView.findViewById(R.id.messageTextLayout);
            profileImage = (CircleImageView) itemView.findViewById(R.id.messageProfileLayout);
        }
    }

    public void onBindViewHolder(MessageViewHolder viewHolder, int i) {

        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();

        Messages c = mMessageList.get(i);

        String fromUser = c.getFrom();
/*
        if(fromUser == null) {

        }

        else if(fromUser.equals(currentUserId)) {
            view


        } else {

            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.WHITE);
        }*/

        viewHolder.messageText.setText(c.getMessage());


    }

    public int getItemCount() {
        return mMessageList.size();
    }

}
