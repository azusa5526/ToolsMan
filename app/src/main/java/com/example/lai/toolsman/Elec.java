package com.example.lai.toolsman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Elec extends AppCompatActivity {

    private RecyclerView mList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser;
    private boolean mProcessLike = false;
    private DatabaseReference mDatabaseLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elec);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ArticleElec");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("UsersElec");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("LikesElec");
        mDatabaseUser.keepSynced(true);
        mDatabase.keepSynced(true);
        mDatabaseLike.keepSynced(true);

        mList = findViewById(R.id.list);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add){
            startActivity(new Intent(Elec.this, PostElec.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
   protected void onStart() {
        super.onStart();
            final FirebaseRecyclerAdapter<Article, ArticleViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Article, ArticleViewHolder>(
                    Article.class,
                    R.layout.article_elec,
                    ArticleViewHolder.class,
                    mDatabase
            ) {
                @Override
                protected void populateViewHolder(ArticleViewHolder viewHolder, Article model, int position) {
                    final String post_key = getRef(position).getKey();
                    viewHolder.setLikeBtn(post_key);
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setUsername(model.getUsername());
                    //User data will be retrieved here...

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent singleArticleIntent = new Intent(Elec.this, SingleArticleElec.class);
                            singleArticleIntent.putExtra("article_id", post_key);
                            startActivity(singleArticleIntent);
                        }
                    });
                    //Like Feature
                    viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mProcessLike = true;

                                mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (mProcessLike) {
                                            if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                                                mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                                mProcessLike = false;

                                            } else {
                                                mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("Random");
                                                mProcessLike = false;

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                        }
                    });
                }
            };
            mList.setAdapter(firebaseRecyclerAdapter);
        }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageButton mLikeBtn;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;
        public ArticleViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mLikeBtn =  mView.findViewById(R.id.LikeBtn);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();
            mDatabaseLike.keepSynced(true);
        }

        public void setLikeBtn(final String post_key){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                        mLikeBtn.setImageResource(R.mipmap.action_like_blue);

                    } else {
                        mLikeBtn.setImageResource(R.mipmap.action_like_gray);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public  void  setDesc(String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public  void setUsername(String username){
            TextView post_username = (TextView) mView.findViewById(R.id.username);
            post_username.setText(username);
        }

    }
}



