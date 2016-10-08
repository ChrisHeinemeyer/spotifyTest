package com.chris.spotifytest.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.chris.spotifytest.ApiClient;
import com.chris.spotifytest.ApiInterface;
import com.chris.spotifytest.R;
import com.chris.spotifytest.dataTypes.SearchResult;
import com.chris.spotifytest.dataTypes.Track;
import com.chris.spotifytest.dataTypes.TrackInfo;
import com.chris.spotifytest.adapters.searchTrackResultAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * Created by Chris on 2016-09-11.
 */
public class SearchableActivity extends AppCompatActivity {
    private RecyclerView tRecyclerView;
    private searchTrackResultAdapter tAdapter;
    private static final String CLIENT_ID = "cc42867f9fb24ed699f6ec68af1f448f";
//    List<TrackInfo> resultsList = new ArrayList<TrackInfo>();
    final String TYPE = "track,artist,album,playlist";
    private Player mPlayer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        tRecyclerView = (RecyclerView) findViewById(R.id.rlv);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get the intent, verify the action and get the query
        Bundle extras = getIntent().getExtras();
        String searchString = extras.getString("searchString");

        String token = extras.getParcelable("authResponse");
       final  Config playerConfig = new Config(this, token, CLIENT_ID);






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
                        Toast.makeText(getBaseContext(), item.track_name, Toast.LENGTH_SHORT).show();
                        final String play = "spotify:track:" +item.track_id;
//                        mPlayer.play(play);
                        Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                                    @Override
                                    public void onInitialized(Player player) {
                                        mPlayer = player;
                                        mPlayer.play(play);
                                        //mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
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
                        Toast.makeText(getBaseContext(), item.album.album_name, Toast.LENGTH_SHORT).show();
                    }
                });
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




}


