package com.example.uploadandviewimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.uploadandviewimage.helper.AppzDatabase;
import com.example.uploadandviewimage.helper.GrainTypeData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GrainHistoryCollection {

    private ArrayList<GrainHistory> listHistory;
    Context context;
    private AppzDatabase db;

    public ArrayList<GrainHistory> GetList() {
        return this.listHistory;
    }

    public GrainHistoryCollection(ArrayList<GrainTypeData> list) {
        listHistory = new ArrayList();
        int count = list.size();
        ArrayList<Date> listDate = new ArrayList();
        //get distinct date
        for (int i = 0; i < count; i++) {
            GrainTypeData obj = list.get(i);
            Date date = obj.getCreatedAt();
            if (!listDate.contains(date)) {
                listDate.add(date);
            }
            //TypeDAO dao = (TypeDAO)obj;
            //String coba="";
        }
        //get object based on datetime
        int countDate = listDate.size();
        List<GrainTypeData> list2 = (ArrayList<GrainTypeData>) list.clone();
        for (int i = 0; i < countDate; i++) {
            GrainHistory history = new GrainHistory();
            Date date = listDate.get(i);
            history.setDateTime(date);

            //check grain type
            int countType = list2.size();
            //Predicate<GrainTypeData> byDate = myList -> myList.getCreatedAt() == date;
            //Object coba = list2.stream().filter(byDate).collect(Collectors.toList());
            //Predicate<GrainTypeData> by2 = myList -> myList.getNamaBarang() == "Karawang";
            //Object coba2 = FluentIterable.from(list2).allMatch(by2);

            ArrayList<GrainPie> listPie = new ArrayList<GrainPie>();
            for (int j = 0; j < countType; j++) {
                GrainTypeData grain1 = list.get(j);
                Date date1 = listDate.get(i);
                Date date2 = grain1.getCreatedAt();
                if (date1.equals(new java.sql.Date(date2.getTime()))) {
                    GrainPie pie1 = new GrainPie();
                    pie1.setName(grain1.getNamaType());
                    pie1.setValue(grain1.getVal());
                    pie1.setPercent(grain1.getPct());

                    //list2.remove(j);
                    listPie.add(pie1);
                }
            }
            history.setType(listPie);
            listHistory.add(history);
        }
        //String coba="";
    }

    public GrainHistory findDate(Date date) {
        GrainHistory ret = new GrainHistory();
        int count = listHistory.size();
        for (int i=0;i<count;i++) {
            GrainHistory h = listHistory.get(i);
            if (h.getDateTime().equals(new java.sql.Date(date.getTime()))) {
                ret = h;
                break;
            }
        }
        return ret;
    }

}
