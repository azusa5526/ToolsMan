package com.example.lai.toolsman;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

public class MainDrawer extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                int id = item.getItemId();
                if (id == R.id.Favorite) {
                    Intent GoFavorite = new Intent();
                    GoFavorite.setClass(MainDrawer.this,Favorite.class);
                    startActivity(GoFavorite);
                } else if (id == R.id.BlackList) {
                    Intent GoBlacklist= new Intent();
                    GoBlacklist.setClass(MainDrawer.this,BlackList.class);
                    startActivity(GoBlacklist);
                    return true;

                } else if (id == R.id.Chat) {
                    Intent GoChat= new Intent();
                    GoChat.setClass(MainDrawer.this,Chat.class);
                    startActivity(GoChat);
                    return true;

                } else if (id == R.id.History) {
                    Intent GoHistory= new Intent();
                    GoHistory.setClass(MainDrawer.this,History.class);
                    startActivity(GoHistory);
                    return true;

                } else if (id == R.id.Setting) {
                    Intent GoSetting= new Intent();
                    GoSetting.setClass(MainDrawer.this,Setting.class);
                    startActivity(GoSetting);
                    return true;

                }
                return false;
            }

        });






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


}