package com.example.uploadandviewimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.example.uploadandviewimage.roomdbGhistory.AppDatabase;
import com.example.uploadandviewimage.roomdbGhistory.GHistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GrainHistoryCollection {

    private ArrayList<GrainHistory> listHistory;
    Context context;
    private AppDatabase db;

    public ArrayList<GrainHistory> GetList() {
        return this.listHistory;
    }

    public GrainHistoryCollection(ArrayList<GHistory> list) {
        listHistory = new ArrayList();
        int count = list.size();
        ArrayList<Date> listDate = new ArrayList();
        //get distinct date
        for (int i = 0; i < count; i++) {
            GHistory obj = list.get(i);
            Date date = obj.getCreatedAt();
            if (!listDate.contains(date)) {
                listDate.add(date);
            }
            //TypeDAO dao = (TypeDAO)obj;
            //String coba="";
        }
        //get object based on datetime
        int countDate = listDate.size();
        List<GHistory> list2 = (ArrayList<GHistory>) list.clone();
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
            ArrayList<GrainPie> listSize = new ArrayList<GrainPie>();
            ArrayList<GrainPie> listType = new ArrayList<GrainPie>();
            for (int j = 0; j < countType; j++) {
                GHistory grain1 = list.get(j);
                Date date1 = listDate.get(i);
                Date date2 = grain1.getCreatedAt();
                if (date1.equals(new java.sql.Date(date2.getTime()))) {
                    if(grain1.getDataType() == 1 ){
                        //type
                        GrainPie pie1 = new GrainPie();
                        pie1.setName(grain1.getName());
                        pie1.setValue(grain1.getVal());
                        pie1.setPercent(grain1.getPct());
                        listType.add(pie1);
                    }else if(grain1.getDataType() == 2){
                        //size
                        GrainPie pie2 = new GrainPie();
                        pie2.setName(grain1.getName());
                        pie2.setValue(grain1.getVal());
                        pie2.setPercent(grain1.getPct());
                        //list2.remove(j);
                        listSize.add(pie2);
                    }


                }
            }

            history.setType(listType);
            history.setSizes(listSize);
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
