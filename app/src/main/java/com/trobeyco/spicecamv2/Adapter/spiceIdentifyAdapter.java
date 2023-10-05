package com.trobeyco.spicecamv2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.trobeyco.spicecamv2.CardArticleActivity;
import com.trobeyco.spicecamv2.Domain.spiceIdentifyDomain;

import com.trobeyco.spicecamv2.R;

import java.util.ArrayList;

public class spiceIdentifyAdapter extends RecyclerView.Adapter<spiceIdentifyAdapter.ViewholderIdentify> {

    ArrayList<spiceIdentifyDomain> identifyItems;
    Context context;
    
    public spiceIdentifyAdapter(ArrayList<spiceIdentifyDomain> identifyItems){
        this.identifyItems = identifyItems;
    }
    
    
    @NonNull
    @Override
    public spiceIdentifyAdapter.ViewholderIdentify onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_spice_news_list,parent, false);
        context= parent.getContext();
        return new ViewholderIdentify(inflator);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewholderIdentify holder, int position) {
        if (position < identifyItems.size()) {
            Log.d("Debug", "Current position: " + position);

            spiceIdentifyDomain identifyItem = identifyItems.get(position);
            holder.title.setText(identifyItems.get(position).getTitle());
            holder.subtitle.setText(identifyItems.get(position).getSubtitle());
            String imageResourceName = identifyItems.get(position).getPicAddress();

            // Load an img using Glide
            int drawableResourceId = holder.itemView.getResources().getIdentifier(imageResourceName,
                    "drawable", holder.itemView.getContext().getPackageName());

            Glide.with(holder.itemView.getContext())
                    .load(drawableResourceId)
                    .transform(new GranularRoundedCorners(10, 10, 0, 0))
                    .into(holder.pic);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), CardArticleActivity.class);
                intent.putExtra("selectedTitle", identifyItems.get(position).getTitle());
                // Check if it's a "news" or "identify" item and set the collectionName accordingly
                intent.putExtra("collectionName", "spice_identify");
                holder.itemView.getContext().startActivity(intent);
            });
        }
    }


    @Override
    public int getItemCount() {
        return identifyItems.size();
    }

    public class ViewholderIdentify extends RecyclerView.ViewHolder{
        TextView title, subtitle;
        ImageView pic;

        public ViewholderIdentify(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_txt);
            subtitle = itemView.findViewById(R.id.sumber_berita_tv);
            pic = itemView.findViewById(R.id.spice_news_pic);
        }
    }

}
