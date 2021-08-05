package com.example.photostomusic.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photostomusic.Activities.ColorFormActivity;
import com.example.photostomusic.Activities.LoginActivity;
import com.example.photostomusic.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Song;

public class UserProfileFragment extends Fragment {

    // Class name used as TAG for debugging
    public final String TAG = this.getClass().getSimpleName();

    public static final int COLOR_FORM_REQUEST_CDOE = 220700;

    // Visual elements of the fragment
    Button btnLogout;
    Button btnResetColorForm;
    HashMap<String, String> colorRelation;
    TextView tvUserName;
    ImageView ivUserImage;
    TextView emotionName1;
    TextView emotionName2;
    TextView emotionName3;
    TextView emotionName4;
    TextView emotionName5;
    TextView emotionName6;
    TextView emotionName7;
    TextView emotionName8;
    ImageView colorCircle1;
    ImageView colorCircle2;
    ImageView colorCircle3;
    ImageView colorCircle4;
    ImageView colorCircle5;
    ImageView colorCircle6;
    ImageView colorCircle7;
    ImageView colorCircle8;

    public UserProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Buttons to interact with the fragment
        btnLogout = view.findViewById(R.id.btnLogout);
        btnResetColorForm = view.findViewById(R.id.btnResetColorForm);

        // Data from the user
        tvUserName = view.findViewById(R.id.tvUserName);
        ivUserImage = view.findViewById(R.id.ivUserImage);

        // Text Views inside the cards to match the cards
        emotionName1 = view.findViewById(R.id.emotionName1);
        emotionName2 = view.findViewById(R.id.emotionName2);
        emotionName3 = view.findViewById(R.id.emotionName3);
        emotionName4 = view.findViewById(R.id.emotionName4);
        emotionName5 = view.findViewById(R.id.emotionName5);
        emotionName6 = view.findViewById(R.id.emotionName6);
        emotionName7 = view.findViewById(R.id.emotionName7);
        emotionName8 = view.findViewById(R.id.emotionName8);

        // Color icons to be filled with the color of each emotion
        colorCircle1 = view.findViewById(R.id.colorCircle1);
        colorCircle2 = view.findViewById(R.id.colorCircle2);
        colorCircle3 = view.findViewById(R.id.colorCircle3);
        colorCircle4 = view.findViewById(R.id.colorCircle4);
        colorCircle5 = view.findViewById(R.id.colorCircle5);
        colorCircle6 = view.findViewById(R.id.colorCircle6);
        colorCircle7 = view.findViewById(R.id.colorCircle7);
        colorCircle8 = view.findViewById(R.id.colorCircle8);

        // List of text views to get them all in a single loop
        List<TextView> emotions = new ArrayList<>(
                Arrays.asList(
                        emotionName1,
                        emotionName2,
                        emotionName3,
                        emotionName4,
                        emotionName5,
                        emotionName6,
                        emotionName7,
                        emotionName8
                )
        );

        // List of image views to get them all in a single loop
        List<ImageView> colors = new ArrayList<>(
                Arrays.asList(
                        colorCircle1,
                        colorCircle2,
                        colorCircle3,
                        colorCircle4,
                        colorCircle5,
                        colorCircle6,
                        colorCircle7,
                        colorCircle8
                )
        );

        // Get current user and then its color relation
        ParseUser user = ParseUser.getCurrentUser();
        colorRelation = (HashMap<String, String>) user.get("ColorRelation");

        // Loop through the color relation, assign each key and value to its corresponding
        // text and image views. Using an index as iterator and increasing by one for each entry
        // on the hashmap.
        int idx = 0;
        for (Map.Entry<String, String> entry: colorRelation.entrySet()){
            emotions.get(idx).setText(entry.getValue());
            colors.get(idx).setColorFilter(Color.parseColor("#ff" + entry.getKey()));
            idx++;
        }

        // Set welcome message with the name of the user.
        tvUserName.setText(String.format("Hello %s!", user.getUsername()));

        // Logout of the Parse backend, looking for ways to logout of the Spotify API, not sure
        // if possible as it is not present on the docs.
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                getActivity().finish();
            }
        });

        // Go into color form activity to reset current color map
        btnResetColorForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ColorFormActivity.class);
                getActivity().startActivityForResult(i, COLOR_FORM_REQUEST_CDOE);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


    }
}