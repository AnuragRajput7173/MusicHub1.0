package developer.anurag.musichub.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import developer.anurag.musichub.R;
import developer.anurag.musichub.custom_adapters.PlaylistTrackAdapter;
import developer.anurag.musichub.data_models.PlaylistDataModel;
import developer.anurag.musichub.databinding.FragmentPlaylistBottomBinding;
import developer.anurag.musichub.listeners.MainActivityListener;
import developer.anurag.musichub.listeners.PlaylistRVItemTouchListener;
import developer.anurag.musichub.listeners.PlaylistTrackRVListener;


public class PlaylistBottomFragment extends BottomSheetDialogFragment {
    private FragmentPlaylistBottomBinding binding;
    private PlaylistTrackAdapter adapter;
    private PlaylistDataModel playlistDataModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentPlaylistBottomBinding.inflate(inflater,container,false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.playlistDataModel=new ViewModelProvider(requireActivity()).get(PlaylistDataModel.class);
        this.setupAdapter();
        this.setupTrackIndexObserver();

    }

    // Function to setup adapter
    private void setupAdapter(){
        this.adapter=new PlaylistTrackAdapter(requireContext(),this.playlistDataModel.playlist);
        this.binding.fpbPlaylistRV.setAdapter(adapter);
        this.adapter.setupClickListener(new PlaylistTrackRVListener() {
            MainActivityListener listener= (MainActivityListener) getContext();
            @Override
            public void onPosterClick(int position) {
                playlistDataModel.setTrackDuration(0);
                playlistDataModel.setTrackPosition(0);
                playlistDataModel.setCurrentTrackIndex(position);
                listener.playTrack();
            }
        });

        // setup touch listener
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new PlaylistRVItemTouchListener(this.adapter,this.playlistDataModel,getContext()));
        itemTouchHelper.attachToRecyclerView(this.binding.fpbPlaylistRV);
    }

    // Function setup track index observer
    private void setupTrackIndexObserver(){
        @SuppressLint("NotifyDataSetChanged") Observer<Integer> trackIndexObserver= index -> {
          this.adapter.setCurrentTrackIndex(index);
          this.adapter.notifyDataSetChanged();
        };
        this.playlistDataModel.getCurrentTrackIndex().observe(requireActivity(),trackIndexObserver);
    }

}