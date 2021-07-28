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
import com.example.photostomusic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
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
    TextView tvSongName;
    TextView tvSongArtist;
    TextView tvSongAlbum;
    ImageView ivSongCover;
    String songName;
    String songArtist;
    String songAlbum;
    String songImage;

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
        tvSongAlbum = getActivity().findViewById(R.id.tvSongAlbum);
        tvSongArtist = getActivity().findViewById(R.id.tvSongArtist);
        tvSongName = getActivity().findViewById(R.id.tvSongName);
        ivSongCover = getActivity().findViewById(R.id.ivSongCover);

        // Retrieve data (image and genres) from the camera fragment
        Bundle bundle = this.getArguments();
        HashMap<String, String> genres = Parcels.unwrap(bundle.getParcelable("genres"));
        Bitmap photo = Parcels.unwrap(bundle.getParcelable("photo"));
        spotifyToken = bundle.getString("key");
        ivSongPhoto.setImageBitmap(photo);

        List<String> genreList = new ArrayList<String>(genres.values());
        String requestUrl = getUrl(genreList);
        final Request request = new Request.Builder()
            .url(requestUrl)
            .addHeader("Authorization", "Bearer " + spotifyToken)
            .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch data: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    // Get the response as a JSON object
                    recommendations = new JSONObject(response.body().string());
                    // Extract the songs as a JSON array
                    songs = recommendations.getJSONArray("tracks");

                    // Get data for the first song (TEST ONLY)
                    // TODO: Switch song if users swipe-rejects current song
                    JSONObject song = (JSONObject) songs.get(0);
                    JSONObject artistData = song.getJSONArray("artists").getJSONObject(0);
                    JSONObject albumData = song.getJSONObject("album");

                    // Store the data of the song on variables
                    songImage = albumData.getJSONArray("images").getJSONObject(0).getString("url");
                    songName = song.getString("name");
                    songArtist = artistData.getString("name");
                    songAlbum = albumData.getString("name");

                    // Call the method to update the View elements.
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            updateUI(songName, songAlbum, songArtist, songImage);
                        }
                    });

                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse data", e);
                }
            }
        });


    }

    // Method used to update the View elements.
    private void updateUI(String songName, String songAlbum, String songArtist, String songImage) {
        tvSongName.setText(songName);
        tvSongArtist.setText(songArtist);
        tvSongAlbum.setText(songAlbum);
        Glide.with(getActivity().getBaseContext())
                .load(songImage)
                .into(ivSongCover);
    }

    // Method to format the endpoint for the Spotify request.
    private String getUrl(List<String> genres){
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.spotify.com/v1/recommendations").newBuilder();
        urlBuilder.addQueryParameter("seed_genres", genres.toString().replace("[", "").replace("]", ""));
        String url = urlBuilder.build().toString();
        return url;
    }
}