package com.example.lai.toolsman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SingleArticleElec extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_article_elec);

        String post_key = getIntent().getExtras().getString("article_id");
        Toast.makeText(SingleArticleElec.this, post_key, Toast.LENGTH_LONG).show();
    }
}
