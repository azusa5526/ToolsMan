package com.example.lai.toolsman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BlackList extends AppCompatActivity {
    private  DatabaseReference mDataBase;
    private  DatabaseReference AccountDataBase;
    EditText Insert ;
    Button AddBlackList;
    private RecyclerView mBlackList;
    //ListView BlackList;
    String AccountName;
    String Email;
    private FirebaseUser mCurrentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        Intent getName = getIntent();
        String name = getName.getStringExtra("AccountName");
        AccountName=name;
        Insert = findViewById(R.id.Insert);
         AddBlackList =findViewById(R.id.AddBlackList);

         mBlackList= findViewById(R.id.BlackList);
         mBlackList.setHasFixedSize(true);
         mBlackList.setLayoutManager(new LinearLayoutManager(this));

         mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
         String currentUid = mCurrentUser.getUid();
         mDataBase=FirebaseDatabase.getInstance().getReference().child("BlackList").child(currentUid);

         AddBlackList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add();
                }
            });

        }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<SingleBlackList, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SingleBlackList, BlogViewHolder>(

                SingleBlackList.class,
                R.layout.single_blacklist,
                BlogViewHolder.class,
                mDataBase
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, SingleBlackList model, int position) {

                viewHolder.setEmail(model.getEmail());
            }
        };
        mBlackList.setAdapter(firebaseRecyclerAdapter);
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

        public void add()
        {
        final String BlackEmail = Insert.getText().toString().trim();

        final DatabaseReference newPost = mDataBase.push();
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newPost.child("Email").setValue(BlackEmail);
                Toast toast = Toast.makeText(BlackList.this, "添加完成", Toast.LENGTH_LONG);
                toast.show();
                Email=BlackEmail;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = Toast.makeText(BlackList.this, "添加失敗", Toast.LENGTH_LONG);
                toast.show();

            }
        });
    }

}
