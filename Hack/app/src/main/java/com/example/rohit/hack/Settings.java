package com.example.rohit.hack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.PriorityQueue;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button signOutButton = (Button) findViewById(R.id.settings_signout_button);

        SeekBar radiusOfSearch = (SeekBar) findViewById(R.id.settings_radius_seekbar);
        radiusOfSearch.setMax(50);
        radiusOfSearch.setProgress(ProfileActivity.radiusOfSearch);
        final TextView searchRadiusText = (TextView) findViewById(R.id.search_radius_text);

        searchRadiusText.setText(String.valueOf(ProfileActivity.radiusOfSearch));

        radiusOfSearch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                searchRadiusText.setText(String.valueOf(i)+" Km");
                ProfileActivity.radiusOfSearch = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));

            }
        });


    }
}
