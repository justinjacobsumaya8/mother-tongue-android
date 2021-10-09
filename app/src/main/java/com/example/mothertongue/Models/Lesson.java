package com.example.mothertongue.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Lesson implements Serializable {

    public Lesson() {
    }

    public String lesson, lesson_number, title;
    public Integer id;
    public Object body;


    public Lesson(String lesson, String lesson_number, String title, Integer id, Object body) {
        this.lesson = lesson;
        this.lesson_number = lesson_number;
        this.title = title;
        this.id = id;
        this.body = body;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getLesson_number() {
        return lesson_number;
    }

    public void setLesson_number(String lesson_number) {
        this.lesson_number = lesson_number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
