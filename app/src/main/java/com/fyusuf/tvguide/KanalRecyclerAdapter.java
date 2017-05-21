package com.fyusuf.tvguide;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class KanalRecyclerAdapter extends RecyclerView.Adapter<KanalRecyclerAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView kanal_name;
        public ImageView kanal_logo;
        public CardView card_view;


        public ViewHolder(View view) {
            super(view);
            card_view = (CardView)view.findViewById(R.id.card_view);
            kanal_name = (TextView)view.findViewById(R.id.kanal_name);
            kanal_logo = (ImageView)view.findViewById(R.id.kanal_logo);

        }
    }

    List<Kanal> list_kanal;
    public KanalRecyclerAdapter(List<Kanal> list_kanal) {

        this.list_kanal = list_kanal;
    }


    @Override
    public KanalRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);

        ViewHolder view_holder = new ViewHolder(v);
        return view_holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.kanal_name.setText(list_kanal.get(position).getName());
        holder.kanal_logo.setImageBitmap(list_kanal.get(position).getLogo());

    }

    @Override
    public int getItemCount() {
        return list_kanal.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}