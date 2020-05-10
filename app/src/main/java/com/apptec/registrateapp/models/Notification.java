package com.apptec.registrateapp.models;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import java.util.Date;

@Entity
public class Notification {
    /*
     * Model of a notification.
     *
     * This will be used for the list adapter attached with the recycler view.
     * */

    // Attributes
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "expirationDate")
    private Date expirationDate;

    @ColumnInfo(name = "sentDate")
    private Date sentDate;

    // Constructor
    public Notification(String title, String text, Date expirationDate, Date sentDate) {

        this.title = title;
        this.text = text;
        this.expirationDate = expirationDate;
        this.sentDate = sentDate;
    }

    // Setter and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }


    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", expirationDate=" + expirationDate +
                ", sentDate=" + sentDate +
                '}';
    }
}