package com.fyusuf.tvguide;

import android.graphics.Bitmap;

public class Program {
    private String name;
    private String time;
    private String channel;
    private Bitmap logo;
    public Program(String txt,String channel,String time,Bitmap logo){
        this.name = txt;
        this.time = time;
        this.channel = channel;
        this.logo = logo;
    }

    public String getName(){
        return this.name;
    }

    public String getChannel(){ return this.channel;}

    public Bitmap getLogo(){
        return this.logo;
    }

    public String getTime(){
        return this.time;
    }


}