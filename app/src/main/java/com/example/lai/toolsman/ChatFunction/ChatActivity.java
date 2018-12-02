package com.example.lai.toolsman.ChatFunction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.lai.toolsman.R;

public class ChatActivity extends AppCompatActivity {

    private String mChatUser;
    private Toolbar mChatToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        mChatToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChatUser = getIntent().getStringExtra("user_id");
        getSupportActionBar().setTitle(mChatUser);

    }
}
