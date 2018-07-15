package com.example.lai.toolsman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button Verification = findViewById(R.id.Verification);
        Verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GoVerification = new Intent();
                GoVerification.setClass(Setting.this, com.example.lai.toolsman.Verification.class);
                startActivity(GoVerification);
            }
        });
    }
}
