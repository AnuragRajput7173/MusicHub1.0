package developer.anurag.musichub.custom_adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import java.util.ArrayList;

import developer.anurag.musichub.R;
import developer.anurag.musichub.listeners.RowTrackRVListener;
import developer.anurag.musichub.tracks_fetcher.Track;

public class RowTrackAdapter extends RecyclerView.Adapter<RowTrackAdapter.RowTrackViewHolder> {

    private final ArrayList<Track> tracksList;
    private final int posterSize;
    private final Context context;
    private RowTrackRVListener listener;
    public RowTrackAdapter(Context context,ArrayList<Track> tracks,int posterSize){
        this.context=context;
        this.posterSize=posterSize;
        this.tracksList=tracks;
    }

    @NonNull
    @Override
    public RowTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_track_view,parent,false);
        return new RowTrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowTrackViewHolder viewHolder, int position) {
        Track track=tracksList.get(position);
        viewHolder.trackTitleTV.setText(track.title);
        viewHolder.trackSingerTV.setText(track.singer);
        this.loadTrackPoster(viewHolder.trackIV,track.posterUri,this.posterSize);
        if(this.listener!=null){
            Log.i("isnull","no it is not null");
            viewHolder.trackIV.setOnClickListener(view->this.listener.onPosterClick(position));
            viewHolder.trackSingerTV.setOnClickListener(view->this.listener.onPosterClick(position));
            viewHolder.trackTitleTV.setOnClickListener(view->this.listener.onPosterClick(position));
            viewHolder.addToPlaylistIB.setOnClickListener(view->this.listener.onAddIconClick(position));
        }else Log.i("isnull","yes it is null");
    }

    @Override
    public int getItemCount() {
        return this.tracksList.size();
    }

    public void setupClickListener(RowTrackRVListener listener){
        this.listener=listener;
    }

    // Function to load track poster
    private void loadTrackPoster(ImageView imageView, Uri uri, int posterSize){
        Glide.with(context).load(uri).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                Bitmap orgBitmap= ((BitmapDrawable)drawable).getBitmap();
                Bitmap newBitmap=Bitmap.createScaledBitmap(orgBitmap,posterSize,posterSize,true);
                imageView.setImageBitmap(newBitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                imageView.setImageDrawable(null);
            }
        });
    }

    public static class RowTrackViewHolder extends RecyclerView.ViewHolder {
        public final ImageView trackIV;
        public final ImageButton addToPlaylistIB;
        public final TextView trackTitleTV,trackSingerTV;
        public RowTrackViewHolder(@NonNull View view) {
            super(view);
            this.trackIV=view.findViewById(R.id.rtvTrackIV);
            this.trackTitleTV=view.findViewById(R.id.rtvTrackTitleTV);
            this.trackSingerTV=view.findViewById(R.id.rtvTrackSingerTV);
            this.addToPlaylistIB=view.findViewById(R.id.rtvAddToPlaylistIB);
        }
    }
}
