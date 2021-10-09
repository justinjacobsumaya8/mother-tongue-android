package com.example.mothertongue.Models;

public class Word {

    String title, description;
    Integer id;

    public Word() {
    }

    public Word(String title, String description, Integer id) {
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
