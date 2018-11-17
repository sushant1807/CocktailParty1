package com.example.susha.StudioProjects.cocktailparty.activities.model;

import java.util.List;

public class Event {

    private String title;
    private String year;
    private String description;
    private String time;
    private String image;
    private String email;

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    private List<String> likes;
    private String createdat;

    public String getTime() {
        return time;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Event(String title,  String description,String year,String time,String image, String email,String createdat,List<String> likes) {
        this.title = title;
        this.time = time;
        this.image = image;
        this.email=email;
        this.likes=likes;
        this.createdat=createdat;

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
