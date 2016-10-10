package com.chris.spotifytest.Activities;

import  android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chris.spotifytest.OnFragmentChange;
import com.chris.spotifytest.OnPlaybackControlButtonPressed;
import com.chris.spotifytest.OnSearchItemSelectedListener;
import com.chris.spotifytest.fragments.AlbumViewFragment;
import com.chris.spotifytest.fragments.PlaybackControlsFragment;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerStateCallback;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import com.chris.spotifytest.R;
import com.chris.spotifytest.fragments.SearchFragment;
import com.chris.spotifytest.fragments.MainFragment;


public class MainActivity extends AppCompatActivity implements
        PlayerNotificationCallback, ConnectionStateCallback, OnPlaybackControlButtonPressed, OnSearchItemSelectedListener, OnFragmentChange {

    static final int NUM_RESULTS = 20;
    private static final String TAG = "MainActivity";



    private static Toolbar myToolbar;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private static EditText edtSearch;

    ProgressBar progressBar;

    private boolean setNowPlaying;
    private boolean playbackControlsShown;



//    List<String> artistResultsList = new ArrayList<String>();
//    List<String> albumResultsList = new ArrayList<String>();
//    List<String> trackResultsList = new ArrayList<String>();


    private static String accessToken;
    private Player mPlayer;



    static final String API_URL = "https://api.spotify.com";

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "cc42867f9fb24ed699f6ec68af1f448f";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "test://callback";

    // Request code that will be used to verify if the result comes from correct activity
// Can be any integer
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment mainFragment = new MainFragment();
        PlaybackControlsFragment controlsFragment = new PlaybackControlsFragment();
        fragmentTransaction.add(R.id.main_view, mainFragment, "HELLO");
        fragmentTransaction.add(R.id.playback_controls, controlsFragment, "controlsFragment");
        fragmentTransaction.hide(controlsFragment);
        fragmentTransaction.commit();



       // hidePlaybackControls();

        myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //spotify authentication
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onSearchTrackItemSelected(String id, String track_name, String artist_name, String art_url){
        Log.d("MainActivity", "Search track item selected");
        PlaybackControlsFragment playbackControlsFragment = (PlaybackControlsFragment)
                getSupportFragmentManager().findFragmentById(R.id.playback_controls);
   //     Toast.makeText(getBaseContext(), track_name, Toast.LENGTH_SHORT).show();

        if (playbackControlsFragment != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
           // playbackControlsFragment.updateView(id, track_name, artist_name, art_url);
            if(!playbackControlsShown){
                showPlaybackControls();
            }
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            PlaybackControlsFragment newFragment = new PlaybackControlsFragment();
            Bundle args = new Bundle();
            args.putString("id", id);
            args.putString("track_name", track_name);
            args.putString("artist_name", artist_name);
            args.putString("art_url", art_url);
            newFragment.setArguments(args);



            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.playback_controls, newFragment);
            // Commit the transaction
            transaction.commit();


            if(!playbackControlsShown){
                showPlaybackControls();
            }
        }
        final String play = "spotify:track:" +id;
        mPlayer.play(play);

    }

    public void onSearchAlbumItemSelected(String id, String art_url){
        Log.d("MainActivity", "Search album item selected");
       AlbumViewFragment albumViewFragment = (AlbumViewFragment)
                getSupportFragmentManager().findFragmentById(R.id.main_content);

        if (albumViewFragment != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            albumViewFragment.tAdapter.notifyDataSetChanged();
            albumViewFragment.update(id,art_url);

        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            AlbumViewFragment newFragment = new AlbumViewFragment();
            Bundle args = new Bundle();
            args.putString("id", id);
            args.putString("art_url", art_url);
            newFragment.setArguments(args);
            newFragment.update(id,art_url);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.main_view,newFragment, "avf");
            transaction.addToBackStack("sf");

            // Commit the transaction
            transaction.commit();

            if(isSearchOpened) {
                handleMenuSearch();
            }
            if(getSupportActionBar()!=null){
                getSupportActionBar().hide();
            }



//            if(playbackControlsShown){
//                showPlaybackControls();
//            }
        }
    }

    public void onSearchTrackItemLongPressed(String id){
        final String uri = "spotify:track:" +id;
        mPlayer.getPlayerState(new PlayerStateCallback() {

            @Override
            public void onPlayerState(PlayerState playerState) {
                Toast.makeText(getApplicationContext(), "Queuing tracks", Toast.LENGTH_SHORT).show();
               mPlayer.queue(uri);
            }
        });
    }

    public void onPausePlayPressed(){
        mPlayer.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                if (playerState.playing){
                    mPlayer.pause();
                }
                else {
                    mPlayer.resume();

                }
            }
        });
    }

    public void onNextTrackPressed(){
        mPlayer.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                Toast.makeText(getApplicationContext(), "Skipping tracks", Toast.LENGTH_SHORT).show();
                mPlayer.skipToNext();
                mPlayer.resume();
            }
        });
    }

    public void appBarElevationNeeded(boolean b){
        if(getSupportActionBar() != null) {
           if(b) getSupportActionBar().setElevation(4);
           else getSupportActionBar().setElevation(0);
        }
    }

    public void appBarNeeded(boolean b){
        if(getSupportActionBar() != null) {
            if(b)  getSupportActionBar().show();
            else  getSupportActionBar().hide();;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

            //add the search icon in the action bar
            //mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_magnify));
            mSearchAction.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_search, null));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSearch = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });




            edtSearch.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSearch, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            //mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_circle));
            mSearchAction.setIcon(R.drawable.ic_cancel);

            isSearchOpened = true;
        }
    }

    private void doSearch() {

    SearchFragment searchFragment = new SearchFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.main_view, searchFragment, "sf");
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
//    if(getSupportActionBar() != null) {
//        getSupportActionBar().setElevation(0);
//    }

    }
    public Player getPlayer(){
        return mPlayer;
    }




    public static String getAccessToken() {
        return accessToken;
    }

    private static void setAccessToken(String accessToken) {
        MainActivity.accessToken = accessToken;
    }

    public static EditText getEdtSearch() {
        return edtSearch;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer = player;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                        setAccessToken(response.getAccessToken());
                        //mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
        mPlayer.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                Toast.makeText(getApplicationContext(), "Clearing queue ", Toast.LENGTH_SHORT).show();

                mPlayer.clearQueue();
            }
        });
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
        PlaybackControlsFragment playbackControlsFragment = (PlaybackControlsFragment)
                getSupportFragmentManager().findFragmentById(R.id.playback_controls);
        switch (eventType.name()) {

            case "PAUSE":
                playbackControlsFragment.onPlaybackPaused();
                break;
            case "PLAY":
                playbackControlsFragment.onPlaybackPlaying();
                break;
            case "TRACK_CHANGED":
                if(playbackControlsFragment != null) {
                    if(playerState.trackUri.length()>0)
                        playbackControlsFragment.newSongFromQueue(playerState.trackUri);
                }
            default: break;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name() + ": " + errorDetails);
    }

    protected void showPlaybackControls() {
        Log.d(TAG, "showPlaybackControls");
        Fragment controlsFragment = getSupportFragmentManager().findFragmentByTag("controlsFragment");
        //Fragment mainFragment = getFragmentManager().findFragmentByTag("HELLO");

        getSupportFragmentManager().beginTransaction()
                .show(controlsFragment)
                //.hide(mainFragment)
                .commit();
        findViewById(R.id.controls_container).setVisibility(View.VISIBLE);
        playbackControlsShown = true;

    }

//    protected void hidePlaybackControls() {
//        Log.d(TAG, "hidePlaybackControls");
//        getFragmentManager().beginTransaction()
//                .hide(controlsFragment)
//                .commit();
//        playbackControlsShown = false;
//    }




}

