package aviv.myicebreaker.module.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import aviv.myicebreaker.R;
import aviv.myicebreaker.module.CustomObjects.FacebookAlbumObject;
import aviv.myicebreaker.module.Listeners.ListenerReachedBottomGridView;

/**
 * Created by Aviad on 22/11/2016.
 */
public class GridViewAdapter extends ArrayAdapter<FacebookAlbumObject> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<FacebookAlbumObject> mGridData = new ArrayList<FacebookAlbumObject>();
    private ListenerReachedBottomGridView listenerReachedBottomGridView;
    private boolean isAlbumsScreen;

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<FacebookAlbumObject> mGridData,ListenerReachedBottomGridView listenerReachedBottomGridView) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
        this.listenerReachedBottomGridView = listenerReachedBottomGridView;
    }

    public void setGridData(ArrayList<FacebookAlbumObject> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.albumTitle);
            holder.imageView = (ImageView) row.findViewById(R.id.albumImageCover);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(position==mGridData.size()-1&& !isAlbumsScreen){
                    Log.d("end",mGridData.size()+"");
                    listenerReachedBottomGridView.onBottomReached();
                }
            }
        },1500);


        FacebookAlbumObject item = mGridData.get(position);
        holder.titleTextView.setText(item.getAlbumName());

        Glide.with(mContext).load(item.getAlbumUrlImgCover()).centerCrop().override(400,400).placeholder(R.mipmap.ic_launcher).into(holder.imageView);
        return row;
    }

    public void setIsAlbumsScreen(boolean isAlbumsScreen) {
        this.isAlbumsScreen = isAlbumsScreen;
    }

    private class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}