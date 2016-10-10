package com.chris.spotifytest.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chris.spotifytest.OnSearchFinished;
import com.chris.spotifytest.OnSearchItemSelectedListener;
import com.chris.spotifytest.dataTypes.Album;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;

import java.util.List;

import com.chris.spotifytest.Activities.MainActivity;
import com.chris.spotifytest.ApiClient;
import com.chris.spotifytest.ApiInterface;
import com.chris.spotifytest.R;
import com.chris.spotifytest.adapters.searchTrackResultAdapter;
import com.chris.spotifytest.dataTypes.SearchResult;
import com.chris.spotifytest.dataTypes.Track;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chris on 2016-10-06.
 */

public class SearchTrackFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    private RecyclerView tRecyclerView;
    private static searchTrackResultAdapter tAdapter;
    private static final String CLIENT_ID = "cc42867f9fb24ed699f6ec68af1f448f";
    //    List<TrackInfo> resultsList = new ArrayList<TrackInfo>();
    final String TYPE = "track,artist,album,playlist";
    private Player mPlayer;
    OnSearchItemSelectedListener mSearchClickedListener;


    public static List<Track> trackList;
    FragmentActivity listener;






    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        mSearchClickedListener = (OnSearchItemSelectedListener) getActivity();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_search, parent, false);


    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        tRecyclerView = (RecyclerView) view.findViewById(R.id.rlv);
        // Get the intent, verify the action and get the query
        tAdapter = new searchTrackResultAdapter(trackList, new searchTrackResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Track item) {
                //Toast.makeText(getActivity().getBaseContext(), item.track_name, Toast.LENGTH_SHORT).show();
                final String play = "spotify:track:" +item.track_id;
                final String track_id = item.track_id;
                final String track_name = item.track_name;
                final String artist_name = item.artists.get(0).artist_name;
                final String art_url = item.album.images.get(1).art_url;
                // TODO: play in main activity
                mSearchClickedListener.onSearchTrackItemSelected(track_id, track_name, artist_name, art_url);
            }


        }, new searchTrackResultAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(Track item) {
              //  Toast.makeText(getActivity().getBaseContext(), item.album.album_name, Toast.LENGTH_SHORT).show();
                mSearchClickedListener.onSearchTrackItemLongPressed(item.track_id);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        tRecyclerView.setLayoutManager(mLayoutManager);
        tRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tRecyclerView.setAdapter(tAdapter);




        }

    public static void update(SearchResult result){
        trackList = result.tracks.tracks;

    }




    }



