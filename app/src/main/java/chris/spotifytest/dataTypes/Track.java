package chris.spotifytest.dataTypes;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chris on 2016-09-19.
 */
public class Track {
    @SerializedName("album")
    public Album album;

    @SerializedName("artists")
    public List<Artist> artists;

    @SerializedName("id")
    public String track_id;

    @SerializedName("name")
    public String track_name;

}
