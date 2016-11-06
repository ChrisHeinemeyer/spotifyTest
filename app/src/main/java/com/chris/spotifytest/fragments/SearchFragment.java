package com.chris.spotifytest.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chris.spotifytest.OnFragmentChange;
import com.chris.spotifytest.adapters.SearchPagerAdapter;
import com.spotify.sdk.android.player.Config;

import com.chris.spotifytest.Activities.MainActivity;
import com.chris.spotifytest.ApiClient;
import com.chris.spotifytest.ApiInterface;
import com.chris.spotifytest.R;
import com.chris.spotifytest.dataTypes.spotify.SearchResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chris on 2016-10-06.
 */

public class SearchFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,

    private static final String CLIENT_ID = "cc42867f9fb24ed699f6ec68af1f448f";
    final String TYPE = "track,artist,album,playlist";
    String TAG = this.getTag();
    OnFragmentChange fragListener;

    FragmentActivity listener;
    ViewPager pager;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        fragListener = (OnFragmentChange) getActivity();
        fragListener.appBarElevationNeeded(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View result =  inflater.inflate(R.layout.pager, parent, false);
        pager = (ViewPager)result.findViewById(R.id.pager);
        TabLayout tabs = (TabLayout) result.findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);



        return(result);

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        String searchString = MainActivity.getEdtSearch().getText().toString();

        String token = MainActivity.getAccessToken();
        final  Config playerConfig = new Config(getActivity().getApplicationContext(), token, CLIENT_ID);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SearchResult> call = apiService.getSearchResult(searchString, TYPE);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult>call, Response<SearchResult> response) {
                SearchResult result = response.body();
               //mSearchFinishedListener.onSearchFinishedListener(result);
                SearchAlbumFragment.update(result);
                SearchTrackFragment.update(result);
                pager.setAdapter(buildAdapter());

            }

            @Override
            public void onFailure(Call<SearchResult>call, Throwable t) {
                Log.d("a","faileda");
            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        fragListener.appBarElevationNeeded(false);
        fragListener.appBarNeeded(true);

    }

    private PagerAdapter buildAdapter() {
        return(new SearchPagerAdapter(getActivity(), getChildFragmentManager()));
    }


}
