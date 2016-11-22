package aviv.myicebreaker.module.CustomObjects;

/**
 * Created by Aviad on 11/08/2016.
 */
public class NewUser {
    private String name;
    private String id;
    private String birthday;
    private String gender;
    private String[] imageUrl;
    private String email;
    private String FCMToken;

    public NewUser(String name, String id, String birthday, String gender, String[] imageUrl, String email, String FCMToken) {
        this.name = name;
        this.id = id;
        this.birthday = birthday;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.email = email;
        this.FCMToken = FCMToken;
    }

    public NewUser(NewUser newUser) {
        this.name = newUser.getName();
        this.id = newUser.getId();
        this.birthday = newUser.getBirthday();
        this.gender = newUser.getGender();
        this.imageUrl = newUser.getImageUrl();
        this.email = newUser.getEmail();
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return name.substring(0, name.lastIndexOf(" "));
    }

    public String getLastName() {
        return name.substring(name.lastIndexOf(" ") + 1, name.length());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String[] getImageUrl() {
        return imageUrl;
    }

    public String getProfileImageUrl() {
        return imageUrl[0];
    }

    public void setImageUrl(String[] imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }
}
