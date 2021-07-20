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
    AppCompatSpinner spinner1;
    AppCompatSpinner spinner2;
    AppCompatSpinner spinner3;
    AppCompatSpinner spinner4;
    AppCompatSpinner spinner5;
    AppCompatSpinner spinner6;

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
        spinner1 = findViewById(R.id.spinner_1);
        spinner2 = findViewById(R.id.spinner_2);
        spinner3 = findViewById(R.id.spinner_3);
        spinner4 = findViewById(R.id.spinner_4);
        spinner5 = findViewById(R.id.spinner_5);
        spinner6 = findViewById(R.id.spinner_6);

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
                Log.i("beto", parent.getSelectedItem().toString());
            }

            // Function to check if option has been selected on any other spinner
            private boolean checkIfUsed(String item) {
                // Currently checking only 2 test spinners
                usedOptions.add(spinner1.getSelectedItem().toString());
                usedOptions.add(spinner2.getSelectedItem().toString());
                usedOptions.add(spinner3.getSelectedItem().toString());
                usedOptions.add(spinner4.getSelectedItem().toString());
                usedOptions.add(spinner5.getSelectedItem().toString());
                usedOptions.add(spinner6.getSelectedItem().toString());

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

        // Set the generic listener as the listener for both of the test spinners
        spinner1.setOnItemSelectedListener(genericListener);
        spinner2.setOnItemSelectedListener(genericListener);
        spinner3.setOnItemSelectedListener(genericListener);
        spinner4.setOnItemSelectedListener(genericListener);
        spinner5.setOnItemSelectedListener(genericListener);
        spinner6.setOnItemSelectedListener(genericListener);
    }
}