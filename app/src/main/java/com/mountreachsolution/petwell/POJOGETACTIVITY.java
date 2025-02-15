package com.mountreachsolution.petwell;

public class POJOGETACTIVITY {
    String id,username,time,date,exercise,duration,dis;

    public POJOGETACTIVITY(String id, String username, String time, String date, String exercise, String duration, String dis) {
        this.id = id;
        this.username = username;
        this.time = time;
        this.date = date;
        this.exercise = exercise;
        this.duration = duration;
        this.dis = dis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }
}
