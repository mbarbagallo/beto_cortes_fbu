package models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

@ParseClassName("Song")
public class Song extends ParseObject {

    // Keys to access each attribute of the model
    public static final String KEY_PICTURE = "cameraPicture";
    public static final String KEY_USER = "user";
    public static final String KEY_SONG_CODE = "songCode";
    public static final String KEY_GENRES = "genres";
    public static final String KEY_COLORS = "colors";

    /* Getters to retrieve each attribute for the Parse object, said attributes
    are the following:
        - Picture: Image captured by the user with the device's camera.
        - User: User who took the picture
        - Song Code: Spotify song code, additional data can be extracted with this
            code, such as the song name, artist, image, etc.
        - Genres: JSON list used to hold the genres the song was searched upon.
        - Colors: Predominant colors extracted from the picture.
    */
    public ParseFile getPicture(){
        return getParseFile(KEY_PICTURE);
    }
    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }
    public String getSongCode(){
        return getString(KEY_SONG_CODE);
    }
    public JSONArray getGenres(){
        return getJSONArray(KEY_GENRES);
    }
    public JSONArray getColors(){
        return getJSONArray(KEY_COLORS);
    }

    // Setters for all the previous attributes
    public void setPicture(ParseFile file){
        put(KEY_PICTURE, file);
    }
    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }
    public void setSongCode(String code){
        put(KEY_SONG_CODE, code);
    }
    public void setGenres(JSONArray genres){
        put(KEY_GENRES, genres);
    }
    public void setColors(JSONArray colors){
        put(KEY_COLORS, colors);
    }



}
