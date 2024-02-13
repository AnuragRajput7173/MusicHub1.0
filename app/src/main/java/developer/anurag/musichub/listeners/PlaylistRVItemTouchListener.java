package developer.anurag.musichub.listeners;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import developer.anurag.musichub.custom_adapters.PlaylistTrackAdapter;
import developer.anurag.musichub.data_models.PlaylistDataModel;

public class PlaylistRVItemTouchListener extends ItemTouchHelper.Callback {
    private final PlaylistTrackAdapter adapter;
    private final PlaylistDataModel playlistDataModel;
    private Context context;
    public PlaylistRVItemTouchListener(PlaylistTrackAdapter adapter,PlaylistDataModel playlistDataModel,Context context){
        this.adapter=adapter;
        this.playlistDataModel=playlistDataModel;
        this.context=context;
    }
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int index=viewHolder.getAbsoluteAdapterPosition();
        if((this.playlistDataModel.getCurrentTrackIndex().getValue()!=null) && (index!=this.playlistDataModel.getCurrentTrackIndex().getValue())){
            this.adapter.removeItemAtIndex(index);
        }
    }
}
