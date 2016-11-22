package aviv.myicebreaker.view_fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import aviv.myicebreaker.R;
import aviv.myicebreaker.Singleton;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Aviad on 27/10/2016.
 */

public class FragmentGalleries extends Fragment implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 188;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ListView galleryListView;
    private ImageView imgFacebookGalleryCover,imgGalleryPhoneCover,imgSelfCamera;
    private GalleryListener galleryListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        imgFacebookGalleryCover = (ImageView) view.findViewById(R.id.imgFacebookGalleryCover);
        imgGalleryPhoneCover = (ImageView) view.findViewById(R.id.imgGalleryPhoneCover);
        imgSelfCamera = (ImageView) view.findViewById(R.id.imgSelfCamera);


        imgGalleryPhoneCover.setOnClickListener(this);
        imgFacebookGalleryCover.setOnClickListener(this);
        imgSelfCamera.setOnClickListener(this);

        return view;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("requestCode", requestCode + " ");
        Log.d("resultCode", resultCode + " ");


        if (requestCode == 1 && resultCode == RESULT_OK) {
            Log.d("dataU", data.getData() + " ");
            //   Bitmap photo = (Bitmap) data.getExtras().get("data"); // If you want to load image as bitmap
            Uri uri = data.getData();
            if (uri != null) {
                String strPath;
                if (data.getAction() != null)//captured image
                {
                    strPath = getRealPathFromURI(uri);
                } else {
                    strPath = getRealPathFromURI2(uri);
                }
                Log.d("strPath ", strPath + " ");

                File file = new File(strPath);
                file = resizeFile(file, strPath);

                galleryListener.uploadChosenImage(Singleton.getInstance().getNewUser().getId(), 2, file);
                Glide.with(this).load(file).into(imgSelfCamera);

            }

        }
    }

    private Bitmap fixImageOrientation(Bitmap srcBitmap, String strPath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(strPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        int rotate = 0;
        Log.d("exifOrientation", exifOrientation + "");
        switch (exifOrientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
        }

        if (rotate != 0) {
            int w = srcBitmap.getWidth();
            int h = srcBitmap.getHeight();

// Setting pre rotate
            Matrix mtx = new Matrix();
            mtx.preRotate(rotate);

            // Rotating Bitmap & convert to ARGB_8888, required by tess
            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, w, h, mtx, false);
            srcBitmap = srcBitmap.copy(Bitmap.Config.ARGB_8888, true);
        }
        return srcBitmap;
    }

    public String getRealPathFromURI(Uri uri) {


        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String realUri = cursor.getString(idx);
      /*  String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String realUri = cursor.getString(columnIndex); // returns null*/
        Log.d("loli2", realUri + " maybenull");

        cursor.close();
        if (realUri != null) {
            return realUri;
        }
        return null;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) throws IOException {
        //Receive uri from bitmap
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] wow = bytes.toByteArray();

        Bitmap bitmap = BitmapFactory.decodeByteArray(wow, 0, wow.length);
        //  Glide.with(this).load(wow).centerCrop().into(imgTest);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GalleryListener) {
            galleryListener = (GalleryListener) context;

            Log.d("hello", "drawerlistener");
        } else {
            throw new ClassCastException(context.toString() + " must implement OnRageComicSelected.");
        }
    }

    private File resizeFile(File srcFile, String strFilePath) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inJustDecodeBounds = true;

            bitmapOptions.inSampleSize = 6;

            // This will let the image to resize
            //  bitmapOptions.inScaled= false;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(srcFile);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
            inputStream.close();

            // The new size we want to scale to
            // 75 is good quality
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (bitmapOptions.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    bitmapOptions.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
                Log.d("scaleX: ", scale + "");
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(srcFile);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);


            selectedBitmap = fixImageOrientation(selectedBitmap, strFilePath);


            inputStream.close();


            srcFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(srcFile);
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);//TODO check quality


            return srcFile;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgGalleryPhoneCover:
                initPhoneGallery();
                break;
            case R.id.imgFacebookGalleryCover:
                initPhoneGallery();
                break;
            case R.id.imgSelfCamera:
                takePictureIntent();
                break;
        }
    }

    private void initPhoneGallery() {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getRealPathFromURI2(Uri contentURI) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(contentURI);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
}
