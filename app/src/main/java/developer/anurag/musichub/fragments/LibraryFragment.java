package developer.anurag.musichub.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import developer.anurag.musichub.custom_adapters.ColumnTrackAdapter;
import developer.anurag.musichub.custom_adapters.FS_RV_ColumnItemDecoration;
import developer.anurag.musichub.data_models.PlaylistDataModel;
import developer.anurag.musichub.databinding.FragmentLibraryBinding;
import developer.anurag.musichub.listeners.ColumnTrackRVListener;
import developer.anurag.musichub.listeners.MainActivityListener;
import developer.anurag.musichub.tracks_fetcher.Track;


public class LibraryFragment extends Fragment {
    private FragmentLibraryBinding binding;
    private PlaylistDataModel playlistDataModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentLibraryBinding.inflate(inflater,container,false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.playlistDataModel=new ViewModelProvider(requireActivity()).get(PlaylistDataModel.class);
        setupLibraryRV();

        // setup play button click listener
        this.binding.flPlayIB.setOnClickListener(v->{
            if(this.playlistDataModel.savedPlaylist.size()<1) {
                Toast.makeText(this.requireContext(), "No songs in Playlist.", Toast.LENGTH_SHORT).show();
                return;
            }
            this.playlistDataModel.playlist.clear();
            this.playlistDataModel.playlist.addAll(this.playlistDataModel.savedPlaylist);
            this.playlistDataModel.setCurrentTrackIndex(0);
            this.playlistDataModel.setTrackPosition(0);
            this.playlistDataModel.setTrackDuration(0);
            PlaylistDataModel.IS_PLAYING=true;
            MainActivityListener listener=(MainActivityListener) requireActivity();
            listener.loadNowPlayingFragment();

        });
    }

    private void setupLibraryRV(){
        ArrayList<Track> savedPlaylist=new ArrayList<>(this.playlistDataModel.savedPlaylist);
        ColumnTrackAdapter adapter=new ColumnTrackAdapter(requireContext(),savedPlaylist,300);
        GridLayoutManager layoutManager=new GridLayoutManager(requireContext(),2);
        FS_RV_ColumnItemDecoration decoration=new FS_RV_ColumnItemDecoration(50,100);
        this.binding.flPlaylistContainerRV.setAdapter(adapter);
        this.binding.flPlaylistContainerRV.setLayoutManager(layoutManager);
        this.binding.flPlaylistContainerRV.addItemDecoration(decoration);

        // setup click listener
        adapter.setOnClickListener(index -> {
            MainActivityListener listener=(MainActivityListener) requireActivity();
            savedPlaylist.add(0,savedPlaylist.remove(index));
            this.playlistDataModel.playlist.clear();
            this.playlistDataModel.playlist.addAll(savedPlaylist);
            this.playlistDataModel.setCurrentTrackIndex(0);
            this.playlistDataModel.setTrackDuration(0);
            this.playlistDataModel.setTrackPosition(0);
            PlaylistDataModel.IS_PLAYING=true;
            listener.loadNowPlayingFragment();
        });
    }


}