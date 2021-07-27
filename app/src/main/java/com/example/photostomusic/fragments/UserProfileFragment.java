package com.example.photostomusic.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.photostomusic.ColorFormActivity;
import com.example.photostomusic.LoginActivity;
import com.example.photostomusic.R;
import com.parse.ParseUser;

public class UserProfileFragment extends Fragment {

    // Class name used as TAG for debugging
    public final String TAG = this.getClass().getSimpleName();

    public static final int COLOR_FORM_REQUEST_CDOE = 220700;

    // Visual elements of the fragment
    Button btnLogout;
    Button btnResetColorForm;


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
        btnLogout = view.findViewById(R.id.btnLogout);
        btnResetColorForm = view.findViewById(R.id.btnResetColorForm);

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