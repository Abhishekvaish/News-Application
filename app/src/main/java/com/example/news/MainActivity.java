package com.example.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemSelected {
    RecyclerView recyclerView;
    ArrayList<String> rssLink , categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Categories)));
        rssLink = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.CatLinks)));

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(this,categories));
    }

    @Override
    public void onItemSelected(int i) {
        Intent intent = new Intent(MainActivity.this,Categories.class);
        intent.putExtra("rsslink",rssLink.get(i));
        intent.putExtra("category",categories.get(i));
        startActivity(intent);
    }
}