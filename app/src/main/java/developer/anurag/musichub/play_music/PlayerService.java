package developer.anurag.musichub.play_music;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import developer.anurag.musichub.controllers.NowPlayingFragmentController;
import developer.anurag.musichub.data_models.PlaylistDataModel;
import developer.anurag.musichub.data_models.TrackDataModel;
import developer.anurag.musichub.tracks_fetcher.Track;
import developer.anurag.musichub.tracks_fetcher.TrackFetcher;

public class PlayerService extends Service {
    private ExoPlayer player;
    private final PlayerServiceBinder binder=new PlayerServiceBinder();
    private PlaylistDataModel playlistDataModel=null;
    private TrackDataModel trackDataModel=null;
    private Handler durationHandler;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }



    public void setupDataModels(PlaylistDataModel playlistDataModel, TrackDataModel trackDataModel) {
        this.playlistDataModel=playlistDataModel;
        this.trackDataModel=trackDataModel;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent==null){
            playNextTrack();
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.player=new ExoPlayer.Builder(this).build();
        this.setupPlayerListener();
        this.durationHandler=new Handler();
        durationHandler.post(trackDurationRunnable);
    }
    /*--------------------------------- Setup player controller functions  ---------------------------*/

    public void playTrack(){
        // return if playlist is empty
        if(this.playlistDataModel==null || this.playlistDataModel.playlist.isEmpty())return;

//        else play music
        Integer index=this.playlistDataModel.getCurrentTrackIndex().getValue();
        Integer position=this.playlistDataModel.getTrackPosition().getValue();
        if(index==null|| position==null)return;;
        Track currentTrack=this.playlistDataModel.playlist.get(index);
        MediaItem mediaItem=MediaItem.fromUri(currentTrack.trackUri);
        this.player.setMediaItem(mediaItem);
        this.player.prepare();
        this.player.seekTo(position);
        if(PlaylistDataModel.IS_PLAYING)this.player.play();
    }

    public void playPausePlayer() {
        if(this.player.isPlaying()){
            PlaylistDataModel.IS_PLAYING=false;
            this.player.pause();
        }else {
            PlaylistDataModel.IS_PLAYING=true;
            this.player.play();
        }
    }

    public void playNextTrack(){
        Integer currentIndex=this.playlistDataModel.getCurrentTrackIndex().getValue();
        if(currentIndex!=null){
            if((this.playlistDataModel.playlist.size()-currentIndex)<5 && this.trackDataModel!=null){
                this.playlistDataModel.playlist.addAll(TrackFetcher.getRandomTracks(20,this.trackDataModel.getAllTracks()));
            }
            this.playlistDataModel.setCurrentTrackIndex(++currentIndex);
            this.playlistDataModel.setTrackPosition(0);
            this.playlistDataModel.setTrackDuration(0);
            playTrack();
        }
    }

    public void setPlayerPosition(int position){
        this.player.seekTo(position*1000L);
    }

    public void playPreviousTrack(){
        Integer currentIndex=this.playlistDataModel.getCurrentTrackIndex().getValue();
        this.playlistDataModel.setTrackPosition(0);
        this.playlistDataModel.setTrackDuration(0);
        if(currentIndex!=null && currentIndex>0){
            this.playlistDataModel.setCurrentTrackIndex(--currentIndex);
        }
        playTrack();
    }

    // Function to setup player listener
    private void setupPlayerListener(){
        this.player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                if(playbackState==Player.STATE_READY){
                    playlistDataModel.setTrackDuration((int) (player.getContentDuration()/1000));
                }
                if(playbackState==Player.STATE_ENDED){
                    playNextTrack();
                }
            }
        });
    }

    // Create runnable for tracking track duration
    Runnable trackDurationRunnable=new Runnable() {
        @Override
        public void run() {
            if(!NowPlayingFragmentController.IS_SEEKBAR_DRAGGING && player.isPlaying()){
                playlistDataModel.setTrackPosition((int) (player.getContentPosition()/1000));
            }
            durationHandler.postDelayed(this,1000);
        }
    };


    /*----------------------------------------- Create a Binder class ------------------------------------*/
    public class PlayerServiceBinder extends Binder{
        public PlayerService getPlayerService(){
            return PlayerService.this;
        }
    }

}
