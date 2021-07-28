package com.example.photostomusic.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.photostomusic.R;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CameraFragment extends Fragment {

    public final String TAG = this.getClass().getSimpleName();
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 220700;

    HashMap<String, String> colorRelation;
    HashMap<String, String> emotionRelation;
    ParseUser user;
    String spotifyToken;

    // Visual elements of the fragment
    Button btnPictureCapture;

    public CameraFragment() {
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
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnPictureCapture = view.findViewById(R.id.btnImageCapture);
        user = ParseUser.getCurrentUser();
        colorRelation = (HashMap<String, String>) user.get("ColorRelation");
        Log.d(TAG, colorRelation.toString());

        btnPictureCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        Bundle bundle = this.getArguments();
        spotifyToken = bundle.getString("key");



        // Hashmap used to relate emotions to music genres
        // Emotions and music relation based on the paper:
        // https://www.pnas.org/content/117/4/1924#sec-3
        // Its results can be easily seen on this interactive
        // cluster: https://www.ocf.berkeley.edu/~acowen/music.html#
        emotionRelation = new HashMap<String, String>() {{
            put("Calm", "acoustic");
            put("Dreamy", "gospel");
            put("Heroic", "classical");
            put("Anxious", "industrial");
            put("Scared", "sad");
            put("Annoyed", "punk");
            put("Defiant", "metal");
            put("Energized", "edm");
            put("Amazed", "funk");
            put("Joyful", "disco");
            put("Desirous", "reggaeton");
            put("Cute", "romance");
        }};

    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if the device is capable of taking pictures
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
                // Get the photo from the extras of the camera activity
                Bundle extras = data.getExtras();
                Bitmap photo = (Bitmap) extras.get("data");

                // Load the taken image into a preview
                ImageView ivPreview = getActivity().findViewById(R.id.ivPreview);
                ivPreview.setImageBitmap(photo);

                // Get genres obtained from the image
                HashMap<String, String> genres = getMusicGenres(photo);

                // Construct a new fragment and pass data with a bundle
                RecommendationFragment fragment = new RecommendationFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("genres", Parcels.wrap(genres));
                bundle.putParcelable("photo", Parcels.wrap(photo));
                bundle.putString("key", spotifyToken);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                android.R.anim.fade_in,  // enter
                                android.R.anim.fade_out // exit
                        )
                        .replace(R.id.flContainer, fragment, "findThisFragment")
                        .commit();

        } else { // Result was a failure
            Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
    }


    public HashMap<String, String> getMusicGenres(Bitmap image){
        // Get image sizes to iterate over each pixel
        int y = image.getHeight();
        int x = image.getWidth();

        // Get the name of hex of each color resource as a string to fill the map
        // The colors are recovered as an int, then logical AND operation to remove
        // the transparency of the color, convert this to a HEX string
        String color1 = Integer.toHexString(getResources().getColor(R.color.option1) & 0x00ffffff);
        String color2 = Integer.toHexString(getResources().getColor(R.color.option2) & 0x00ffffff);
        String color3 = Integer.toHexString(getResources().getColor(R.color.option3) & 0x00ffffff);
        String color4 = Integer.toHexString(getResources().getColor(R.color.option4) & 0x00ffffff);
        String color5 = Integer.toHexString(getResources().getColor(R.color.option5) & 0x00ffffff);
        String color6 = Integer.toHexString(getResources().getColor(R.color.option6) & 0x00ffffff);
        String color7 = Integer.toHexString(getResources().getColor(R.color.option7) & 0x00ffffff);
        String color8 = Integer.toHexString(getResources().getColor(R.color.option8) & 0x00ffffff);

        // Map used to count the appearances of each main color of an image
        HashMap <String, Integer> frequencies = new HashMap<String, Integer>() {{
            put(color1, 0);
            put(color2, 0);
            put(color3, 0);
            put(color4, 0);
            put(color5, 0);
            put(color6, 0);
            put(color7, 0);
            put(color8, 0);
        }};

        // Nested for to iterate over every pixel
        for (int j=0; j < y; j++){
            for (int i=0; i < x; i++){
                // Get pixel at current position
                int colour = image.getPixel(i, j);
                // Extract colors of said pixel and add values to temporal list
                int red = Color.red(colour);
                int green = Color.green(colour);
                int blue = Color.blue(colour);

                // Determine the color's closest base
                String base = euclideanDistance(red, green, blue);
                // Get the current value of the count for this base
                int currentCount = frequencies.get(base);
                // Increment count by 1
                frequencies.put(base, currentCount + 1);
            }
        }
        // Sort color frequency hashmap by value
        HashMap<String, Integer> orderedFrequencies = sortByComparator(frequencies);
        // Empty hashmap to store the top 3 colors
        HashMap<String, String> genres = new HashMap<>();

        int top = 0;
        // Loop to get the top 3 colors
        for (Map.Entry<String, Integer> entry : orderedFrequencies.entrySet()){
            if (top < 3){
                String emotion = colorRelation.get(entry.getKey());
                genres.put(entry.getKey(), emotionRelation.get(emotion));
                top++;
            } else {
                break;
            }
        }
        Log.d(TAG, "Colors: " + genres.toString());
        return genres;
    }

    // Method to sort a the color count hashmap by its values on descending order
    private static HashMap<String, Integer> sortByComparator(Map<String, Integer> unsortedMap)
    {
        // Use a LinkedList to keep a track of the order of the entries
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortedMap.entrySet());

        // Sorting the list on descending order with a custom comparator
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                // Compare next to previous to get descending order
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Insert elements in order with the linked list
        HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


    // Method to get the closest base color of the app to a color point passed
    // The idea is to handle color as a 3D vector space, with euclidean distance
    // it is possible to find the closest color to any other color, both interpreted
    // as points on said space.
    private String euclideanDistance(int red, int green, int blue) {
        HashMap<String, Integer> distances = new HashMap<>();
        for (String key: colorRelation.keySet()){
            // Array of 3 ints to store the RGB values of the key string
            int[] base = new int[3];
            // For loop to get all 3 colors
            for (int i = 0; i < 3; i++) {
                base[i] = Integer.parseInt(key.substring(i * 2, i * 2 + 2), 16);
            }
            //Log.d(TAG, Arrays.toString(base) + " " + key);
            // Euclidean distance in 3D space
            int distance = (int) Math.sqrt( Math.pow(base[0] - red, 2) + Math.pow(base[1] - green, 2) + Math.pow(base[2] - blue, 2) );
            // Add distance to HashMap
            distances.put(key, distance);
        }
        //Log.d(TAG,"RGB: " + red + " " + green + " " + blue);

        // Get minimal distance between the color and all the bases
        Map.Entry<String, Integer> minDistance = null;

        for (Map.Entry<String, Integer> entry : distances.entrySet())
        {
            if (minDistance == null || minDistance.getValue() > entry.getValue())
            {
                minDistance = entry;
            }
        }
        // Return the base with the closest distance
        //Log.d(TAG, distances.toString());
        return minDistance.getKey();
    }
}