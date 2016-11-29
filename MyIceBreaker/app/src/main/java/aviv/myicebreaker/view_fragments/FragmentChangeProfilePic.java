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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import aviv.myicebreaker.R;
import aviv.myicebreaker.Singleton;
import aviv.myicebreaker.module.CustomObjects.CustomImageView;
import aviv.myicebreaker.network.OpenGalleryListener;

/**
 * Created by Aviad on 03/10/2016.
 */
public class FragmentChangeProfilePic extends Fragment implements View.OnClickListener{
private CustomImageView profilePicTop,profilePicRight,profilePicLeft;
    private ImageView chosenImage;
    private Button btnTopImgReplace,btnLeftImgReplace,btnRightImgReplace;
    private OpenGalleryListener openGalleryListener;
    private String urlNewImage;
    private File fileNewImage;
    private ImageButton btnBackFromChangeProfilePic;

    public void setImageOrder(int imageOrder) {
        switch (imageOrder){
            case 1:
                chosenImage=profilePicTop;
                break;
            case 2:

                chosenImage = profilePicLeft;
                break;
            case 3:
                chosenImage = profilePicRight;
                break;
        }

    }

    public void setFileNewImage(File fileNewImage) {
        this.fileNewImage = fileNewImage;
    }

    public void setUrlNewImage(String urlNewImage) {
        this.urlNewImage = urlNewImage;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_change_profile_pic, container, false);


        btnTopImgReplace = (Button)view.findViewById(R.id.btnTopImgReplace);
        btnLeftImgReplace = (Button)view.findViewById(R.id.btnLeftImageReplace);
        btnRightImgReplace = (Button)view.findViewById(R.id.btnRightImgReplace);
        profilePicTop = (CustomImageView)view.findViewById(R.id.profilePicTop);
        profilePicLeft = (CustomImageView)view.findViewById(R.id.profilePicLeft);
        profilePicRight = (CustomImageView)view.findViewById(R.id.profilePicRight);
        btnBackFromChangeProfilePic = (ImageButton)view.findViewById(R.id.btnBackFromChangeProfilePic);
Log.d("created","wall");


       // Glide.with(getContext()).load(Singleton.getInstance().getNewUser().getImageUrl()[0]).centerCrop().bitmapTransform(new RoundedCornersTransformation(getContext(),40,40)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(profilePicTop);

       /* Glide.with(getContext()).load(Singleton.getInstance().getNewUser().getImageUrl()[0]).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(profilePicTop) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                super.onResourceReady(bitmap, anim);
                Glide.with(getContext()).load(Singleton.getInstance().getNewUser().getImageUrl()[0]).centerCrop().bitmapTransform(new RoundedCornersTransformation(getContext(),40,40)).into(profilePicTop);
            }
        });*/

       // Glide.with(getContext()).load(Singleton.getInstance().getNewUser().getImageUrl()[1]).centerCrop().transform(new TopCropTransformation(getContext())).into(profilePicLeft);
        Glide.with(getContext()).load(Singleton.getInstance().getNewUser().getImageUrl()[0]).centerCrop().into(profilePicTop);
        Glide.with(getContext()).load(Singleton.getInstance().getNewUser().getImageUrl()[1]).centerCrop().into(profilePicLeft);
        Glide.with(getContext()).load(Singleton.getInstance().getNewUser().getImageUrl()[2]).centerCrop().into(profilePicRight);



        btnTopImgReplace.setOnClickListener(this);
        btnLeftImgReplace.setOnClickListener(this);
        btnRightImgReplace.setOnClickListener(this);

        btnBackFromChangeProfilePic.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnTopImgReplace:
            case R.id.btnLeftImageReplace:
            case R.id.btnRightImgReplace:
               int imageOrder = Integer.parseInt(view.getContentDescription().toString()); // Get the Image order
              //  setImageOrder(imageOrder);
                openGalleryListener.onOpenGalleryClicked(imageOrder);
                Log.d("gallery",imageOrder+"");
                break;
            case R.id.btnMakeLeftImageFavorite:
            case R.id.btnMakeRightImageFavorite:
                break;
            case R.id.btnBackFromChangeProfilePic:
                openGalleryListener.onBackPressedFromChangeProfilePic();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  if(urlNewImage!=null) {


            Glide.with(this).load(urlNewImage).bitmapTransform(new RoundedCornersTransformation(getContext(), 40, 40)).into(chosenImage);
            Log.d("justResumed", urlNewImage);
        }
      /*  if(fileNewImage!=null){
            Glide.with(this).load(fileNewImage).bitmapTransform(new RoundedCornersTransformation(getContext(), 40, 40)).into(chosenImage);
            Log.d("justResumed", fileNewImage.toString());
        }*/
    }

}
