package aviv.myicebreaker.network;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import aviv.myicebreaker.Singleton;
import aviv.myicebreaker.facebook_check.GridItem;
import aviv.myicebreaker.module.CustomObjects.FacebookAlbumObject;
import aviv.myicebreaker.module.CustomObjects.NewUser;

/**
 * Created by Aviv on 20/07/2016.
 */
public class FacebookConnectivity {

    static final String TAG = "FacebookConnectivity";
    private static final String USER_FB_TOKEN = "userFirebaseToken";


    private Context context;
    private BaseListener baseListener;
    private LoginButton loginButton;
    private String nName, nId, nEmail, nGender, nPhoto, nBirthday;
    private String[] photosUrlArr = new String[3];
    private String FCMToken;
    private ArrayList<FacebookAlbumObject> finalFBalbumsObj,facebookAlbumObjects;
    private int jarraySize;


    public FacebookConnectivity(Activity activity) {
        this.context = activity;
        this.baseListener = (BaseListener) activity;
    }

    public void initFacebook(CallbackManager callbackManager) {

        loginButton = new LoginButton(context);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_photos", "email", "user_birthday"));
        Log.d("beforeAl", "wlala");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("newToken", AccessToken.getCurrentAccessToken().getToken());

                receiveFirstThreeFacebookPhotos();
                Log.d("accesdToken", loginResult.getAccessToken().getToken() + "");
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("GraphResponse", response.getRawResponse());
                        Log.d("GraphResponse!", response.toString());
                        Log.d("LoginResults", loginResult.getAccessToken().getToken() + "");


                        try {

//////////
                            Bundle params = new Bundle();
                            params.putString("fields", "id,name,birthday,email,gender,cover,picture.type(large)");
                            new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        @Override
                                        public void onCompleted(GraphResponse response) {
                                            if (response != null) {
                                                try {
                                                    JSONObject data = response.getJSONObject();
                                                    Log.d("newData", data + "");

                                                    nName = data.getString("name");
                                                    nId = data.getString("id");
                                                    nEmail = data.getString("email");
                                                    nGender = data.getString("gender");
                                                    nBirthday = data.getString("birthday");
                                                    nPhoto = data.getJSONObject("picture").getJSONObject("data").getString("url");


                                                    Log.d("localUser YEAH", "localUser(facebook ID)" + nId +
                                                            "\nfirstName " + nName.substring(0, nName.lastIndexOf(" ")) +
                                                            "\nlastName " + nName.substring(nName.lastIndexOf(" ") + 1, nName.length()) +
                                                            "\ndateOfBirth " + nBirthday +
                                                            "\ngender " + nGender +
                                                            "\nimageUrl " + nPhoto +
                                                            "\nemail " + nEmail);
                                                    onDoneDownloadUserDetails();

                                                } catch (Exception e) {
                                                    Log.e("bigProblam", e.toString());
                                                }
                                            }
                                        }
                                    }).executeAsync();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("GraphExeception", e.toString());
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,bio,photos{link}");
                parameters.putString("redirect", "false");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {


            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();

            }
        });
        FCMToken = loadFirebaseToken();
    }

    public void performLogin() {
        loginButton.performClick();
    }

    public ArrayList<FacebookAlbumObject> getAllFacebookAlbumsInfo() {

        final Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        FacebookAlbumObject facebookAlbumObject;
        facebookAlbumObjects = new ArrayList<>();

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/albums",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("FacebookAlbumRes", response.toString());

                        try {
                            JSONObject jsObj = new JSONObject(response.getRawResponse());

                            JSONArray jarray = jsObj.getJSONArray("data");
                            jarraySize = jarray.length();
                            for (int i = 0; i < jarray.length(); i++) {


                                JSONObject albums = jarray.getJSONObject(i);
                                final String albumId = albums.getString("id");
                                final String albumName = albums.getString("name");



                                new GraphRequest(
                                        AccessToken.getCurrentAccessToken(),
                                        "/" + albumId + "/picture",
                                        params,
                                        HttpMethod.GET,
                                        new GraphRequest.Callback() {
                                            public void onCompleted(GraphResponse response) {
                                                Log.d("photoCover", response + "");
                                                try {
                                                    JSONObject jsObjPhoto = new JSONObject(response.getRawResponse());
                                                    JSONObject jarrayPhoto = jsObjPhoto.getJSONObject("data");
                                                    String coverUrl = jarrayPhoto.getString("url");
                                                    Log.d("coverUrl", coverUrl);
                                                    facebookAlbumObjects.add(new FacebookAlbumObject(coverUrl, albumName, albumId));


                                                } catch (Exception e) {
                                                    Log.e("Problem photosUrlArr", " Connectivity e:" + e);
                                                }



                                            }
                                        }
                                ).executeAsync();
                                Log.d("2", "complete"+ i);
                            }




                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Problem photosUrlArr", " Connectivity e:" + e);

                        }

                        Log.d("1", "complete inside");
                    }

                }

        ).executeAsync();
        Log.d("1", "complete");
        return facebookAlbumObjects;
    }

    public void receiveFirstThreeFacebookPhotos() {
        final Bundle params = new Bundle();
        params.putBoolean("redirect", false);


        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/albums",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("FacebookAlbumRes", response.toString());

                        try {
                            JSONObject jsObj = new JSONObject(response.getRawResponse());

                            JSONArray jarray = jsObj.getJSONArray("data");
                            for (int i = 0; i < jarray.length(); i++) {

                                JSONObject albums = jarray.getJSONObject(i);
                                final String albumId = albums.getString("id");
                                if (albums.getString("name").equalsIgnoreCase("Profile Pictures")) {
                                    i = jarray.length();
                                    Log.d("albumIdF", albumId);

                                    new GraphRequest(
                                            AccessToken.getCurrentAccessToken(),
                                            "/" + albumId + "/photos",
                                            params,
                                            HttpMethod.GET,
                                            new GraphRequest.Callback() {
                                                public void onCompleted(GraphResponse response) {
                                                    Log.d("photoRes", response + "");
                                                    try {
                                                        JSONObject jsObjPhoto = new JSONObject(response.getRawResponse());
                                                        JSONArray jarrayPhoto = jsObjPhoto.getJSONArray("data");
                                                        String[] photoArr = new String[3];
                                                        Log.d("jarrayPhoto", jarrayPhoto + "");
                                                        for (int i = 0; i < jarrayPhoto.length() && i < 3; i++) {

                                                            JSONObject jPhoto = jarrayPhoto.getJSONObject(i);
                                                            final String photoId = jPhoto.getString("id");
                                                            photoArr[i] = photoId;
                                                            Log.d("photoArr", photoArr[i]);


                                                            final int finalI = i;
                                                            new GraphRequest(
                                                                    AccessToken.getCurrentAccessToken(),
                                                                    "/" + photoArr[i] + "/picture",
                                                                    params,
                                                                    HttpMethod.GET,
                                                                    new GraphRequest.Callback() {
                                                                        public void onCompleted(GraphResponse response) {
                                                                            try {
                                                                                JSONObject jPics = new JSONObject(response.getRawResponse());
                                                                                JSONObject jPicsData = jPics.getJSONObject("data");
                                                                                Log.d("photoCheck", jPics + "");
                                                                                if (jPicsData.has("url")) {

                                                                                    photosUrlArr[finalI] = jPicsData.getString("url");

                                                                                    Log.d("photourl", photosUrlArr[finalI]);
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                                Log.e("Problem photosUrlArr", " Connectivity e:" + e);

                                                                            }

                                                                        }
                                                                    }
                                                            ).executeAsync();

                                                        }

                                                    } catch (Exception e) {
                                                        Log.e("Problem photosUrlArr", " Connectivity e:" + e);
                                                    }
                                                    // Log.d("photosUrlArr THIS",photosUrlArr[1] );
                                                    //  ((SplashDoneDownload)baseListener).onDownloadProfilePicsDone(photosUrlArr);
                                                }
                                            }
                                    ).executeAsync();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Problem photosUrlArr", " Connectivity e:" + e);

                        }
                    }
                }
        ).executeAsync();

    }

    private void onDoneDownloadUserDetails() {
        if (nName != null) {
            Log.d("nName", nName); //TODO check why not recieve nName

            // NewUser localUser = new NewUser(nName, nId, "05/05/1995", nGender, photosUrlArr,"walla@balla.co.il",FCMToken);
            NewUser localUser = new NewUser(nName, nId, nBirthday, nGender, photosUrlArr, nEmail, FCMToken);
            Singleton.getInstance().setNewUser(localUser);
            Log.d(getClass().getSimpleName(), "User Ok " + localUser.getProfileImageUrl());
        } else {
            onDoneDownloadUserDetails();
            Log.e(getClass().getSimpleName(), " Problem With User: ");
        }
    }

    private String loadFirebaseToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("refreshed_fb_token", 0);
        String restoredUserFBToken = sharedPreferences.getString(USER_FB_TOKEN, "");
        Log.d("USER_FB_TOKEN", restoredUserFBToken);
        return restoredUserFBToken;
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(
                AccessToken oldAccessToken,
                AccessToken currentAccessToken) {

        }
    };

    public void facebookTry() {
        final Bundle params = new Bundle();
        params.putBoolean("redirect", false);

        Log.d("accsesc", AccessToken.USER_ID_KEY + "");
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/433629944724/photos",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        /**Logs response from facebook, which will be in JSON format**/

                        Log.i("ResponseT", response.toString());
                        try {
                            JSONArray jsonPhotos = response.getJSONObject().getJSONArray("data");
                            GridItem item;

                            /**Parse json array, search for "name" and "id"**/

                            for (int j = 0; j < jsonPhotos.length(); j++) {
                                item = new GridItem();
                                JSONObject jsonFBPhoto = jsonPhotos.getJSONObject(j);

                                /**If image has no description, set its title to "No Title"**/
                                item.setTitle("No Title");

                                /**find "id" which identifies the image**/
                                String id = jsonFBPhoto.getString("id");
                                URL image_url = new URL("https://graph.facebook.com/" + id + "/picture?type=normal");
                                String url = image_url.toString();
                                Log.d("TTT ", url);
                                /**prepare to send "title" and "image" to the adapter**/
                                item.setImage(url);

                                /**Image and Title are sent to the adapter**/
                                //   gallery_items.add(item);
                                //   gAdapter.setGridData(gallery_items);

                                new GraphRequest(
                                        AccessToken.getCurrentAccessToken(),
                                        "/" + id + "/picture",
                                        params, HttpMethod.GET,
                                        new GraphRequest.Callback() {
                                            public void onCompleted(GraphResponse response) {
                                                try {
                                                    JSONObject jPics = new JSONObject(response.getRawResponse());
                                                    JSONObject jPicsData = jPics.getJSONObject("data");
                                                    Log.d("photoCheck", jPics + "");
                                                    if (jPicsData.has("url")) {

                                                        Log.d("the url: ", jPicsData.getString("url"));

                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Log.e("Problem photosUrlArr", " Connectivity e:" + e);

                                                }

                                            }
                                        }
                                ).executeAsync();


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        request.executeAsync();

        /**Handle photo clicks in the gallery**/
      /*  gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                *//**gets the item (image and title) which was clicked**//*
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                *//**Passes the item to a new activity where it will be viewed at its original size**//*
                Intent intent = new Intent(MainActivity.this, GridItemActivity.class);
                intent.putExtra("Image Title", item.getTitle());
                intent.putExtra("Image", item.getImage());
                startActivity(intent);
            }
        });*/


    }



}



