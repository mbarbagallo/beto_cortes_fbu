package com.example.photostomusic.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.photostomusic.R;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.Song;

public class SongDetailFragment extends Fragment {

    public final String TAG = this.getClass().getSimpleName();

    // Visual elements of the view
    Song song;
    ImageView ivSongDetailPhoto;
    ImageView ivSongDetailCover;
    TextView tvSongDetailName;
    TextView tvSongDetailArtist;
    TextView tvSongDetailAlbum;
    String preview_url;
    MediaPlayer mp;
    List<CarouselItem> images;
    ImageCarousel carousel;

    public SongDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Connect visual and logical elements in fragment
        tvSongDetailName = view.findViewById(R.id.tvSongDetailName);
        tvSongDetailArtist = view.findViewById(R.id.tvSongDetailArtist);
        tvSongDetailAlbum = view.findViewById(R.id.tvSongDetailAlbum);

        // Get song that was passed for this fragment
        Bundle bundle = this.getArguments();
        song = Parcels.unwrap(bundle.getParcelable("song"));
        // Initialize empty image list
        images = new ArrayList<>();

        // Get photo of the song as bitmap and add to images array
        ParseFile file = song.getPicture();
        Log.d(TAG, "onViewCreated: " + song.getPreviewUrl());
        images.add(
                new CarouselItem(
                    song.getSongCover(),
                    ""
                )
        );
        images.add(
                new CarouselItem(
                    file.getUrl(),
                    ""
                )
        );
        carousel = view.findViewById(R.id.carousel);
        carousel.addData(images);

        // Load data extracted from the song into visual containers
        tvSongDetailName.setText(song.getSongName());
        tvSongDetailArtist.setText(song.getSongArtist());
        tvSongDetailAlbum.setText(song.getSongAlbum());


        mp = new MediaPlayer();
        preview_url = song.getPreviewUrl();
        Log.d(TAG, "onViewCreated: " + preview_url);
        if (preview_url != null){
            try {
                mp.setDataSource(preview_url);
                mp.prepare();
                mp.start();
                Toast.makeText(getContext(), "Playing song preview! :)", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Song has no preview :(", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Song has no preview :(", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onPause() {
        super.onPause();
        mp.stop();
        mp.reset();
    }
}