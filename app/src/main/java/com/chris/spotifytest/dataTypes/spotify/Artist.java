package com.chris.spotifytest.dataTypes.spotify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chris on 2016-09-19.
 */
public class Artist {

    @SerializedName("id")
    public String artist_id;

    @SerializedName("images")
    public List<Image>  artist_images;

    @SerializedName("name")
    public String artist_name;

}
