package com.example.photostomusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import models.Song;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {

    // TODO: Implement tap to go to detail fragment
    public interface SongInteractionListener {
        void onSongClicked(int position);
    }

    public LikesAdapter(SongInteractionListener listener, Context context, List<Song> songs) {
        this.listener = listener;
        this.context = context;
        this.songs = songs;
    }

    SongInteractionListener listener;
    Context context;
    List<Song> songs;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.liked_song_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikesAdapter.ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSongListPhoto;
        ImageView ivSongListCover;
        TextView tvSongListName;
        TextView tvSongListArtist;
        TextView tvSongListAlbum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivSongListPhoto = itemView.findViewById(R.id.ivSongListPhoto);
            ivSongListCover = itemView.findViewById(R.id.ivSongListCover);
            tvSongListAlbum = itemView.findViewById(R.id.tvSongListAlbum);
            tvSongListArtist = itemView.findViewById(R.id.tvSongListArtist);
            tvSongListName = itemView.findViewById(R.id.tvSongListName);
        }

        public void bind(Song song) {
            tvSongListName.setText(song.getSongName());
            tvSongListArtist.setText(song.getSongArtist());
            tvSongListAlbum.setText(song.getSongAlbum());

            // TODO: Set images from Parse
            String temp_url = "https://www.facebook.com/images/fb_icon_325x325.png";
            Glide.with(context).load(temp_url).into(ivSongListCover);
            Glide.with(context).load(temp_url).into(ivSongListPhoto);

            ivSongListPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSongClicked(getAdapterPosition());
                }
            });
        }
    }
}
