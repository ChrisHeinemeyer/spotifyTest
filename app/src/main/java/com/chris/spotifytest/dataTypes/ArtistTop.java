package com.chris.spotifytest.dataTypes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chris on 2016-09-19.
 */
public class ArtistTop {

    @SerializedName("items")
    public List<Artist> artists;
}
