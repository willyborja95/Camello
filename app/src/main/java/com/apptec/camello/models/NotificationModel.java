package com.apptec.camello.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.apptec.camello.repository.localdatabase.DBConstants;
import com.apptec.camello.repository.localdatabase.converter.DateConverter;

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
    private Long sentDate;

    @ColumnInfo(name = DBConstants.NOTIFICATION_EXPIRATION)
    private Long expirationDate;

    // Constructor
    public NotificationModel(String title, String text, Long expirationDate, Long sentDate) {

        this.title = title;
        this.text = text;
        this.expirationDate = expirationDate;
        this.sentDate = sentDate;
    }

    /**
     * This constructor is called from the Notification Builder
     */
    @Ignore
    public NotificationModel() {

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

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getSentDate() {
        return sentDate;
    }

    public void setSentDate(Long sentDate) {
        this.sentDate = sentDate;
    }


    // Attributes and methods for the view
    @Ignore
    String readableSentDate = null;
    @Ignore
    String readAbleExpirationDate = null;

    // Getters
    public String getReadableSentDate() {
        if (readableSentDate == null) {
            readableSentDate = DateConverter.toStringDateFormat(this.sentDate);
        }
        return readableSentDate;
    }

    public String getReadAbleExpirationDate() {
        if (expirationDate == null) {
            readAbleExpirationDate = DateConverter.toStringDateFormat(this.expirationDate);
        }
        return readAbleExpirationDate;
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