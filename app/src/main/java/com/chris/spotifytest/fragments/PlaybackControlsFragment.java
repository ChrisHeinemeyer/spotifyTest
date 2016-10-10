package com.chris.spotifytest.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chris.spotifytest.ApiClient;
import com.chris.spotifytest.ApiInterface;
import com.chris.spotifytest.OnPlaybackControlButtonPressed;
import com.chris.spotifytest.R;
import com.chris.spotifytest.dataTypes.AlbumDetailed;
import com.chris.spotifytest.dataTypes.Track;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.chris.spotifytest.R.id.album_art_image;


/**
 * Created by Chris on 2016-10-06.
 */

public class PlaybackControlsFragment extends Fragment implements View.OnClickListener {
    static final String TAG = "ControlsFragment";
    private Track nowPlayingTrack;
    private static ImageView nowPlayingImage;
    private static ImageView pausePlay;
    private static TextView nowPlayingTitle;
    private static TextView nowPlayingArtist;
    private static ImageView nextTrack;

    public static String track_id;
    OnPlaybackControlButtonPressed mListener;




    public void onAttach(Context context){
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
            mListener = (OnPlaybackControlButtonPressed) a;
        }


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_playback_controls, parent, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        nowPlayingImage = (ImageView) getView().findViewById(album_art_image);
        nowPlayingTitle = (TextView) getView().findViewById(R.id.selected_track_title);
        nowPlayingArtist = (TextView) getView().findViewById(R.id.artist);
        pausePlay = (ImageView) getView().findViewById(R.id.play_pause);
        pausePlay.setOnClickListener(this);

        nextTrack = (ImageView) getView().findViewById(R.id.next_track);
        nextTrack.setOnClickListener(this);
        nextTrack.setImageResource(R.drawable.ic_pause_black_24dp);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_pause:
                mListener.onPausePlayPressed();
                break;
            case R.id.next_track:
                mListener.onNextTrackPressed();
                break;
            default:
                break;
        }

    }
    public void onPlaybackPaused(){
        pausePlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }
    public void onPlaybackPlaying(){
        pausePlay.setImageResource(R.drawable.ic_pause_black_24dp);
    }
    public void updateView(String id, String track_name, String artist_name, String art_url){
        Log.d(TAG, "updateView called");
        nowPlayingTitle.setText(track_name);
        nowPlayingArtist.setText(artist_name);
        Picasso.with(getView().getContext())
                .load(art_url)
                .into(nowPlayingImage);
        track_id = id;
    }

    public void newSongFromQueue(String id){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Track> call = apiService.getTrack(id.substring(14));
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track>call, Response<Track> response) {
                Track result = response.body();
                trackUpdated(result);
                //mSearchFinishedListener.onSearchFinishedListener(result);



            }

            @Override
            public void onFailure(Call<Track>call, Throwable t) {
                Log.d("a","faileda");
            }
        });
    }

    public void trackUpdated(final Track result){
        String id = result.album.album_id;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AlbumDetailed> call = apiService.getAlbum(id);
        call.enqueue(new Callback<AlbumDetailed>() {
            @Override
            public void onResponse(Call<AlbumDetailed>call, Response<AlbumDetailed> response) {
                AlbumDetailed album = response.body();
                updateView(result.track_id,result.track_name,result.artists.get(0).artist_name,album.images.get(1).art_url);
                //mSearchFinishedListener.onSearchFinishedListener(result);



            }

            @Override
            public void onFailure(Call<AlbumDetailed>call, Throwable t) {
                Log.d("a","faileda");
            }
        });
    }



}
