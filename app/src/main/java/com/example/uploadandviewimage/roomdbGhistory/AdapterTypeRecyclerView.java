package com.example.uploadandviewimage.roomdbGhistory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.uploadandviewimage.GrainHistory;
import com.example.uploadandviewimage.GrainHistoryCollection;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

//try change history
public class AdapterTypeRecyclerView extends RecyclerView.Adapter<AdapterTypeRecyclerView.ViewHolders> {
//    public ArrayList<GHistory> daftarHistory;
    public ArrayList<GrainHistory> histories;
    public Context context;
    private AppDatabase db;

    public AdapterTypeRecyclerView(ArrayList<GrainHistory> histories, Context ctx) {
//        this.daftarType = daftarTypes;
        this.context = ctx;
        this.histories = histories;
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "tbGrainHistory").allowMainThreadQueries().build();

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
        GrainHistory note = getItem(position);


        holder.cvMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Toast.makeText(context , "ID : "+ daftarHistory.size() , Toast.LENGTH_LONG).show();

//                ArrayList<GHistory> grainHistory = new ArrayList<GHistory>();
//                grainHistory.addAll(Arrays.asList(db.gHistoryDao().selectAllItems()));

//                GrainHistoryCollection historyCollection = new GrainHistoryCollection(grainHistory);

                Date dateSelected = histories.get(position).getDateTime() ;

                //cari history berdasarkan date selected
                GrainHistory history = new GrainHistory();
                int count = histories.size();
                for (int i=0;i<count;i++) {
                    GrainHistory h = histories.get(i);
                    if (h.getDateTime().equals(dateSelected)) {
                        history = h;
                        break;
                    }
                }

//                GrainHistory history = historyCollection.findDate(dateSelected);

                context.startActivity(HistoryReadSingleActivity.getActIntent((Activity) context).putExtra("data", history));


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

                            }
                        }
                );
                return true;
            }
        });

        holder.itemTime.setText(AppUtils.getFormattedDateString(note.getDateTime()));

    }

    private GrainHistory getItem(int position) {
        return histories.get(position);
    }


    @Override
    public int getItemCount() {
        return histories.size();
    }


}
