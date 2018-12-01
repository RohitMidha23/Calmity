package com.example.rohit.hack;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class SOSFragment extends Fragment {

    static ArrayList<Post> posts;
    Post p;
    RecyclerViewAdapter recyclerViewAdapter;
    static RecyclerView recyclerView;

    private LocationRequest mLocationRequest;
    LocationManager locationManager;
    static Location currentUserLocation;
    Location postLocation = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.sos_fragment_layout,null);

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



        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !ProfileActivity.searching) {
            posts = new ArrayList<>();


            recyclerView = (RecyclerView) rootView.findViewById(R.id.sos_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewAdapter = new RecyclerViewAdapter(posts, getActivity());
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();

            DatabaseReference amenitiesDB = FirebaseDatabase.getInstance().getReference("SOS");

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


        if(ProfileActivity.searching) {

            posts = new ArrayList<>();

            recyclerView = (RecyclerView) rootView.findViewById(R.id.sos_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewAdapter = new RecyclerViewAdapter(posts, getActivity());
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();

            DatabaseReference amenitiesDB = FirebaseDatabase.getInstance().getReference("SOS");

            amenitiesDB.orderByKey().addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    p = dataSnapshot.getValue(Post.class);
                    postLocation = new Location("");
                    postLocation.setLatitude(p.lat);
                    postLocation.setLongitude(p.lon);
                    if (currentUserLocation.distanceTo(postLocation) / 1000.0 < ProfileActivity.radiusOfSearch) {
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
            ProfileActivity.searching = false;
        }


        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


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

                            recyclerView = (RecyclerView) rootView.findViewById(R.id.sos_recycler_view);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewAdapter = new RecyclerViewAdapter(posts, getActivity());
                            recyclerView.setAdapter(recyclerViewAdapter);
                            recyclerViewAdapter.notifyDataSetChanged();

                            DatabaseReference amenitiesDB = FirebaseDatabase.getInstance().getReference("SOS");

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





        return rootView;
    }
}
