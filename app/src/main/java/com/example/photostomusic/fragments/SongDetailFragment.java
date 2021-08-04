package com.example.photostomusic.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.photostomusic.R;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.io.IOException;

import models.Song;

public class SongDetailFragment extends Fragment {

    // Visual elements of the view
    Song song;
    ImageView ivSongDetailPhoto;
    ImageView ivSongDetailCover;
    TextView tvSongDetailName;
    TextView tvSongDetailArtist;
    TextView tvSongDetailAlbum;
    String preview_url;
    MediaPlayer mp;


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

        // Get song that was passed for this fragment
        Bundle bundle = this.getArguments();
        song = Parcels.unwrap(bundle.getParcelable("song"));

        // Connect visual and logical elements in fragment
        ivSongDetailPhoto = view.findViewById(R.id.ivSongDetailPhoto);
        ivSongDetailCover = view.findViewById(R.id.ivSongDetailCover);
        tvSongDetailName = view.findViewById(R.id.tvSongDetailName);
        tvSongDetailArtist = view.findViewById(R.id.tvSongDetailArtist);
        tvSongDetailAlbum = view.findViewById(R.id.tvSongDetailAlbum);

        // Load data extracted from the song into visual containers
        tvSongDetailName.setText(song.getSongName());
        tvSongDetailArtist.setText(song.getSongArtist());
        tvSongDetailAlbum.setText(song.getSongAlbum());

        // Load the album cover URL
        Glide.with(getContext()).load(song.getSongCover()).into(ivSongDetailCover);

        // Photo file is a bit more complex, the file must be pulled, then its data must be
        // extracted as a byte array, later on to be built into a Bitmap that is sent to its
        // corresponding container
        ParseFile file = song.getPicture();
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                Glide.with(getContext()).asBitmap().load(bitmap).into(ivSongDetailPhoto);
            }
        });

        preview_url = song.getPreviewUrl();
        mp = new MediaPlayer();
        try {
            mp.setDataSource(preview_url);
            mp.prepare();
            mp.start();
            Toast.makeText(getContext(), "Playing song preview! :)", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Song has no preview :(", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        mp.stop();
        mp.reset();
    }
}