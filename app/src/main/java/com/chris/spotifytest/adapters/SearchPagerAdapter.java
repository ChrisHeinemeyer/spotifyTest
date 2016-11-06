package com.chris.spotifytest.adapters;

/**
 * Created by Chris on 2016-10-08.
 */



import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chris.spotifytest.fragments.LocalSearchFragment;
import com.chris.spotifytest.fragments.SearchAlbumFragment;
import com.chris.spotifytest.fragments.SearchFragment;
import com.chris.spotifytest.fragments.SearchTrackFragment;

public class SearchPagerAdapter extends FragmentPagerAdapter {
    Context ctxt=null;

    public SearchPagerAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt=ctxt;
    }

    @Override
    public int getCount() {
        return(3);
    }

    @Override
    public Fragment getItem(int position) {
       switch(position){
           case 0: return new SearchTrackFragment();
           case 1: return new SearchAlbumFragment();
           default: return new LocalSearchFragment();
       }
    }

    @Override
    public String getPageTitle(int position) {
        switch(position){
            case 0: return "Tracks";
            case 1: return "Albums";
            default: return "Local";
        }
    }
}