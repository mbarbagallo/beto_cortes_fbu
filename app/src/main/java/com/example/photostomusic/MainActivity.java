package com.example.photostomusic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

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
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.action_camera:
                        //Toast.makeText(MainActivity.this, "Camera", Toast.LENGTH_SHORT).show();
                        fragment = new CameraFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("key", spotifyToken);
                        fragment.setArguments(bundle);
                        break;
                    case R.id.action_history:
                        //Toast.makeText(MainActivity.this, "History", Toast.LENGTH_SHORT).show();
                        fragment = new SongHistoryFragment();
                        break;
                    case R.id.action_user:
                        //Toast.makeText(MainActivity.this, "User", Toast.LENGTH_SHORT).show();
                        fragment = new UserProfileFragment();
                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                android.R.anim.fade_in,  // enter
                                android.R.anim.fade_out // exit
                        )
                        .replace(R.id.flContainer, fragment)
                        .commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_camera);
    }

    // Check for result of other activities
    // Current use is only for color form activity
    // TODO: Add switch to check which activity returned a result

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COLOR_FORM_REQUEST_CDOE && resultCode == RESULT_OK){
            // TODO: update color wheel when user returns form form
            Log.i(TAG, "Color wheel updated successfully");
        } else {
            Log.e(TAG, "colors did not come through");
        }
    }
}