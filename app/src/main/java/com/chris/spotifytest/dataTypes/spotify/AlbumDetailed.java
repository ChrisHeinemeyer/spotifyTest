package com.chris.spotifytest.dataTypes.spotify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chris on 2016-10-09.
 */

public class AlbumDetailed {
    @SerializedName("id")
    public String album_id;

    @SerializedName("images")
    public List<Image> images;

    @SerializedName("name")
    public String album_name;

    @SerializedName("artists")
    public List<Artist> artists;

    @SerializedName("tracks")
    public TrackTop tracks;
}
