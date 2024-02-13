package developer.anurag.musichub.data_models;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashSet;

import developer.anurag.musichub.tracks_fetcher.Track;

public class PlaylistDataModel extends ViewModel {

    public static boolean IS_PLAYING=false;
    public  final ArrayList<Track> playlist=new ArrayList<>();
    public final HashSet<Track> savedPlaylist=new HashSet<>();
    private final MutableLiveData<Integer> trackDuration=new MutableLiveData<>(0);
    private final MutableLiveData<Integer> trackPosition=new MutableLiveData<>(0);
    private final MutableLiveData<Integer> currentTrackIndex=new MutableLiveData<>(0);


    public void setTrackDuration(int duration) {
        this.trackDuration.setValue(duration);
    }

    public void setTrackPosition(int position) {
        this.trackPosition.setValue(position);
    }

    public void setCurrentTrackIndex(int index) {
        this.currentTrackIndex.setValue(index);
    }

    @NonNull
    public MutableLiveData<Integer> getTrackDuration() {
        return trackDuration;
    }

    @NonNull
    public MutableLiveData<Integer> getTrackPosition() {
        return trackPosition;
    }

    @NonNull
    public MutableLiveData<Integer> getCurrentTrackIndex() {
        return currentTrackIndex;
    }
}
