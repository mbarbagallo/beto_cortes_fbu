package com.example.photostomusic;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;

import models.Song;

public class ParseApplication extends Application {

    // Connect with app ID and keys when created
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Song.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APP_ID)
                .clientKey(BuildConfig.PARSE_CLIENT_KEY)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
