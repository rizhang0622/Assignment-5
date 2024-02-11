package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.LinkViewHolder> {
//
//    private List<LinkItem> linkItems;
//    private Context context;
//
//    // Constructor
//    public LinksAdapter(Context context, List<LinkItem> linkItems) {
//        this.context = context;
//        this.linkItems = linkItems;
//    }
//
//    @NonNull
//    @Override
//    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_vh, parent, false);
//        return new LinkViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {
//        LinkItem item = linkItems.get(position);
//        holder.nameTextView.setText(item.getName());
//        holder.urlTextView.setText(item.getUrl());
//
//        // Tap to open URL
//        holder.itemView.setOnClickListener(v -> {
//            try{
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
//                context.startActivity(browserIntent);
//            }catch (Exception e){
//                Toast.makeText(context, "Invalid link", Toast.LENGTH_LONG).show();
//            }
//
//        });
//
//        // Implement other interactions (long press, swipe, etc.) as needed
//    }
//
//    @Override
//    public int getItemCount() {
//        return linkItems.size();
//    }
//
//    static class LinkViewHolder extends RecyclerView.ViewHolder {
//        TextView nameTextView, urlTextView;
//
//        public LinkViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nameTextView = itemView.findViewById(R.id.nameTextView);
//            urlTextView = itemView.findViewById(R.id.urlTextView);
//        }
//    }
//}

public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.LinkViewHolder> {

    private List<LinkItem> linkItems;
    private Context context;
    private OnItemClickListener listener;

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(LinkItem item, int position);
    }

    // Constructor
    public LinksAdapter(Context context, List<LinkItem> linkItems, OnItemClickListener listener) {
        this.context = context;
        this.linkItems = linkItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_vh, parent, false);
        return new LinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {
        LinkItem item = linkItems.get(position);
        holder.nameTextView.setText(item.getName());
        holder.urlTextView.setText(item.getUrl());

        holder.urlTextView.setOnClickListener(v -> {
            try{
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                context.startActivity(browserIntent);
            }catch (Exception e){
                Toast.makeText(context, "Invalid link", Toast.LENGTH_LONG).show();
            }

        });
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return linkItems.size();
    }

    static class LinkViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, urlTextView;

        public LinkViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            urlTextView = itemView.findViewById(R.id.urlTextView);
        }
    }
}

