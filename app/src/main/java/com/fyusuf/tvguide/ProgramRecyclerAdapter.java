package com.fyusuf.tvguide;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProgramRecyclerAdapter extends RecyclerView.Adapter<ProgramRecyclerAdapter.ViewHolder> {
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView program_name;
        public TextView program_time;
        public ImageView kanal_logo;
        public CardView card_view;
        public ImageView star;

        public ViewHolder(View view) {
            super(view);
            card_view = (CardView)view.findViewById(R.id.card_view_program);
            program_name = (TextView)view.findViewById(R.id.program_name);
            program_time = (TextView)view.findViewById(R.id.program_time);
            kanal_logo = (ImageView)view.findViewById(R.id.kanalImage);
            star = (ImageView) view.findViewById(R.id.star_icon);
        }
    }

    List<Program> list_program;
    public ProgramRecyclerAdapter(List<Program> list_program) {
        this.list_program = list_program;
    }


    @Override
    public ProgramRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout_program, parent, false);
        ViewHolder view_holder = new ViewHolder(v);
        this.context = parent.getContext();
        return view_holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Program program = list_program.get(position);
        holder.program_name.setText(list_program.get(position).getName());
        holder.program_time.setText(list_program.get(position).getTime());
        holder.kanal_logo.setImageBitmap(list_program.get(position).getLogo());
        //holder.star.setImageResource(android.R.drawable.btn_star_big_off);
        //holder.star.setTag("grey");
        if (check(program.getChannel()+program.getTime()+program.getName())) {
            holder.star.setImageResource(android.R.drawable.btn_star_big_on);
            holder.star.setTag("red");
        } else {
            holder.star.setImageResource(android.R.drawable.btn_star_big_off);
            holder.star.setTag("grey");
        }
    }

    @Override
    public int getItemCount() {
        return list_program.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public boolean check(String str){
        boolean check = false;
        //Log.i("str: ", str);
        SharedPreferences sharedPreferences = context.getSharedPreferences("SelectFile",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("program", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        if(arrayList !=null) {
            for (int i = 0; i < arrayList.size(); i++) {
                //Log.i("list",arrayList.get(i));
                if (arrayList.get(i).equals(str)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }
}