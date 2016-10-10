package com.chris.spotifytest.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chris.spotifytest.ApiClient;
import com.chris.spotifytest.ApiInterface;
import com.chris.spotifytest.OnFragmentChange;
import com.chris.spotifytest.OnSearchFinished;
import com.chris.spotifytest.OnSearchItemSelectedListener;
import com.chris.spotifytest.R;
import com.chris.spotifytest.adapters.albumViewTrackAdapter;
import com.chris.spotifytest.adapters.searchTrackResultAdapter;
import com.chris.spotifytest.dataTypes.AlbumDetailed;
import com.chris.spotifytest.dataTypes.SearchResult;
import com.chris.spotifytest.dataTypes.SquareImageView;
import com.chris.spotifytest.dataTypes.Track;
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
    public static albumViewTrackAdapter tAdapter;
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

//        tAdapter = new albumViewTrackAdapter(trackList, new albumViewTrackAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Track item) {
//
//                final String play = "spotify:track:" +item.track_id;
//                final String track_id = item.track_id;
//                final String track_name = item.track_name;
//                final String artist_name = item.artists.get(0).artist_name;
//                mSearchClickedListener.onSearchTrackItemSelected(track_id, track_name, artist_name, art_url);
//            }
//
//
//        }, new albumViewTrackAdapter.OnLongItemClickListener() {
//            @Override
//            public void onLongItemClick(Track item) {
//                Toast.makeText(getActivity().getBaseContext(), item.album.album_name, Toast.LENGTH_SHORT).show();
//            }
//        });
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//        tRecyclerView.setLayoutManager(mLayoutManager);
//        tRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        tRecyclerView.setAdapter(tAdapter);
//        title = "";
//        collapsingToolbar.setTitle(title);





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
                tAdapter = new albumViewTrackAdapter(trackList, new albumViewTrackAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Track item) {

                        final String track_id = item.track_id;
                        final String track_name = item.track_name;
                        final String artist_name = item.artists.get(0).artist_name;
                        mSearchClickedListener.onSearchTrackItemSelected(track_id, track_name, artist_name, art_url);
                    }


                }, new albumViewTrackAdapter.OnLongItemClickListener() {
                    @Override
                    public void onLongItemClick(Track item) {
                      //  Toast.makeText(getActivity().getBaseContext(), item.album.album_id, Toast.LENGTH_SHORT).show();
                    }
                });
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
