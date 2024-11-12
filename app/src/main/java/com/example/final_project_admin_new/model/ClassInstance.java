package com.example.final_project_admin_new.model;

public class ClassInstance {

    private int id;
    private String date;
    private String teacher;
    private String comments;
    private int classId;


    public ClassInstance(String date, String teacher, String comments, int classId) {
        this.date = date;
        this.teacher = teacher;
        this.comments = comments;
        this.classId = classId;
    }
    public ClassInstance(int id, String date, String teacher, String comments, int classId) {
        this.date = date;
        this.teacher = teacher;
        this.comments = comments;
        this.classId = classId;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
