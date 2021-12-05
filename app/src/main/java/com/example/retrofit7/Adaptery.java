package com.example.retrofit7;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> {


    private final Context mContext;
    private final List<Model> movieList;

    public Adaptery(Context mContext, List<Model> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(mContext);
        View view=layoutInflater.inflate(R.layout.activity_main,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text.setText(movieList.get(position).getId());

        Glide.with(mContext).load(movieList.get(position).getImage()).into(holder.mg);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView mg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.tv);
            mg=itemView.findViewById(R.id.image);
        }
    }
}
