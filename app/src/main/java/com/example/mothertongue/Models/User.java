package com.example.mothertongue.Models;

public class User {
    String first_name, last_name, android_id, date_created;

    public User() {
    }

    public User(String first_name, String last_name, String android_id, String date_created) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.android_id = android_id;
        this.date_created = date_created;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }
}
