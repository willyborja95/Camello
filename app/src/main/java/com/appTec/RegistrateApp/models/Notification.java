package com.appTec.RegistrateApp.models;


import java.sql.Date;

public class Notification {
    /*
     * Model of a notification.
     *
     * This will be used for the list adapter attached with the recycler view.
     * */

    // Attributes
    private String title;
    private Date sentDate;
    private String content;
    private Date expirationDate;

    // Constructor
    public Notification(String title, Date sentDate, String content, Date expirationDate) {
        this.title = title;
        this.sentDate = sentDate;
        this.content = content;
        this.expirationDate = expirationDate;
    }


    // Setter and getters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }


}