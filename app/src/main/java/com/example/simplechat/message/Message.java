package com.example.simplechat.message;

public class Message {

    String userName;
    String text;
    String photoUlr;

    public Message() {
    }

    public Message(String userName, String text, String photoUlr) {
        this.userName = userName;
        this.text = text;
        this.photoUlr = photoUlr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhotoUlr() {
        return photoUlr;
    }

    public void setPhotoUlr(String photoUlr) {
        this.photoUlr = photoUlr;
    }
}
