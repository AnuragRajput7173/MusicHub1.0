package developer.anurag.musichub.custom_adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import developer.anurag.musichub.R;
import developer.anurag.musichub.listeners.ColumnTrackRVListener;
import developer.anurag.musichub.tracks_fetcher.Track;

public class ColumnTrackAdapter extends RecyclerView.Adapter<ColumnTrackAdapter.ColumnTrackViewHolder> {
    private final ArrayList<Track> tracksList;
    private final Context context;
    private final int posterSize;
     private ColumnTrackRVListener listener=null;

    public ColumnTrackAdapter(Context context, ArrayList<Track> tracks, int posterSize){
        this.tracksList=tracks;
        this.context=context;
        this.posterSize=posterSize;

    }


    @NonNull
    @Override
    public ColumnTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.column_track_view,parent,false);
        return new ColumnTrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColumnTrackViewHolder viewHolder, int position) {
        Track track=this.tracksList.get(position);
        viewHolder.trackTitleTV.setText(track.title);
        viewHolder.trackTitleTV.setMaxWidth(this.posterSize);
        loadTrackPoster(viewHolder.trackSIV,track.posterUri,this.posterSize);

        if(listener!=null){
            viewHolder.trackSIV.setOnClickListener(v -> {
                this.listener.onViewClick(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.tracksList.size();
    }

    // Function to load track poster
    private void loadTrackPoster(ShapeableImageView imageView, Uri uri,int posterSize){
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

    // Function to set listener
    public void setOnClickListener(ColumnTrackRVListener trackRVListener){
        this.listener=trackRVListener;
    }



//  ----------------------------------------------------- View Holder Class --------------------------------------------------------------

    public static class ColumnTrackViewHolder extends RecyclerView.ViewHolder{
        public final TextView trackTitleTV;
        public final ShapeableImageView trackSIV;
        public ColumnTrackViewHolder(@NonNull View itemView) {
            super(itemView);
            this.trackTitleTV=itemView.findViewById(R.id.ctvTrackTitleTV);
            this.trackSIV=itemView.findViewById(R.id.ctvTrackSIV);
        }


    }
}
