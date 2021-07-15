package com.example.photostomusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    // Visual elements of the activity
    Button btnLogout;
    BottomNavigationView bottomNavigationView;

    // String used to capture the token passed through the Login intent
    String spotifyToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect visual and logic parts
        btnLogout = findViewById(R.id.btnLogout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Get token from the intent extras
        spotifyToken = getIntent().getStringExtra("token");

        // Logout of the Parse backend, looking for ways to logout of the Spotify API, not sure
        // if possible as it is not present on the docs.
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}