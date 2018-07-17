package com.example.lai.toolsman;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseAuth.AuthStateListener AuthStateListener= new FirebaseAuth.AuthStateListener() {


        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser User = firebaseAuth.getCurrentUser();
            if(User==null)
            {

                Intent go = new Intent();
                go.setClass(Login.this,Login.class);
                startActivity(go);
            }
            else
            {
                //Do after Login
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button login = (Button) findViewById(R.id.Login);
        Button Register = (Button)findViewById(R.id.Register);
        final EditText Email = findViewById(R.id.Email);
        final EditText Password = findViewById(R.id.Password);

        login.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             auth.signInWithEmailAndPassword(Email.getText().toString(),Password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                 @Override
                 public void onSuccess(AuthResult authResult) {
                     Intent Success = new Intent();
                     Success.setClass(Login.this,Main.class);
                     startActivity(Success);
                     finish();
                 }

             })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             login.setError("登入失敗，請檢查");
                         }
                     });
         }
     });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToRegister = new Intent();
                ToRegister.setClass(Login.this, com.example.lai.toolsman.Register.class);
                startActivity(ToRegister);
            }
        });
    }



}
