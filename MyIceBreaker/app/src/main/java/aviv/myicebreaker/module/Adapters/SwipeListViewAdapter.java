package aviv.myicebreaker.module.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import aviv.myicebreaker.R;
import aviv.myicebreaker.module.CustomObjects.UserInfoForChatCell;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Aviad on 09/09/2016.
 */
public class SwipeListViewAdapter extends BaseSwipeListAdapter {


    private Context context;
    private ArrayList<UserInfoForChatCell> chatCellsArrayList;

    public SwipeListViewAdapter(Context context, ArrayList<UserInfoForChatCell> chatCellsArrayList) {
        this.context = context;
        this.chatCellsArrayList = chatCellsArrayList;
    }

    @Override
    public int getCount() {
        return chatCellsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatCellsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.chat_cell, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.nameAndAge.setText(chatCellsArrayList.get(position).getUserNameAndAge());
     //  imageLoader.displayImage(chatCellsArrayList.get(position).getUrlUserProfileImg(),holder.otherSideProfileImage);
        Glide.with(context).load(chatCellsArrayList.get(position).getUrlUserProfileImg()).bitmapTransform(new CropCircleTransformation(context)).into(holder.otherSideProfileImage);

        if (chatCellsArrayList.get(position).getLastMsgContent() == null) {
            holder.arrowMessageWhoSent.setVisibility(View.INVISIBLE);
            holder.notificationCount.setVisibility(View.INVISIBLE);
            holder.lastMessage.setVisibility(View.INVISIBLE);
        } else {
            holder.lastMessage.setText(chatCellsArrayList.get(position).getLastMsgContent());
            if (chatCellsArrayList.get(position).isLocalUserSentLast()) {
                holder.arrowMessageWhoSent.setImageResource(R.drawable.arrow_in_message);
            } else {
                holder.arrowMessageWhoSent.setImageResource(R.drawable.arrow_out_message);
            }
            if (chatCellsArrayList.get(position).getNotificationCounter() == 0) {
                holder.notificationCount.setVisibility(View.INVISIBLE);
            } else {
               holder.notificationCount.setText(chatCellsArrayList.get(position).getNotificationCounter()+"");

            }
        }

        return convertView;
    }

   private class ViewHolder {
        ImageView arrowMessageWhoSent, otherSideProfileImage;
        TextView nameAndAge, lastMessage, notificationCount;

        public ViewHolder(View view) {
            arrowMessageWhoSent = (ImageView) view.findViewById(R.id.imgArrowDirection);
            otherSideProfileImage = (ImageView) view.findViewById(R.id.imgProfileCellPic);
            notificationCount = (TextView) view.findViewById(R.id.txtNotificationMsgCounter);
            nameAndAge = (TextView) view.findViewById(R.id.txtNameAndAgeCell);
            lastMessage = (TextView) view.findViewById(R.id.txtLastMessageCell);

            view.setTag(this);
        }
    }

    @Override
    public boolean getSwipEnableByPosition(int position) {
        if(position % 2 == 0){
            return false;
        }
        return true;
    }

}

