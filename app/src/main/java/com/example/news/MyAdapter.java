package com.example.news;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {

    ArrayList<String> list;
    ItemSelected context;

    public interface ItemSelected{
        void onItemSelected(int i);
    }

    public MyAdapter(Context context, ArrayList<String> list){
        this.context =(ItemSelected) context;
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_layout,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int i) {
        holder.tvRow.setText(list.get(i));
        Log.d("ITEM",list.get(i));
        holder.itemView.setId(i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView IvRow;
        TextView tvRow;
        public myViewHolder(@NonNull final View itemView) {
            super(itemView);
            IvRow = itemView.findViewById(R.id.IvRow);
            tvRow = itemView.findViewById(R.id.tvRow);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.onItemSelected(itemView.getId());
                }
            });

        }
    }
}
