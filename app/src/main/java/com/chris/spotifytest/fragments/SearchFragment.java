package com.chris.spotifytest.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class SearchFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    private RecyclerView tRecyclerView;
    private searchTrackResultAdapter tAdapter;
    private static final String CLIENT_ID = "cc42867f9fb24ed699f6ec68af1f448f";
    //    List<TrackInfo> resultsList = new ArrayList<TrackInfo>();
    final String TYPE = "track,artist,album,playlist";
    private Player mPlayer;
    OnSearchItemSelectedListener mListener;



    public interface OnSearchItemSelectedListener{
        public void onSearchItemSelected(String id, String track_name, String artist_name, String art_url);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
            mListener = (OnSearchItemSelectedListener) a;
        }


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


        String searchString = MainActivity.getEdtSearch().getText().toString();

        String token = MainActivity.getAccessToken();
        final  Config playerConfig = new Config(getActivity().getApplicationContext(), token, CLIENT_ID);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SearchResult> call = apiService.getSearchResult(searchString, TYPE);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult>call, Response<SearchResult> response) {
                SearchResult result = response.body();
                List<Track> trackList = result.tracks.tracks;
                tAdapter = new searchTrackResultAdapter(trackList, new searchTrackResultAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Track item) {
                        Toast.makeText(getActivity().getBaseContext(), item.track_name, Toast.LENGTH_SHORT).show();
                        final String play = "spotify:track:" +item.track_id;
                        final String track_id = item.track_id;
                        final String track_name = item.track_name;
                        final String artist_name = item.artists.get(0).artist_name;
                        final String art_url = item.album.images.get(1).art_url;

                        Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                            @Override
                            public void onInitialized(Player player) {
                                mPlayer = player;
                                mPlayer.play(play);
                                mListener.onSearchItemSelected(track_id, track_name, artist_name, art_url);

                            }
                            @Override
                            public void onError(Throwable throwable) {
                                Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                            }

                        });

                    }


                }, new searchTrackResultAdapter.OnLongItemClickListener() {
                    @Override
                    public void onLongItemClick(Track item) {
                        Toast.makeText(getActivity().getBaseContext(), item.album.album_name, Toast.LENGTH_SHORT).show();
                    }
                });
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                tRecyclerView.setLayoutManager(mLayoutManager);
                tRecyclerView.setItemAnimator(new DefaultItemAnimator());
                tRecyclerView.setAdapter(tAdapter);
            }

            @Override
            public void onFailure(Call<SearchResult>call, Throwable t) {
                Log.d("a","faileda");
            }
        });

    }


}
