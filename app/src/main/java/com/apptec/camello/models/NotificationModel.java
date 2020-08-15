package com.apptec.camello.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.apptec.camello.repository.localdatabase.DBConstants;
import com.apptec.camello.repository.localdatabase.converter.DateConverter;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import timber.log.Timber;

/**
 * Model of a notification.
 * <p>
 * This will be used for the list adapter attached with the recycler view.
 */
@Entity(tableName = DBConstants.NOTIFICATION_TABLE)
public class NotificationModel {


    // Attributes
    @ColumnInfo(name = DBConstants.NOTIFICATION_SENT_DATE)
    @PrimaryKey(autoGenerate = false)  // Using this attribute as primary key
    @SerializedName("createdAt")
    private Long sentDate;

    @ColumnInfo(name = DBConstants.NOTIFICATION_TITLE)
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = DBConstants.NOTIFICATION_TEXT)
    @SerializedName("message")
    private String text;

    @ColumnInfo(name = DBConstants.NOTIFICATION_EXPIRATION)
    @SerializedName("expiresAt")
    private Long expirationDate;

    @ColumnInfo(name = DBConstants.NOTIFICATION_IS_READ)
    @SerializedName("isReaded")
    private int isRead = 0;

    /**
     * Constructor used when a notification is get from the push notification
     *
     * @param title          title of the notification
     * @param text           principal message
     * @param expirationDate a long that indicates when the notification should be deleted from the device database
     * @param sentDate       the time when the notifications was created on the server.
     * @param isRead         a boolean to know if the notification has been read (1 = True, 0 = False)
     */
    public NotificationModel(String title, String text, @NotNull Long expirationDate, @NotNull Long sentDate, int isRead) {
        this.title = title;
        this.text = text;
        this.expirationDate = expirationDate;
        this.sentDate = sentDate;
        this.isRead = isRead;
    }

    /**
     * This constructor is called from the Notification Builder
     */
    @Ignore
    public NotificationModel() {

    }

    // Setter and getters

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

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    /**
     * Method to know if the notifications are the same
     * We compare the
     */

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
    public boolean equals(Object o) {
        Timber.d("Equals method called");
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationModel that = (NotificationModel) o;
        Timber.d("Comparing between this element: " + this.toString() + " and this " + that.toString());
        return sentDate.equals(that.sentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentDate);
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "sentDate=" + sentDate +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", expirationDate=" + expirationDate +
                ", isRead=" + isRead +
                ", readableSentDate='" + readableSentDate + '\'' +
                ", readAbleExpirationDate='" + readAbleExpirationDate + '\'' +
                '}';
    }
}