package aviv.myicebreaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import aviv.myicebreaker.R;
import aviv.myicebreaker.Singleton;
import aviv.myicebreaker.module.Adapters.GridViewAdapter;
import aviv.myicebreaker.module.CustomObjects.FacebookAlbumObject;
import aviv.myicebreaker.module.Listeners.ListenerReachedBottomGridView;

/**
 * Created by Aviad on 12/11/2016.
 */

public class ActivityFacebookGallery extends AppCompatActivity implements ListenerReachedBottomGridView {
    private ArrayList<FacebookAlbumObject> facebookAlbumObjects,facebookPictureObjects;
    private GridView albumsGridView;
    private GridViewAdapter gridViewAdapter;
    private boolean isAlbumGalleryCurrentScreen = true;
    private GraphResponse lastGraphResponse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.fragment_facebook_gallery);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        final int extrasFromIntentImage = bd.getInt("imageUrl");
        facebookAlbumObjects=new ArrayList<>();
        facebookPictureObjects = new ArrayList<>();

        albumsGridView= (GridView)findViewById(R.id.gridViewFacebookAlbums);
        gridViewAdapter = new GridViewAdapter(this,R.layout.grid_facebook_album,facebookPictureObjects,this);


       getAllFacebookAlbumsInfo();


        albumsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isAlbumGalleryCurrentScreen) {
                    getPhotosFromSelectedAlbum(facebookAlbumObjects.get(i).getAlbumId());
                }else{
                    String choosenImageUrl = facebookPictureObjects.get(i).getAlbumUrlImgCover();
                    Log.d("thisOne", choosenImageUrl);
                    Singleton.getInstance().getNewUser().setUploadedImageUrl(choosenImageUrl,extrasFromIntentImage);

                    returnToMainActivity(choosenImageUrl);

                }
            }
        });




    }

    private void returnToMainActivity(String choosenImageUrl) {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.putExtra("name", "ty");
        mainIntent.putExtra("imageUrl",choosenImageUrl);

      //   finish();
       startActivity(mainIntent);
    }

    private void getPhotosFromSelectedAlbum(String albumId) {
        gridViewAdapter = new GridViewAdapter(this,R.layout.grid_facebook_album,facebookPictureObjects,this);
        isAlbumGalleryCurrentScreen = false;
        gridViewAdapter.setIsAlbumsScreen(false);
        final Bundle params = new Bundle();
        params.putBoolean("redirect", false);

        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+albumId+"/photos",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse lastResponse) {
                        Log.i("ResponseT", lastResponse.toString());
                        getPhotosFromResponse(lastResponse);
                    }
                });
        request.executeAsync();
        albumsGridView.setAdapter(gridViewAdapter);
    }

    private void getPhotosFromResponse(GraphResponse lastResponse) {
        lastGraphResponse = lastResponse;
        final Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        try {


            JSONArray jsonPhotos = lastResponse.getJSONObject().getJSONArray("data");


            for (int j = 0; j < jsonPhotos.length(); j++) {

                JSONObject jsonFBPhoto = jsonPhotos.getJSONObject(j);
                String id = jsonFBPhoto.getString("id");
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + id + "/picture",
                        params, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try {
                                    JSONObject jPics = new JSONObject(response.getRawResponse());
                                    JSONObject jPicsData = jPics.getJSONObject("data");
                                    String photoUrl = jPicsData.getString("url");
                                    facebookPictureObjects.add(new FacebookAlbumObject(photoUrl,"a","2143"));
                                    gridViewAdapter.setGridData(facebookPictureObjects);
                                    Log.d("photoUrl: ", photoUrl);

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
       // nextPagingGraph(lastResponse);

    }

    public void getAllFacebookAlbumsInfo() {
        gridViewAdapter = new GridViewAdapter(this,R.layout.grid_facebook_album,facebookAlbumObjects,this);
        gridViewAdapter.setIsAlbumsScreen(true);
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
                                                    gridViewAdapter.setGridData(facebookAlbumObjects);
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
        albumsGridView.setAdapter(gridViewAdapter);

    }

    private void nextPagingGraph(GraphResponse lastResponse){
        Log.d("whatPro",lastResponse.toString());
        GraphRequest nextResultsRequests = lastResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        if (nextResultsRequests != null) {
            nextResultsRequests.setCallback(new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                   getPhotosFromResponse(response);
                    Log.d("wolverine", response+""); // TODO להכניס את זה יפה יפה
                }
            });
            nextResultsRequests.executeAsync();
        }
    }

    @Override
    public void onBottomReached() {
        Log.d("thisIs","workiing");
        nextPagingGraph(lastGraphResponse);
    }
}
