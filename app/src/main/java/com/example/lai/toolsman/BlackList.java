package com.example.lai.toolsman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class BlackList extends AppCompatActivity {
    private  DatabaseReference mDataBase;
    private  DatabaseReference AccountDataBase;
    EditText Insert ;
    Button AddBlackList;
    private RecyclerView BlackList;
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

         BlackList= findViewById(R.id.BlackList);
         BlackList.setHasFixedSize(true);
         BlackList.setLayoutManager(new LinearLayoutManager(this));

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
