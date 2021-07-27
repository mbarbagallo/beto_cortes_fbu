package com.example.photostomusic.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.photostomusic.R;

import org.parceler.Parcels;

import java.util.HashMap;

public class RecommendationFragment extends Fragment {

    // TAG used for debugging
    public final String TAG = this.getClass().getSimpleName();

    ImageView ivSongPhoto;

    public RecommendationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommendation, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivSongPhoto = getActivity().findViewById(R.id.ivSongPhoto);

        // Retrieve data (image and genres) from the camera fragment
        Bundle bundle = this.getArguments();
        HashMap<String, String> genres = Parcels.unwrap(bundle.getParcelable("genres"));
        Bitmap photo = Parcels.unwrap(bundle.getParcelable("photo"));

        ivSongPhoto.setImageBitmap(photo);

    }
}