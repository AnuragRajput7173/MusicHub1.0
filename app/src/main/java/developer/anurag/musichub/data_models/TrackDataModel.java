package developer.anurag.musichub.data_models;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import developer.anurag.musichub.tracks_fetcher.Track;

public class TrackDataModel extends ViewModel {


    private final ArrayList<Track> allTracks=new ArrayList<>();
    public String searchedQuery="";
    private final ArrayList<Track> topPicksTracks=new ArrayList<>();
    private final ArrayList<Track> quickPicksTRacks=new ArrayList<>();
    private final ArrayList<Track> recommendedTracks=new ArrayList<>();
    private final ArrayList<Track> searchedTracks=new ArrayList<>();

    public void setAllTracks(ArrayList<Track> tracks){
        this.allTracks.addAll(tracks);
    }

    public void setTopPicksTracks(ArrayList<Track> tracks){
        this.topPicksTracks.addAll(tracks);
    }

    public void setSearchedTracks(ArrayList<Track> searchedTracks) {
        this.searchedTracks.addAll(searchedTracks);
    }

    public void setQuickPicksTRacks(ArrayList<Track> tracks){
        this.quickPicksTRacks.addAll(tracks);
    }
    public void setRecommendedTracks(ArrayList<Track> tracks){
        this.recommendedTracks.addAll(tracks);}
    public ArrayList<Track> getAllTracks() {
        return this.allTracks;
    }

    public ArrayList<Track> getRecommendedTracks() {
        return this.recommendedTracks;
    }

    public ArrayList<Track> getTopPicksTracks() {
        return this.topPicksTracks;
    }

    public ArrayList<Track> getQuickPicksTRacks() {
        return this.quickPicksTRacks;
    }

    public ArrayList<Track> getSearchedTracks() {
        return searchedTracks;
    }
}
