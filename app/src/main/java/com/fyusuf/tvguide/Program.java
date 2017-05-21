package com.fyusuf.tvguide;

import android.graphics.Bitmap;

public class Program {
    private String name;
    private String time;
    private Bitmap logo;
    public Program(String txt,String time,Bitmap logo){
        this.name = txt;
        this.time = time;
        this.logo = logo;
    }

    public String getName(){
        return this.name;
    }

    public Bitmap getLogo(){
        return this.logo;
    }

    public String getTime(){
        return this.time;
    }


}