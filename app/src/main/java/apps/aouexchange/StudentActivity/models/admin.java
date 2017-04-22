package apps.aouexchange.StudentActivity.models;

/**
 * Created by mac on 3/16/17.
 */

public class admin {
    private String userName;
    private String Password;
    private String display;

    public admin() {
    }

    public admin(String userName, String password, String display) {
        this.userName = userName;
        this.Password = password;
        this.display = display;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
