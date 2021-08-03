package com.example.photostomusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

import models.Song;

// Recycler view custom adapter class
public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {

    // Interface to navigate to detail view of each song
    public interface SongInteractionListener {
        void onSongClicked(int position);
    }

    // Constructor for the class
    public LikesAdapter(SongInteractionListener listener, Context context, List<Song> songs) {
        this.listener = listener;
        this.context = context;
        this.songs = songs;
    }

    // Objects needed for the class
    SongInteractionListener listener;
    Context context;
    List<Song> songs;

    @NonNull
    @Override
    // Get RV from layout on the SongHistoryFragment
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.liked_song_item, parent, false);
        return new ViewHolder(view);
    }

    // Method to call the binder for each song
    @Override
    public void onBindViewHolder(@NonNull LikesAdapter.ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.bind(song);
    }

    // Get the total number of songs on the array
    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Visual elements of each row of the recycler view
        ImageView ivSongListPhoto;
        ImageView ivSongListCover;
        TextView tvSongListName;
        TextView tvSongListArtist;
        TextView tvSongListAlbum;
        MaterialCardView cvSongList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Visual elements are conneceted with a logical part
            ivSongListPhoto = itemView.findViewById(R.id.ivSongListPhoto);
            ivSongListCover = itemView.findViewById(R.id.ivSongListCover);
            tvSongListAlbum = itemView.findViewById(R.id.tvSongListAlbum);
            tvSongListArtist = itemView.findViewById(R.id.tvSongListArtist);
            tvSongListName = itemView.findViewById(R.id.tvSongListName);
            cvSongList = itemView.findViewById(R.id.cvSongList);
        }

        // Bind all the data of each song to the card row
        public void bind(Song song) {

            // Simple text views
            tvSongListName.setText(song.getSongName());
            tvSongListArtist.setText(song.getSongArtist());
            tvSongListAlbum.setText(song.getSongAlbum());

            // Load images with glide, album cover is a direct URL load
            Glide.with(context).load(song.getSongCover()).into(ivSongListCover);

            // Photo file is a bit more complex, the file must be pulled, then its data must be
            // extracted as a byte array, later on to be built into a Bitmap that is sent to its
            // corresponding container
            ParseFile file = song.getPicture();
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                    Glide.with(context).asBitmap().load(bitmap).into(ivSongListPhoto);
                }
            });


            // If the card was clicked then the interface method is called;
            // this is implemented on the SongHistoryFragment but ultimately sends to the
            // SongDetailFragment with the corresponding song.
            cvSongList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSongClicked(getAdapterPosition());
                }
            });
        }
    }
}
