package com.example.lai.toolsman;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.lai.toolsman.BlackList.BlackList;
import com.example.lai.toolsman.ChatFunction.Chat;
import com.example.lai.toolsman.Favorite.Favorite;
import com.example.lai.toolsman.HistoryFunction.History;
import com.example.lai.toolsman.LoginFunction.Login;
import com.example.lai.toolsman.Post.Air;
import com.example.lai.toolsman.Post.Elec;
import com.example.lai.toolsman.Post.Water;
import com.example.lai.toolsman.SearchUser.SearchUser;
import com.example.lai.toolsman.UserInfo.AccountSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    String RealName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Intent GetName = getIntent();
        String AccountName = GetName.getStringExtra("AccountName");
        RealName = AccountName;

        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button GoWater = findViewById(R.id.button4);
        Button GoElec = findViewById(R.id.button5);
        Button GoAir = findViewById(R.id.Air);
        GoAir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GoAir = new Intent();
                GoAir.setClass(Main.this, Air.class);
                GoAir.putExtra("AccountName", RealName);
                startActivity(GoAir);
            }
        });

        GoWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GoWater = new Intent();
                GoWater.setClass(Main.this, Water.class);
                GoWater.putExtra("AccountName", RealName);
                startActivity(GoWater);
            }
        });
        GoElec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GoElec = new Intent();
                GoElec.setClass(Main.this, Elec.class);
                GoElec.putExtra("AccountName", RealName);
                startActivity(GoElec);

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot != null) {
                    mUserRef.child("online").onDisconnect().setValue(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void onStart() {

        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null) {

        } else {
            mUserRef.child("online").setValue(true);
        }
    }

    public void onStop() {

        super.onStop();
        mUserRef.child("online").setValue(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        TextView Account = findViewById(R.id.AccountName);
        Account.setText(RealName);
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Favorite) {
            Intent GoFavorite = new Intent(Main.this, Favorite.class);
            GoFavorite.putExtra("AccountName", RealName);

            startActivity(GoFavorite);
            return true;
        } else if (id == R.id.BlackList) {
            Intent GoBlacklist = new Intent(Main.this, BlackList.class);
            GoBlacklist.putExtra("AccountName", RealName);

            startActivity(GoBlacklist);
            return true;

        } else if (id == R.id.Chat) {
            Intent GoChat = new Intent();
            GoChat.setClass(Main.this, Chat.class);
            startActivity(GoChat);
            return true;

        } else if (id == R.id.SearchUser) {
            Intent GoSearchUser = new Intent();
            GoSearchUser.setClass(Main.this, SearchUser.class);
            startActivity(GoSearchUser);
            return true;

        } else if (id == R.id.History) {
            Intent GoHistory = new Intent();
            GoHistory.setClass(Main.this, History.class);
            startActivity(GoHistory);
            return true;

        } else if (id == R.id.AccountSetting) {
            Intent AccountSettingsIntent = new Intent(Main.this, AccountSettings.class);
            startActivity(AccountSettingsIntent);
            finish();
        } else if (id == R.id.Verification) {
            String[] to = {"t12340905@gmail.com"};
            String title = "師傅驗證信";
            String Picture = "請於此附上證件照片";
            String name = RealName;
            Intent Send = new Intent(Intent.ACTION_SEND);
            Send.putExtra(Intent.EXTRA_EMAIL, to);
            Send.putExtra(Intent.EXTRA_SUBJECT, name + "   的" + title);
            Send.putExtra(Intent.EXTRA_TEXT, Picture);

            Send.setType("text/plain");
            startActivity(Send.createChooser(Send, "選擇工具"));
        } else if (id == R.id.Logout) {
            mAuth.signOut();
            Intent loginIntent = new Intent(Main.this, Login.class);
            startActivity(loginIntent);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

}
