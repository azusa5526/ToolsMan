package com.example.lai.toolsman.Post;

public class ArticleWater {
    private String title;
    private String desc;
    private String image;
    private String username;
    private String Profile;

    public ArticleWater(){

    }
    public ArticleWater(String title, String desc, String image, String username, String profile) {
        this.title = title;
        this.desc = desc;
        this.username = username;
        this.image = image;
        this.Profile = profile;
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
}