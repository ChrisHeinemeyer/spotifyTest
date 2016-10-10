package com.chris.spotifytest;

/**
 * Created by Chris on 2016-10-10.
 */

public interface OnSearchItemSelectedListener{
    void onSearchTrackItemSelected(String id, String track_name, String artist_name, String art_url);

    void onSearchAlbumItemSelected(String id, String art_url);
}
