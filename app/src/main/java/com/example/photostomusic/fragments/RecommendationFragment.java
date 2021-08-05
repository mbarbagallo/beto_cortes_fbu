package com.example.photostomusic.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.photostomusic.CardStackAdapter;
import com.example.photostomusic.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wenchao.cardstack.CardStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import models.Song;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecommendationFragment extends Fragment {

    // TAG used for debugging
    public final String TAG = this.getClass().getSimpleName();
    private final OkHttpClient mOkHttpClient = new OkHttpClient();

    ImageView ivSongPhoto;
    String spotifyToken;
    JSONObject recommendations;
    JSONArray songs;
    String songImage;
    String songName;
    String songArtist;
    String songAlbum;
    String songCode;
    String previewUrl;
    CardStack mCardStack;
    CardStackAdapter mCardAdapter;
    Button btnRetakePhoto;
    Button btnFetchSongs;
    MediaPlayer mp;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mp = new MediaPlayer();

        // Visual elements of the fragment
        ivSongPhoto = getActivity().findViewById(R.id.ivSongPhoto);
        mCardStack = getActivity().findViewById(R.id.cardStackContainer);
        btnRetakePhoto = getActivity().findViewById(R.id.btnRetakePhoto);
        btnFetchSongs = getActivity().findViewById(R.id.btnFetchMoreSongs);

        // Set the layout for each card
        mCardStack.setContentResource(R.layout.card_layout);

        // Create a new adapter for the card stack
        mCardAdapter = new CardStackAdapter(getActivity().getBaseContext(),0);

        // Retrieve data (image and genres) from the camera fragment
        Bundle bundle = this.getArguments();
        HashMap<String, String> genres = Parcels.unwrap(bundle.getParcelable("genres"));
        Bitmap photo = Parcels.unwrap(bundle.getParcelable("photo"));

        // Get token from MainActivity
        spotifyToken = bundle.getString("key");

        // Set song photo as the captured image
        ivSongPhoto.setImageBitmap(photo);

        // Call the UI thread to load the adapter and bind the data to the swipeable cards
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                fetchRecommendations(genres, photo);
            }
        });

        // User ran out of recommendations and wants to take a new picture
        btnRetakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraFragment cameraFragment = new CameraFragment();
                Bundle bundle = new Bundle();
                bundle.putString("key", spotifyToken);
                cameraFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                android.R.anim.fade_in,  // enter
                                android.R.anim.fade_out // exit
                        )
                        .replace(R.id.flContainer, cameraFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        // User ran out of recommendations, button clicked to get more from the same picture
        btnFetchSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the UI thread to load the adapter and bind the data to the swipeable cards
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fetchRecommendations(genres, photo);
                    }
                });

            }
        });

    }

    // Method to fetch songs from the Spotify API
    private void fetchRecommendations(HashMap<String , String > genres, Bitmap photo) {
        // Build a request to the Spotify API to get the recommendations
        List<String> genreList = new ArrayList<>(genres.values());
        String requestUrl = getUrl(genreList);
        final Request request = new Request.Builder()
                .url(requestUrl)
                // Add token to the headers with the "Bearer " prefix
                .addHeader("Authorization", "Bearer " + spotifyToken)
                .build();

        // Make the request and check the answer
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            // Request was unsuccessful
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch data: " + e);
            }

            @Override
            // Request was successful, extract data from JSON response
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    // Get the response as a JSON object
                    recommendations = new JSONObject(response.body().string());
                    // Extract the songs as a JSON array
                    songs = recommendations.getJSONArray("tracks");

                    // Loop through songs and send data to the card stack adapter
                    for (int i = 0; i < songs.length(); i++){
                        JSONObject song = songs.getJSONObject(i);
                        JSONObject artistData = song.getJSONArray("artists").getJSONObject(0);
                        JSONObject albumData = song.getJSONObject("album");

                        // Store the data of the song on variables
                        songImage = albumData.getJSONArray("images").getJSONObject(0).getString("url");
                        songName = song.getString("name");
                        songArtist = artistData.getString("name");
                        songAlbum = albumData.getString("name");
                        songCode = song.getString("id");
                        previewUrl = song.getString("preview_url");

                        List<String> songData = new ArrayList<>(Arrays.asList(songImage, songName, songArtist, songAlbum));
                        // Add songs through the UI thread so it later modify the visual elements
                        // of the card stack
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Stuff that updates the UI
                                mCardAdapter.add(songData);
                            }
                        });
                        //mCardAdapter.add(songData);
                    }

                    // Call the UI thread to load the adapter and bind the data to the swipeable cards
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            mCardStack.setAdapter(mCardAdapter);
                        }
                    });



                    // Add a listener to the card stack
                    mCardStack.setListener(new CardStack.CardEventListener() {
                        @Override
                        public boolean swipeEnd(int direction, float distance) {
                            // The output of this function is determined by how much a card
                            // was dragged enough dp distance, if enough then the dismiss function is called,
                            // else the card returns to the top.
                            return distance > 150;
                        }

                        @Override
                        public boolean swipeStart(int direction, float distance) {

                            return true;
                        }

                        @Override
                        public boolean swipeContinue(int direction, float distanceX, float distanceY) {

                            return true;
                        }

                        @Override
                        public void discarded(int mIndex, int direction) {
                            // there are four directions
                            //  0  |  1
                            // ----------
                            //  2  |  3
                            Log.d(TAG, "discarded: " + direction);
                            switch (direction){
                                case 0: case 2:
                                    // Song rejected, it is now removed from stack
                                    stopMusic();
                                    break;
                                case 1: case 3:
                                    // Song accepted, sending to Parse
                                    try {
                                        saveSong(songs.getJSONObject(mIndex-1), genres, photo);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void topCardTapped() {
                            stopMusic();
                            try {
                                JSONObject song = songs.getJSONObject(mCardStack.getCurrIndex());
                                String preview_url = song.getString("preview_url");
                                mp.setDataSource(preview_url);
                                mp.prepare();
                                mp.start();
                                Toast.makeText(getContext(), "Playing song preview! :)", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                Toast.makeText(getContext(), "Song has no preview :(", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse data", e);
                }
            }
        });

    }
    private void stopMusic(){
        mp.stop();
        mp.reset();
    }

    // Method used to save songs to the DB
    private void saveSong(JSONObject data, HashMap<String, String> genres, Bitmap photo) throws JSONException {
        // Create new song
        Song song = new Song();

        JSONObject artistData = data.getJSONArray("artists").getJSONObject(0);
        JSONObject albumData = data.getJSONObject("album");

        songImage = albumData.getJSONArray("images").getJSONObject(0).getString("url");
        songName = data.getString("name");
        songArtist = artistData.getString("name");
        songAlbum = albumData.getString("name");
        songCode = data.getString("id");
        previewUrl = data.getString("preview_url");

        // Add all the string values to the song
        song.setSongName(songName);
        song.setSongCover(songImage);
        song.setSongArtist(songArtist);
        song.setSongAlbum(songAlbum);
        song.setSongCode(songCode);
        song.setPreviewUrl(previewUrl);

        // Extract keys and values of the photo, as colors and genres
        song.setColors(new ArrayList<String>(genres.keySet()));
        song.setGenres(new ArrayList<String>(genres.values()));

        // Song belongs to current user
        song.setUser(ParseUser.getCurrentUser());

        // Convert captured photo to byte array after compressing to PNG,
        // then the byte array is added as a new Parsefile that will
        // be uploaded to the DB
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG,100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile file = new ParseFile("image.png", byteArray);
        song.setPicture(file);

        // Save song in background so it is now on the DB
        song.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){ // Something went wrong
                    Log.e(TAG, "Parse save error", e);
                } else {
                    // Swap fragments to the song history
                    SongHistoryFragment fragment = new SongHistoryFragment();
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(
                                    android.R.anim.fade_in,  // enter
                                    android.R.anim.fade_out // exit
                            )
                            .replace(R.id.flContainer, fragment)
                            .commit();
                }
            }
        });
        mp.stop();
        mp.reset();
    }

    // Method to format the endpoint for the Spotify request.
    private String getUrl(List<String> genres){
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.spotify.com/v1/recommendations").newBuilder();
        urlBuilder.addQueryParameter("seed_genres", genres.toString().replace("[", "").replace("]", ""));
        String url = urlBuilder.build().toString();
        return url;
    }
}