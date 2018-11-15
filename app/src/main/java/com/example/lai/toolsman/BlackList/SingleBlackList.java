package com.example.lai.toolsman.BlackList;

public class SingleBlackList {

    private String EmailForBlackList;

    public SingleBlackList(){

    }

    public SingleBlackList(String email){
        this.EmailForBlackList = email;
    }

    public String getEmail() {
        return EmailForBlackList;
    }

    public void setEmail(String email) {
        this.EmailForBlackList = email;
    }
}

