package com.chris.spotifytest.adapters;

/**
 * Created by Chris on 2016-10-10.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chris.spotifytest.R;
import com.chris.spotifytest.dataTypes.Track;

import java.util.List;



/**
 * Created by Chris on 2016-09-14.
 */



public class albumViewTrackAdapter extends RecyclerView.Adapter<albumViewTrackAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Track item);
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(Track item);
    }
    private List<Track> trackList;
    private final OnItemClickListener listener;
    private final OnLongItemClickListener longListener;

    public albumViewTrackAdapter(List<Track> trackInfoList, OnItemClickListener listener, OnLongItemClickListener longListener) {
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
        if (trackList != null) {
            return trackList.size();
        }
        else return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {

        public TextView track, artist_album;



        public MyViewHolder(View view) {
            super(view);
            track = (TextView) view.findViewById(R.id.title1);
            artist_album = (TextView) view.findViewById(R.id.sub_title);

        }

        public void bind(final Track t, final OnItemClickListener listener, final OnLongItemClickListener longListener){
            String artists = "";
            for(int i =0; i < t.artists.size(); i++){
                artists += t.artists.get(i).artist_name;
                if(i < t.artists.size() - 1) artists +=", ";

            }
            track.setText(t.track_name);
            artist_album.setText(artists);

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
