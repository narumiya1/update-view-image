package com.example.uploadandviewimage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    private  ArrayList<ExampleItem> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public TextView textScoreType;
        public TextView textScoreSize;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.text1);
            textScoreType = itemView.findViewById(R.id.text2);
            textScoreSize = itemView.findViewById(R.id.text3);
        }
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        holder.textName.setText(String.valueOf(position+1) + ". " + currentItem.getName());
        //holder.textScoreType.setText(currentItem.getScoreType());
        //holder.textScoreSize.setText(currentItem.getScoreSize());
        holder.textScoreType.setText(currentItem.getScore());
        holder.textScoreSize.setText("");
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }

    public ExampleAdapter(GrainData data) {
        mExampleList = new ArrayList<>();
        GrainItem[] items = data.getItems();
        int len = items.length;
        for (int i=0;i<len;i++) {
            GrainItem item = items[i];
            ExampleItem grain = new ExampleItem(item);
            mExampleList.add(grain);
        }
    }
}
