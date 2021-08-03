package models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Song")
public class Song extends ParseObject {

    // Keys to access each attribute of the model
    public static final String KEY_PICTURE = "cameraPicture";
    public static final String KEY_USER = "user";
    public static final String KEY_SONG_CODE = "songCode";
    public static final String KEY_GENRES = "genres";
    public static final String KEY_COLORS = "colors";
    public static final String KEY_NAME = "song_name";
    public static final String KEY_ARTIST = "song_artist";
    public static final String KEY_ALBUM = "song_album";
    public static final String KEY_COVER = "songCover";

    /* Getters to retrieve each attribute for the Parse object, said attributes
    are the following:
        - Picture: Image captured by the user with the device's camera.
        - User: User who took the picture
        - Song Code: Spotify song code, additional data can be extracted with this
            code, such as the song name, artist, image, etc.
        - Genres: JSON list used to hold the genres the song was searched upon.
        - Colors: Predominant colors extracted from the picture.
        - Name: Name of the song.
        - Artist: Artist that plays the song.
        - Album: Album the song belongs to.
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
    public List<String> getGenres(){
        return getList(KEY_GENRES);
    }
    public List<String> getColors(){
        return getList(KEY_COLORS);
    }
    public String getSongName(){
        return getString(KEY_NAME);
    }
    public String getSongArtist(){
        return getString(KEY_ARTIST);
    }
    public String getSongAlbum(){
        return getString(KEY_ALBUM);
    }
    public String  getSongCover(){
        return getString(KEY_COVER);
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
    public void setGenres(List<String> genres){
        put(KEY_GENRES, genres);
    }
    public void setColors(List<String> colors){
        put(KEY_COLORS, colors);
    }
    public void setSongName(String name){
        put(KEY_NAME, name);
    }
    public void setSongArtist(String artist){
        put(KEY_ARTIST, artist);
    }
    public void setSongAlbum(String album){
        put(KEY_ALBUM, album);
    }
    public void setSongCover(String file){
        put(KEY_COVER, file);
    }


}
