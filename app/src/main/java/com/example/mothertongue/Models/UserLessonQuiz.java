package com.example.mothertongue.Models;

import java.sql.Timestamp;

public class UserLessonQuiz {

    Integer lesson_id, score, total;
    String user_id, date_created;

    public UserLessonQuiz() {
    }

    public UserLessonQuiz(Integer lesson_id, Integer score, Integer total, String user_id, String date_created) {
        this.lesson_id = lesson_id;
        this.score = score;
        this.total = total;
        this.user_id = user_id;
        this.date_created = date_created;
    }

    public Integer getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(Integer lesson_id) {
        this.lesson_id = lesson_id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
