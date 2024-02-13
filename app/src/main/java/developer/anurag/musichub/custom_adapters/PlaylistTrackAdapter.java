package developer.anurag.musichub.custom_adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
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
import developer.anurag.musichub.listeners.PlaylistTrackRVListener;
import developer.anurag.musichub.tracks_fetcher.Track;

public class PlaylistTrackAdapter extends RecyclerView.Adapter<PlaylistTrackAdapter.PlaylistTrackViewHolder> {

    private Context context;
    private ArrayList<Track> playlist;
    private int currentTrackIndex=0;
    private PlaylistTrackRVListener listener;

    public PlaylistTrackAdapter(Context context, ArrayList<Track> playlist) {
        this.context = context;
        this.playlist = playlist;
    }


    @NonNull
    @Override
    public PlaylistTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(this.context).inflate(R.layout.playlist_track_view,parent,false);
        return new PlaylistTrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistTrackViewHolder holder, int position) {
        Track currentTrack=this.playlist.get(position);
        holder.trackTitleTV.setText(currentTrack.title);
        holder.trackSingerTV.setText(currentTrack.singer);
        if(position==this.currentTrackIndex){
            holder.mainContainerGL.setBackgroundResource(R.color.hard_pink_alpha_30);
        }
        else {
            holder.mainContainerGL.setBackgroundResource(R.color.white);
        }
        // load poster image
        Glide.with(this.context).load(currentTrack.posterUri).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                Bitmap orgBitmap= ((BitmapDrawable)drawable).getBitmap();
                Bitmap newBitmap=Bitmap.createScaledBitmap(orgBitmap,150,150,true);
                holder.trackPosterIV.setImageBitmap(newBitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

        if(this.listener!=null){
            holder.mainContainerGL.setOnClickListener(view-> this.listener.onPosterClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return this.playlist.size();
    }

    public void setCurrentTrackIndex(int index){
        this.currentTrackIndex=index;
    }

    public void removeItemAtIndex(int index){
        this.playlist.remove(index);
        notifyItemRemoved(index);
    }
    public void setupClickListener(PlaylistTrackRVListener listener){
        this.listener=listener;
    }
    public static class PlaylistTrackViewHolder extends RecyclerView.ViewHolder{
        public TextView trackTitleTV,trackSingerTV;
        public ImageView trackPosterIV;
        public GridLayout mainContainerGL;

        public PlaylistTrackViewHolder(@NonNull View itemView) {
            super(itemView);
            this.trackTitleTV=itemView.findViewById(R.id.ptvTrackTitleTV);
            this.trackSingerTV=itemView.findViewById(R.id.ptvTrackSingerTV);
            this.trackPosterIV=itemView.findViewById(R.id.ptvTrackIV);
            this.mainContainerGL=itemView.findViewById(R.id.ptvMainContainerLayout);
        }
    }
}
