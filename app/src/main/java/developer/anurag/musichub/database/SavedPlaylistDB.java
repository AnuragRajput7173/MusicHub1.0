package developer.anurag.musichub.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserPlaylist.class},version = 1)
public abstract class SavedPlaylistDB extends RoomDatabase {
    private static SavedPlaylistDB helper;
    public final static String DATABASE_NAME="user_playlist_db";

    public static synchronized SavedPlaylistDB getHelper(Context context){
        if(helper==null){
            helper= Room.databaseBuilder(context, SavedPlaylistDB.class,DATABASE_NAME).build();
        }
        return helper;
    }

    public abstract UserPlaylistDAO userPlaylistDAO();
}
