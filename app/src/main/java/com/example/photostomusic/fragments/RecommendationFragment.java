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
        spotifyToken = bundle.getString("key");

        ivSongPhoto.setImageBitmap(photo);

        Log.d(TAG, spotifyToken);
        List<String> genreList = new ArrayList<String>(genres.values());
        String requestUrl = getUrl(genreList);
        Log.d(TAG, "onViewCreated: " + requestUrl + " " + genreList.toString().replace("[", "").replace("]", ""));
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
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.i(TAG, "onResponse: " + jsonObject.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse data", e);
                }
            }
        });


    }
    private String getUrl(List<String> genres){
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.spotify.com/v1/recommendations").newBuilder();
        urlBuilder.addQueryParameter("seed_genres", genres.toString().replace("[", "").replace("]", ""));
        String url = urlBuilder.build().toString();
        return url;
    }
}