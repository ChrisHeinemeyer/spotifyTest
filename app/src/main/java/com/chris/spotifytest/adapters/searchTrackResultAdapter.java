package com.chris.spotifytest.adapters;

/**
 * Created by Chris on 2016-09-14.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;


import chris.spotifytest.R;
import com.chris.spotifytest.dataTypes.Track;
import com.chris.spotifytest.dataTypes.TrackInfo;

public class searchTrackResultAdapter extends RecyclerView.Adapter<searchTrackResultAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Track item);
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(Track item);
    }
    private List<Track> trackList;
    private final OnItemClickListener listener;
    private final OnLongItemClickListener longListener;

    public searchTrackResultAdapter(List<Track> trackInfoList, OnItemClickListener listener, OnLongItemClickListener longListener) {
        this.trackList = trackInfoList;
        this.listener = listener;
        this.longListener = longListener;
    }



    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(trackList.get(position),listener, longListener);
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {

        public TextView track, artist_album;
        public ImageView album_art;


        public MyViewHolder(View view) {
            super(view);
            track = (TextView) view.findViewById(R.id.track);
            artist_album = (TextView) view.findViewById(R.id.artist_album);
            album_art = (ImageView) view.findViewById(R.id.album_art);
        }

        public void bind(final Track t, final OnItemClickListener listener, final OnLongItemClickListener longListener){
            String artist_albumS = t.artists.get(0).artist_name + " | " + t.album.album_name;
            track.setText(t.track_name);
            artist_album.setText(artist_albumS);
            Picasso.with(itemView.getContext())
                    .load(t.album.images.get(1).art_url)
                    .resize(200,200)
                    .into(album_art);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(t);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override public boolean onLongClick(View v){
                    longListener.onLongItemClick(t);
                    return true;
                }
            });



        }



    }
}