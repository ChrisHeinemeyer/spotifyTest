package com.chris.spotifytest.fragments;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chris.spotifytest.Activities.MainActivity;
import com.chris.spotifytest.OnSearchItemSelectedListener;
import com.chris.spotifytest.R;
import com.chris.spotifytest.dataTypes.LocalTrack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Chris on 2016-10-24.
 */

public class LocalSearchFragment extends Fragment {
    private ArrayList<LocalTrack> trackList;
    private RecyclerView tRecyclerView;
    private Adapter tAdapter;
    String searchString;
    MediaPlayer mediaPlayer;
    static OnSearchItemSelectedListener mSearchClickedListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mSearchClickedListener = (OnSearchItemSelectedListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        mediaPlayer = new MediaPlayer();
        return inflater.inflate(R.layout.fragment_search, parent, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        searchString = MainActivity.getEdtSearch().getText().toString();
        trackList = new ArrayList<>();
        getTracks();
        tRecyclerView = (RecyclerView) view.findViewById(R.id.rlv);
        tAdapter = new Adapter(trackList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        tRecyclerView.setLayoutManager(mLayoutManager);
        tRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tRecyclerView.setAdapter(tAdapter);
        //tRecyclerView.setAdapter();
    }


    public void getTracks(){
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ALBUM);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                if(contains(thisTitle,searchString) || contains(thisArtist,searchString) || contains(thisAlbum, searchString)){
                    trackList.add(new LocalTrack(thisId, thisTitle, thisArtist, thisAlbum));
                }
            }
            while (musicCursor.moveToNext());
        }
    }
    private boolean contains(String str1, String str2){
        return str1.toLowerCase().contains(str2.toLowerCase());
    }
    public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

        protected ArrayList<LocalTrack> trackList;


        public Adapter(ArrayList<LocalTrack> trackInfoList) {
            this.trackList = trackInfoList;
        }




        @Override
        public Adapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_row, parent, false);
            return new Adapter.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(Adapter.MyViewHolder holder, int position) {
            holder.bind(trackList.get(position));//,listener, longListener);
        }

        @Override
        public int getItemCount() {
            if (trackList != null) {
                return trackList.size();
            }
            else return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener { //, View.OnLongClickListener

            public TextView track, artist_album;
            public ImageView album_art, more_button;

            public MyViewHolder(View view) {
                super(view);
                track = (TextView) view.findViewById(R.id.title1);
                artist_album = (TextView) view.findViewById(R.id.sub_title);
                album_art = (ImageView) view.findViewById(R.id.album_art);
                more_button = (ImageView) view.findViewById(R.id.buttonMore);
                view.setOnClickListener(this);
                album_art.setOnClickListener(this);
                more_button.setOnClickListener(this);
            }

            public void bind(final LocalTrack t){//, final OnItemClickListener listener, final OnLongItemClickListener longListener){
                String artistAlbums = t.artist;// " | " + t.album;
                track.setText(t.title);
                artist_album.setText(artistAlbums);
            }

            @Override
            public void onClick(View v) {
                ArrayList<LocalTrack> list = Adapter.this.trackList;
                LocalTrack item = list.get(getAdapterPosition());
                Uri uri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        item.id);

                final String track_name = item.title;
                final String artist_name = item.artist;

              if (v.getId() == more_button.getId()){
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), more_button);
                    getActivity().getMenuInflater().inflate(R.menu.popup_track, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(this);
                    popupMenu.show();

                } else {
                    //   Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();

                    mSearchClickedListener.onLocalTrackItemSelected(uri, track_name, artist_name);
                }
            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.queue:
                       // mSearchClickedListener.onSearchTrackItemMenuPressed(Adapter.this.trackList.get(getAdapterPosition()).track_id);
                        return true;
                    default:
                        return false;
                }
            }
        }
    }

}
