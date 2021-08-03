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
import com.parse.ParseUser;

import org.parceler.Parcels;

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

        // Define empty list to hold songs
        songs = new ArrayList<>();

        // Fetch songs liked by the user from the DB
        fetchLikes();

        // Instantiate an adapter and set it to the RV
        likesAdapter = new LikesAdapter(this, getContext(), songs);
        rvSongs.setAdapter(likesAdapter);

        // Add a simple linear layout manager for the rows
        rvSongs.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    // Method used to get liked songs from the DB
    private void fetchLikes(){
        // Create a new query to parse
        ParseQuery<Song> query = ParseQuery.getQuery(Song.class);

        // Only include songs from the current user
        query.include(Song.KEY_USER);
        query.whereContains(Song.KEY_USER, ParseUser.getCurrentUser().getObjectId());

        // Sort by most recent
        // TODO: Add capture date
        query.addDescendingOrder("createdAt");
        // Get the songs
        query.findInBackground(new FindCallback<Song>() {
            @Override
            public void done(List<Song> objects, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Could not get likes", e);
                }
                // Empty song array, add all new songs and notify adapter.
                songs.clear();
                songs.addAll(objects);
                likesAdapter.notifyDataSetChanged();
            }
        });
    }

    // If a song card was clicked go to song detail with this song
    @Override
    public void onSongClicked(int position) {
        SongDetailFragment fragment = new SongDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("song", Parcels.wrap(songs.get(position)));
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,  // enter
                        android.R.anim.fade_out // exit
                )
                .replace(R.id.flContainer, fragment, "findThisFragment")
                .commit();
    }
}