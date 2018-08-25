package com.example.lai.toolsman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class PostAir extends AppCompatActivity {

    private Toolbar PostAirBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_air);

        PostAirBar = findViewById(R.id.post_air_bar);
        setSupportActionBar(PostAirBar);
        getSupportActionBar().setTitle("Add New Post");
    }
}
