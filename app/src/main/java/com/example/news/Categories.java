package com.example.news;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Categories extends AppCompatActivity implements MyAdapter.ItemSelected {

    TextView tvMsg;
    RecyclerView recyclerView;
    ArrayList<String> Titles,Links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        tvMsg = findViewById(R.id.tvMsg);
        Titles = new ArrayList<>(0);
        Links = new ArrayList<>(0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("category"));


        if(!ApplicationClass.isNetAvailable(this))
            tvMsg.setVisibility(View.VISIBLE);
        else{

            recyclerView = findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //adapter
            new fetching().execute();

        }

    }
    public InputStream getInputStream(URL url){
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }


    public class fetching extends AsyncTask<Void,Void,Void>{
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Categories.this);
            dialog.setMessage("Loading feed");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                URL url = new URL(getIntent().getStringExtra("rsslink"));
                xpp.setInput(getInputStream(url),"UTF_8");

                boolean insideItem = false;
                for (int evType = xpp.getEventType() ; evType != xpp.END_DOCUMENT ;evType =  xpp.next())
                {
                    if(evType == xpp.START_TAG){
                        if (xpp.getName().equalsIgnoreCase("item"))
                            insideItem = true;
                        else if (insideItem && xpp.getName().equalsIgnoreCase("title"))
                            Titles.add(xpp.nextText());
                        else if (insideItem && xpp.getName().equalsIgnoreCase("link"))
                            Links.add(xpp.nextText());
                    }
                    else if (evType == xpp.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                        insideItem = false;
                    }
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (Titles.isEmpty()) {
                tvMsg.setText("Nothing to show here");
                tvMsg.setVisibility(View.VISIBLE);
            }
            else
                recyclerView.setAdapter(new MyAdapter(Categories.this,Titles));

        }
    }

    @Override
    public void onItemSelected(int i) {
        Intent intent = new Intent(this,Article.class);
        intent.putExtra("link",Links.get(i));
        intent.putExtra("title",Titles.get(i));
        startActivity(intent);

    }


}
