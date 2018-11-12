package com.example.lai.toolsman.LoginFunction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lai.toolsman.Main;
import com.example.lai.toolsman.R;
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
        //getSupportActionBar().hide();

        final Button login = (Button) findViewById(R.id.Login);
        Button Register = (Button)findViewById(R.id.Register);
        final EditText Email = findViewById(R.id.Email);
        final EditText Password = findViewById(R.id.Password);

        login.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(Email.getText().toString().matches("")||Password.getText().toString().matches(""))
             {
                 Toast toast = Toast.makeText(Login.this, "帳號及密碼請勿空白", Toast.LENGTH_LONG);
                 toast.show();
             }
             else{

             auth.signInWithEmailAndPassword(Email.getText().toString(),Password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                 @Override
                 public void onSuccess(AuthResult authResult) {
                     Intent MainIntent = new Intent(Login.this,Main.class);
                     String AccountName = Email.getText().toString();
                     MainIntent.putExtra("AccountName",AccountName);


                     MainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  //設定切換應用程式畫面後不會登出系統
                     startActivity(MainIntent);
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
     }});

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToRegister = new Intent();
                ToRegister.setClass(Login.this, com.example.lai.toolsman.RegisterFunction.Register.class);
                startActivity(ToRegister);
            }
        });
    }



}
