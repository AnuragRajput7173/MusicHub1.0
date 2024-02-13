package developer.anurag.musichub.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import developer.anurag.musichub.MainActivity;
import developer.anurag.musichub.R;
import developer.anurag.musichub.data_models.PlaylistDataModel;
import developer.anurag.musichub.database.SavedPlaylistDB;
import developer.anurag.musichub.database.UserPlaylist;
import developer.anurag.musichub.databinding.ActivityMainBinding;
import developer.anurag.musichub.fragments.HomeFragment;
import developer.anurag.musichub.fragments.LibraryFragment;
import developer.anurag.musichub.fragments.NowPlayingFragment;
import developer.anurag.musichub.fragments.PlaylistBottomFragment;
import developer.anurag.musichub.fragments.SearchFragment;
import developer.anurag.musichub.listeners.MainActivityListener;
import developer.anurag.musichub.tracks_fetcher.Track;

public class MainActivityController{
    private Context context;
    private ActivityMainBinding binding;
    private PlaylistDataModel playlistDataModel;

    public MainActivityController(Context context, ViewModelStoreOwner storeOwner, ActivityMainBinding binding, LifecycleOwner lifecycleOwner){
        this.context=context;
        this.binding=binding;
        this.playlistDataModel=new ViewModelProvider(storeOwner).get(PlaylistDataModel.class);

        // init views
        if(MainActivity.MINI_PLAYER_VISIBILITY){
            this.binding.amCurrentPlayingTrackGL.setVisibility(View.VISIBLE);
            this.binding.amCurrentPlayingTrackGLTopBorderV.setVisibility(View.VISIBLE);
        }else {
            this.binding.amCurrentPlayingTrackGL.setVisibility(View.GONE);
            this.binding.amCurrentPlayingTrackGLTopBorderV.setVisibility(View.GONE);
        }

        // setup mini player observer
        this.playlistDataModel.getCurrentTrackIndex().observe(lifecycleOwner,trackIndexObserver);
        this.setupMiniPlayer();

    }


    /* ------------------------------ Functions to setup listeners ---------------------------------- */
    public void onHomeIconClick(FragmentManager manager){
        this.binding.amHomeIB.setImageResource(R.drawable.home_selected_icon);
        this.binding.amSearchIB.setImageResource(R.drawable.search_unselected_icon);
        this.binding.amLibraryIB.setImageResource(R.drawable.library_unselected_icon);
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.amMainFragmentContainer, HomeFragment.class,null);
        transaction.commit();
    }

    public void onSearchIconClick(FragmentManager manager){
        this.binding.amSearchIB.setImageResource(R.drawable.search_selected_icon);
        this.binding.amHomeIB.setImageResource(R.drawable.home_unselected_icon);
        this.binding.amLibraryIB.setImageResource(R.drawable.library_unselected_icon);
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.amMainFragmentContainer, SearchFragment.class,null);
        transaction.commit();
    }

    public void onLibraryIconClick(FragmentManager manager){
        this.binding.amLibraryIB.setImageResource(R.drawable.library_selected_icon);
        this.binding.amHomeIB.setImageResource(R.drawable.home_unselected_icon);
        this.binding.amSearchIB.setImageResource(R.drawable.search_unselected_icon);
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.cf_fade_in,R.anim.cf_fade_out,R.anim.cf_fade_in,R.anim.cf_fade_out);
        transaction.replace(R.id.amMainFragmentContainer, LibraryFragment.class,null);
        transaction.commit();
    }

    public void loadNowPlayingFragment(FragmentManager manager){
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.npf_slide_in,R.anim.cf_fade_out,R.anim.cf_fade_in,R.anim.npf_slide_out);
        transaction.replace(R.id.amMainFragmentContainer,NowPlayingFragment.class,null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setupFragmentManagerListener(FragmentManager manager){
        manager.addFragmentOnAttachListener((fragmentManager, fragment) -> {
            this.showHideBottomNavbarAndMiniPlayer(fragment);
            setupMiniPlayerState();
        });
        manager.addOnBackStackChangedListener(() -> {
            this.showHideBottomNavbarAndMiniPlayer(manager.findFragmentById(R.id.amMainFragmentContainer));
            setupMiniPlayerState();
        });

    }

    private void showHideBottomNavbarAndMiniPlayer(Fragment fragment){
        if ((fragment instanceof NowPlayingFragment) || (fragment instanceof PlaylistBottomFragment)) {
            this.binding.amBottomNavbarFlow.setVisibility(View.GONE);
            this.binding.amBottomNavbarFlowBorderTopV.setVisibility(View.GONE);
            this.binding.amCurrentPlayingTrackGL.setVisibility(View.GONE);
            this.binding.amCurrentPlayingTrackGLTopBorderV.setVisibility(View.GONE);
        }else {
            if(!this.playlistDataModel.playlist.isEmpty()){
                this.binding.amCurrentPlayingTrackGL.setVisibility(View.VISIBLE);
                this.binding.amCurrentPlayingTrackGLTopBorderV.setVisibility(View.VISIBLE);
            }
            this.binding.amBottomNavbarFlowBorderTopV.setVisibility(View.VISIBLE);
            this.binding.amBottomNavbarFlow.setVisibility(View.VISIBLE);
        }
    }

 /*------------------------------------ Setup mini player -------------------------------------*/

    private void setupMiniPlayer(){
        MainActivityListener listener=(MainActivityListener) context;

        // on play pause click
        this.binding.amCurrentPlayingTrackPausePlayIB.setOnClickListener(view->{
            if(PlaylistDataModel.IS_PLAYING)this.binding.amCurrentPlayingTrackPausePlayIB.setImageResource(R.drawable.play_icon);
            else this.binding.amCurrentPlayingTrackPausePlayIB.setImageResource(R.drawable.pause_icon);
            listener.playPausePlayer();
        });

    }
    private void setupMiniPlayerState(){
        if(PlaylistDataModel.IS_PLAYING)this.binding.amCurrentPlayingTrackPausePlayIB.setImageResource(R.drawable.pause_icon);
        else this.binding.amCurrentPlayingTrackPausePlayIB.setImageResource(R.drawable.play_icon);
    }

    // Create observer for mini player
    private final Observer<Integer> trackIndexObserver= index -> {
      if(!this.playlistDataModel.playlist.isEmpty()){
          Track currentTrack=this.playlistDataModel.playlist.get(index);
          this.binding.amCurrentPlayingTrackSingerTV.setText(currentTrack.singer);
          this.binding.amCurrentPlayingTrackTitleTV.setText(currentTrack.title);

          // load poster
          Glide.with(this.context).load(currentTrack.posterUri).into(new CustomTarget<Drawable>() {
              @Override
              public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                  Bitmap orgBitmap= ((BitmapDrawable)drawable).getBitmap();
                  Bitmap newBitmap=Bitmap.createScaledBitmap(orgBitmap,150,150,true);
                binding.amCurrentPlayingTrackIV.setImageBitmap(newBitmap);
              }

              @Override
              public void onLoadCleared(@Nullable Drawable placeholder) {

              }
          });
      }
    };

    // Functions to save and fetch saved playlist
    public void fetchSavedPlaylist(){
        Thread thread=new Thread(() -> {
            this.playlistDataModel.savedPlaylist.clear();
            SavedPlaylistDB helper=SavedPlaylistDB.getHelper(this.context);
            List<UserPlaylist> list=helper.userPlaylistDAO().getPlaylist();
            for(UserPlaylist userTrack:list){
                Track track=new Track(userTrack.trackID,userTrack.title, userTrack.singer, Uri.parse(userTrack.trackUri),Uri.parse(userTrack.posterUri));
                this.playlistDataModel.savedPlaylist.add(track);
            }
        });
        thread.start();

    }

    public void savePlaylist(){
        Thread thread=new Thread(() -> {
            SavedPlaylistDB helper=SavedPlaylistDB.getHelper(this.context);
            helper.userPlaylistDAO().clearSavedPlaylist();
            for(Track track:this.playlistDataModel.savedPlaylist){
                helper.userPlaylistDAO().saveTrack(new UserPlaylist(track.getId(),track.trackUri.toString(),track.posterUri.toString(),track.title,track.singer));
            }
        });
        thread.start();
    }





}
