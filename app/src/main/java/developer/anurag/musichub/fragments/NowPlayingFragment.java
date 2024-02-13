package developer.anurag.musichub.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import developer.anurag.musichub.controllers.NowPlayingFragmentController;
import developer.anurag.musichub.databinding.FragmentNowPlayingBinding;


public class NowPlayingFragment extends Fragment {
    private FragmentNowPlayingBinding binding;
    private NowPlayingFragmentController controller;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentNowPlayingBinding.inflate(inflater,container,false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.controller=new NowPlayingFragmentController(requireContext(),requireActivity(),this.binding);
        this.controller.setupObservers(requireActivity());
        this.controller.setupIBListeners();
        this.controller.setupSeekbarListener();
        this.controller.setupPlaylistIBListener(getParentFragmentManager());
        this.controller.setupCollapseIBListener(getParentFragmentManager());


    }
}