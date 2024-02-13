package developer.anurag.musichub.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import developer.anurag.musichub.R;
import developer.anurag.musichub.custom_adapters.ColumnTrackAdapter;
import developer.anurag.musichub.custom_adapters.FS_RV_ColumnItemDecoration;
import developer.anurag.musichub.data_models.PlaylistDataModel;
import developer.anurag.musichub.data_models.TrackDataModel;
import developer.anurag.musichub.databinding.FragmentSearchBinding;
import developer.anurag.musichub.listeners.ColumnTrackRVListener;
import developer.anurag.musichub.listeners.MainActivityListener;
import developer.anurag.musichub.tracks_fetcher.Track;
import developer.anurag.musichub.tracks_fetcher.TrackFetcher;


public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private TrackDataModel trackDataModel;
    private PlaylistDataModel playlistDataModel;
    private ColumnTrackAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentSearchBinding.inflate(inflater,container,false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.playlistDataModel=new ViewModelProvider(requireActivity()).get(PlaylistDataModel.class);
        this.trackDataModel=new ViewModelProvider(requireActivity()).get(TrackDataModel.class);

        // setup adapter
        this.adapter=new ColumnTrackAdapter(requireContext(),this.trackDataModel.getSearchedTracks(),300);
        FS_RV_ColumnItemDecoration decoration=new FS_RV_ColumnItemDecoration(50,100);
        this.binding.fsSearchedTracksContainerRV.addItemDecoration(decoration);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        this.binding.fsSearchedTracksContainerRV.setLayoutManager(gridLayoutManager);
        this.binding.fsSearchedTracksContainerRV.setAdapter(adapter);
        this.setupSearchBtnListener();
        loadPreviousSearchedQuery();
        setupAdapterListener();

        // setup query views
        this.binding.fsSearchSongSV.setQuery(this.trackDataModel.searchedQuery,false);
        this.binding.fsSearchedQueryTV.setText(this.trackDataModel.searchedQuery);
    }

    // Function to load previous searched query
    private void loadPreviousSearchedQuery(){
        if(this.trackDataModel.getSearchedTracks().isEmpty()){
            return;
        }
        this.binding.fsSearchedTracksContainerRV.setAdapter(this.adapter);
    }

    // Function to setup search button listener
    private void setupSearchBtnListener(){
        this.binding.fsSearchSongSV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextSubmit(String query) {
                trackDataModel.searchedQuery=query;
                binding.fsSearchedQueryTV.setText(query);
                trackDataModel.getSearchedTracks().clear();
                for(Track track:trackDataModel.getAllTracks()) {
                    if (track.singer.trim().toLowerCase().contains(query.trim().toLowerCase()) || track.title.trim().toLowerCase().contains(query.trim().toLowerCase())) {
                        trackDataModel.getSearchedTracks().add(track);
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // FUnction to setup adapter listener
    private void setupAdapterListener(){
        this.adapter.setOnClickListener(index -> {
            MainActivityListener listener= (MainActivityListener) getContext();
            this.playlistDataModel.playlist.clear();
            this.playlistDataModel.playlist.add(this.trackDataModel.getSearchedTracks().get(index));
            this.playlistDataModel.playlist.addAll(TrackFetcher.getRandomTracks(TrackFetcher.TRACK_LIST_SIZE,this.trackDataModel.getAllTracks()));
            this.playlistDataModel.setTrackDuration(0);
            this.playlistDataModel.setTrackPosition(0);
            this.playlistDataModel.setCurrentTrackIndex(0);
            PlaylistDataModel.IS_PLAYING=true;
            assert listener != null;
            listener.loadNowPlayingFragment();

        });
    }





}