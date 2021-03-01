package com.example.uploadandviewimage.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.uploadandviewimage.GrainHistory;
import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.roomdbGhistory.AdapterTypeRecyclerView;
import com.example.uploadandviewimage.roomdbGhistory.AppDatabase;
import com.example.uploadandviewimage.roomdbGhistory.HistoryReadSingleActivity;
import com.example.uploadandviewimage.utils.AppUtils;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Date;

public class AdapterFragmentHome extends RecyclerView.Adapter<AdapterFragmentHome.ViewHolders> {
    //    public ArrayList<GHistory> daftarHistory;
    public ArrayList<GrainHistory> histories;
    public Context context;
    private AppDatabase db;

    public AdapterFragmentHome(ArrayList<GrainHistory> histories, Context ctx) {
//        this.daftarType = daftarTypes;
        this.context = ctx;
        this.histories = histories;
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "tbGrainHistory").allowMainThreadQueries().build();

    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView tv_item1_home, tv_items2_home,data_szie_home,tv_items3_home,item_time_home;
        CardView cvMain;
        ImageView img;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            tv_item1_home = itemView.findViewById(R.id.tv_item1_home);
            tv_items2_home = itemView.findViewById(R.id.tv_items2_home);
            data_szie_home = itemView.findViewById(R.id.data_szie_home);
            tv_items3_home = itemView.findViewById(R.id.tv_items3_home);
            item_time_home=itemView.findViewById(R.id.item_time_home);
            cvMain = itemView.findViewById(R.id.cv_main_home);

        }
    }

    @NonNull
    @Override
    public AdapterFragmentHome.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterFragmentHome.ViewHolders vh = new AdapterFragmentHome.ViewHolders(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFragmentHome.ViewHolders holder, int position) {
        GrainHistory note = getItem(position);


        holder.cvMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Date dateSelected = histories.get(position).getDateTime();

                //cari history berdasarkan date selected
                GrainHistory history = new GrainHistory();
                int count = histories.size();
                for (int i = 0; i < count; i++) {
                    GrainHistory h = histories.get(i);
                    if (h.getDateTime().equals(dateSelected)) {
                        history = h;
                        break;
                    }
                }

                context.startActivity(HistoryReadSingleActivity.getActIntent((Activity) context).putExtra("data", history));


            }
        });
        Date dateSelected = histories.get(position).getDateTime();
        GrainHistory history = new GrainHistory();
        int count = histories.size();

        StringBuilder builders = new StringBuilder();
        for (int i = 0; i < count; i++) {
            GrainHistory h = histories.get(i);
            if (h.getDateTime().equals(dateSelected)) {
                history = h;
                ArrayList<GrainPie> mySize = history.getSize();
                int counts =mySize.size();
                for (int j=0;j<counts;j++) {
                    GrainPie sizc = mySize.get(j);
                    float a = (float) sizc.getValue();
                    int b = (int)Math.round(a);
                    String c = String.valueOf(b);
                    String s = sizc.getName();
                    builders.append(s).append(" - ").append(b).append(" ").append("\t\r\n");
                    holder.tv_items2_home.setText(builders.toString());
                }
                break;
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            GrainHistory h = histories.get(i);
            if (h.getDateTime().equals(dateSelected)) {
                history = h;
                ArrayList<GrainPie> myType = history.getType();
                int counts =myType.size();
                for (int j=0;j<counts;j++) {
                    GrainPie size = myType.get(j);
                    float a = (float) size.getValue();
                    int b = (int)Math.round(a);
                    String c = String.valueOf(b);
                    String s = size.getName();
                    builder.append(s).append(" - ").append(b).append(" ").append("\t\r\n");
                    holder.tv_items3_home.setText(builder.toString());
                }
                break;
            }
        }


        holder.item_time_home.setText(AppUtils.getFormattedDateString(note.getDateTime()));

    }

    private GrainHistory getItem(int position) {
        return histories.get(position);
    }


    @Override
    public int getItemCount() {
        return histories.size();
    }
}
