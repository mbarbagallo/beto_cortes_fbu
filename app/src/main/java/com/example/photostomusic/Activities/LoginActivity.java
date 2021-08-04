package com.example.photostomusic.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.photostomusic.BuildConfig;
import com.example.photostomusic.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    // Class name used as TAG for debugging
    public final String TAG = this.getClass().getSimpleName();

    // PSF variables required to interact with the Spotify Auth API
    private static final String CLIENT_ID = BuildConfig.SPOTIFY_KEY;
    private static final int REQUEST_CODE = 1415926535;
    private static final String REDIRECT_URI = "intent://";

    // Visual components present on the activity
    EditText etUser;
    EditText etPassword;
    Button btnParseLogin;
    Button btnSignup;
    Button btnSpotifyLogin;

    // String used to store the Spotify Auth Token
    String spotifyToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Connect visual components with their logic
        etUser = findViewById(R.id.etUserLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnParseLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnSpotifyLogin = findViewById(R.id.btnSpotifyLogin);

        // Login to Parse backend
        btnParseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUser.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                user.setUsername(etUser.getText().toString());
                user.setPassword(etPassword.getText().toString());

                // Attempt user signup
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            // If signup was successful, go to ColorFormActivity
                            Intent i = new Intent(LoginActivity.this, ColorFormActivity.class);
                            i.putExtra("token", spotifyToken);
                            startActivity(i);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();

                        } else {
                            // Handle signup exception
                            Log.e(TAG, "Parse signup error", e);
                        }
                    }
                });
            }
        });

        // Login to spotify, this will launch a browser from which the user can login
        // Spotify Login is persistent.
        btnSpotifyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Spotify Auth API object instance to request a token
                AuthenticationRequest.Builder builder =
                        new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

                // Empty string to generate token with all permissions
                builder.setScopes(new String[]{""});
                AuthenticationRequest request = builder.build();

                // Launch browser for login
                AuthenticationClient.openLoginActivity(LoginActivity.this, REQUEST_CODE, request);
            }
        });
    }

    // Parse backend login method, will send an error Toast if the login is unsuccesful on Parse's
    // side or if the user has not logged in to Spotify.
    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                HashMap<String, String> colors = (HashMap<String, String>) user.get("ColorRelation");
                // If an exception occurs send a log
                if (e != null){
                    Toast.makeText(LoginActivity.this, "Parse login error", Toast.LENGTH_SHORT).show();
                    return;
                } else if (spotifyToken.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Missing spotify login", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!colors.containsKey("0")){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("token", spotifyToken);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    Intent i = new Intent(LoginActivity.this, ColorFormActivity.class);
                    i.putExtra("token", spotifyToken);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
        });
    }

    // Check the result of the browser launched by the Spotify Auth API to fetch the token or
    // notify the user if an error occurred
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Toast.makeText(this, "Successful Spotify login", Toast.LENGTH_SHORT).show();
                    spotifyToken = response.getAccessToken();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Toast.makeText(this, "Something went wrong with the Spotify login", Toast.LENGTH_SHORT).show();
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
}

