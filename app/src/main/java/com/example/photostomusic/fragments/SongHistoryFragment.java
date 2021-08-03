package com.example.photostomusic.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.photostomusic.LikesAdapter;
import com.example.photostomusic.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import models.Song;

public class SongHistoryFragment extends Fragment implements LikesAdapter.SongInteractionListener {

    public final String TAG = this.getClass().getSimpleName();

    // Visual elements of the fragment
    RecyclerView rvSongs;
    LikesAdapter likesAdapter;
    List<Song> songs;

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
        rvSongs = view.findViewById(R.id.rvSongs);
        songs = new ArrayList<>();
        fetchLikes();
        likesAdapter = new LikesAdapter(this, getContext(), songs);
        rvSongs.setAdapter(likesAdapter);
        rvSongs.setLayoutManager(new LinearLayoutManager(getContext()));
        /*
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
        });*/
    }
    private void fetchLikes(){
        ParseQuery<Song> query = ParseQuery.getQuery(Song.class);
        query.include(Song.KEY_USER);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Song>() {
            @Override
            public void done(List<Song> objects, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Could not get likes", e);
                }
                songs.clear();
                songs.addAll(objects);
                likesAdapter.notifyDataSetChanged();
                Log.d(TAG, songs.toString());
            }
        });
    }

    @Override
    public void onSongClicked(int position) {
        SongDetailFragment fragment = new SongDetailFragment();

    }
}