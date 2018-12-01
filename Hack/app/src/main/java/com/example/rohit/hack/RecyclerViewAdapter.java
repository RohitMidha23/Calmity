package com.example.rohit.hack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataHolder> {

    public List<Post> posts;
    Context context;
    Post p;

    int position;



    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {

            position = -1;

            DatabaseReference db = null;
            if(ProfileActivity.activitySelected == 1)
                db = FirebaseDatabase.getInstance().getReference("Amenities");
            else if(ProfileActivity.activitySelected == 2)
                db = FirebaseDatabase.getInstance().getReference("SOS");
            else if(ProfileActivity.activitySelected == 3)
                db = FirebaseDatabase.getInstance().getReference("Volunteer");

            db.orderByKey().addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    position++;


                    if(position == AmenitiesFragment.recyclerView.getChildLayoutPosition(view)) {
                        if(ProfileActivity.activitySelected == 1)
                            p = AmenitiesFragment.posts.get(position);
                        else if(ProfileActivity.activitySelected == 2)
                            p = SOSFragment.posts.get(position);
                        else if(ProfileActivity.activitySelected == 3)
                            p = VolunteerFragment.posts.get(position);
                        context.startActivity(new Intent(context,PostExpanded.class).putExtra("post",p));

                        //LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(context,ProfileActivity.class).putExtra("pid",p.pid));

                    }


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
    };

    public RecyclerViewAdapter(List<Post> p,Context c) {
        posts = p;
        context = c;

    }

    class DataHolder extends RecyclerView.ViewHolder {

        TextView h,b,l,contact;
        ImageView i;

        public DataHolder(@NonNull View itemView) {
            super(itemView);
            h = (TextView) itemView.findViewById(R.id.card_heading);
            b = (TextView) itemView.findViewById(R.id.card_bytext);
            l = (TextView) itemView.findViewById(R.id.card_location);
            contact = (TextView) itemView.findViewById(R.id.expanded_post_contact_number);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater l = LayoutInflater.from(context);
        View view = l.inflate(R.layout.recycler_view_layout,null);
        view.setOnClickListener(onClickListener);

        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.DataHolder dataHolder, int i) {
        Post p = posts.get(i);
        if(p.heading.length() > 20)
            dataHolder.h.setText(p.heading.substring(0,19) + "...");
        else
            dataHolder.h.setText(p.heading);
        if(p.description.length() > 45)
            dataHolder.b.setText(p.description.substring(0,39) + "...");
        else
            dataHolder.b.setText(p.description);
        if(p.distancetoPost == null)
            dataHolder.l.setText("Enable gps to show distance");
        else
            dataHolder.l.setText(new DecimalFormat("##.##").format(p.distancetoPost) + " Km");
        dataHolder.contact.setText(p.contact);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
