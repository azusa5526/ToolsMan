package com.example.lai.toolsman;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

public class Verification extends AppCompatActivity {
    ImageView Picture1;
    EditText VerifyName;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Button SendPic= findViewById(R.id.SendPic);
        VerifyName =(EditText)findViewById(R.id.VerifyName);
        ImageView Picture1 = (ImageView)findViewById(R.id.Picture1);
        final Button choose1 = findViewById(R.id.Choose1);
        choose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ChoosePic1 =new Intent();
                ChoosePic1.setType("image/*");
                ChoosePic1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(ChoosePic1,1);

            }
        });
        SendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMail();

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                Picture1 = (ImageView) findViewById(R.id.Picture1);
                Picture1.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception","Error");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void SendMail()
    {

        String[] to={"t12340905@gmail.com"};
        String title = "師傅驗證信";
        String Picture="請附上照片";
        String name = VerifyName.getText().toString();
        Intent Send = new Intent(Intent.ACTION_SEND);
        Send.putExtra(Intent.EXTRA_EMAIL,to);
        Send.putExtra(Intent.EXTRA_SUBJECT,"帳號:  "+name+"   的"+title);
        Send.putExtra(Intent.EXTRA_TEXT,Picture);

        Send.setType("text/plain");
        startActivity(Send.createChooser(Send,"選擇工具"));

    }


}
