package chris.spotifytest.dataTypes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chris on 2016-09-19.
 */
public class AlbumTop {
    @SerializedName("items")
    public List<Album> albums;

}