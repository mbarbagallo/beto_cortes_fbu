package com.example.photostomusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.photostomusic.fragments.CameraFragment;
import com.example.photostomusic.fragments.SongHistoryFragment;
import com.example.photostomusic.fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    // Visual elements of the activity
    BottomNavigationView bottomNavigationView;

    // String used to capture the token passed through the Login intent
    String spotifyToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fragment manager for bottom navigation view
        FragmentManager fragmentManager = getSupportFragmentManager();


        // Connect visual and logic parts
        //btnLogout = findViewById(R.id.btnLogout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Get token from the intent extras
        spotifyToken = getIntent().getStringExtra("token");

        // Set a listener for the bottom navigation view and each of its options, Android Studio
        // shows the method as deprecated but the docs do not mark it as such.
        // https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView.OnNavigationItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.action_camera:
                        Toast.makeText(MainActivity.this, "Camera", Toast.LENGTH_SHORT).show();
                        fragment = new CameraFragment();
                        break;
                    case R.id.action_history:
                        Toast.makeText(MainActivity.this, "History", Toast.LENGTH_SHORT).show();
                        fragment = new SongHistoryFragment();
                        break;
                    case R.id.action_user:
                        Toast.makeText(MainActivity.this, "User", Toast.LENGTH_SHORT).show();
                        fragment = new UserProfileFragment();
                        break;
                    default:
                        fragment = new CameraFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_camera);
    }
}