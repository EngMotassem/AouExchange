package apps.aouexchange.StudentActivity.models;

/**
 * Created by mac on 3/14/17.
 */

public class NotificationMessages {
    private String id;
    private String userId;
    private String userName;
    private String title;
    private String body;


    public NotificationMessages() {
    }

    public NotificationMessages(String id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public NotificationMessages(String id, String userName, String title, String body) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
