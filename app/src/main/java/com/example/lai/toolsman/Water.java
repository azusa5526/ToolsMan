package com.example.lai.toolsman;

import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Water extends AppCompatActivity {

    private RecyclerView mList;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ArticleWater");
        mProgress = new ProgressDialog(this);
        mList = (RecyclerView) findViewById(R.id.list);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));

        final Button ToPostWater = findViewById(R.id.PostWater);
        ToPostWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToPostWater = new Intent();
                ToPostWater.setClass(Water.this,PostWater.class);
                startActivity(ToPostWater);
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
            startActivity(new Intent(Water.this, PostWater.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ArticleWater, ArticleViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ArticleWater, ArticleViewHolder>(

                ArticleWater.class,
                R.layout.articlewater,
                ArticleViewHolder.class,
                mDatabase


        ) {
            @Override
            protected void populateViewHolder(ArticleViewHolder viewHolder, ArticleWater model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());

            }
        };

        mList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setTitle(String title){

            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public  void  setDesc(String desc){

            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);

        }

    }


}
