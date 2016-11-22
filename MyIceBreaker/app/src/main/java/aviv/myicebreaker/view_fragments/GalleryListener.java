package aviv.myicebreaker.view_fragments;

import java.io.File;

import aviv.myicebreaker.network.BaseListener;

/**
 * Created by Aviad on 08/11/2016.
 */
public interface GalleryListener extends BaseListener{
    void uploadChosenImage(String userId, int imageIndexInArray, File imageFile);
    void initFacebookGalleryFragment();
}
