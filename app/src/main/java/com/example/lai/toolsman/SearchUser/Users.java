package com.example.lai.toolsman.SearchUser;

public class Users {
    public String email, image, status;

    public Users() {

    }

    public Users(String email, String image, String status) {
        this.email = email;
        this.image = image;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
