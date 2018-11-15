package com.example.lai.toolsman.BlackList;

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

import com.example.lai.toolsman.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BlackList extends AppCompatActivity {
    private  DatabaseReference mBlackDataBase;
    private  DatabaseReference AccountDataBase;
    EditText InsertEmailForBlackList ;
    Button AddBlackList;
    private RecyclerView mBlackListRecyclerView;
    //ListView BlackList;
    String AccountNameForBlackList;
    String EmailForBlackList;
    private FirebaseUser mCurrentUserForBlackList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        Intent getName = getIntent();
        String nameForBlaclName = getName.getStringExtra("AccountName");
        AccountNameForBlackList = nameForBlaclName;
        InsertEmailForBlackList = findViewById(R.id.Insert);
         AddBlackList =findViewById(R.id.AddBlackList);

         mBlackListRecyclerView= findViewById(R.id.BlackList);
         mBlackListRecyclerView.setHasFixedSize(true);
         mBlackListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCurrentUserForBlackList = FirebaseAuth.getInstance().getCurrentUser();
         String currentUid = mCurrentUserForBlackList.getUid();
         mBlackDataBase=FirebaseDatabase.getInstance().getReference().child("BlackList").child(currentUid);

         AddBlackList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addBlackList();
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
                mBlackDataBase
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, SingleBlackList model, int position) {

                viewHolder.setEmail(model.getEmail());
            }
        };
        mBlackListRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

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

        public void addBlackList()
        {
        final String BlackEmail = InsertEmailForBlackList.getText().toString().trim();

        final DatabaseReference newPost = mBlackDataBase.push();
            mBlackDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newPost.child("Email").setValue(BlackEmail);
                Toast toast = Toast.makeText(BlackList.this, "添加完成", Toast.LENGTH_LONG);
                toast.show();
                EmailForBlackList=BlackEmail;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = Toast.makeText(BlackList.this, "添加失敗", Toast.LENGTH_LONG);
                toast.show();

            }
        });
    }

}
