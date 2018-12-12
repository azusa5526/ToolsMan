package com.example.lai.toolsman.Post;

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

import com.example.lai.toolsman.BlackList.BlackList;
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

public class SingleArticleWater extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference mDatabase;
    private DatabaseReference mCommentDB;

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
            protected void populateViewHolder(BlogViewHolder viewHolder, SingleCommentWater model, int position) {
                viewHolder.setComment(model.getComment());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setComment(String comment) {
            TextView post_comment = mView.findViewById(R.id.postComment);
            post_comment.setText(comment);
        }

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
