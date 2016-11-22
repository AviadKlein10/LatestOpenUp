package aviv.myicebreaker.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.BuildConfig;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Locale;

import aviv.myicebreaker.R;
import aviv.myicebreaker.Singleton;
import aviv.myicebreaker.module.CustomObjects.FacebookAlbumObject;
import aviv.myicebreaker.module.CustomObjects.NewUser;
import aviv.myicebreaker.module.JsonParser;
import aviv.myicebreaker.network.Connectivity;
import aviv.myicebreaker.network.ConnectivityError;
import aviv.myicebreaker.network.FacebookConnectivity;
import aviv.myicebreaker.network.ResponseObject;
import aviv.myicebreaker.network.SplashDoneDownload;

public class SplashActivity extends AppCompatActivity implements SplashDoneDownload {

    public static final String TAG = Connectivity.class.getSimpleName();

    private static final String USER_FB_TOKEN = "userFirebaseToken";

    private static final String USER_KEY = "userKey";
    private Connectivity connectivity;
    private NewUser globalLocalUser;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;
    private Button buttonUserDetails;
    private LinearLayout layoutSignIn;
    private FacebookConnectivity facebookConnectivity;
    private Button btnDeleteUser;
    private Button btnCheck;
    private ArrayList<FacebookAlbumObject> newFBarray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNoTitleScreen();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        setContentView(R.layout.splash_screen);

        facebookConnectivity = new FacebookConnectivity(this);
        //  LastLocation appLocationManager = new LastLocation(this);
        callbackManager = CallbackManager.Factory.create();
        facebookConnectivity.initFacebook(callbackManager);


        init();
        loadUserSharedPreferences();
        //loadFirebaseToken();
        buttonUserDetails = (Button) findViewById(R.id.buttonUserDetails);
        btnCheck = (Button) findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFBarray =facebookConnectivity.getAllFacebookAlbumsInfo();
dataReceived();            //  facebookConnectivity.getAllFacebookAlbumsInfo();
              /*  new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("please complete ",newFBarray.get(0).getAlbumName()+" not null");
                    }
                },2000);*/
            }
        });
        buttonUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookConnectivity.facebookTry();
               if(globalLocalUser !=null) {
                   Log.d(getClass().getSimpleName(), "User Ok " + globalLocalUser.getProfileImageUrl());
                   Log.d("final check", Singleton.getInstance().getNewUser().getImageUrl()[0]);
                  connectivity.testUserDetails();
                   connectivity.receiveChatMsgs();
                  // connectivity.receiveUserChats(Singleton.getInstance().getNewUser().getId());
                   connectivity.receiveUserChats("57b726b26470e2cb10896ac1");
                   //    logoutAndDeleteUser();

                   runMainActivity();
               }

            }
        });
        btnDeleteUser = (Button)findViewById(R.id.btnDeleteUser);
      btnDeleteUser.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              logoutAndDeleteUser();
              layoutSignIn.setVisibility(View.VISIBLE);
          }
      });

    }

    private void initNoTitleScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.hide();
        }
    }

    private void logoutAndDeleteUser() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logOut();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        sharedPreferences.edit().remove(USER_KEY).apply();

    }

    private void init() {


        AssetManager am = getApplicationContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "OpenSansHebrew-Regular.ttf"));

        TextView txtSignWithFacebook = (TextView) findViewById(R.id.txtSignWithFacebook);
        if (txtSignWithFacebook != null) {
            txtSignWithFacebook.setTypeface(typeface);
            Log.e(TAG, "no problam with typeface");
        } else {
            Log.e(TAG, "problam with typeface");
        }


        layoutSignIn = (LinearLayout) findViewById(R.id.layoutSignIn);
        assert layoutSignIn != null;
        //  layoutSignIn.setVisibility(View.GONE);
        layoutSignIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("actionDown", event.toString());
                    layoutSignIn.setBackgroundColor(getColor(getApplicationContext(), (R.color.facebookBluePressed)));


                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    layoutSignIn.setBackgroundColor(getColor(getApplicationContext(), (R.color.facebookBlue)));
                    Log.d("actionUp", event.toString());

                }
                return false;
            }
        });
        layoutSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               facebookConnectivity.performLogin();
            }
        });


        connectivity = new Connectivity(this);
        Log.d("before load", " well");


    }


    private void loadUserSharedPreferences() {
        Log.d("inside load", " well");

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String restoredUser = sharedPreferences.getString(USER_KEY, "");

        if (restoredUser.length() > 0) {

            Log.d("restoredUser", restoredUser + " z");
            globalLocalUser = JsonParser.parseUserToObject(restoredUser);


            Log.d("run run", globalLocalUser.getName());

            layoutSignIn.setVisibility(View.INVISIBLE);

            Singleton.getInstance().setNewUser(globalLocalUser);
            globalLocalUser = new NewUser(Singleton.getInstance().getNewUser());
            Log.d("glocl ," , globalLocalUser.getFirstName());

        } else {




            Log.d("no user", " login00");
        }

    }

    @Override
    public void onDownloadProfilePicsDone(String[] profilePicsArr) {
           /* Log.d("profilePicsArr", profilePicsArr[1]);
            if (profilePicsArr != null) {
                //Log.d("nName", nName); //TODO check why not recieve nName
                NewUser glolocalUser = new NewUser(nName, nId, nBirthday, nGender, profilePicsArr, nEmail);
                Singleton.getInstance().setNewUser(glolocalUser);
                Log.d(getClass().getSimpleName(), "User Ok " + glolocalUser.getProfileImageUrl());
            //    saveUserSharedPreferences(glolocalUser);
            } else {
                Log.e(getClass().getSimpleName(), " Problem With User: ");
            }*/
    }

    private void saveUserSharedPreferences(NewUser localUser) {
        if(localUser!=null) {
            SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            String userStr = JsonParser.parseUserToJson(localUser);
            editor.putString(USER_KEY, userStr);
            Log.d("local user ", userStr);
            editor.apply();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult ", resultCode + "");
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @TargetApi(Build.VERSION_CODES.M)
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 22) {
            Log.d("sdk", version + "");
            return ContextCompat.getColor(context, id);
        } else {
            Log.d("sdk", version + "");
            return context.getResources().getColor(id, null);
        }
    }

    @Override
    protected void onStop() {
        saveUserSharedPreferences(Singleton.getInstance().getNewUser());
        super.onStop();
    }

    private void runMainActivity() {


                    Log.d("run","splash");
                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.putExtra("Hello", "fck");
        startActivity(mainIntent);


    }

    private boolean isAppReady(){
/*        if(allReady != null  && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;*/
        Log.d("isAppReady","yes");
        return true;
    }
    private String loadFirebaseToken(){
       SharedPreferences sharedPreferences = getSharedPreferences("refreshed_fb_token",MODE_PRIVATE);
       String restoredUserFBToken = sharedPreferences.getString(USER_FB_TOKEN, "");
       Log.d("USER_FB_TOKEN",restoredUserFBToken);
        return restoredUserFBToken;
    }

    @Override
    public void receiveServerResponse(ConnectivityError error, ResponseObject response) {
        //TODO With This
    }

    private void dataReceived (){

        Log.d("size dize", newFBarray+"");
        if(newFBarray.size()==0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dataReceived();
                }
            },200);
        }else{
            Log.d("final complete " , newFBarray.get(4).getAlbumName());
        }
    }
}
