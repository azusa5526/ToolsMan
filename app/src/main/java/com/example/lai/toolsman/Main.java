package com.example.lai.toolsman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseAuth;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button Favorite = (Button)findViewById(R.id.Favorite);
        Button BlackList = (Button)findViewById(R.id.BlackList);
        Button Chat = (Button)findViewById(R.id.Chat);
        Button History = (Button)findViewById(R.id.History);
        final Button Setting = (Button)findViewById(R.id.Setting);
        final Button Water = findViewById(R.id.Water);
        Button Elec = findViewById(R.id.Elec);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToFavorite = new Intent();
                ToFavorite.setClass(Main.this, com.example.lai.toolsman.Favorite.class);
                startActivity(ToFavorite);
            }
        });

        BlackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToBlackList = new Intent();
                ToBlackList.setClass(Main.this, com.example.lai.toolsman.BlackList.class);
                startActivity(ToBlackList);
            }
        });
        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToChat = new Intent();
                ToChat.setClass(Main.this, com.example.lai.toolsman.Chat.class);
                startActivity(ToChat);
            }
        });
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToHistory = new Intent();
                ToHistory.setClass(Main.this, com.example.lai.toolsman.History.class);
                startActivity(ToHistory);

            }
        });
        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToSetting = new Intent();
                ToSetting.setClass(Main.this, com.example.lai.toolsman.Setting.class);
                startActivity(ToSetting);
            }
        });

      Water.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent Towater = new Intent();
              Towater.setClass(Main.this, com.example.lai.toolsman.Water.class);
              startActivity(Towater);
          }
      });

        Elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Toelec = new Intent();
                Toelec.setClass(Main.this, com.example.lai.toolsman.Elec.class);
                startActivity(Toelec);
            }
        });


    }
}
