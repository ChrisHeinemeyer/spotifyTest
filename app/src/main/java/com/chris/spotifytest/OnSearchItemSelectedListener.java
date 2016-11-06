package com.chris.spotifytest;

import android.net.Uri;

import com.chris.spotifytest.dataTypes.spotify.Track;

import java.util.List;

/**
 * Created by Chris on 2016-10-10.
 */

public interface OnSearchItemSelectedListener{
    void onSearchTrackItemSelected(String id, String track_name, String artist_name, String art_url);
    void onSearchTrackItemSelected(String id, String track_name, String artist_name, String art_url, int index, List<Track> trackList);
    void onLocalTrackItemSelected(Uri uri, String track_name, String artist_name);
    void onSearchTrackItemMenuPressed(String id);

    void onSearchAlbumItemSelected(String id, String art_url);
    void onSearchAlbumItemMenuPressed(String id);

}
