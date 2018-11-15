package com.example.lai.toolsman.Favorite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lai.toolsman.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Favorite extends AppCompatActivity {
    private DatabaseReference mDataBase;
    private  DatabaseReference AccountDataBase;
    EditText InsertForFavorite ;
    Button AddFavorite;
    private RecyclerView mFavoriteRecyclerView;
    ListView FavoriteList;
    String AccountNameForFavorite;
    String EmailForFavorite;
    private FirebaseUser mCurrentUserForFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Intent getName = getIntent();
        String nameForFavorite = getName.getStringExtra("AccountName");
        AccountNameForFavorite=nameForFavorite;
        InsertForFavorite = findViewById(R.id.Insert);
        AddFavorite =findViewById(R.id.AddFavorite);

        mFavoriteRecyclerView= findViewById(R.id.Favorite);
        mFavoriteRecyclerView.setHasFixedSize(true);
        mFavoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCurrentUserForFavorite = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = mCurrentUserForFavorite.getUid();
        mDataBase= FirebaseDatabase.getInstance().getReference().child("Favorite").child(currentUid);

        AddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorite();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<SingleFavorite, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SingleFavorite, BlogViewHolder>(

                SingleFavorite.class,
                R.layout.single_blacklist,
                BlogViewHolder.class,
                mDataBase
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, SingleFavorite model, int position) {

                viewHolder.setEmail(model.getEmail());
            }
        };
        mFavoriteRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static  class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setEmail(String email) {

            TextView post_email = mView.findViewById(R.id.postemail);
            post_email.setText(email);
        }

    }

    public void addFavorite()
    {
        final String FavoriteEmail = InsertForFavorite.getText().toString().trim();

        final DatabaseReference newPost = mDataBase.push();
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newPost.child("Email").setValue(FavoriteEmail);
                Toast toast = Toast.makeText(Favorite.this, "添加完成", Toast.LENGTH_LONG);
                toast.show();
                EmailForFavorite=FavoriteEmail;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = Toast.makeText(Favorite.this, "添加失敗", Toast.LENGTH_LONG);
                toast.show();

            }
        });
    }

}

