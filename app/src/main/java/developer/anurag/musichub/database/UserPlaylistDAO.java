package developer.anurag.musichub.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import developer.anurag.musichub.tracks_fetcher.Track;

@Dao
public interface UserPlaylistDAO {
    @Query("SELECT * FROM user_playlist")
    List<UserPlaylist> getPlaylist();

    @Query("DELETE FROM user_playlist")
    void clearSavedPlaylist();

    @Insert
    void saveTrack(UserPlaylist... playlists);



}
