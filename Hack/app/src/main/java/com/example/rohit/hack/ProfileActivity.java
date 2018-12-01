package com.example.rohit.hack;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Post implements Serializable {
    String heading,description,address,pid,contact;
    Double lat,lon,distancetoPost;
}

public class ProfileActivity extends AppCompatActivity {

    static int radiusOfSearch = 7;

    static boolean searching = false;

    static int fabOptionClicked;

    static int activitySelected = 1;

    //Firebase auth object //
    private FirebaseAuth firebaseAuth;

    private Fragment fragment = null;

    //View objects //
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private int fab_clicked = 0;
    private boolean formOnScreen = false;

    EditText search;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    activitySelected = 1;
                    fragment = new AmenitiesFragment();
                    break;
                case R.id.navigation_dashboard:
                    activitySelected = 2;
                    fragment = new SOSFragment();
                    break;
                case R.id.navigation_notifications:
                    activitySelected = 3;
                    fragment = new VolunteerFragment();
                    break;
            }
            loadFragment(fragment,0);
            return true;
        }
    };

    private void loadFragment(Fragment fragment, int i) {
        if(i == 0) {
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment).commit();
            } else
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, new AmenitiesFragment()).commit();
        }
        else
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.form_appear,R.animator.form_appear).replace(R.id.form_fragment_frame,fragment).commit();

    }

    private void removeForm() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.form_disappear,R.animator.form_disappear).remove(fragment).commit();
        findViewById(R.id.navigation).animate().translationYBy(-150);
        findViewById(R.id.navbar_drop_shadow).animate().translationYBy(-150);
        findViewById(R.id.translucent_bg).setVisibility(View.GONE);
        formOnScreen = false;
        fab_clicked = 0;

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firebaseAuth = FirebaseAuth.getInstance();

        ////getCurrentUser returns null if user is not logged in //
       /* if(firebaseAuth.getCurrentUser() == null){
            // Close Activity //
            finish();
            //Start Login Activity //
            startActivity(new Intent(this, LoginActivity.class));
        } */

        //user is now the currentUser //
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //Initialise Views //
        //textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        //buttonLogout = (Button) findViewById(R.id.buttonLogout);

        //Display Email //
        //textViewUserEmail.setText("Hola "+user.getEmail());

        //Add Listener to Button //
        //buttonLogout.setOnClickListener(this);

        search  = (EditText) findViewById(R.id.search_bar);

        //Creating button Navigation object //
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.v7.widget.Toolbar homeToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);


        // FAB objects Created //
        final FloatingActionButton fab_plus = (FloatingActionButton) findViewById(R.id.fab_plus);
        final FloatingActionButton fab_amenities = (FloatingActionButton) findViewById(R.id.fab_amenities);
        final FloatingActionButton fab_sos = (FloatingActionButton) findViewById(R.id.fab_sos);
        final FloatingActionButton fab_volunteer = (FloatingActionButton) findViewById(R.id.fab_volunteer);
        //FAB Layouts created //
        final LinearLayout fab_amenities_layout = (LinearLayout) findViewById(R.id.fab_amenities_layout);
        final LinearLayout fab_sos_layout = (LinearLayout) findViewById(R.id.fab_sos_layout);
        final LinearLayout fab_volunteer_layout = (LinearLayout) findViewById(R.id.fab_volunteer_layout);



        //Translucent Background Created //
        final View trans_bg = findViewById(R.id.translucent_bg);

        //Load default fragment//
        loadFragment(null,0);


        findViewById(R.id.navigation).animate().alpha(100);

        trans_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Transparentbg","clicked");

                if (formOnScreen)
                        removeForm();

                else if(fab_clicked==1)
                {
                    fab_plus.animate().rotationBy(-90);
                    findViewById(R.id.navigation).animate().translationYBy(-150);
                    findViewById(R.id.navbar_drop_shadow).animate().translationYBy(-150);
                    trans_bg.setVisibility(View.INVISIBLE);
                    fab_amenities_layout.animate().translationYBy(250);
                    fab_sos_layout.animate().translationYBy(500);
                    fab_volunteer_layout.animate().translationYBy(750);
                    fab_amenities_layout.setVisibility(View.INVISIBLE);
                    fab_sos_layout.setVisibility(View.INVISIBLE);
                    fab_volunteer_layout.setVisibility(View.INVISIBLE);
                    fab_clicked = 0;
                }
            }
        });

        //Action Listener for FAB ADD //

        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab_clicked == 0) {
                    fab_plus.animate().rotationBy(90);
                    findViewById(R.id.navigation).animate().translationYBy(150);
                    findViewById(R.id.navbar_drop_shadow).animate().translationYBy(150);
                    trans_bg.setVisibility(View.VISIBLE);
                    fab_amenities_layout.animate().translationYBy(-250);
                    fab_amenities_layout.setVisibility(View.VISIBLE);

                    fab_sos_layout.animate().translationYBy(-500);
                    fab_sos_layout.setVisibility(View.VISIBLE);

                    fab_volunteer_layout.animate().translationYBy(-750);
                    fab_volunteer_layout.setVisibility(View.VISIBLE);
                    fab_clicked = 1;
                } else {
                    fab_plus.animate().rotationBy(-90);
                    findViewById(R.id.navigation).animate().translationYBy(-150);
                    findViewById(R.id.navbar_drop_shadow).animate().translationYBy(-150);
                    trans_bg.setVisibility(View.INVISIBLE);
                    fab_amenities_layout.animate().translationYBy(+250);
                    fab_amenities_layout.setVisibility(View.INVISIBLE);

                    fab_sos_layout.animate().translationYBy(500);
                    fab_sos_layout.setVisibility(View.INVISIBLE);

                    fab_volunteer_layout.animate().translationYBy(750);
                    fab_volunteer_layout.setVisibility(View.INVISIBLE);
                    fab_clicked = 0;

                }
            }
        });
        fab_amenities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Amenities","clicked");

                fabOptionClicked = 1;

                formOnScreen = true;

                fragment = new FormFragment();

                trans_bg.setVisibility(View.VISIBLE);
                fab_amenities_layout.animate().translationYBy(250);
                fab_sos_layout.animate().translationYBy(500);
                fab_volunteer_layout.animate().translationYBy(750);
                fab_amenities_layout.setVisibility(View.INVISIBLE);
                fab_sos_layout.setVisibility(View.INVISIBLE);
                fab_volunteer_layout.setVisibility(View.INVISIBLE);
                //fab_plus.setVisibility(View.INVISIBLE);

                loadFragment(fragment,1);
            }
        });

        fab_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fabOptionClicked = 2;

                formOnScreen = true;

                fragment = new FormFragment();

                trans_bg.setVisibility(View.VISIBLE);
                fab_amenities_layout.animate().translationYBy(250);
                fab_sos_layout.animate().translationYBy(500);
                fab_volunteer_layout.animate().translationYBy(750);
                fab_amenities_layout.setVisibility(View.INVISIBLE);
                fab_sos_layout.setVisibility(View.INVISIBLE);
                fab_volunteer_layout.setVisibility(View.INVISIBLE);
                //fab_plus.setVisibility(View.INVISIBLE);

                loadFragment(fragment,1);

            }
        });

        fab_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabOptionClicked = 3;

                formOnScreen = true;

                fragment = new FormFragment();

                trans_bg.setVisibility(View.VISIBLE);
                fab_amenities_layout.animate().translationYBy(250);
                fab_sos_layout.animate().translationYBy(500);
                fab_volunteer_layout.animate().translationYBy(750);
                fab_amenities_layout.setVisibility(View.INVISIBLE);
                fab_sos_layout.setVisibility(View.INVISIBLE);
                fab_volunteer_layout.setVisibility(View.INVISIBLE);
                //fab_plus.setVisibility(View.INVISIBLE);

                loadFragment(fragment,1);
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(!formOnScreen)
            super.onBackPressed();
        if(formOnScreen)
            removeForm();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.toolbar_settings)
            startActivity(new Intent(this,Settings.class));
        else if(item.getItemId() == R.id.search_toolbar_icon) {
            if(search.getVisibility() == View.INVISIBLE) {
                search.setVisibility(View.VISIBLE);
                searching = true;

            }
            else if(search.getVisibility() == View.VISIBLE) {

                Geocoder geocoder = new Geocoder(getApplicationContext());
                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocationName(search.getEditableText().toString(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address location = addresses.get(0);
                Log.i("COMEON",String.valueOf(activitySelected));
                if(activitySelected == 1) {
                    AmenitiesFragment.currentUserLocation = new Location("");
                    AmenitiesFragment.currentUserLocation.setLatitude(location.getLatitude());
                    AmenitiesFragment.currentUserLocation.setLongitude(location.getLongitude());
                }
                else if(activitySelected == 2) {
                    SOSFragment.currentUserLocation = new Location("");
                    SOSFragment.currentUserLocation.setLatitude(location.getLatitude());
                    SOSFragment.currentUserLocation.setLongitude(location.getLongitude());
                }

                loadFragment(fragment,0);
                search.setVisibility(View.INVISIBLE);



            }

        }

        return super.onOptionsItemSelected(item);
    }
}