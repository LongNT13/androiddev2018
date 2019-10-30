package group3facebook.usth.edu.vn.group3facebook;

import android.graphics.Bitmap;

/**
 * Created by dangvinhbao on 25/11/2017.
 */

public class PostItem {

    private String creatorName, createDate, createTime, message;
    private Bitmap picture;

    public PostItem(String creatorName, String createDateTime, String message, Bitmap picture) {
        // Required empty public constructor
        this.creatorName = creatorName;
        this.createDate = createDateTime.substring(0, 10);
        this.createTime = createDateTime.substring(11, 19);
        this.message = message;
        this.picture = picture;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

}
