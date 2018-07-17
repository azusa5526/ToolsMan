package com.example.lai.toolsman;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.AuthResult;
import java.util.HashMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class Register extends AppCompatActivity {
    private DatabaseReference Database;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        Button Submit = findViewById(R.id.Submit);
        final EditText EmailRegister =(EditText) findViewById(R.id.EmailRegister);
        final EditText PasswordRegister =(EditText)findViewById(R.id.PasswordRegister);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String Email = EmailRegister.getText().toString();
               String Password = PasswordRegister.getText().toString();
               auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           Toast.makeText(Register.this, "帳戶建立成功", Toast.LENGTH_SHORT).show();
                           Intent BackToLogin = new Intent();
                           BackToLogin.setClass(Register.this,Login.class);
                           startActivity(BackToLogin);
                       } else {
                           Toast.makeText(Register.this, "帳戶建立失敗"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }
        });

    }




}
