package com.example.mothertongue.Models;

public class Word {

    String title;
    String description;
    String english_description;
    String audio_url;
    Integer id;

    public Word() {
    }

    public Word(String title, String description, String english_description, String audio_url, Integer id) {
        this.title = title;
        this.description = description;
        this.english_description = english_description;
        this.audio_url = audio_url;
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

    public String getEnglish_description() {
        return english_description;
    }

    public void setEnglish_description(String english_description) {
        this.english_description = english_description;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
