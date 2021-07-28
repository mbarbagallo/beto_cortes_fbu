package com.example.photostomusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// This activity will hold the color form to be filled out by users to map their feelings
public class ColorFormActivity extends AppCompatActivity {

    public final String TAG = this.getClass().getSimpleName();

    // Visual elements of the activity
    Button btnConfirmColors;
    AppCompatSpinner spinner1;
    AppCompatSpinner spinner2;
    AppCompatSpinner spinner3;
    AppCompatSpinner spinner4;
    AppCompatSpinner spinner5;
    AppCompatSpinner spinner6;
    AppCompatSpinner spinner7;
    AppCompatSpinner spinner8;

    // Emotions present on the tellegen-watson-clark mood model
    // https://www.researchgate.net/publication/318510880_Applying_Data_Mining_for_Sentiment_Analysis_in_Music
    List<String> emotions = new ArrayList<String>(Arrays.asList(
            "default",
            "Calm",
            "Dreamy",
            "Heroic",
            "Anxious",
            "Scared",
            "Annoyed",
            "Defiant",
            "Energized",
            "Amazed",
            "Joyful",
            "Desirous",
            "Cute"
    ));

    // List used to contain all currently selected options
    Set<String> usedOptions = new HashSet<String>();

    // List used to hold all previously selected items
    AdapterView.OnItemSelectedListener genericListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_form);

        // Connect visual and logic elements
        btnConfirmColors = findViewById(R.id.btnConfirmColors);
        spinner1 = findViewById(R.id.colorSpinner1);
        spinner2 = findViewById(R.id.colorSpinner2);
        spinner3 = findViewById(R.id.colorSpinner3);
        spinner4 = findViewById(R.id.colorSpinner5);
        spinner5 = findViewById(R.id.colorSpinner6);
        spinner6 = findViewById(R.id.colorSpinner7);
        spinner7 = findViewById(R.id.colorSpinner8);
        spinner8 = findViewById(R.id.colorSpinner4);

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


        // Add listener to confirm button, set a RESULT_OK code for the activity if the form is valid
        btnConfirmColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<String, String>() {{
                    //getResources().getString(R.color.someColor);
                    put(color1, spinner1.getSelectedItem().toString());
                    put(color2, spinner2.getSelectedItem().toString());
                    put(color3, spinner3.getSelectedItem().toString());
                    put(color4, spinner4.getSelectedItem().toString());
                    put(color5, spinner5.getSelectedItem().toString());
                    put(color6, spinner6.getSelectedItem().toString());
                    put(color7, spinner7.getSelectedItem().toString());
                    put(color8, spinner8.getSelectedItem().toString());
                }};
                if (map.containsValue("default")){
                    Toast.makeText(ColorFormActivity.this, "Please don't leave any selector as 'default'.", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject toUpload = new JSONObject(map);

                // Retrieve the user
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    currentUser.put("ColorRelation", toUpload);

                    // Saves the object.
                    currentUser.saveInBackground(e -> {
                        if(e==null){
                            //Save successful
                            Toast.makeText(ColorFormActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent();
                            setResult(RESULT_OK, i);
                            finish();
                        }else{
                            // Something went wrong while saving
                            Toast.makeText(ColorFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Generic adapter to be used by all the spinners as they share the same functionality
        genericListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                // Set user selection to "default" but keep what option they chose
                parent.setSelection(0);
                // Check if user checked option is not used in any other spinner
                String item = parent.getItemAtPosition(position).toString();
                if (!checkIfUsed(item)){
                    // If it is, set current spinner selection as "default"
                    parent.setSelection(0);
                } else {
                    // Option was unused, set current selection
                    parent.setSelection(position);
                }
            }

            // Function to check if option has been selected on any other spinner
            private boolean checkIfUsed(String item) {
                if (item.equals("default")){
                    return false;
                }
                // Currently checking only 2 test spinners
                usedOptions.add(spinner1.getSelectedItem().toString());
                usedOptions.add(spinner2.getSelectedItem().toString());
                usedOptions.add(spinner3.getSelectedItem().toString());
                usedOptions.add(spinner4.getSelectedItem().toString());
                usedOptions.add(spinner5.getSelectedItem().toString());
                usedOptions.add(spinner6.getSelectedItem().toString());
                usedOptions.add(spinner7.getSelectedItem().toString());
                usedOptions.add(spinner8.getSelectedItem().toString());

                if (!usedOptions.contains(item)){
                    // Option is not being used, clear set for next usage of function
                    // validate selection
                    usedOptions.clear();
                    return true;
                } else {
                    // Option is being used, reject selection and clear set for next usage
                    Toast.makeText(ColorFormActivity.this, "Option " + item + " is already in use", Toast.LENGTH_SHORT).show();
                    usedOptions.clear();
                    return false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        ArrayAdapter genericAdapter = new ArrayAdapter<String>(this, R.layout.layout_spinner_item, emotions);
        genericAdapter.setDropDownViewResource(R.layout.layout_spinner_item);

        // Set the generic adapter to all the spinners
        spinner1.setAdapter(genericAdapter);
        spinner1.setSelection(0);

        spinner2.setAdapter(genericAdapter);
        spinner2.setSelection(0);

        spinner3.setAdapter(genericAdapter);
        spinner3.setSelection(0);

        spinner4.setAdapter(genericAdapter);
        spinner4.setSelection(0);

        spinner5.setAdapter(genericAdapter);
        spinner5.setSelection(0);

        spinner6.setAdapter(genericAdapter);
        spinner6.setSelection(0);

        spinner7.setAdapter(genericAdapter);
        spinner7.setSelection(0);

        spinner8.setAdapter(genericAdapter);
        spinner8.setSelection(0);

        // Set the generic listener as the listener for both of the test spinners
        spinner1.setOnItemSelectedListener(genericListener);
        spinner2.setOnItemSelectedListener(genericListener);
        spinner3.setOnItemSelectedListener(genericListener);
        spinner4.setOnItemSelectedListener(genericListener);
        spinner5.setOnItemSelectedListener(genericListener);
        spinner6.setOnItemSelectedListener(genericListener);
        spinner7.setOnItemSelectedListener(genericListener);
    }
}