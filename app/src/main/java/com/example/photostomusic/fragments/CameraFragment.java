package com.example.photostomusic.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import android.os.Environment;
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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import models.Song;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {


    public final String TAG = this.getClass().getSimpleName();
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 220700;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HashMap<String, String> map;
    ParseUser user;

    // Visual elements of the fragment
    Button btnPictureCapture;

    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
        map = (HashMap<String, String>) user.get("ColorRelation");
        Log.d(TAG, map.toString());

        btnPictureCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

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
                // Hashmap of color frequencies
                // TODO: Send this method call to loading screen
                // TODO: Sort frequencies map by values and get first 3 values
                HashMap<String, Integer> colors = getRGBs(photo);

        } else { // Result was a failure
            Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
    }


    public HashMap<String, Integer> getRGBs(Bitmap image){
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
        // Get the max count of the HashMap
        Map.Entry<String, Integer> maxCount = null;
        for (Map.Entry<String, Integer> entry : frequencies.entrySet())
        {
            if (maxCount == null || maxCount.getValue() < entry.getValue())
            {
                maxCount = entry;
            }
        }
        Log.d(TAG, "Color count: " + frequencies.toString() + " Most present color: " + maxCount.toString());
        return frequencies;
    }

    // Method to get the closest base color of the app to a color point passed
    // The idea is to handle color as a 3D vector space, with euclidean distance
    // it is possible to find the closest color to any other color, both interpreted
    // as points on said space.
    private String euclideanDistance(int red, int green, int blue) {
        HashMap<String, Integer> distances = new HashMap<>();
        for (String key: map.keySet()){
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