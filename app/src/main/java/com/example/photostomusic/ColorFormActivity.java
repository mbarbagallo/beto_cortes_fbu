package com.example.photostomusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// This activity will hold the color form to be filled out by users to map their feelings
public class ColorFormActivity extends AppCompatActivity {

    public final String TAG = this.getClass().getSimpleName();

    // Visual elements of the activity
    Button btnConfirmColors;
    AppCompatSpinner redSpinner;
    AppCompatSpinner violetSpinner;
    AppCompatSpinner blueSpinner;
    AppCompatSpinner greenSpinner;
    AppCompatSpinner yellowSpinner;
    AppCompatSpinner orangeSpinner;

    // Temp list used to test dropdowns
    List<String> emotions = new ArrayList<String>(Arrays.asList(
            "default", "Joy", "Trust", "Fear", "Surprise",
            "Sadness", "Disgust", "Anger", "Alert"));

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
        redSpinner = findViewById(R.id.redSpinner);
        violetSpinner = findViewById(R.id.violetSpinner);
        blueSpinner = findViewById(R.id.blueSpinner);
        greenSpinner = findViewById(R.id.greenSpinner);
        yellowSpinner = findViewById(R.id.yellowSpinner);
        orangeSpinner = findViewById(R.id.orangeSpinner);

        // Add listener to confirm button, set a RESULT_OK code for the activity if the form is valid
        btnConfirmColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: validate color form before setting results
                // TODO: add new color dictionary to intent, upload to Parse and send it through
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
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
                // Currently checking only 2 test spinners
                usedOptions.add(redSpinner.getSelectedItem().toString());
                usedOptions.add(violetSpinner.getSelectedItem().toString());
                usedOptions.add(blueSpinner.getSelectedItem().toString());
                usedOptions.add(greenSpinner.getSelectedItem().toString());
                usedOptions.add(yellowSpinner.getSelectedItem().toString());
                usedOptions.add(orangeSpinner.getSelectedItem().toString());

                if (!usedOptions.contains(item)){
                    // Option is not being used, clear set for next usage of function
                    // validate selection
                    usedOptions.clear();
                    return true;
                } else {
                    // Option is being used, reject selection and clear set for next usage
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
        redSpinner.setAdapter(genericAdapter);
        redSpinner.setSelection(0);

        violetSpinner.setAdapter(genericAdapter);
        violetSpinner.setSelection(0);

        blueSpinner.setAdapter(genericAdapter);
        blueSpinner.setSelection(0);

        greenSpinner.setAdapter(genericAdapter);
        greenSpinner.setSelection(0);

        yellowSpinner.setAdapter(genericAdapter);
        yellowSpinner.setSelection(0);

        orangeSpinner.setAdapter(genericAdapter);
        orangeSpinner.setSelection(0);

        // Set the generic listener as the listener for both of the test spinners
        redSpinner.setOnItemSelectedListener(genericListener);
        violetSpinner.setOnItemSelectedListener(genericListener);
        blueSpinner.setOnItemSelectedListener(genericListener);
        greenSpinner.setOnItemSelectedListener(genericListener);
        yellowSpinner.setOnItemSelectedListener(genericListener);
        orangeSpinner.setOnItemSelectedListener(genericListener);
    }
}