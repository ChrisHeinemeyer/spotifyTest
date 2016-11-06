package com.chris.spotifytest.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chris.spotifytest.OnSearchItemSelectedListener;
import com.chris.spotifytest.adapters.searchAlbumResultAdapter;
import com.chris.spotifytest.dataTypes.spotify.Album;

import java.util.List;

import com.chris.spotifytest.R;
import com.chris.spotifytest.dataTypes.spotify.SearchResult;

/**
 * Created by Chris on 2016-10-06.
 */

public class SearchAlbumFragment extends Fragment{
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    private static RecyclerView tRecyclerView;
    private static  searchAlbumResultAdapter tAdapter;
    private static List<Album> albumList;
    OnSearchItemSelectedListener mSearchClickedListener;
    FragmentActivity listener;






    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        mSearchClickedListener = (OnSearchItemSelectedListener) getActivity();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment



        return inflater.inflate(R.layout.fragment_search, parent, false);


    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        tRecyclerView = (RecyclerView) view.findViewById(R.id.rlv);
        tAdapter = new searchAlbumResultAdapter(albumList, new searchAlbumResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Album item) {
                Toast.makeText(getActivity().getBaseContext(), item.album_name, Toast.LENGTH_SHORT).show();
                mSearchClickedListener.onSearchAlbumItemSelected(item.album_id, item.images.get(0).art_url);
            }

        }, new searchAlbumResultAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(Album item) {

                Toast.makeText(getActivity().getBaseContext(), item.album_id, Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        tRecyclerView.setLayoutManager(mLayoutManager);
        tRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tRecyclerView.setAdapter(tAdapter);




    }

    public static void update(SearchResult result){
       albumList = result.albums.albums;
    }


}
