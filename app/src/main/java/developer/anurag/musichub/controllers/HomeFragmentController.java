package developer.anurag.musichub.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import developer.anurag.musichub.custom_adapters.ColumnTrackAdapter;
import developer.anurag.musichub.custom_adapters.FH_RV_ColumnItemDecoration;
import developer.anurag.musichub.custom_adapters.FH_RV_RowItemDecoration;
import developer.anurag.musichub.custom_adapters.RowTrackAdapter;
import developer.anurag.musichub.data_models.PlaylistDataModel;
import developer.anurag.musichub.data_models.TrackDataModel;
import developer.anurag.musichub.databinding.FragmentHomeBinding;
import developer.anurag.musichub.listeners.ColumnTrackRVListener;
import developer.anurag.musichub.listeners.MainActivityListener;
import developer.anurag.musichub.listeners.RowTrackRVListener;
import developer.anurag.musichub.listeners.TracksFetchListener;
import developer.anurag.musichub.tracks_fetcher.Track;
import developer.anurag.musichub.tracks_fetcher.TrackFetcher;

public class HomeFragmentController {
    private final Context context;
    private final TrackDataModel trackDataModel;
    private final FragmentHomeBinding binding;
    private ColumnTrackAdapter topPicksAdapter,recommendedAdapter;
    private RowTrackAdapter quickPicksAdapter;
    private final PlaylistDataModel playlistDataModel;
    private MainActivityListener mainActivityListener=null;
    public HomeFragmentController(Context context, FragmentActivity owner, FragmentHomeBinding binding){
        this.context=context;
        this.trackDataModel=new ViewModelProvider(owner).get(TrackDataModel.class);
        this.playlistDataModel=new ViewModelProvider(owner).get(PlaylistDataModel.class);
        this.binding=binding;

        // setup MainActivityListener
        if((this.context instanceof MainActivityListener)){
            mainActivityListener= (MainActivityListener) this.context;
        }
    }


    //----------------------------------- Function to fetch and set all tracks ----------------------------------
    public void inflateAllTrackRVs(){
        if(!this.trackDataModel.getAllTracks().isEmpty()){
            setupTopPicksRV();
            setupQuickPicksRV();
            setupRecommendedRV();
            setupAdapterListeners();
            binding.fhLoadingBarContainerFL.setAlpha(0);
            binding.fhLoadingBarContainerFL.setVisibility(View.GONE);
            return;
        }

        this.binding.fhLoadingBarContainerFL.setAlpha(1);
        this.binding.fhLoadingBarContainerFL.setVisibility(View.VISIBLE);

        TrackFetcher.fetchAllTracks(this.trackDataModel.getAllTracks(),fetchListener);
    }

    private final TracksFetchListener fetchListener=new TracksFetchListener() {
        @Override
        public void onDoneFetching() {
            trackDataModel.getTopPicksTracks().addAll(TrackFetcher.getRandomTracks(TrackFetcher.TRACK_LIST_SIZE,trackDataModel.getAllTracks()));
            trackDataModel.getQuickPicksTRacks().addAll(TrackFetcher.getRandomTracks(TrackFetcher.TRACK_LIST_SIZE,trackDataModel.getAllTracks()));
            trackDataModel.getRecommendedTracks().addAll(TrackFetcher.getRandomTracks(TrackFetcher.TRACK_LIST_SIZE,trackDataModel.getAllTracks()));
            setupTopPicksRV();
            setupQuickPicksRV();
            setupRecommendedRV();
            setupAdapterListeners();
            binding.fhLoadingBarContainerFL.setAlpha(0);
            binding.fhLoadingBarContainerFL.setVisibility(View.GONE);
            binding.fhMainScrollView.setScrollY(30);
        }
    };

    // Function to setup TopPicks RecyclerView
    private void setupTopPicksRV(){
        this.topPicksAdapter=new ColumnTrackAdapter(context,trackDataModel.getTopPicksTracks(),200);
        LinearLayoutManager topPicksLLManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        FH_RV_ColumnItemDecoration topPicksDecoration=new FH_RV_ColumnItemDecoration(50);
        this.binding.fhTopPicksContainerRV.setLayoutManager(topPicksLLManager);
        this.binding.fhTopPicksContainerRV.setAdapter(topPicksAdapter);
        this.binding.fhTopPicksContainerRV.addItemDecoration(topPicksDecoration);
    }

    // Function to setup QuickPicks RecyclerView
    private void setupQuickPicksRV(){
        this.quickPicksAdapter=new RowTrackAdapter(context,trackDataModel.getQuickPicksTRacks(),170);
        LinearLayoutManager quickPicksLLManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        FH_RV_RowItemDecoration quickPicksDecoration=new FH_RV_RowItemDecoration(50);
        this.binding.fhQuickPicksContainerRV.setLayoutManager(quickPicksLLManager);
        this.binding.fhQuickPicksContainerRV.addItemDecoration(quickPicksDecoration);
        this.binding.fhQuickPicksContainerRV.setAdapter(quickPicksAdapter);
    }

    // Function to setup Recommended RecyclerView
    private void setupRecommendedRV(){
        this.recommendedAdapter=new ColumnTrackAdapter(context,trackDataModel.getRecommendedTracks(),200);
        LinearLayoutManager recommendedLLManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        FH_RV_ColumnItemDecoration recommendedDecoration=new FH_RV_ColumnItemDecoration(50);
        this.binding.fhRecommendedContainerRV.setLayoutManager(recommendedLLManager);
        this.binding.fhRecommendedContainerRV.addItemDecoration(recommendedDecoration);
        this.binding.fhRecommendedContainerRV.setAdapter(recommendedAdapter);
    }

    // Function to setup Adapter listeners
    public void setupAdapterListeners(){

        if (this.mainActivityListener==null)return;

        // set TopPicks click listener
        this.topPicksAdapter.setOnClickListener(index -> {
            PlaylistDataModel.IS_PLAYING=true;
            this.playlistDataModel.playlist.clear();
            this.trackDataModel.getTopPicksTracks().add(0,this.trackDataModel.getTopPicksTracks().remove(index));
            this.playlistDataModel.playlist.addAll(this.trackDataModel.getTopPicksTracks());
            this.playlistDataModel.setTrackPosition(0);
            this.playlistDataModel.setCurrentTrackIndex(0);
            this.playlistDataModel.setTrackPosition(0);
            this.mainActivityListener.loadNowPlayingFragment();
        });

        // set Recommended click listener
        this.recommendedAdapter.setOnClickListener(index -> {
            PlaylistDataModel.IS_PLAYING=true;
            this.playlistDataModel.playlist.clear();
            this.trackDataModel.getRecommendedTracks().add(0,this.trackDataModel.getRecommendedTracks().remove(index));
            this.playlistDataModel.playlist.addAll(this.trackDataModel.getRecommendedTracks());
            this.playlistDataModel.setCurrentTrackIndex(0);
            this.playlistDataModel.setTrackDuration(0);
            this.playlistDataModel.setTrackPosition(0);
            this.mainActivityListener.loadNowPlayingFragment();
        });

        // setup Quick Picks click listener
        this.quickPicksAdapter.setupClickListener(new RowTrackRVListener() {
            @Override
            public void onPosterClick(int index) {
                PlaylistDataModel.IS_PLAYING=true;
                playlistDataModel.playlist.clear();
                trackDataModel.getQuickPicksTRacks().add(0,trackDataModel.getQuickPicksTRacks().remove(index));
                playlistDataModel.playlist.addAll(trackDataModel.getQuickPicksTRacks());
                playlistDataModel.setCurrentTrackIndex(0);
                playlistDataModel.setTrackDuration(0);
                playlistDataModel.setTrackPosition(0);
                mainActivityListener.loadNowPlayingFragment();
            }

            @Override
            public void onAddIconClick(int index) {
                Toast.makeText(context, "Song added in Playlist.", Toast.LENGTH_SHORT).show();
                playlistDataModel.savedPlaylist.add(trackDataModel.getQuickPicksTRacks().get(index));
            }
        });
    }

    /*---------------------------------- Function to setup listeners ---------------------------------*/
    @SuppressLint("NotifyDataSetChanged")
    public void setupListeners(){
        // quick picks play ib click
        this.binding.fhQuickPicksPlayIB.setOnClickListener(view->{
            PlaylistDataModel.IS_PLAYING=true;
            this.playlistDataModel.playlist.clear();
            this.playlistDataModel.playlist.addAll(this.trackDataModel.getQuickPicksTRacks());
            this.playlistDataModel.setCurrentTrackIndex(0);
            this.playlistDataModel.setTrackDuration(0);
            this.playlistDataModel.setTrackPosition(0);
            mainActivityListener.loadNowPlayingFragment();

        });

        this.binding.fhTopPicksPlayBtn.setOnClickListener(view->{
            PlaylistDataModel.IS_PLAYING=true;
            this.playlistDataModel.playlist.clear();
            this.playlistDataModel.playlist.addAll(this.trackDataModel.getTopPicksTracks());
            this.playlistDataModel.setCurrentTrackIndex(0);
            this.playlistDataModel.setTrackDuration(0);
            this.playlistDataModel.setTrackPosition(0);
            mainActivityListener.loadNowPlayingFragment();
        });

        // recommended picks click
        this.binding.fhRecommendedPlayBtn.setOnClickListener(view->{
            PlaylistDataModel.IS_PLAYING=true;
            this.playlistDataModel.playlist.clear();
            this.playlistDataModel.playlist.addAll(this.trackDataModel.getRecommendedTracks());
            this.playlistDataModel.setCurrentTrackIndex(0);
            this.playlistDataModel.setTrackDuration(0);
            this.playlistDataModel.setTrackPosition(0);
            mainActivityListener.loadNowPlayingFragment();
        });

        // setup refreshing listener
        this.binding.fhRefreshLayout.setOnRefreshListener(() -> {
            this.trackDataModel.getRecommendedTracks().clear();
            this.trackDataModel.getQuickPicksTRacks().clear();
            this.trackDataModel.getTopPicksTracks().clear();
            this.trackDataModel.getRecommendedTracks().addAll(TrackFetcher.getRandomTracks(TrackFetcher.TRACK_LIST_SIZE,this.trackDataModel.getAllTracks()));
            this.trackDataModel.getTopPicksTracks().addAll(TrackFetcher.getRandomTracks(TrackFetcher.TRACK_LIST_SIZE,this.trackDataModel.getAllTracks()));
            this.trackDataModel.getQuickPicksTRacks().addAll(TrackFetcher.getRandomTracks(TrackFetcher.TRACK_LIST_SIZE,this.trackDataModel.getAllTracks()));
            this.quickPicksAdapter.notifyDataSetChanged();
            this.topPicksAdapter.notifyDataSetChanged();
            this.recommendedAdapter.notifyDataSetChanged();
            this.binding.fhRefreshLayout.setRefreshing(false);
            this.binding.fhMainScrollView.setScrollY(30);
        });
    }


}
