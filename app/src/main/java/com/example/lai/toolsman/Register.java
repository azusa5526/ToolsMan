package com.example.lai.toolsman;

import android.content.Intent;

import android.provider.ContactsContract;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class Register extends AppCompatActivity {
    private DatabaseReference mDatabase;
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
               final String Email = EmailRegister.getText().toString();
               String Password = PasswordRegister.getText().toString();
               if(Email.matches("")||Password.matches(""))
               {
                   Toast.makeText(Register.this, "註冊資料請勿空白", Toast.LENGTH_SHORT).show();


               }
               else{
               auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {

                           FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                           String uid = currentUser.getUid();
                           mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                           HashMap<String, String> userMap = new HashMap<>();
                           userMap.put("email", Email);
                           userMap.put("image", "default");
                           userMap.put("thumbImage", "default");
                           userMap.put("status", "Hi, I'm a General member in ToolsMan");

                           mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()) {
                                       Toast.makeText(Register.this, "帳戶建立成功", Toast.LENGTH_SHORT).show();

                                       Intent loginIntent = new Intent(Register.this, Login.class);
                                       loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       startActivity(loginIntent);
                                       finish();
                                   }
                               }
                           });
                       } else {
                           Toast.makeText(Register.this, "帳戶建立失敗"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }
        }});
    }
}
