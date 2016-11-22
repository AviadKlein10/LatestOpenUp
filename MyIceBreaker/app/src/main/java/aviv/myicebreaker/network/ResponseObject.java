package aviv.myicebreaker.network;

/**
 * Created by Aviad on 11/10/2016.
 */
public class ResponseObject {
    public static final int QUESTION_FROM_SERVER = 4;
    public static final int QUESTION_LIKE_DISLIKE = 5;
    public static final int CURRENT_REVEAL_STAGE = 6;

    private int typeCode;
    private String content;


    public ResponseObject(int typeCode, String content) {
        this.typeCode = typeCode;
        this.content = content;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public String getContent() {
        return content;
    }
}
