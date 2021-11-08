package com.example.mothertongue.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Lesson implements Serializable {

    public Lesson() {
    }

    public String lesson, title, lesson_number, quiz_instruction;
    public Integer id;
    public Object body;

    public Lesson(String lesson, String title, String lesson_number, String quiz_instruction, Integer id, Object body) {
        this.lesson = lesson;
        this.title = title;
        this.lesson_number = lesson_number;
        this.quiz_instruction = quiz_instruction;
        this.id = id;
        this.body = body;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLesson_number() {
        return lesson_number;
    }

    public void setLesson_number(String lesson_number) {
        this.lesson_number = lesson_number;
    }

    public String getQuiz_instruction() {
        return quiz_instruction;
    }

    public void setQuiz_instruction(String quiz_instruction) {
        this.quiz_instruction = quiz_instruction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
