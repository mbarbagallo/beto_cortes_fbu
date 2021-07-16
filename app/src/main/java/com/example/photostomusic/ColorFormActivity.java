package com.example.photostomusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// This activity will hold the color form to be filled out by users to map their feelings
public class ColorFormActivity extends AppCompatActivity {

    // Visual elements of the activity
    Button btnConfirmColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_form);

        // Connect visual and logic elements
        btnConfirmColors = findViewById(R.id.btnConfirmColors);

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
    }
}