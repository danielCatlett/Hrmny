package com.daniel_catlett.hrmny;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    private ViewPager viewPager;
    TabsPagerAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up tabs
        ViewPager vpPager = (ViewPager)findViewById(R.id.viewpager);
        myAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(myAdapter);

    }

}
