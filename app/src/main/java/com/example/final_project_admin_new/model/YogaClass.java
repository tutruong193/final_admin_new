package com.example.final_project_admin_new.model;

public class YogaClass {
    private int id;
    private String dayOfWeek;  // Ngày trong tuần, ví dụ: "Tuesday"
    private String time;       // Thời gian lớp học, ví dụ: "10:00"
    private int capacity;      // Sức chứa
    private int duration;      // Thời lượng lớp học, ví dụ: 60 phút
    private double price;      // Giá mỗi buổi học
    private String classType;  // Loại lớp, ví dụ: "Flow Yoga"
    private String description; // Mô tả (không bắt buộc)

    // Constructor
    public YogaClass(String dayOfWeek, String time, int capacity, int duration, double price, String classType, String description) {
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.capacity = capacity;
        this.duration = duration;
        this.price = price;
        this.classType = classType;
        this.description = description;
    }

    // Getters và setters cho các trường
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

