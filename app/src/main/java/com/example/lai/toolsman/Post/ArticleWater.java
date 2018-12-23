package com.example.lai.toolsman.Post;

public class ArticleWater {
    private String title;
    private String desc;
    private String image;
    private String username;
    private String Profile;
    private String date;

    public ArticleWater(){

    }
    public ArticleWater(String title, String desc, String image, String username, String profile, String date) {
        this.title = title;
        this.desc = desc;
        this.username = username;
        this.image = image;
        this.Profile = profile;
        this.date = date;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        this.Profile = profile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}