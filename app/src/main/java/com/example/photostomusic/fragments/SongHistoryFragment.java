package com.example.photostomusic.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.photostomusic.R;

public class SongHistoryFragment extends Fragment {

    // Visual elements of the fragment
    // Currently only holds a temporary button that will be replaced with a Recycler View on
    // the following weeks
    Button btnTempSongDetail;

    public SongHistoryFragment() {
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
        return inflater.inflate(R.layout.fragment_song_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Connect visual and logic parts
        btnTempSongDetail = view.findViewById(R.id.btnTempSongDetail);

        // Button listener that sends to song detail fragment, will be swapped by a Recycler View
        // Interface click handler in coming days.
        btnTempSongDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Swap current fragment with song detail one, add animation to transition
                SongDetailFragment nextFrag = new SongDetailFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                android.R.anim.fade_in,  // enter
                                android.R.anim.fade_out // exit
                        )
                        .replace(R.id.flContainer, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}