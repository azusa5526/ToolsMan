package com.example.lai.toolsman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
    EditText InsertF ;
    Button AddFavorite;
    ListView FavoriteList;
    String AccountName;
    String Email;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Intent getName = getIntent();
        String name = getName.getStringExtra("AccountName");
        AccountName=name;
        InsertF = findViewById(R.id.InsertF);
        AddFavorite =findViewById(R.id.AddFavorite);
        FavoriteList= (ListView) findViewById(R.id.FavoriteList);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = mCurrentUser.getUid();
        mDataBase= FirebaseDatabase.getInstance().getReference().child("Favorite").child(currentUid);

        AddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

    }

    public void add()
    {
        final String FavoriteEmail = InsertF.getText().toString().trim();

        final DatabaseReference newPost = mDataBase.push();
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newPost.child("Email").setValue(FavoriteEmail);
                Toast toast = Toast.makeText(Favorite.this, "添加完成", Toast.LENGTH_LONG);
                toast.show();
                Email=FavoriteEmail;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = Toast.makeText(Favorite.this, "添加失敗", Toast.LENGTH_LONG);
                toast.show();

            }
        });
    }

}

