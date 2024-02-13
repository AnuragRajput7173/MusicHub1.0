package developer.anurag.musichub.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import developer.anurag.musichub.R;
import developer.anurag.musichub.data_models.PlaylistDataModel;
import developer.anurag.musichub.data_models.TrackDataModel;
import developer.anurag.musichub.databinding.FragmentNowPlayingBinding;
import developer.anurag.musichub.fragments.PlaylistBottomFragment;
import developer.anurag.musichub.listeners.MainActivityListener;
import developer.anurag.musichub.tracks_fetcher.Track;

public class NowPlayingFragmentController {
    public static boolean IS_SEEKBAR_DRAGGING=false;
    private Context context;
    private FragmentNowPlayingBinding binding;
    private PlaylistDataModel playlistDataModel;
    private MainActivityListener mainActivityListener;

    public NowPlayingFragmentController(Context context, ViewModelStoreOwner storeOwner, FragmentNowPlayingBinding binding){
        this.context=context;
        this.binding=binding;
        this.playlistDataModel=new ViewModelProvider(storeOwner).get(PlaylistDataModel.class);

        // setup MainActivityListener
        if(context instanceof MainActivityListener){
            this.mainActivityListener=(MainActivityListener) context;
        }

        // setup play pause icon
        if(PlaylistDataModel.IS_PLAYING)this.binding.fnpPlayPauseIB.setImageResource(R.drawable.pause_circle_filled_lg_icon);
        else this.binding.fnpPlayPauseIB.setImageResource(R.drawable.play_circle_filled_lg_icon);


    }

    /*---------------------------------------- Setup listeners ----------------------------------------*/
    public void setupIBListeners(){
        if(this.mainActivityListener==null)return;

        // setup PlayPauseIB listener
        this.binding.fnpPlayPauseIB.setOnClickListener(view->{
            if(PlaylistDataModel.IS_PLAYING){
                this.binding.fnpPlayPauseIB.setImageResource(R.drawable.play_circle_filled_lg_icon);
                this.mainActivityListener.playPausePlayer();
            }else {
                this.binding.fnpPlayPauseIB.setImageResource(R.drawable.pause_circle_filled_lg_icon);
                this.mainActivityListener.playPausePlayer();
            }
        });

        // setup NextIB listener
        this.binding.fnpNextIB.setOnClickListener(view->{
            PlaylistDataModel.IS_PLAYING=true;
            this.mainActivityListener.playNext();
        });

        // setup PreviousIB listener
        this.binding.fnpPreviousIB.setOnClickListener(view->{
            PlaylistDataModel.IS_PLAYING=true;
            this.mainActivityListener.playPrevious();
        });
    }
    public void setupSeekbarListener(){
        this.binding.fnpPlayerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.fnpTrackCurrentPositionTV.setText(getDurationStr(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                IS_SEEKBAR_DRAGGING=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mainActivityListener.setPlayerPosition(seekBar.getProgress());
                IS_SEEKBAR_DRAGGING=false;
            }
        });
    }

    public void setupPlaylistIBListener(FragmentManager manager){
        this.binding.fnpPlaylistIB.setOnClickListener(view->{
            PlaylistBottomFragment playlistBottomFragment=new PlaylistBottomFragment();
            playlistBottomFragment.show(manager,playlistBottomFragment.getTag());
        });
    }

    public void setupCollapseIBListener(FragmentManager manager){
        this.binding.fnpCollapseIB.setOnClickListener(view->{
            manager.popBackStack();
        });
    }

    /*----------------------------------------- Create Observer ------------------------------------------*/
    private final Observer<Integer> trackIndexObserver= index -> {
        if(!this.playlistDataModel.playlist.isEmpty()){
            Track currentTrack=this.playlistDataModel.playlist.get(index);
            this.binding.fnpTrackTitleTV.setText(currentTrack.title);
            this.binding.fnpTrackSingerTV.setText(currentTrack.singer);

            // load the poster image
            Glide.with(this.context).load(currentTrack.posterUri).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                    Bitmap orgBitmap= ((BitmapDrawable)drawable).getBitmap();
                    Bitmap newBitmap=Bitmap.createScaledBitmap(orgBitmap,500,500,true);
                    binding.fnpTrackSIV.setImageBitmap(newBitmap);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });

        }
    };

    private final Observer<Integer> trackDurationObserver=duration->{
        this.binding.fnpTrackDurationTV.setText(this.getDurationStr(duration));
        this.binding.fnpPlayerSeekbar.setMin(0);
        this.binding.fnpPlayerSeekbar.setMax(duration);
    };

    private final Observer<Integer> trackPositionObserver=position->{
        this.binding.fnpPlayerSeekbar.setProgress(position);
        this.binding.fnpTrackCurrentPositionTV.setText(this.getDurationStr(position));
    };
    public void setupObservers(LifecycleOwner lifecycleOwner){
        this.playlistDataModel.getCurrentTrackIndex().observe(lifecycleOwner,trackIndexObserver);
        this.playlistDataModel.getTrackDuration().observe(lifecycleOwner,trackDurationObserver);
        this.playlistDataModel.getTrackPosition().observe(lifecycleOwner,trackPositionObserver);
    }



    /*------------------------------------ Common functions ----------------------------------------*/
    private String getDurationStr(int duration){
        int min=duration/60;
        int sec=duration%60;
        return String.format(Locale.ENGLISH,"%02d:%02d",min,sec);
    }


}
