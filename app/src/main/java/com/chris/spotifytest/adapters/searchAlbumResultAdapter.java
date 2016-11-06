package com.chris.spotifytest.adapters;

/**
 * Created by Chris on 2016-09-14.
 */


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chris.spotifytest.ApiClient;
import com.chris.spotifytest.ApiInterface;
import com.chris.spotifytest.dataTypes.spotify.Album;
import com.chris.spotifytest.dataTypes.spotify.AlbumDetailed;
import com.squareup.picasso.Picasso;

import java.util.List;


import com.chris.spotifytest.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class searchAlbumResultAdapter extends RecyclerView.Adapter<searchAlbumResultAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Album item);
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(Album item);
    }
    private List<Album> albumList;
    private final OnItemClickListener listener;
    private final OnLongItemClickListener longListener;

    public searchAlbumResultAdapter(List<Album> albumInfoList, OnItemClickListener listener, OnLongItemClickListener longListener) {
        this.albumList = albumInfoList;
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
        holder.bind(albumList.get(position),listener, longListener);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {

        public TextView album, artist;
        public ImageView album_art;


        public MyViewHolder(View view) {
            super(view);
            album = (TextView) view.findViewById(R.id.title1);
            artist = (TextView) view.findViewById(R.id.sub_title);
            album_art = (ImageView) view.findViewById(R.id.album_art);
        }

        public void bind(final Album a, final OnItemClickListener listener, final OnLongItemClickListener longListener){
            String album_name = a.album_name;
            album.setText(album_name);
            //artist.setText("artist name placeholder");
            if(a.images.size() > 1) {
                Picasso.with(itemView.getContext())
                        .load(a.images.get(1).art_url)
                        .resize(200, 200)
                        .into(album_art);
            } else if (a.images.size() == 1){
                Picasso.with(itemView.getContext())
                        .load(a.images.get(0).art_url)
                        .resize(200, 200)
                        .into(album_art);
            } else if (a.images.size() == 0){
                Picasso.with(itemView.getContext())
                        .load(R.drawable.album_art_blank)
                        .resize(200, 200)
                        .into(album_art);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(a);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override public boolean onLongClick(View v){
                    longListener.onLongItemClick(a);
                    return true;
                }
            });
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<AlbumDetailed> call = apiService.getAlbum(a.album_id);
            call.enqueue(new Callback<AlbumDetailed>() {
                @Override
                public void onResponse(Call<AlbumDetailed>call, Response<AlbumDetailed> response) {
                    AlbumDetailed result = response.body();
                    artist.setText(result.artists.get(0).artist_name);

                }

                @Override
                public void onFailure(Call<AlbumDetailed>call, Throwable t) {
                    Log.d("a","faileda");
                }
            });

        }
    }
}