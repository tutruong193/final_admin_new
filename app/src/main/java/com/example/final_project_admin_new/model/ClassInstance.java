package com.example.final_project_admin_new.model;

public class ClassInstance {

    private int id;           // ID của instance
    private String date;      // Ngày lớp học
    private String teacher;   // Tên giáo viên
    private String comments;  // Bình luận thêm
    private int classId;      // ID của lớp YogaClass mà instance này liên kết đến

    // Constructor
    public ClassInstance(String date, String teacher, String comments, int classId) {
        this.date = date;
        this.teacher = teacher;
        this.comments = comments;
        this.classId = classId;
    }

    // Getter và Setter cho các thuộc tính
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
