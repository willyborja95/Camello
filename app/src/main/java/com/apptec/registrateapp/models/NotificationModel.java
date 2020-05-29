package com.apptec.registrateapp.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.apptec.registrateapp.repository.localdatabase.DBConstants;

import java.util.Date;

@Entity(tableName = DBConstants.NOTIFICATION_TABLE)
public class NotificationModel {
    /*
     * Model of a notification.
     *
     * This will be used for the list adapter attached with the recycler view.
     * */

    // Attributes
    @ColumnInfo(name = DBConstants.NOTIFICATION_PK)
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = DBConstants.NOTIFICATION_TITLE)
    private String title;

    @ColumnInfo(name = DBConstants.NOTIFICATION_TEXT)
    private String text;

    @ColumnInfo(name = DBConstants.NOTIFICATION_SENT_DATE)
    private Date sentDate;

    @ColumnInfo(name = DBConstants.NOTIFICATION_EXPIRATION)
    private Date expirationDate;

    // Constructor
    public NotificationModel(String title, String text, Date expirationDate, Date sentDate) {

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