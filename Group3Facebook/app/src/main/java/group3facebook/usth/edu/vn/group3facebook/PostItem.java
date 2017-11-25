package group3facebook.usth.edu.vn.group3facebook;

/**
 * Created by dangvinhbao on 25/11/2017.
 */

public class PostItem {

    private String creatorName, createDateTime, message, pictureURL;

    public PostItem(String creatorName, String createDateTime, String message, String pictureURL) {
        // Required empty public constructor
        this.creatorName = creatorName;
        this.createDateTime = createDateTime;
        this.message = message;
        this.pictureURL = pictureURL;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}
