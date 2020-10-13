package com.example.arun.firealert;

public class Upload {
    private String username;
    private String email;
    private String mobile;
    private String post;
    private String mImageUrl;
    private String key;

    public Upload() {

    }

    public Upload(String username, String email, String mobile, String post, String mImageUrl) {
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.post = post;
        this.mImageUrl = mImageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
