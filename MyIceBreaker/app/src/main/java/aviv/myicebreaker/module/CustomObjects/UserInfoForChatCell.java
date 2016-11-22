package aviv.myicebreaker.module.CustomObjects;

/**
 * Created by Aviad on 09/09/2016.
 */
public class UserInfoForChatCell {
    private String name,age,lastMsgContent, urlUserProfileImg;
    private int notificationCounter =0, revealImgStage =0;
    private boolean isLocalUserSentLast;

    public UserInfoForChatCell(String name, String age, int notificationCounter, int revealImgStage, String lastMsgContent, String urlUserProfileImg, boolean isLocalUserSentLast) {
        this.name = name;
        this.age = age;
        this.notificationCounter = notificationCounter;
        this.revealImgStage = revealImgStage;
        this.lastMsgContent = lastMsgContent;
        this.urlUserProfileImg = urlUserProfileImg;
        this.isLocalUserSentLast = isLocalUserSentLast;
    }

    public int getRevealImgStage() {
        return revealImgStage;
    }

    public void setRevealImgStage(int revealImgStage) {
        this.revealImgStage = revealImgStage;
    }

    public String getLastMsgContent() {
        if(lastMsgContent.length()>35) {
            return lastMsgContent.substring(0, 35) + "...";
        }
            return lastMsgContent;
    }

    public void setLastMsgContent(String lastMsgContent) {
        this.lastMsgContent = lastMsgContent;
    }

    public String getUrlUserProfileImg() {
        return urlUserProfileImg;
    }


    public String getUserNameAndAge(){
        return name +", "+ age;
    }

    public void setUrlUserProfileImg(String urlUserProfileImg) {
        this.urlUserProfileImg = urlUserProfileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getNotificationCounter() {
        return notificationCounter;
    }

    public void setNotificationCounter(int notificationCounter) {
        this.notificationCounter = notificationCounter;
    }

    public boolean isLocalUserSentLast() {
        return isLocalUserSentLast;
    }

    public void setIsLocalUserSentLast(boolean isLocalUserSentLast) {
        this.isLocalUserSentLast = isLocalUserSentLast;
    }
}


