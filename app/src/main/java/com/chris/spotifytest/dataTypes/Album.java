package com.chris.spotifytest.dataTypes;
import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Created by Chris on 2016-09-19.
 */
public class Album {
    @SerializedName("id")
    public String album_id;

    @SerializedName("images")
    public List<Image> images;

    @SerializedName("name")
    public String album_name;

}
