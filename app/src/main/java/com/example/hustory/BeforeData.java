package com.example.hustory;

public class BeforeData {
    private String uid;
    private String profesor;
    private String date;
    private String onOff;
    private String place;
    private String accept;
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

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public BeforeData(String profesor, String date, String onOff, String place, String accept, String contents) {
        this.profesor = profesor;
        this.date = date;
        this.onOff = onOff;
        this.place = place;
        this.accept = accept;
        this.contents = contents;
    }
}
