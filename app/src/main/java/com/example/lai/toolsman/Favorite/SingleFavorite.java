package com.example.lai.toolsman.Favorite;

public class SingleFavorite {

    private String EmailForFavorite;

    public SingleFavorite(){

    }

    public SingleFavorite(String email){
        this.EmailForFavorite = email;
    }

    public String getEmail() {
        return EmailForFavorite;
    }

    public void setEmail(String email) {
        this.EmailForFavorite = email;
    }
}

