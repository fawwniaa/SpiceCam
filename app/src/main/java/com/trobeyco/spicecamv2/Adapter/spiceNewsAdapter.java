package com.trobeyco.spicecamv2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.trobeyco.spicecamv2.CardArticleActivity;
import com.trobeyco.spicecamv2.R;
import com.trobeyco.spicecamv2.Domain.spiceNewsDomain;


public class spiceNewsAdapter extends RecyclerView.Adapter<spiceNewsAdapter.Viewholder> {

    ArrayList<spiceNewsDomain> newsItems;
    Context context;

    public spiceNewsAdapter(ArrayList<spiceNewsDomain> newsItems){this.newsItems = newsItems; }

    @NonNull
    @Override
    public spiceNewsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_spice_news_list,parent, false);
        context= parent.getContext();
        return new Viewholder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull spiceNewsAdapter.Viewholder holder, int position) {

        if(position < newsItems.size()) {
            Log.d("Debug", "Current position: " + position);

            spiceNewsDomain newsItem = newsItems.get(position);
            holder.title.setText(newsItems.get(position).getTitle());
            holder.subtitle.setText(newsItems.get(position).getSubtitle());
            String imageResourceName = newsItems.get(position).getPicAddress();

            //load an img using glide
            int drawableResourceId=holder.itemView.getResources().getIdentifier(imageResourceName,
                    "drawable",holder.itemView.getContext().getPackageName());

            Glide.with(holder.itemView.getContext())
                    .load(drawableResourceId)
                    .transform(new GranularRoundedCorners(10, 10, 0, 0))
                    .into(holder.pic);


            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), CardArticleActivity.class);
                intent.putExtra("selectedTitle", newsItems.get(position).getTitle());
                // Check if it's a "news" or "identify" item and set the collectionName accordingly
                intent.putExtra("collectionName", "spice_news");
                holder.itemView.getContext().startActivity(intent);
            });

        }
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView title, subtitle;
        ImageView pic;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title_txt);
            subtitle=itemView.findViewById(R.id.sumber_berita_tv);
            pic=itemView.findViewById(R.id.spice_news_pic);
        }
    }
}

