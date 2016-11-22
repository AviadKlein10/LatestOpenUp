package aviv.myicebreaker.module.CustomObjects;

/**
 * Created by Aviad on 03/11/2016.
 */

public class NotificationObject {

    private String sender, msgContent;

    public NotificationObject(String sender, String msgContent) {
        this.sender = sender;
        this.msgContent = msgContent;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
