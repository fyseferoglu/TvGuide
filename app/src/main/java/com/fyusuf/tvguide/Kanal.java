package com.fyusuf.tvguide;

import android.graphics.Bitmap;

/**
 * Created by Pc on 19.5.2017.
 */
public class Kanal {

    private String name;
    private Bitmap logo;
    private String link;
    public Kanal(String txt,String link,Bitmap logo){
        this.name = txt;
        this.link = link;
        this.logo = logo;
    }

    public String getName(){
        return this.name;
    }

    public String getLink(){
        return this.link;
    }

    public Bitmap getLogo(){
        return this.logo;
    }


}
