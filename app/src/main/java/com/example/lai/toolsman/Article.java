package com.example.lai.toolsman;

public class Article {

    private String title;
    private String desc;
    private String username;

    public Article(){

    }

    public Article(String title, String desc, String username) {
        this.title = title;
        this.desc = desc;
        this.username = username;
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
}
