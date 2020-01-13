package com.example.news;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Article extends AppCompatActivity {
    TextView tvMsg,tvHead,tvArticle;
    ImageView IvUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" Article");

        tvMsg = findViewById(R.id.tvMsg);
        tvArticle = findViewById(R.id.TvArticle);
        tvHead = findViewById(R.id.TvTitle);
        IvUrl = findViewById(R.id.IvUrl);

        if(!ApplicationClass.isNetAvailable(this)){
            tvMsg.setVisibility(View.VISIBLE);
        }else {
            tvHead.setText(getIntent().getStringExtra("title"));
            new loadArticle().execute();

        }
    }

    public class loadArticle extends AsyncTask<Void,Void,Void>{
        ProgressDialog dialog;
        Element content ,image;
        Bitmap bitmap = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Article.this);
            dialog.setMessage("Loading feed");
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            String link = getIntent().getStringExtra("link");


            try {
                Document doc =  Jsoup.connect(link).get();
                content =  doc.getElementsByClass("_3WlLe clearfix  ").first();
                image = doc.getElementsByClass("_2gIK-").first();
                if (content == null && image == null){
                    content = doc.getElementsByClass("Normal").first();
                    image = doc.getElementsByClass("highlight clearfix").first();
                }
                if (image ==null)
                    image = doc.getElementsByClass("coverimgIn").first();

                if(image != null){
                    image = image.selectFirst("img");
                    String url = image.attr("src");
                    if (url.charAt(0) == '/')
                        url = "https://timesofindia.indiatimes.com"+url;
                    Log.d("IMAGE",url);
                    InputStream ips = new URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(ips);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

//                Element image = doc.getElementsByClass("_2gIK-").first().selectFirst("img");
//                System.out.println(content.text());
//                tvArticle.setText(content.text());
//                System.out.println(image.attr("src"));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (bitmap != null)
                IvUrl.setImageBitmap(bitmap);
            else
                IvUrl.setVisibility(View.GONE);
            tvArticle.setText(content.text().replace(".",".\n"));
        }
    }

// _2gIK- and 3WlLe clearfix
// Normal and  highlight clearfix
}
