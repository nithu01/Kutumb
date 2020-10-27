package com.app.kutumb.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.Activity.DetailActivity;
import com.app.kutumb.Model.ItemData;
import com.app.kutumb.R;

import java.util.List;

public class RecyclerViewExampleAdapter extends RecyclerView.Adapter<RecyclerViewExampleAdapter.ViewHolder> {
    private List<ItemData> moviesList;
    private Context context;

    public RecyclerViewExampleAdapter(Context context,List<ItemData> itemsData) {
        this.moviesList = itemsData;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerViewExampleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                    int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer, null);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        ItemData movie = moviesList.get(position);

        viewHolder.imgViewIcon.setImageResource(movie.getImageUrl());
        viewHolder.imgViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgViewIcon =  itemLayoutView.findViewById(R.id.img_offer);
        }
    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
