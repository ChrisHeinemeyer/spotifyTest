package com.chris.spotifytest.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chris.spotifytest.Activities.MainActivity;
import com.chris.spotifytest.OnPausePlayListener;
import com.chris.spotifytest.R;
import com.spotify.sdk.android.player.Spotify;
import com.squareup.picasso.Picasso;

import static com.chris.spotifytest.R.id.album_art;
import static com.chris.spotifytest.R.id.album_art_image;


/**
 * Created by Chris on 2016-10-06.
 */

public class PlaybackControlsFragment extends Fragment implements View.OnClickListener {
    static final String TAG = "ControlsFragment";
    private static ImageView nowPlayingImage;
    private static ImageView pausePlay;
    private static TextView nowPlayingTitle;
    private static TextView nowPlayingArtist;

    public static String track_id;
    OnPausePlayListener mListener;




    public void onAttach(Context context){
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
            mListener = (OnPausePlayListener) a;
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_pause:
                mListener.onPausePlayPressed();
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



}
