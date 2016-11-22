package aviv.myicebreaker.module.CustomObjects;

import aviv.myicebreaker.module.BubbleType;

/**
 * Created by Aviad on 20/09/2016.
 */
public class BubbleChatObject {
    private BubbleType bubbleType;
    private String messageContent;
    private String dateTime;
    private int counterFromSameSide;



    public BubbleChatObject(BubbleType bubbleType, String messageContent, String dateTime,int counterFromSameSide) {
        this.bubbleType = bubbleType;
        this.messageContent = messageContent;
        this.dateTime = dateTime;
        this.counterFromSameSide = counterFromSameSide;
    }

    public BubbleType getBubbleType() {
        return bubbleType;
    }

    public void setBubbleType(BubbleType bubbleType) {
        this.bubbleType = bubbleType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getCounterFromSameSide() {
        return counterFromSameSide;
    }

    public void setCounterFromSameSide(int counterFromSameSide) {
        this.counterFromSameSide = counterFromSameSide;
    }


}
