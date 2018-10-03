package com.example.susha.StudioProjects.cocktailparty.activities.model;

public class Event {

    private String title, year, description,time,image;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Event(String title,  String description,String year,String time,String image) {
        this.title = title;
        this.time = time;
        this.image = image;

        this.year = year;
        this.description=description;
    }

    public Event() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
