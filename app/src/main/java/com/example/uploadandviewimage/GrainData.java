package com.example.uploadandviewimage;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GrainData implements Parcelable {
    private String Message;

    private GrainItem[] Items;

    protected GrainData(Parcel in) {
        Message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GrainData> CREATOR = new Creator<GrainData>() {
        @Override
        public GrainData createFromParcel(Parcel in) {
            return new GrainData(in);
        }

        @Override
        public GrainData[] newArray(int size) {
            return new GrainData[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public GrainItem[] getItems() {
        return Items;
    }

    //20201126
    private GrainPie[] TypePie;
    private GrainPie[] SizePie;

    public GrainPie[] getTypePie() {
        int count = Items.length;
        List<GrainItem> list = Arrays.asList(Items);
        List<GrainType> listType = new ArrayList<GrainType>();

        //List<GrainSize> listSize= new ArrayList<GrainSize>();
        for (int i = 0; i < count; i++) {
            listType.add(list.get(i).getGrainType());
            //listSize.add(list.get(i).getGrainSize());
        }
        // Now let's group grain type
        Map<String, List<GrainType>> countByType = new HashMap<>();
        for (GrainType p : listType) {
            if (!countByType.containsKey(p.getName())) {
                countByType.put(p.getName(), new ArrayList<>());
            }
            countByType.get(p.getName()).add(p);
        }


        int countType = countByType.size();
        int pos;
        pos = 0;
        TypePie = new GrainPie[countType];
        for (Map.Entry<String, List<GrainType>> entry : countByType.entrySet()) {
            GrainPie p = new GrainPie();
            p.setName(entry.getKey());
            double size = entry.getValue().size();
            p.setValue(size);
            p.setPercent(size / count);
            TypePie[pos] = p;
            pos = pos + 1;
        }

        return TypePie;
    }

    public GrainPie[] getSizePie() {
        int count = Items.length;
        List<GrainItem> list = Arrays.asList(Items);
        //List<GrainType> listType = new ArrayList<GrainType>();
        List<GrainSize> listSize = new ArrayList<GrainSize>();
        for (int i = 0; i < count; i++) {
            //listType.add(list.get(i).getGrainType());
            listSize.add(list.get(i).getGrainSize());
        }
        // Now let's group grain size
        Map<String, List<GrainSize>> countBySize = new HashMap<>();
        for (GrainSize p : listSize) {
            if (!countBySize.containsKey(p.getName())) {
                countBySize.put(p.getName(), new ArrayList<>());
            }
            countBySize.get(p.getName()).add(p);
        }

        //int countType = countByType.size();
        int pos;
        pos = 0;
        int countSize = countBySize.size();
        SizePie = new GrainPie[countSize];
        for (Map.Entry<String, List<GrainSize>> entry : countBySize.entrySet()) {
            GrainPie p = new GrainPie();
            p.setName(entry.getKey());
            double size = entry.getValue().size();
            p.setValue(size);
            p.setPercent(size / count);
            SizePie[pos] = p;
            pos = pos + 1;
        }

        return SizePie;
    }

    public void setItems(GrainItem[] Items) {
        this.Items = Items;
    }

    @Override
    public String toString() {
        return "ClassPojo [Message = " + Message + ", Items = " + Items + "]";
    }
}