package com.example.uploadandviewimage.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.uploadandviewimage.GrainHistory;
import com.example.uploadandviewimage.GrainHistoryCollection;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.SecondActivity;
import com.example.uploadandviewimage.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

//try change history
public class AdapterTypeRecyclerView extends RecyclerView.Adapter<AdapterTypeRecyclerView.ViewHolders> {
    public ArrayList<GrainTypeData> daftarType;
    public ArrayList<GrainHistory> histories;
    public Context context;
    private AppzDatabase db;

    public AdapterTypeRecyclerView(ArrayList<GrainTypeData> daftarTypes,ArrayList<GrainHistory> histories, Context ctx) {
        this.daftarType = daftarTypes;
        this.context = ctx;
        this.histories = histories;
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppzDatabase.class, "tbType").allowMainThreadQueries().build();


    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView  itemTime;
        CardView cvMain;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            itemTime = itemView.findViewById(R.id.item_desc);
            cvMain = itemView.findViewById(R.id.cv_main);

        }
    }

    @NonNull
    @Override
    public ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolders vh = new ViewHolders(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolders holder, int position) {
        GrainTypeData note = getItem(position);

        final String name = daftarType.get(position).getNamaType();
        final double val = daftarType.get(position).getVal();
        final double pct = daftarType.get(position).getPct();
        String value = new Double(val).toString();

        holder.cvMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                GrainHistoryCollection historyCollection = new GrainHistoryCollection(daftarType);
                //ArrayList<GrainHistory> history = historyCollection.GetList();

                //Date date = new Date();
                Date dateSelected = daftarType.get(position).getCreatedAt() ;

                GrainHistory history = historyCollection.findDate(dateSelected);

                //GrainTypeData barang = db.typeDAO().selectBarangDetail(daftarType.get(position).getTypeId());

                //context.startActivity(RoomReadSingleActivity.getActIntent((Activity) context).putExtra("data", barang));
                context.startActivity(RoomReadSingleActivity.getActIntent((Activity) context).putExtra("data", history));

//                Bundle bundles = new Bundle();
//                bundles.putSerializable("test", history);
//                bundles.putSerializable("data", dateSelected);
//                Intent intent = new Intent(context, RoomReadSingleActivity.class);
//                intent.putExtras(bundles);
//                context.startActivity(intent);

            }
        });
        holder.cvMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                /**
                 *  Kodingan untuk tutorial Selanjutnya :p Delete dan update data
                 */
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.view_dialog);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button delButton = dialog.findViewById(R.id.bt_delete_data);


                //apabila tombol delete diklik
                delButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                onDeteleBarang(position);
                            }
                        }
                );
                return true;
            }
        });

        holder.itemTime.setText(AppUtils.getFormattedDateString(note.getCreatedAt()));

    }

    private GrainTypeData getItem(int position) {
        return daftarType.get(position);
    }


    private void onDeteleBarang(int position) {
        db.typeDAO().deleteBarang(daftarType.get(position));
        daftarType.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, daftarType.size());
    }

    @Override
    public int getItemCount() {
        return daftarType.size();
    }


}
