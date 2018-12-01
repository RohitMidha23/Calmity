package com.example.rohit.hack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataHolder> {

    public List<Post> posts;
    Context context;

    public RecyclerViewAdapter(List<Post> p,Context c) {
        posts = p;
        context = c;

    }

    class DataHolder extends RecyclerView.ViewHolder {

        TextView h,b,l;
        ImageView i;

        public DataHolder(@NonNull View itemView) {
            super(itemView);
            h = (TextView) itemView.findViewById(R.id.card_heading);
            b = (TextView) itemView.findViewById(R.id.card_bytext);
            l = (TextView) itemView.findViewById(R.id.card_location);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater l = LayoutInflater.from(context);
        View view = l.inflate(R.layout.recycler_view_layout,null);

        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.DataHolder dataHolder, int i) {
        Post p = posts.get(i);
        dataHolder.h.setText(p.heading);
        dataHolder.b.setText(p.description);
        if(p.distancetoPost == null)
            dataHolder.l.setText("Enable gps to show distance");
        else
            dataHolder.l.setText(new DecimalFormat("##.##").format(p.distancetoPost) + " Km");

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
