package com.example.uploadandviewimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uploadandviewimage.ExampleItem;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.roomdbGhistory.GHistory;
import com.example.uploadandviewimage.roomdbGhistory.Gitem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CustomRecyclerview extends RecyclerView.Adapter<CustomRecyclerview.ViewHolder> {

    Context context;
    List<Gitem> arrayList;
    private ArrayList<ExampleItem> mExampleList;
    public CustomRecyclerview(Context context, List<Gitem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Gitem repo = arrayList.get(position);
        holder.name.setText("" + repo.getName());
        holder.description.setText("" + repo.getShape());
        holder.price.setText("" + repo.getId()+""+position+1);

        holder.wd.setText("" + repo.getJson());
        holder.textName.setText(""+String.valueOf(position+1) + ". " + repo.getName());
//        holder.textName.setText(""+repo.getName());
        holder.textScoreType.setText(""+repo.getScore());
        holder.textScoreSize.setText("");

        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        holder.chef.setText("" + json.getBytes());

//        Glide.with(context)
//                .load(repo.getThumbnail())
//                .into(holder.thumbnail);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, description, price, chef, wd, textName,textScoreType,textScoreSize;
        public ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            chef = itemView.findViewById(R.id.chef);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            wd = itemView.findViewById(R.id.wd);
            textName = itemView.findViewById(R.id.text1);
            textScoreType = itemView.findViewById(R.id.text2);
            textScoreSize = itemView.findViewById(R.id.text3);

        }
    }
}