package com.chris.spotifytest.dataTypes.spotify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chris on 2016-09-19.
 */
public class PlaylistTop {
    @SerializedName("items")
    public List<Playlist> playlists;
}
