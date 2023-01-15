package com.beekeeperpro.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.beekeeperpro.R;
import com.bumptech.glide.Glide;

import java.io.File;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PictureViewHolder> {
    private File[] pictures;
    public GalleryAdapter() {
        this.pictures = new File[0];
    }
    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new PictureViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        File picture = pictures[position];
        Glide.with(holder.itemView.getContext())
                .load(picture)
                .into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return pictures.length;
    }
    public void setPictures(File[] pictures) {
        this.pictures = pictures;
        notifyDataSetChanged();
    }
    class PictureViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        PictureViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.picture);
            imageView.setOnClickListener(v -> {
                File picture = pictures[getAdapterPosition()];
                Uri pictureUri = FileProvider.getUriForFile(v.getContext(), "com.beekeeperpro.fileprovider", picture);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(pictureUri, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                v.getContext().startActivity(intent);
            });

        }
    }

}
