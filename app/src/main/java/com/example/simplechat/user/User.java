package com.example.simplechat.user;

public class User {

    private String id;
    private String name;
    private String email;
    private int avatarResource;

    public User() {

    }

    public User(String id, String name, String email, int avatarResource) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatarResource = avatarResource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAvatarResource() {
        return avatarResource;
    }

    public void setAvatarResource(int avatarResource) {
        this.avatarResource = avatarResource;
    }
}
