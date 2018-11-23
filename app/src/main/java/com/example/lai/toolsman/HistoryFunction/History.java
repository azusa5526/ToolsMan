package com.example.lai.toolsman.HistoryFunction;

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

public class History extends AppCompatActivity {
    private DatabaseReference mhistoryDatabase;
    private RecyclerView mHistoryRecycleView;
    String AccountName;
    private FirebaseUser mCurrentUserForHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mHistoryRecycleView = findViewById(R.id.HistoryRecyclerView);
        mHistoryRecycleView.setHasFixedSize(true);
        mHistoryRecycleView.setLayoutManager(new LinearLayoutManager(this));



        mCurrentUserForHistory = FirebaseAuth.getInstance().getCurrentUser();
        String HistoryUid = mCurrentUserForHistory.getUid();
        mhistoryDatabase = FirebaseDatabase.getInstance().getReference().child("History").child(HistoryUid);
    }

    /*protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter(mhistoryDatabase) {
            @Override
            protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, Object model, int position) {

            }
        }

    }*/
}
