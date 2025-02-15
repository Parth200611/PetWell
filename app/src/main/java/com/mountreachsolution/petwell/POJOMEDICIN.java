package com.mountreachsolution.petwell;

public class POJOMEDICIN {
    String id,username,time,date,name,dic,with,image;

    public POJOMEDICIN(String id, String username, String time, String date, String name, String dic, String with, String image) {
        this.id = id;
        this.username = username;
        this.time = time;
        this.date = date;
        this.name = name;
        this.dic = dic;
        this.with = with;
        this.image = image;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
