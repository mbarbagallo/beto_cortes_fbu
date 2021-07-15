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
                .applicationId("U0QJQJaso5fDH0ZzD0G8gF7y6nVcEfpfD67l81Ez")
                .clientKey("XaHGTAgQZ78nZSaHmlZX5ppXwJ4WdMYirwRfMabm")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
