package aviv.myicebreaker.view_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import aviv.myicebreaker.R;
import aviv.myicebreaker.Singleton;
import aviv.myicebreaker.network.OpenGalleryListener;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Aviad on 03/10/2016.
 */
public class FragmentChangeProfilePic extends Fragment{
private ImageView profilePicTop,profilePicLeft,profilePicRight;
    private Button btnImgReplace;
    private OpenGalleryListener openGalleryListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_change_profile_pic, container, false);

        btnImgReplace = (Button)view.findViewById(R.id.btnImgReplace);
        profilePicTop = (ImageView)view.findViewById(R.id.profilePicTop);
        profilePicLeft = (ImageView)view.findViewById(R.id.profilePicLeft);
        profilePicRight = (ImageView)view.findViewById(R.id.profilePicRight);




        Glide.with(this).load(Singleton.getInstance().getNewUser().getImageUrl()[0]).bitmapTransform(new RoundedCornersTransformation(getContext(),20,20),new BlurTransformation(getContext(),25)).into(profilePicTop);
        Glide.with(this).load(Singleton.getInstance().getNewUser().getImageUrl()[1]).bitmapTransform(new RoundedCornersTransformation(getContext(),50,50),new BlurTransformation(getContext(),50)).into(profilePicLeft);
        Glide.with(this).load(Singleton.getInstance().getNewUser().getImageUrl()[2]).bitmapTransform(new RoundedCornersTransformation(getContext(),30,30),new BlurTransformation(getContext(),75)).into(profilePicRight);

        btnImgReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryListener.onOpenGalleryClicked();

            }
        });

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OpenGalleryListener) {
            openGalleryListener = (OpenGalleryListener) context;

            Log.d("hello", "drawerlistener");
        } else {
            throw new ClassCastException(context.toString() + " must implement OnRageComicSelected.");
        }

    }
}
