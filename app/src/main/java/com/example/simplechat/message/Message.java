package com.example.simplechat.message;

public class Message {

    private String userName;
    private String text;
    private String imageUlr;
    private String sender;
    private String recipient;

    private boolean isSenderSide;

    public Message() {
    }

    public Message(String userName, String text, String imageUlr) {
        this.userName = userName;
        this.text = text;
        this.imageUlr = imageUlr;
    }

    public Message(String userName, String imageUlr, String sender, String recipient) {
        this.userName = userName;
        this.imageUlr = imageUlr;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Message(String userName, String text, String imageUlr, String sender, String recipient) {
        this.userName = userName;
        this.text = text;
        this.imageUlr = imageUlr;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Message(String userName, String text, String imageUlr, String sender, String recipient, boolean isSenderSide) {
        this.userName = userName;
        this.text = text;
        this.imageUlr = imageUlr;
        this.sender = sender;
        this.recipient = recipient;
        this.isSenderSide = isSenderSide;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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

    public boolean isSenderSide() {
        return isSenderSide;
    }

    public void setSenderSide(boolean senderSide) {
        isSenderSide = senderSide;
    }
}
