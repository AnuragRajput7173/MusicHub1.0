package developer.anurag.musichub.tracks_fetcher;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

import developer.anurag.musichub.listeners.TracksFetchListener;

public class TrackFetcher {
    public static String TABLE_NAME="track_data";
    public static int TRACK_LIST_SIZE=30;

    public static void fetchAllTracks(ArrayList<Track> tracksList,TracksFetchListener listener){
        DatabaseReference database= FirebaseDatabase.getInstance().getReference(TABLE_NAME);
        Task<DataSnapshot> task=database.get();
        task.addOnSuccessListener(table -> {
            int id=0;String title=null,singer=null,posterUri=null,trackUri=null;
            Iterable<DataSnapshot> rows=table.getChildren();
            for(DataSnapshot row:rows){
                Iterable<DataSnapshot>columns=row.getChildren();
                int counter=0;
                for(DataSnapshot column:columns){
                    switch (counter){
                        case 0:
                            try{
                                id=Integer.parseInt(Objects.requireNonNull(column.getValue()).toString());
                            }catch (NumberFormatException ignore){}
                            break;
                        case 1:
                            try{
                                posterUri= Objects.requireNonNull(column.getValue()).toString();
                            }catch (NullPointerException ignore){};
                            break;
                        case 2:
                            try{
                                singer= Objects.requireNonNull(column.getValue()).toString();
                            }catch (NullPointerException ignore){}
                            break;
                        case 3:
                            try{
                                title= Objects.requireNonNull(column.getValue()).toString();
                            }catch (NumberFormatException ignore){}
                            break;
                        case 4:
                            try{
                                trackUri= Objects.requireNonNull(column.getValue()).toString();
                            }catch (NumberFormatException ignore){}
                            break;
                    }
                    counter++;
                }
                if(title!=null && singer!=null && trackUri!=null && posterUri!=null && id!=0){
                    tracksList.add(new Track(id,title,singer, Uri.parse(trackUri),Uri.parse(posterUri)));
                }
            }
            if(listener!=null){
                listener.onDoneFetching();
            }
        });
    }

    public static HashSet<Track> getRandomTracks(int noOfTracks,ArrayList<Track> allTracks){
        HashSet<Track> randomTracksSet=new HashSet<>();
        Random random=new Random();
        if(noOfTracks>allTracks.size()){
            noOfTracks=allTracks.size();
        }

        for(int i=0;i<noOfTracks;i++){
            int randIndex=random.nextInt(allTracks.size());
            randomTracksSet.add(allTracks.get(randIndex));
        }
        return randomTracksSet;
    }
}
