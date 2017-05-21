package com.fyusuf.tvguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler_view;
    private List<Kanal> kanal_list;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this,4);

        layoutManager.scrollToPosition(0);

        recycler_view.setLayoutManager(layoutManager);

        kanal_list = new ArrayList<>();

        /*kanal_list.add(new Kanal("Kanal", R.drawable.star_tv));
        kanal_list.add(new Kanal("Kanal", R.drawable.star_tv));
        kanal_list.add(new Kanal("Kanal", R.drawable.star_tv));
        kanal_list.add(new Kanal("Kanal", R.drawable.star_tv));
        kanal_list.add(new Kanal("Kanal", R.drawable.star_tv));
        kanal_list.add(new Kanal("Kanal", R.drawable.star_tv));
        kanal_list.add(new Kanal("Kanal", R.drawable.star_tv));
        kanal_list.add(new Kanal("Kanal", R.drawable.star_tv));
        kanal_list.add(new Kanal("Kanal", R.drawable.star_tv));
        kanal_list.add(new Kanal("Kanal", R.drawable.star_tv));*/

        KanalRecyclerAdapter adapter_items = new KanalRecyclerAdapter(kanal_list);
        recycler_view.setHasFixedSize(true);
        recycler_view.setAdapter(adapter_items);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recycler_view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Kanal kanal = kanal_list.get(position);
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra("url",kanal.getLink());
                intent.putExtra("logo",kanal.getLogo());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        new Channel().execute();
    }

    private class Channel extends AsyncTask<Void, Void, Void> {
        Bitmap bitmapKanalLogo;
        ArrayList<String> myList = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("TvRehberi");
            progressDialog.setMessage("Veri Çekiliyor...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Document doc  = Jsoup.connect("http://www.tvyayinakisi.com/yayin-akislari").get();
                //film parse
                //Element kanal = doc.select("div[class=TVProgram FL]").get(0);
                Elements kanalName = doc.select("div[class=row all-channels] div[class=two columns shadow-right]");
                Elements kanalName2 = doc.select("div[class=row all-channels] div[class=two columns]");
                Elements kanalLink = kanalName.select("a[href]");
                Elements kanalLogo = kanalName.select("img[src]");
                Elements kanalLink2 = kanalName2.select("a[href]");
                Elements kanalLogo2 = kanalName2.select("img[src]");
                /*for(int i = 0; i < kanalLogo.size(); i+=3){
                    myList.add(kanalLogo.get(i).getElementsByTag("img").attr("src"));
                }*/
                for(int i = 0; i < kanalName.size(); i++){
                    if(i == 7) continue;
                    String[] token = kanalLogo.get(i).getElementsByTag("img").attr("src").split("/");
                    String url = "http://www.tvyayinakisi.com/" + token[0] + "/b/" + token[1];
                    //Log.i("url",url);
                    InputStream input = new java.net.URL(url).openStream();
                    bitmapKanalLogo = BitmapFactory.decodeStream(input);
                    url = "http://www.tvyayinakisi.com/" + kanalLink.get(i).attr("href");
                    kanal_list.add(new Kanal(kanalName.get(i).text(),url,bitmapKanalLogo));
                }
                for(int i = 0; i < kanalName2.size(); i++){
                    String[] token = kanalLogo2.get(i).getElementsByTag("img").attr("src").split("/");
                    String url = "http://www.tvyayinakisi.com/" + token[0] + "/b/" + token[1];
                    //Log.i("url",url);
                    InputStream input = new java.net.URL(url).openStream();
                    bitmapKanalLogo = BitmapFactory.decodeStream(input);
                    url = "http://www.tvyayinakisi.com/" + kanalLink2.get(i).attr("href");
                    kanal_list.add(new Kanal(kanalName2.get(i).text(),url,bitmapKanalLogo));
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            KanalRecyclerAdapter adapter_items = new KanalRecyclerAdapter(kanal_list);
            recycler_view.setHasFixedSize(true);
            recycler_view.setAdapter(adapter_items);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            progressDialog.dismiss();
        }
    }
}
