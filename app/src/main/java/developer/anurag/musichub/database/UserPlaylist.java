package developer.anurag.musichub.database;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.net.URI;

@Entity(tableName = "user_playlist")
public class UserPlaylist {
    @PrimaryKey
    @ColumnInfo(name = "trackID")
    public int trackID;
    @ColumnInfo(name = "trackUri")
    public String trackUri;
    @ColumnInfo(name = "posterUri")
    public String posterUri;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "singer")
    public String singer;


    public int getTrackID() {
        return trackID;
    }

    public void setTrackID(int trackID) {
        this.trackID = trackID;
    }

    public String getTrackUri() {
        return trackUri;
    }

    public void setTrackUri(String trackUri) {
        this.trackUri = trackUri;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
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

    public UserPlaylist(int trackID, String trackUri, String posterUri, String title, String singer) {
        this.trackID = trackID;
        this.trackUri = trackUri;
        this.posterUri = posterUri;
        this.title = title;
        this.singer = singer;
    }
}
