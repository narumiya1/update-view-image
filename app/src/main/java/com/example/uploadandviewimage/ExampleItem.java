package com.example.uploadandviewimage;

import java.util.ArrayList;

public class ExampleItem {
    private String nameType;
    private double classificationType;
    private double identificationType;
    private String nameSize;
    private double classificationSize;
    private double identificationSize;

    public ExampleItem(String nameSize, double classificationSize, double identificationSize, String nameType, double classificationType, double identificationType) {
        this.nameSize = nameSize;
        this.classificationSize = classificationSize;
        this.identificationSize = identificationSize;
        this.nameType = nameType;
        this.classificationType = classificationType;
        this.identificationType = identificationType;
    }

    public ExampleItem(GrainItem item) {
        this.nameSize = item.getGrainSize().getName();
        this.classificationSize = item.getGrainSize().getScoreClassification();
        this.identificationSize = item.getGrainSize().getScoreIdentification();
        this.nameType = item.getGrainType().getName();
        this.classificationType = item.getGrainType().getScoreClassification();
        this.identificationType = item.getGrainType().getScoreIdentification();
    }

    public String getName() {

        return this.nameType + ", " + this.nameSize;
    }

    public String getScoreSize() {
        String str = "(";
        str = str + String.format("%.0f", this.classificationSize);
        str = str + ", ";
        str = str + String.format("%.0f", this.identificationSize);
        str = str + ")";
        return str;
    }

    public String getScoreType() {
        String str = "(";
        str = str + String.format("%.0f", this.classificationType);
        str = str + ", ";
        str = str + String.format("%.0f", this.identificationType);
        str = str + ")";
        return str;
    }

    public String getScore() {
        String str = "(";
        str = str + String.format("%.0f", this.classificationSize);
        str = str + ", ";
        str = str + String.format("%.0f", this.identificationSize);
        str = str + ")";
        str = str + ", ";
        str = str + "(";
        str = str + String.format("%.0f", this.classificationType);
        str = str + ", ";
        str = str + String.format("%.0f", this.identificationType);
        str = str + ")";
        return str;
    }


}


