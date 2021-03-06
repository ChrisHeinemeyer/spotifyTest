package com.chris.spotifytest.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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

import com.chris.spotifytest.OnSearchItemSelectedListener;
import com.spotify.sdk.android.player.Player;

import java.util.List;

import com.chris.spotifytest.R;
import com.chris.spotifytest.dataTypes.spotify.SearchResult;
import com.chris.spotifytest.dataTypes.spotify.Track;
import com.squareup.picasso.Picasso;

/**
 * Created by Chris on 2016-10-06.
 */

public class SearchTrackFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    private RecyclerView tRecyclerView;
    private static Adapter tAdapter;
    private static final String CLIENT_ID = "cc42867f9fb24ed699f6ec68af1f448f";
    //    List<TrackInfo> resultsList = new ArrayList<TrackInfo>();
    final String TYPE = "track,artist,album,playlist";
    private Player mPlayer;
    static OnSearchItemSelectedListener mSearchClickedListener;


    public static List<Track> trackList;

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
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        tRecyclerView = (RecyclerView) view.findViewById(R.id.rlv);
        // Get the intent, verify the action and get the query
        tAdapter = new Adapter(trackList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        tRecyclerView.setLayoutManager(mLayoutManager);
        tRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tRecyclerView.setAdapter(tAdapter);




        }

    public static void update(SearchResult result){
        trackList = result.tracks.tracks;

    }

    public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

        protected List<Track> trackList;


        public Adapter(List<Track> trackInfoList) {
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

            public void bind(final Track t){//, final OnItemClickListener listener, final OnLongItemClickListener longListener){
                String artist_albumS = t.artists.get(0).artist_name+ " | " + t.album.album_name;
                track.setText(t.track_name);
                artist_album.setText(artist_albumS);

                switch (t.album.images.size()){
                    case 0:
                        Picasso.with(itemView.getContext())
                            .load(R.drawable.album_art_blank)
                            .resize(200, 200)
                            .into(album_art);
                        break;
                    case 1:
                        Picasso.with(itemView.getContext())
                                .load(t.album.images.get(0).art_url)
                                .resize(200, 200)
                                .into(album_art);
                        break;
                    default:
                        Picasso.with(itemView.getContext())
                                .load(t.album.images.get(1).art_url)
                                .resize(200, 200)
                                .into(album_art);
                        break;

                }
            }

            @Override
            public void onClick(View v) {
                List<Track> list = Adapter.this.trackList;
                Track item = list.get(getAdapterPosition());
                final String track_id = item.track_id;
                final String track_name = item.track_name;
                final String artist_name = item.artists.get(0).artist_name;
                final String art_url = item.album.images.get(1).art_url;

                if (v.getId() == album_art.getId()){
                    //   Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();

                } else if (v.getId() == more_button.getId()){
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), more_button);
                    getActivity().getMenuInflater().inflate(R.menu.popup_track, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(this);
                    popupMenu.show();

                } else {
                    //   Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();

                    mSearchClickedListener.onSearchTrackItemSelected(track_id, track_name, artist_name, art_url, getAdapterPosition(), list);
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



    }



