package com.example.rohit.hack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.PriorityQueue;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView myPosts = (TextView) findViewById(R.id.settings_myposts);
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

        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*TODO:
                    My posts activity
                 */
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* TODO:
                    Log out user
                 */
            }
        });


    }
}
