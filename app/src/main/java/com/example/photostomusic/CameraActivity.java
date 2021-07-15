package com.example.photostomusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class CameraActivity extends AppCompatActivity {

    // Visual elements of the activity
    Button btnLogout;

    // String used to capture the token passed through the Login intent
    String spotifyToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Connect visual and logic parts
        btnLogout = findViewById(R.id.btnLogout);

        // Get token from the intent extras
        spotifyToken = getIntent().getStringExtra("token");

        // Logout of the Parse backend, looking for ways to logout of the Spotify API, not sure
        // if possible as it is not present on the docs.
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(CameraActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}