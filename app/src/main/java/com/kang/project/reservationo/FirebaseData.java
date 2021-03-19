package com.kang.project.reservationo;

import java.util.HashMap;
import java.util.Map;

public class FirebaseData {
    public String uid;
    private String ProfessorStr;
    private String SummaryStr;
    private String DateStr;
    private String TimeStr;
    private String WayStr;
    private String PlaceStr;
    private String allowStr;
    private String contentsStr;
    private boolean before_after_data;
    private String key;



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfessorStr() {
        return ProfessorStr;
    }

    public void setProfessorStr(String professorStr) {
        ProfessorStr = professorStr;
    }

    public String getSummaryStr() {
        return SummaryStr;
    }

    public void setSummaryStr(String summaryStr) {
        SummaryStr = summaryStr;
    }

    public String getDateStr() {
        return DateStr;
    }

    public void setDateStr(String dateStr) {
        DateStr = dateStr;
    }

    public String getTimeStr() {
        return TimeStr;
    }

    public void setTimeStr(String timeStr) {
        TimeStr = timeStr;
    }

    public String getWayStr() {
        return WayStr;
    }

    public void setWayStr(String wayStr) {
        WayStr = wayStr;
    }

    public String getPlaceStr() {
        return PlaceStr;
    }

    public void setPlaceStr(String placeStr) {
        PlaceStr = placeStr;
    }

    public String getAllowStr() {
        return allowStr;
    }

    public void setAllowStr(String allowStr) {
        this.allowStr = allowStr;
    }

    public String getContentsStr() {
        return contentsStr;
    }

    public void setContentsStr(String contentsStr) {
        this.contentsStr = contentsStr;
    }

    public boolean isBefore_after_data() {
        return before_after_data;
    }

    public void setBefore_after_data(boolean before_after_data) {
        this.before_after_data = before_after_data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FirebaseData(String uid, String professorStr, String summaryStr, String dateStr, String timeStr, String wayStr, String placeStr, String allowStr, String contentsStr, boolean before_after_data, String key) {
        this.uid = uid;
        ProfessorStr = professorStr;
        SummaryStr = summaryStr;
        DateStr = dateStr;
        TimeStr = timeStr;
        WayStr = wayStr;
        PlaceStr = placeStr;
        this.allowStr = allowStr;
        this.contentsStr = contentsStr;
        this.before_after_data = before_after_data;
        this.key = key;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", this.uid);
        result.put("professor", this.ProfessorStr);
        result.put("date", this.DateStr);
        result.put("time",this.TimeStr);
        result.put("way", this.WayStr);
        result.put("place", this.PlaceStr);
        result.put("allow", this.allowStr);
        result.put("contents", this.contentsStr);
        result.put("summary", this.SummaryStr);
        result.put("before_after_data", this.before_after_data);
        result.put("key", this.key);

        return result;
    }


}
