package com.example.photostomusic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

// Class used to adapt data from the passed JSONObjects to the cards.
public class CardStackAdapter extends ArrayAdapter {

    // Visual elements of each card
    TextView tvSongName;
    TextView tvSongArtist;
    TextView tvSongAlbum;
    ImageView ivSongCover;

    // Constructor of the class
    public CardStackAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    // Connect visual and logic elements, fill each card with its corresponding data
    public View getView(int position, final View contentView, ViewGroup parent){
        List<String> songData = (List<String>) getItem(position);
        tvSongName = contentView.findViewById(R.id.tvSongName);
        tvSongAlbum = contentView.findViewById(R.id.tvSongAlbum);
        tvSongArtist = contentView.findViewById(R.id.tvSongArtist);
        ivSongCover = contentView.findViewById(R.id.ivSongCover);

        Glide.with(contentView.getContext())
                .load(songData.get(0))
                .into(ivSongCover);

        tvSongName.setText(songData.get(1));
        tvSongArtist.setText(songData.get(2));
        tvSongAlbum.setText(songData.get(3));

        return contentView;
    }
}
