package chris.spotifytest.dataTypes;

/**
 * Created by Chris on 2016-09-14.
 */
public class TrackInfo {
    private String track, album, artist, uid, artist_album;

    public TrackInfo() {
        this.track = "";
        this.album = "";
        this.artist = "";
        this.uid = "";
    }

    public TrackInfo(String track, String album, String artist, String uid) {
        this.track = track;
        this.album = album;
        this.artist = artist;
        this.uid = uid;
        this.artist_album = artist + " | " + album;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String name) {
        this.track = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUid (String uid){
        return uid;
    }

    public void setUid (String uid){
        this.uid = uid;
    }

    public String getArtistAlbum(){
        return artist_album;
    }
}
