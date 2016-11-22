package aviv.myicebreaker.view_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import aviv.myicebreaker.R;
import aviv.myicebreaker.module.CustomObjects.FacebookAlbumObject;

/**
 * Created by Aviad on 12/11/2016.
 */

public class FragmentFacebookGallery extends Fragment {
    private ArrayList<FacebookAlbumObject> facebookAlbumList;

    public void setFacebookAlbumList(ArrayList<FacebookAlbumObject> newFacebookAlbumList){
        facebookAlbumList = newFacebookAlbumList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_facebook_gallery, container, false);

        return view;
    }
}
