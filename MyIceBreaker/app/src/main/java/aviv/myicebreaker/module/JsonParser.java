package aviv.myicebreaker.module;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import aviv.myicebreaker.module.CustomObjects.BubbleChatObject;
import aviv.myicebreaker.module.CustomObjects.NewUser;

/**
 * Created by Aviv on 03/07/2016.
 */
public class JsonParser {

    private static final String USER_NAME = "name";
    private static final String USER_ID = "localUser (facebook ID)";
    private static final String GENDER = "gender";
    private static final String AGE = "dateOfBirth";
    private static final String EMAIL = "email";
    private static final String IMAGE_URL = "imageUrl";
    private static final String BUBBLE_TYPE = "bubbleType";
    private static final String MESSAGE_CONTENT = "messageContent";
    private static final String MESSAGE_TIME = "dateTime";
    private static final String FCMTOKEN = "deviceId";


    public static NewUser parseUserToObject(String result){
        NewUser  user = null;

        try {
            JSONObject jsonUserObject = new JSONObject(result);
            String userName = jsonUserObject.getString(USER_NAME);
            String userID = jsonUserObject.getString(USER_ID);
            String age = jsonUserObject.getString(AGE);
            String email = jsonUserObject.getString(EMAIL);
            String FCMToken = jsonUserObject.getString(FCMTOKEN);
            JSONArray imageUrl = jsonUserObject.getJSONArray(IMAGE_URL);

            Log.d("imageArr2",imageUrl.toString());

            String[] imageUrlArr = convertImageUrlToArr(imageUrl);
            String gender= jsonUserObject.getString(GENDER);

            user = new NewUser(userName,userID,age,gender,imageUrlArr,email,FCMToken);

        } catch (JSONException e) {
            Log.e(e.toString(), " 404");
        }
        return user;
    }

    private static String[] convertImageUrlToArr(JSONArray imageUrl) throws JSONException {
        String[]tempImageUrlArr = new String[3];
        for (int i = 0; i < 3; i++) {
            Log.d("imageUrl",imageUrl.get(2).toString());
            tempImageUrlArr[i] = imageUrl.get(i).toString();
        }
        Log.d("againImg", tempImageUrlArr[0]);
        return tempImageUrlArr;
    }

    public static String parseUserToJson(NewUser localUser){

        if(localUser==null){
            return null;
        }

        JSONObject jsonUserObject = new JSONObject();
        try {
            jsonUserObject.put("localUser (facebook ID)", localUser.getId());
            jsonUserObject.put("name", localUser.getName());
                     // TODO might problam
            jsonUserObject.put("dateOfBirth", localUser.getBirthday());
            jsonUserObject.put("gender", localUser.getGender());
            JSONArray imageArr = new JSONArray();

            imageArr.put(localUser.getImageUrl()[0]);
            imageArr.put(localUser.getImageUrl()[1]);
            imageArr.put(localUser.getImageUrl()[2]);


            jsonUserObject.put("imageUrl", imageArr); /// TODO Might problam
            jsonUserObject.put("email", localUser.getEmail());
            jsonUserObject.put("deviceId",localUser.getFCMToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("local user parser", jsonUserObject.toString());
        return jsonUserObject.toString();
    }


    public static String parseConversationToJson(ArrayList<BubbleChatObject> arrConversation){

        if(arrConversation==null){
            return null;
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < arrConversation.size(); i++) {

            JSONObject jsonUserObject = new JSONObject();
            try {
                jsonUserObject.put("bubbleType", arrConversation.get(i).getBubbleType().toString());
                jsonUserObject.put("messageContent", arrConversation.get(i).getMessageContent());
                jsonUserObject.put("dateTime", arrConversation.get(i).getDateTime());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonUserObject);
        }
        Log.d("local conversation", jsonArray.toString());
        return jsonArray.toString();
    }

    public static ArrayList<BubbleChatObject> parseConversationToObject(String result){
        ArrayList<BubbleChatObject> arrConversation = new ArrayList<>();


        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonUserObject = jsonArray.getJSONObject(i);

                BubbleType bubbleType = BubbleType.valueOf(jsonUserObject.getString(BUBBLE_TYPE));
                String messageContent = jsonUserObject.getString(MESSAGE_CONTENT);
                String dateTime = jsonUserObject.getString(MESSAGE_TIME);


                arrConversation.add(new BubbleChatObject(bubbleType,messageContent,dateTime,0));
            }
        } catch (JSONException e) {
            Log.e(e.toString(), " 404");
        }


        return arrConversation;
    }


}
