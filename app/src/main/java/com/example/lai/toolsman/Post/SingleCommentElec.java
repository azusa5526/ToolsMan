package com.example.lai.toolsman.Post;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SingleCommentElec  {
    private String Comment;

    public SingleCommentElec() {

    }
    public SingleCommentElec(String comment) {
        this.Comment = comment;
    }
    public String getComment()
    {
        return  Comment;

    }

    public void setComment(String comment) {
        Comment = comment;
    }

}
