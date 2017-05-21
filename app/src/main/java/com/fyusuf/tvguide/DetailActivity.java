package com.fyusuf.tvguide;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private List<Program> program_list;
    private List<String> sp_list;
    private ProgressDialog progressDialog;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private final static int REQUEST_CODE = 1;
    private static String URL;
    private Bitmap bitmapKanalLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        recycler_view = (RecyclerView)findViewById(R.id.recycler_view_detail);
        Intent intent = getIntent();
        URL = intent.getStringExtra("url");
        bitmapKanalLogo = intent.getParcelableExtra("logo");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recycler_view.setLayoutManager(layoutManager);
        gson = new Gson();
        program_list = new ArrayList<>();
        sp_list = new ArrayList<>();
        sharedPreferences = this.getSharedPreferences("SelectFile", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String json = sharedPreferences.getString("program", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        if(arrayList !=null) {
            for (int i = 0; i < arrayList.size(); i++) {
                sp_list.add(arrayList.get(i));
            }
        }

       /* program_list.add(new Program("program1","time1",R.drawable.film1, R.drawable.logoatv));
        program_list.add(new Program("program2","time2",R.drawable.film1, R.drawable.logo));
        program_list.add(new Program("program3","time3",R.drawable.film1, R.drawable.logoatv));
        program_list.add(new Program("program4","time4",R.drawable.film1, R.drawable.logo));*/
        final ProgramRecyclerAdapter adapter_items = new ProgramRecyclerAdapter(program_list);
        recycler_view.setHasFixedSize(true);
        recycler_view.setAdapter(adapter_items);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recycler_view, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                ImageView starView = (ImageView) view.findViewById(R.id.star_icon);
                Program program = program_list.get(position);
                Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                intent.putExtra("programName", program.getName());
                intent.putExtra("programTime",program.getTime());
                intent.putExtra("programChannel",program.getChannel());
                intent.putExtra("programImg", program.getLogo());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), program.getName().hashCode(), intent, 0);
                Log.i("hashcode", String.valueOf(program.getName().hashCode()));
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                String time = program.getTime();
                Log.i("time: ", time);
                String[] token = time.split(":");
                int hourOfDay = Integer.valueOf(token[0]);
                int minute = Integer.valueOf(token[1]);
                if (hourOfDay == 0)
                    token[0] = "24";
                Log.i("hour: ", String.valueOf(hourOfDay));
                Log.i("minute: ", String.valueOf(minute));
                int programTime = Integer.parseInt(token[0] + token[1]);
                Log.i("minute: ", token[0] + token[1]);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String formatTime = sdf.format(calendar.getTime());
                String[] token1 = formatTime.split(":");
                int currentTime = Integer.parseInt(token1[0] + token1[1]);
                calendar.setTimeInMillis(System.currentTimeMillis());
                if (programTime > currentTime) {
                    if (starView.getTag().equals("grey")) {
                        Toast.makeText(getApplicationContext(), program.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                        sp_list.add(program.getChannel()+program.getTime()+program.getName());
                        Log.i("size",String.valueOf(sp_list.size()));
                        String json = gson.toJson(sp_list);
                        editor.putString("program", json);
                        editor.apply();
                        starView.setImageResource(android.R.drawable.btn_star_big_on);
                        starView.setTag("yellow");
                        adapter_items.notifyItemChanged(position);
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } else {
                        sp_list.remove(program.getChannel()+program.getTime()+program.getName());
                        Log.i("size", String.valueOf(sp_list.size()));
                        String json = gson.toJson(sp_list);
                        editor.putString("program", json);
                        editor.apply();
                        starView.setTag("grey");
                        starView.setImageResource(android.R.drawable.btn_star_big_off);
                        adapter_items.notifyItemChanged(position);
                        alarmManager.cancel(pendingIntent);

                    }

                /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }*/

                } else {
                    Toast.makeText(getApplicationContext(), "Bu program seçilemez...", Toast.LENGTH_SHORT).show();
                    editor.clear();
                    editor.commit();
                }

            }
        }));
        new ProgramClass().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private class ProgramClass extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DetailActivity.this);
            progressDialog.setTitle("TvRehberi");
            progressDialog.setMessage("Veri Çekiliyor...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                String time = sdf.format(cal.getTime());
                Document doc  = Jsoup.connect(URL).get();
                Elements programName = doc.select("div.row div[class=ten columns]");
                Element programChannel = doc.select("div.row h1").first();
                Elements programTime = doc.select("div.row div[class=two columns time]");
                for(int i = 0; i < programName.size(); i++){
                    String[] token = programTime.get(i).attr("title").split(" ");
                    if(token[0].equals(time))
                        program_list.add(new Program(programName.get(i).text(),programChannel.text(),programTime.get(i).text(),bitmapKanalLogo));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ProgramRecyclerAdapter adapter_items = new ProgramRecyclerAdapter(program_list);
            recycler_view.setHasFixedSize(true);
            recycler_view.setAdapter(adapter_items);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            progressDialog.dismiss();
        }
    }


}
