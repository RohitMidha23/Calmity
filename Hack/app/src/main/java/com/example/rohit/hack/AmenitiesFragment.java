package com.example.rohit.hack;

import android.Manifest;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class AmenitiesFragment extends Fragment {

    ArrayList<Post> posts;
    Post p;
    RecyclerViewAdapter recyclerViewAdapter;

    private LocationRequest mLocationRequest;
    LocationManager locationManager;
    static Location currentUserLocation;
    Location postLocation = null;

    static double distanceToLocation = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.amenities_fragment_layout, null);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setInterval(500);
        //mLocationRequest.setFastestInterval(1000);


        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        99);
                return null;
            }
            Log.i("COMEON", "REQUESTING LOCATION");
            getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            currentUserLocation = locationResult.getLastLocation();
                            Log.i("COMEON", "Location received");
                            posts = new ArrayList<>();

                            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.amenities_recycler_view);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewAdapter = new RecyclerViewAdapter(posts, getActivity());
                            recyclerView.setAdapter(recyclerViewAdapter);
                            recyclerViewAdapter.notifyDataSetChanged();

                            DatabaseReference amenitiesDB = FirebaseDatabase.getInstance().getReference("Amenities");

                            amenitiesDB.orderByKey().addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    p = dataSnapshot.getValue(Post.class);
                                        postLocation = new Location("");
                                        postLocation.setLatitude(p.lat);
                                        postLocation.setLongitude(p.lon);
                                        if (currentUserLocation.distanceTo(postLocation) / 1000 < ProfileActivity.radiusOfSearch) {
                                            p.distancetoPost = currentUserLocation.distanceTo(postLocation) / 1000.0;
                                            posts.add(p);
                                        }
                                    recyclerViewAdapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    },
                    Looper.myLooper());
        } else {
            Toast.makeText(getContext(), "GPS is disabled. Enable to view posts near you", Toast.LENGTH_LONG).show();
        }


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            posts = new ArrayList<>();


            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.amenities_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewAdapter = new RecyclerViewAdapter(posts, getActivity());
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();

            DatabaseReference amenitiesDB = FirebaseDatabase.getInstance().getReference("Amenities");

            amenitiesDB.orderByKey().addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    p = dataSnapshot.getValue(Post.class);
                    p.distancetoPost = null;
                    posts.add(p);

                    recyclerViewAdapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return rootView;
    }
}
