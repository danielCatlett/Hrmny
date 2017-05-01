package com.daniel_catlett.hrmny;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by daniel on 4/23/2017.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter
{
    private int NUM_ITEMS = 4;
    private String[] titles= new String[]{"Artists", "Albums", "Songs", "Genres"};

    public TabsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    // Returns total number of pages
    @Override
    public int getCount()
    {
        return  NUM_ITEMS ;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new ArtistsFragment();
            case 1:
                return new AlbumsFragment();
            case 2:
                return new SongsFragment();
            case 3:
                return new GenresFragment();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position)
    {
        return  titles[position];
    }

}
