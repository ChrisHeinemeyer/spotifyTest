package com.chris.spotifytest.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chris.spotifytest.OnFragmentChange;
import com.chris.spotifytest.R;

/**
 * Created by Chris on 2016-10-06.
 */

public class MainFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    OnFragmentChange fragListener;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        fragListener = (OnFragmentChange) getActivity();
        fragListener.appBarElevationNeeded(true);
        fragListener.appBarNeeded(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_main, parent, false);
    }



    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

    @Override
    public void onResume(){
        super.onResume();
        fragListener.appBarElevationNeeded(true);
        fragListener.appBarNeeded(true);

    }
}
