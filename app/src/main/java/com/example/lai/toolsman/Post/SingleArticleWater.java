package com.example.lai.toolsman.Post;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lai.toolsman.ChatFunction.ProfileActivity;
import com.example.lai.toolsman.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleArticleWater extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference mDatabase;
    private DatabaseReference mCommentDB;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private ImageView mHeadsticker;
    private TextView mPoster;
    private TextView mTitle;
    private TextView mDesc;
    private ImageView mImage;


    EditText Comment;
    ImageButton AddComment;
    String CommentText;
    private RecyclerView mCommentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_article_water);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ArticleWater");

        mPost_key = getIntent().getExtras().getString("article_id");
        //Toast.makeText(SingleArticleElec.this, post_key, Toast.LENGTH_LONG).show();

        //原來的singleArticle能夠取得貼文 現在暫時拿掉
        /*mPoster = findViewById(R.id.poster);
        mTitle = findViewById(R.id.title);
        mDesc = findViewById(R.id.desc);
        mImage = findViewById(R.id.image);*/

        //留言功能
        Comment = (EditText) findViewById(R.id.postComment);
        AddComment = (ImageButton) findViewById(R.id.AddCommentBtn);
        AddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCommentList();
            }
        });
        mCommentDB = FirebaseDatabase.getInstance().getReference().child("ArticleWater").child(mPost_key).child("CommentWater");   //存留言的資料庫

        mCommentList = (RecyclerView) findViewById(R.id.CommentList);
        mCommentList.setHasFixedSize(true);
        mCommentList.setLayoutManager(new LinearLayoutManager(this));


        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String post_title = (String) dataSnapshot.child("title").getValue();
                //String post_desc = (String) dataSnapshot.child("desc").getValue();
                //String post_image = (String) dataSnapshot.child("image").getValue();
                //String post_poster = (String) dataSnapshot.child("username").getValue();


                //mPoster.setText(post_poster);
                // mTitle.setText(post_title);
                //mDesc.setText(post_desc);
                //Picasso.get().load(post_image).into(mImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<SingleCommentWater, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SingleCommentWater, BlogViewHolder>(
                SingleCommentWater.class,
                R.layout.activity_single_comment_water,
                BlogViewHolder.class,
                mCommentDB
        ) {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, SingleCommentWater model, int position) {
                viewHolder.setComment(model.getComment());

                //viewHolder.setDetail(getApplicationContext(), model.getEmail(), model.getProfile());

                viewHolder.mMenu.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        viewHolder.mView.setBackgroundColor(Color.GRAY);
                        Notification();
                    }


                });

                /*viewHolder.mProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Intent profileIntent = new Intent(SingleArticleWater.this, ProfileActivity.class);
                        //profileIntent.putExtra("user_id", userId);
                        //startActivity(profileIntent);
                        Toast toast = Toast.makeText(SingleArticleWater.this, "師傅頁面", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });*/

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //viewHolder.mView.setBackgroundColor(Color.GRAY);
                        Toast toast = Toast.makeText(SingleArticleWater.this, "選擇師傅", Toast.LENGTH_SHORT);
                        toast.show();

                        ;

                    }
                });
            }
        };
        mCommentList.setAdapter(firebaseRecyclerAdapter);
    }




    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView mProfile;
        ImageButton mMenu;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mProfile = (ImageView) mView.findViewById(R.id.headsticker);
            mMenu = (ImageButton) mView.findViewById(R.id.menu);
        }

        public void setComment(String comment) {
            TextView post_comment = mView.findViewById(R.id.postComment);
            post_comment.setText(comment);
        }


       /* public void setDetail(Context ctx, String userEmail, String userImage) {
            TextView user_email = (TextView) mView.findViewById(R.id.postemail);
            ImageView user_image = (ImageView) mView.findViewById(R.id.headsticker);

            user_email.setText(userEmail);
            Glide.with(ctx).load(userImage).into(user_image);

        }*/


    }

    private void Notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Notification notification = builder.setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("師傅已被選擇")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification);


    }

    public void addCommentList() {
        final String comment = Comment.getText().toString().trim();


        final DatabaseReference newPost = mCommentDB.push();
        mCommentDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (comment.matches(""))
                {
                    Toast toast = Toast.makeText(SingleArticleWater.this, "請輸入回覆", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    newPost.child("Comment").setValue(comment);
                    newPost.child("uid").setValue(mCurrentUser.getUid());
                    Toast toast = Toast.makeText(SingleArticleWater.this, "回覆成功", Toast.LENGTH_SHORT);
                    toast.show();
                    CommentText = comment;
                    Comment.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = Toast.makeText(SingleArticleWater.this, "回覆失敗", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
