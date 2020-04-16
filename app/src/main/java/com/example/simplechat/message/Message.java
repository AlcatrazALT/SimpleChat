package com.example.simplechat.message;

public class Message {

    String userName;
    String text;
    String imageUlr;

    public Message() {
    }

    public Message(String userName, String text, String imageUlr) {
        this.userName = userName;
        this.text = text;
        this.imageUlr = imageUlr;
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

    public String getImageUlr() {
        return imageUlr;
    }

    public void setImageUlr(String imageUlr) {
        this.imageUlr = imageUlr;
    }
}
