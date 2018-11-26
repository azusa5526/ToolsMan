package com.example.lai.toolsman.HistoryFunction;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lai.toolsman.Post.Elec;
import com.example.lai.toolsman.Post.SingleArticleAir;
import com.example.lai.toolsman.Post.SingleArticleElec;
import com.example.lai.toolsman.Post.SingleArticleWater;
import com.example.lai.toolsman.Post.SingleCommentAir;
import com.example.lai.toolsman.Post.SingleCommentElec;
import com.example.lai.toolsman.Post.SingleCommentWater;
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

public class History extends AppCompatActivity {
    private Toolbar toolbar;
    private DatabaseReference mhistoryDatabase;
    private RecyclerView mHistoryRecycleView;
    private FirebaseAuth mAuth;
    private DatabaseReference mhistoryDatabaseUser;
    String AccountName;
    private FirebaseUser mCurrentUserForHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent GetName = getIntent();
        AccountName = GetName.getStringExtra("AccountName");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserForHistory = FirebaseAuth.getInstance().getCurrentUser();
        String HistoryUid = mCurrentUserForHistory.getUid();
        mhistoryDatabase = FirebaseDatabase.getInstance().getReference().child("History").child(HistoryUid);;
        //mhistoryDatabaseUser = FirebaseDatabase.getInstance().getReference().child("UsersElec");
        mhistoryDatabase.keepSynced(true);

        mHistoryRecycleView = findViewById(R.id.HistoryRecyclerView);
        mHistoryRecycleView.setHasFixedSize(true);
        mHistoryRecycleView.setLayoutManager(new LinearLayoutManager(this));

    }

    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ArticleHistory, History.ArticleViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ArticleHistory, History.ArticleViewHolder>(
                ArticleHistory.class,
                R.layout.article_history,
                ArticleViewHolder.class,
                mhistoryDatabase
        ) {
            @Override
            protected void populateViewHolder(ArticleViewHolder viewHolder, ArticleHistory model, int position) {
                final String post_key = getRef(position).getKey();
                final String typeForClick;

                viewHolder.setType(model.getType());
                typeForClick = model.getType();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(typeForClick.equals("水務類")){
                            Intent historyArticle = new Intent(History.this, SingleArticleWater.class);
                            historyArticle.putExtra("article_id", post_key);
                            startActivity(historyArticle);
                        }else if (typeForClick.equals("電器類")){
                            Intent historyArticle = new Intent(History.this, SingleArticleElec.class);
                            historyArticle.putExtra("article_id", post_key);
                            startActivity(historyArticle);
                        }else if(typeForClick.equals("冷氣類")){
                            Intent historyArticle = new Intent(History.this, SingleArticleAir.class);
                            historyArticle.putExtra("article_id", post_key);
                            startActivity(historyArticle);
                        }
                    }
                });
            }
        };
        mHistoryRecycleView.setAdapter(firebaseRecyclerAdapter);



    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder{

        View mView;
        FirebaseAuth mAuth;
        //String type;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


            mAuth = FirebaseAuth.getInstance();

        }

        public void setType(String type){
            TextView post_type = (TextView) mView.findViewById(R.id.post_type);
            post_type.setText(type);
        }

        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public  void  setDesc(String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image) {
            ImageView post_image = mView.findViewById(R.id.post_image);
            Picasso.get().load(image).into(post_image);
        }

        public  void setUsername(String username){
            TextView post_username = (TextView) mView.findViewById(R.id.username);
            post_username.setText(username);
        }

    }
}
