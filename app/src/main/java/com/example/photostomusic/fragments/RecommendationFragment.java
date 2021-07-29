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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.photostomusic.CardStackAdapter;
import com.example.photostomusic.R;
import com.wenchao.cardstack.CardStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    CardStack mCardStack;
    CardStackAdapter mCardAdapter;

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

        // Visual elements of the fragment
        ivSongPhoto = getActivity().findViewById(R.id.ivSongPhoto);
        mCardStack = getActivity().findViewById(R.id.cardStackContainer);

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

                        List<String> songData = new ArrayList<>(Arrays.asList(songImage, songName, songArtist, songAlbum));
                        mCardAdapter.add(songData);
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
                        // TODO: Check for direction with switch case, push song to Parse if 1 or 3 were selected, next song if 0 or 2 were selected.
                        public void discarded(int mIndex, int direction) {
                            // there are four directions
                            //  0  |  1
                            // ----------
                            //  2  |  3
                            Log.d(TAG, "discarded: " + direction);
                        }

                        @Override
                        // TODO: (STRETCH) Add preview of song with Spotify API
                        public void topCardTapped() {
                        }
                    });

                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse data", e);
                }
            }
        });


    }

    // Method to format the endpoint for the Spotify request.
    private String getUrl(List<String> genres){
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.spotify.com/v1/recommendations").newBuilder();
        urlBuilder.addQueryParameter("seed_genres", genres.toString().replace("[", "").replace("]", ""));
        String url = urlBuilder.build().toString();
        return url;
    }
}