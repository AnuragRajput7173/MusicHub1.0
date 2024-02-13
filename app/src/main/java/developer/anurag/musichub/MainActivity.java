package developer.anurag.musichub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import developer.anurag.musichub.controllers.MainActivityController;
import developer.anurag.musichub.data_models.PlaylistDataModel;
import developer.anurag.musichub.data_models.TrackDataModel;
import developer.anurag.musichub.databinding.ActivityMainBinding;
import developer.anurag.musichub.listeners.MainActivityListener;
import developer.anurag.musichub.play_music.PlayerService;

public class MainActivity extends AppCompatActivity implements MainActivityListener {

    public static boolean MINI_PLAYER_VISIBILITY=false;
    private PlayerService playerService;

    ActivityMainBinding binding;
    private MainActivityController controller;
    private FragmentManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.manager=getSupportFragmentManager();
        this.controller=new MainActivityController(this,this,this.binding,this);
        this.setupListeners();

        // start the service
        Intent playerServiceIntent=new Intent(this, PlayerService.class);
        startService(playerServiceIntent);
        bindService(playerServiceIntent,playerServiceConnection, Context.BIND_AUTO_CREATE);

        // fetch saved playlist
        this.controller.fetchSavedPlaylist();
    }




    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        this.controller.savePlaylist();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    // Function to setup listeners
    private void setupListeners(){
        this.binding.amHomeIB.setOnClickListener(view->controller.onHomeIconClick(this.manager));
        this.binding.amSearchIB.setOnClickListener(view-> controller.onSearchIconClick(this.manager));
        this.binding.amLibraryIB.setOnClickListener(view->controller.onLibraryIconClick(this.manager));
        this.binding.amCurrentPlayingTrackGL.setOnClickListener(view->controller.loadNowPlayingFragment(this.manager));
        this.controller.setupFragmentManagerListener(this.manager);
    }

    /* ----------------------------------- Listener functions --------------------------------- */

    @Override
    public void loadNowPlayingFragment() {
        this.controller.loadNowPlayingFragment(this.manager);
        this.playerService.playTrack();
    }

    @Override
    public void playPausePlayer() {
        this.playerService.playPausePlayer();
    }

    @Override
    public void setPlayerPosition(int position) {
        this.playerService.setPlayerPosition(position);
    }

    @Override
    public void playNext() {
        this.playerService.playNextTrack();
    }

    @Override
    public void playPrevious() {
        this.playerService.playPreviousTrack();
    }

    @Override
    public void playTrack() {
        this.playerService.playTrack();
    }

    /*-------------------------------- Create service connection ---------------------------------*/
    private final ServiceConnection playerServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.PlayerServiceBinder binder= (PlayerService.PlayerServiceBinder) service;
            playerService=binder.getPlayerService();
            PlaylistDataModel playlistDataModel=new ViewModelProvider(MainActivity.this).get(PlaylistDataModel.class);
            TrackDataModel trackDataModel=new ViewModelProvider(MainActivity.this).get(TrackDataModel.class);
            playerService.setupDataModels(playlistDataModel,trackDataModel);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}