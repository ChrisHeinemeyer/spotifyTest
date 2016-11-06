package com.chris.spotifytest.dataTypes;

/**
 * Created by Chris on 2016-10-24.
 */

public class LocalTrack {
    public long id;
    public long albumID;
    public String title;
    public String artist;
    public String album;
    public LocalTrack(long songID, String trackTitle, String trackArtist, String trackAlbum) {
        id=songID;
        title=trackTitle;
        artist=trackArtist;
        album = trackAlbum;
    }


}
