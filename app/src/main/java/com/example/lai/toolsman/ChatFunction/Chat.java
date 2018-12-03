package com.example.lai.toolsman.ChatFunction;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;

import com.example.lai.toolsman.R;


public class Chat extends AppCompatActivity {

    private ViewPager mViewPager;
    public SectionsPagerAdapter mSectionsPagerAdapter;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("ToolsMan");

        //Tabs
        mViewPager = (ViewPager) findViewById(R.id.chatPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.chatTabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
