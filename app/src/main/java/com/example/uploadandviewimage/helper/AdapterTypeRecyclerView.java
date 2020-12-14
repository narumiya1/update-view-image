package com.example.uploadandviewimage.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class AdapterTypeRecyclerView extends RecyclerView.Adapter<AdapterTypeRecyclerView.ViewHolders> {
    public ArrayList<Type> daftarType;
    public Context context;
    private AppzDatabase db;

    public AdapterTypeRecyclerView(ArrayList<Type> daftarTypes, Context ctx) {
        this.daftarType = daftarTypes;
        this.context = ctx;

        db = Room.databaseBuilder(context.getApplicationContext(),
                AppzDatabase.class, "tbType").allowMainThreadQueries().build();


    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView tvTitle, itemTime, item_jumlah;
        CardView cvMain;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_namabarang);
            itemTime = itemView.findViewById(R.id.item_desc);
            cvMain = itemView.findViewById(R.id.cv_main);
            item_jumlah = itemView.findViewById(R.id.tv_jumlah1);

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
        Type note = getItem(position);

        final String name = daftarType.get(position).getNamaType();
        final double jumlah = daftarType.get(position).getJumlahType();
        String value = new Double(jumlah).toString();

        holder.cvMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /**
                 *  Kodingan untuk tutorial Selanjutnya :p Read detail data
                 */
                Type barang = db.typeDAO().selectBarangDetail(daftarType.get(position).getTypeId());
                context.startActivity(RoomReadSingleActivity.getActIntent((Activity) context).putExtra("data", barang));
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
        holder.tvTitle.setText(name);
        holder.itemTime.setText(AppUtils.getFormattedDateString(note.getCreatedAt()));
        holder.item_jumlah.setText(value + " %");
    }

    private Type getItem(int position) {
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
