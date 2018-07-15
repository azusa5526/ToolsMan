package com.example.lai.toolsman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Elec extends AppCompatActivity {

    private RecyclerView mList;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private boolean mProcessLike = false;
    private DatabaseReference mDatabaseLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elec);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Article");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseLike.keepSynced(true);
        mProgress = new ProgressDialog(this);
        mList = (RecyclerView) findViewById(R.id.list);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));


        final Button ToPostElec = findViewById(R.id.PostElec);
        ToPostElec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToPostElec = new Intent();
                ToPostElec.setClass(Elec.this,PostElec.class);
                startActivity(ToPostElec);
            }
        });

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

            FirebaseRecyclerAdapter<Article, ArticleViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Article, ArticleViewHolder>(

                    Article.class,
                    R.layout.article_elec,
                    ArticleViewHolder.class,
                    mDatabase

            ) {
                @Override
                protected void populateViewHolder(ArticleViewHolder viewHolder, Article model, int position) {

                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setUsername(model.getUsername());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(Elec.this, "You Clicked a View", Toast.LENGTH_LONG).show();
                        }
                    });

                    viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mProcessLike = true;

                        }
                    });
                }
            };
            mList.setAdapter(firebaseRecyclerAdapter);

        }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageButton mLikeBtn;
        public ArticleViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mLikeBtn = (ImageButton) mView.findViewById(R.id.LikeBtn);

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



