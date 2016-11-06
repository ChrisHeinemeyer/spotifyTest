package com.chris.spotifytest.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chris.spotifytest.ApiClient;
import com.chris.spotifytest.ApiInterface;
import com.chris.spotifytest.OnFragmentChange;
import com.chris.spotifytest.OnSearchItemSelectedListener;
import com.chris.spotifytest.R;
import com.chris.spotifytest.dataTypes.spotify.AlbumDetailed;
import com.chris.spotifytest.dataTypes.spotify.SquareImageView;
import com.chris.spotifytest.dataTypes.spotify.Track;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chris on 2016-10-10.
 */

public class AlbumViewFragment extends Fragment{
    Toolbar toolbar;
     CollapsingToolbarLayout collapsingToolbar;
    SquareImageView albumArt;
    RecyclerView tRecyclerView;
    static List<Track> trackList;
    OnFragmentChange fragListener;
    OnSearchItemSelectedListener mSearchClickedListener;

    private static String title;
    public  static Adapter tAdapter;
    private static String art_url;
    private static String id;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSearchClickedListener = (OnSearchItemSelectedListener) getActivity();
        fragListener = (OnFragmentChange) getActivity();
        fragListener.appBarNeeded(false);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View result = inflater.inflate(R.layout.fragment_album, parent, false);

        Bundle bundle = this.getArguments();
        id = bundle.getString("id");
        art_url = bundle.getString("art_url");


        collapsingToolbar = (CollapsingToolbarLayout) result.findViewById(R.id.collapsing_toolbar);
        //collapsingToolbar.setTitle("Title");
        albumArt = (SquareImageView) result.findViewById(R.id.image);
        Picasso.with(result.getContext())
                .load(art_url)
                .fit()
                //.resize(200,200)
                .into(albumArt);

        tRecyclerView = (RecyclerView) result.findViewById(R.id.rv);

        return result;
    }





    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        fragListener.appBarNeeded(false);

    }

    public void update(String uid, String uart_url){
        id = uid;
        art_url = uart_url;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AlbumDetailed> call = apiService.getAlbum(id);
        call.enqueue(new Callback<AlbumDetailed>() {
            @Override
            public void onResponse(Call<AlbumDetailed>call, Response<AlbumDetailed> response) {
                final AlbumDetailed result = response.body();

                trackList = result.tracks.tracks;
                title = result.album_name ;
                tRecyclerView.setAdapter(tAdapter);
                tAdapter = new Adapter(trackList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                tRecyclerView.setLayoutManager(mLayoutManager);
                tRecyclerView.setItemAnimator(new DefaultItemAnimator());
                tRecyclerView.setAdapter(tAdapter);

                collapsingToolbar.setTitle(title);

            }

            @Override
            public void onFailure(Call<AlbumDetailed>call, Throwable t) {
                Log.d("AlbumViewFragment",t.getMessage());
            }
        });

    }

    public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {


        private List<Track> trackList;


        public Adapter(List<Track> trackList) {
            this.trackList = trackList;
        }




        @Override
        public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_row, parent, false);

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(trackList.get(position));
        }

        @Override
        public int getItemCount() {
            if(trackList != null) {
                return trackList.size();
            }
            else return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener  {

            public TextView album, artist;
            public ImageView  more_button;


            public MyViewHolder(View view) {
                super(view);
                album = (TextView) view.findViewById(R.id.title1);
                artist = (TextView) view.findViewById(R.id.sub_title);
                more_button = (ImageView) view.findViewById(R.id.buttonMore);
                view.setOnClickListener(this);
                more_button.setOnClickListener(this);
            }

            public void bind(final Track t){
                String track_name = t.track_name;
                album.setText(track_name);
                artist.setText(t.artists.get(0).artist_name);

            }
            @Override
            public void onClick(View v) {
                List<Track> list = Adapter.this.trackList;
                Track item = list.get(getAdapterPosition());
                final String id = item.track_id;
                final String track_name = item.track_name;
                final String art_url = null;
                final String artist_name = item.artists.get(0).artist_name;
                int index = getAdapterPosition();

                if (v.getId() == more_button.getId()){
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), more_button);
                    getActivity().getMenuInflater().inflate(R.menu.popup_album, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(this);
                    popupMenu.show();

                } else {
                    //   Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();

                    mSearchClickedListener.onSearchTrackItemSelected(id, track_name, artist_name, art_url, index, trackList);
                }
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.queue:
                        mSearchClickedListener.onSearchTrackItemMenuPressed(Adapter.this.trackList.get(getAdapterPosition()).track_id);
                        return true;
                    default:
                        return false;
                }
            }
        }
    }

//    public   void dataSetChanged(){
//        Log.d("AlbumViewFragment", "dataSetChanged");
//        tAdapter.notifyDataSetChanged();
//        tRecyclerView.invalidate();
//      //  collapsingToolbar.setTitle(title);
//    }

//    public interface OnSearchComplete{
//        public void onSearchFinishedListener();
//    }
}
