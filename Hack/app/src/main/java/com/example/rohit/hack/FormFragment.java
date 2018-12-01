package com.example.rohit.hack;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class FormFragment extends Fragment {

    DatabaseReference db;

    Post post = new Post();

    private LocationRequest mLocationRequest;
    LocationManager locationManager;

    public void onLocationChanged(Location location) {
        // New location has now been determined
        post.lat = location.getLatitude();
        post.lon = location.getLongitude();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(post.lat,post.lon,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        post.address = addresses.get(0).getAddressLine(0);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.form_fragment_layout, null);

        TextView formName = (TextView) rootView.findViewById(R.id.form_name);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);



        final RadioButton useCurrentLocation = (RadioButton) rootView.findViewById(R.id.current_location_radiobutton);

        final EditText address = (EditText) rootView.findViewById(R.id.amenities_form_address);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);


        useCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                if(useCurrentLocation.isChecked() && address.getEditableText().toString().equals("")) {

                    if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    99);
                            return;
                        }
                        getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        // do work here
                                        onLocationChanged(locationResult.getLastLocation());
                                        address.setText(post.address);
                                    }
                                },
                                Looper.myLooper());
                    }
                    else {
                        Toast.makeText(getContext(), "GPS is disabled. Enable and try again", Toast.LENGTH_SHORT).show();
                        useCurrentLocation.setChecked(false);
                    }

                }
                else {
                    useCurrentLocation.setChecked(false);
                    address.setText("");
                }

            }
        });



        if(ProfileActivity.fabOptionClicked == 1) {
            formName.setText("Amenities");
            db = FirebaseDatabase.getInstance().getReference("Amenities");
        }

        else if(ProfileActivity.fabOptionClicked == 2) {
            formName.setText("SOS");
            db = FirebaseDatabase.getInstance().getReference("SOS");
        }

        else if(ProfileActivity.fabOptionClicked == 3) {
            formName.setText("Volunteer");
            db = FirebaseDatabase.getInstance().getReference("Volunteer");
        }




        final EditText description = (EditText) rootView.findViewById(R.id.amenities_form_description);


        final EditText heading = (EditText) rootView.findViewById(R.id.amenities_form_heading);
        Button submit = rootView.findViewById(R.id.submit_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                post.heading = heading.getEditableText().toString();
                post.description = description.getEditableText().toString();
                post.address = address.getEditableText().toString();

                Geocoder geocoder = new Geocoder(getContext());
                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocationName(post.address,5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address location = addresses.get(0);
                post.lat = location.getLatitude();
                post.lon = location.getLongitude();

                post.pid = db.push().getKey();
                db.child(post.pid).setValue(post);
            }
        });

        return rootView;
    }
}
