package com.example.rohit.hack;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class PostExpanded extends AppCompatActivity {

    GoogleMap nMap;
    ImageView map;
    Post p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_expanded);

        TextView expandedpostheading = (TextView) findViewById(R.id.expanded_post_heading);
        TextView expandedpostcontact = (TextView) findViewById(R.id.expanded_post_contact_number);
        TextView expandedpostdescription = (TextView) findViewById(R.id.expanded_post_description);
        map = (ImageView) findViewById(R.id.expanded_post_map_snapshot);

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


        p = (Post) getIntent().getSerializableExtra("post");
        expandedpostheading.setText(p.heading);
        if(p.contact == null)
            expandedpostcontact.setText("NIL");
        else if(p.contact != null)
            expandedpostcontact.setText(p.contact);

        expandedpostdescription.setText(p.description);
        LatLng location = new LatLng(p.lat, p.lon);
        map.setImageResource(R.drawable.map);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                /*Uri gmmIntentUri = Uri.parse("geo:"+p.lat+","+p.lon);
                //String u = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)", p.lat, p.lon, "Here");

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent); */



                try {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("geo:" + p.lat + "," + p.lon + "?q=" + p.lat + "," + p.lon + "( location )"));
                    intent.setComponent(new ComponentName(
                            "com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity"));
                    getApplicationContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {

                    try {
                        getApplicationContext().startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.google.android.apps.maps")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        getApplicationContext().startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                    }

                    e.printStackTrace();
                }



            }
        });

    }


}
