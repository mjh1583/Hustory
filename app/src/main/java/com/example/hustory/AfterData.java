package com.example.hustory;

public class AfterData {
    private String uid;
    private String profesor;
    private String date;
    private String onOff;
    private String place;

    private String contents;

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOnOff() {
        return onOff;
    }

    public void setOnOff(String onOff) {
        this.onOff = onOff;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public AfterData(String profesor, String date, String onOff, String place,  String contents) {
        this.profesor = profesor;
        this.date = date;
        this.onOff = onOff;
        this.place = place;
        this.contents = contents;
    }
}
