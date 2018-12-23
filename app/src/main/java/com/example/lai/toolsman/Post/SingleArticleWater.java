package com.example.lai.toolsman.Post;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import java.text.DateFormat;
import java.util.Date;

public class SingleArticleWater extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference mDatabase;
    private DatabaseReference mCommentDB;
    private  DatabaseReference PosterDB;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUsersDatebase;
    private String selected;
    String currentUser;//這行
    String  Poster;//這行
    String PcurrentUser;//這行
    private DatabaseReference PosterUser;
    int PosterSelectTime;
    String image;
    String id;




    EditText Comment;
    ImageButton AddComment;
    String CommentText;
    private RecyclerView mCommentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_article_water);

        mUsersDatebase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatebase.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ArticleWater");

        mPost_key = getIntent().getExtras().getString("article_id");
        currentUser=mCurrentUser.getUid();//這行現在使用者
        PcurrentUser=currentUser.toString();//這行

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

        PosterDB=FirebaseDatabase.getInstance().getReference().child("ArticleWater").child(mPost_key);//這行
        PosterDB.addValueEventListener(new ValueEventListener() {////這行
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Poster=dataSnapshot.child("uid").getValue().toString();
                selected=dataSnapshot.child("select").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        PosterUser=FirebaseDatabase.getInstance().getReference().child("Users");
        PosterUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PosterSelectTime=Integer.parseInt(dataSnapshot.child(Poster).child("selecttime").getValue().toString());
                image=dataSnapshot.child(currentUser).child("thumbImage").getValue().toString();
                id=dataSnapshot.child(currentUser).child("id").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//這行

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
                viewHolder.setEmail(model.getEmail());
                viewHolder.setProfile(model.getProfile());
                viewHolder.setDate(model.getDate());

                final String comment_key = getRef(position).getKey();
                final String userId = model.getUid();
                //viewHolder.setDetail(getApplicationContext(), model.getEmail(), model.getProfile());
                //viewHolder.mView.setBackgroundColor(Color.WHITE);


             if (selected.matches("false")) {

                 AddComment.setEnabled(true);
                }
                else {
                 viewHolder.mView.setBackgroundColor(Color.GRAY);
                 AddComment.setEnabled(false);
                 Comment.setText("已有師傅被選擇");
                 Comment.setEnabled(false);

                }
                /*if (mCommentDB.child(comment_key).child("isselect").equals("false")) {
                    viewHolder.mView.setBackgroundColor(Color.WHITE);
                }
                else if (mCommentDB.child(comment_key).child("isselect").equals("true")){
                    viewHolder.mView.setBackgroundColor(Color.GRAY);
                }*/

                if(PcurrentUser.matches(Poster))//這行
                 {
                       viewHolder.mMenu.setVisibility(View.VISIBLE);
                 }
                else
                {
                    viewHolder.mMenu.setVisibility(View.INVISIBLE);
                }

                viewHolder.mMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{"選擇師傅", "取消選擇","檢舉"};
                        //final String[] list = {"選擇師傅", "檢舉"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(SingleArticleWater.this);

                        builder.setTitle("Select options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0) {
                                    if(selected.matches("false")) {
                                        //viewHolder.mView.setBackgroundColor(Color.GRAY);        //點擊選擇師傅後comment改為灰色，isselect設為true
                                        mCommentDB.child(comment_key).child("isselect").setValue("true");
                                        PosterSelectTime = PosterSelectTime + 1;
                                        PosterUser.child(Poster).child("selecttime").setValue(PosterSelectTime);
                                        selected = "true";
                                        PosterDB.child("select").setValue(selected);
                                    }
                                    else
                                    {
                                        Toast toast = Toast.makeText(SingleArticleWater.this, "師傅已被選擇，無法重複選擇", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }

                                if(i == 1) {
                                    if(selected.matches("true")) {
                                        //viewHolder.mView.setBackgroundColor(Color.WHITE);
                                        mCommentDB.child(comment_key).child("isselect").setValue("false");
                                        PosterSelectTime = PosterSelectTime - 1;
                                        PosterUser.child(Poster).child("selecttime").setValue(PosterSelectTime);
                                        selected = "false";
                                        PosterDB.child("select").setValue(selected);
                                    }
                                    else{
                                        Toast toast = Toast.makeText(SingleArticleWater.this, "此師傅尚未被選擇", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }
                                if(i==2)
                                {

                                }
                            }
                        });
                        builder.show();
                    }
                });

               /* viewHolder.mProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(SingleArticleWater.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id",comment_key );
                        startActivity(profileIntent);
                        Toast toast = Toast.makeText(SingleArticleWater.this, "師傅頁面", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });*/

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(SingleArticleWater.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", userId);
                        startActivity(profileIntent);
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
            mProfile = (ImageView) mView.findViewById(R.id.profile);
            mMenu = (ImageButton) mView.findViewById(R.id.menu);
        }

        public void setComment(String comment) {
            TextView post_comment = mView.findViewById(R.id.postComment);
            post_comment.setText(comment);
        }

        public void setEmail(String email) {
            TextView post_email = mView.findViewById(R.id.postemail);
            post_email.setText(email);
        }

        public void setProfile(String profile) {
            ImageView user_profile = mView.findViewById(R.id.profile);
            Picasso.get().load(profile).placeholder(R.drawable.defaultavatar).into(user_profile);
        }

        public  void  setDate(String date){
            TextView post_date = (TextView) mView.findViewById(R.id.date);
            post_date.setText(date);
        }

    }

    /*private void Notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Notification notification = builder.setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("師傅已被選擇")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }*/

    public void addCommentList() {
        final String comment = Comment.getText().toString().trim();
        final DatabaseReference newPost = mCommentDB.push();
        
            if (comment.matches(""))
            {
                Toast toast = Toast.makeText(SingleArticleWater.this, "請輸入回覆", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                newPost.child("Comment").setValue(comment);
                newPost.child("uid").setValue(mCurrentUser.getUid());
                newPost.child("isselect").setValue("false");
                newPost.child("email").setValue(mCurrentUser.getEmail()+"("+id+")");
                newPost.child("profile").setValue(image);
                final String currentDate = DateFormat.getDateInstance().format(new Date());
                newPost.child("date").setValue(currentDate);
                Toast toast = Toast.makeText(SingleArticleWater.this, "回覆成功", Toast.LENGTH_SHORT);
                toast.show();
                CommentText = comment;
                Comment.setText("");
            }
        }
  }
