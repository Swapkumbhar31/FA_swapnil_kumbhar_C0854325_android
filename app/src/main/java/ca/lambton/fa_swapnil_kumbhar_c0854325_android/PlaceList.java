package ca.lambton.fa_swapnil_kumbhar_c0854325_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.lambton.fa_swapnil_kumbhar_c0854325_android.SharedPreferences.UserSettings;

public class PlaceList extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        setTitle("Home");


        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(view -> {
            Intent i = new Intent(PlaceList.this, MapsActivity.class);
            startActivity(i);
        });

        UserSettings userSettings = new UserSettings().getInstance(getApplicationContext());
        boolean firstTimeOpen = new UserSettings().getInstance(getApplicationContext()).isFirstTimeOpen();

        if (firstTimeOpen) {
            insertPlaces();
            userSettings.setIsFirstTimeOpen(false);
        }
    }

    private void insertPlaces() {

    }
}