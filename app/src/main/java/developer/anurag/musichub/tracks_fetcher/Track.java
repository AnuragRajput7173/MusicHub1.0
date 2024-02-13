package developer.anurag.musichub.tracks_fetcher;

import android.annotation.SuppressLint;
import android.net.Uri;

@SuppressLint("RestrictedApi")
public class Track{
    int id;
    public Uri trackUri,posterUri;
    public String title,singer;
    public Track(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getTrackUri() {
        return trackUri;
    }

    public void setTrackUri(Uri trackUri) {
        this.trackUri = trackUri;
    }

    public Uri getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(Uri posterUri) {
        this.posterUri = posterUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Track(int id, String title, String singer, Uri trackUri, Uri posterUri){
        this.id=id;
        this.title=title;
        this.singer=singer;
        this.trackUri=trackUri;
        this.posterUri=posterUri;
    }
}
