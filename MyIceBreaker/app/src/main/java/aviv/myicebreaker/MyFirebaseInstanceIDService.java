package aviv.myicebreaker;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import aviv.myicebreaker.module.JsonParser;

/**
 * Created by Belal on 5/27/2016.
 */


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static final String USER_FB_TOKEN = "userFirebaseToken";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat 
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        SharedPreferences sharedPref = getSharedPreferences("refreshed_fb_token",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(USER_FB_TOKEN, token);

        editor.apply();
    }
}