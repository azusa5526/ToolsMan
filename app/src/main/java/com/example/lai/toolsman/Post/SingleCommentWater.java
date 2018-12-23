package com.example.lai.toolsman.Post;

public class SingleCommentWater {
    private String Comment;
    private String Email;
    private String Profile;
    private String uid;
    private String date;

    public SingleCommentWater() {

    }

    public SingleCommentWater(String comment, String email, String profile, String uid, String date) {
        Comment = comment;
        Email = email;
        Profile = profile;
        this.uid = uid;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
