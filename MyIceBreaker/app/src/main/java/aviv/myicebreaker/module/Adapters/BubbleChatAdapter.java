package aviv.myicebreaker.module.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import aviv.myicebreaker.R;
import aviv.myicebreaker.module.BubbleOrderType;
import aviv.myicebreaker.module.BubbleType;
import aviv.myicebreaker.module.CustomObjects.BubbleChatObject;

import static aviv.myicebreaker.module.BubbleType.LOCAL_USER;
import static aviv.myicebreaker.module.BubbleType.QUESTION;

/**
 * Created by Aviad on 30/09/2016.
 */
public class BubbleChatAdapter extends BaseAdapter {

    private ArrayList<BubbleChatObject> listConversation;
    private Context context;

    public BubbleChatAdapter(Context context, ArrayList<BubbleChatObject> listConversation) {
        this.context = context;
        this.listConversation = listConversation;
    }


    @Override
    public int getCount() {
        if (listConversation != null) {
            return listConversation.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (listConversation != null) {
            return listConversation.get(position);
        } else {
            return null;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.bubble_cell, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        BubbleChatObject currentBubble = listConversation.get(position);

        BubbleOrderType bubbleOrderType = whatLocationOfSameSideBubble(position, listConversation.get(position).getBubbleType());
        changeParametersOfViews(holder, currentBubble.getBubbleType(), bubbleOrderType);
        holder.textSendingTime.setText(listConversation.get(position).getDateTime());
        holder.textMsgContent.setText(currentBubble.getMessageContent());

        return convertView;
    }

    private void changeParametersOfViews(ViewHolder holder, BubbleType bubbleType, BubbleOrderType bubbleOrderType) {

        String stringResBubbleShapeOrder, stringResBubbleShapeUser = "user";
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        holder.textMsgContent.setId(R.id.textMsgId);
        holder.imgBubblePointer.setId(R.id.imgPointerId);


        if (bubbleOrderType == BubbleOrderType.FIRST || bubbleOrderType == BubbleOrderType.MIDDLE || bubbleType == QUESTION) {
            holder.textSendingTime.setVisibility(View.GONE);
            holder.imgBubblePointer.setVisibility(View.INVISIBLE);
        } else {

            imageParams.addRule(RelativeLayout.ALIGN_BOTTOM, holder.textMsgContent.getId());
            imageParams.setMargins(0,0,0, -5);
            holder.textSendingTime.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams textTimeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            textTimeParams.addRule(RelativeLayout.BELOW, holder.textMsgContent.getId());
            if(bubbleType==LOCAL_USER){
                textTimeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); // TODO fix time apperance
            }else{
                textTimeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            }
            holder.textSendingTime.setLayoutParams(textTimeParams);

            holder.imgBubblePointer.setVisibility(View.VISIBLE);
        }
        if (bubbleOrderType == BubbleOrderType.FIRST || bubbleOrderType == BubbleOrderType.SINGLE) {
            stringResBubbleShapeOrder = "single_or_first";
        } else {
            stringResBubbleShapeOrder = "middle_or_last";
        }

        switch (bubbleType) {
            case LOCAL_USER:
                textParams.setMargins(0, 0, 0, 0);
                textParams.addRule(RelativeLayout.LEFT_OF, holder.imgBubblePointer.getId()); // WORKING set right to

                imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.textMsgContent.setGravity(Gravity.RIGHT);
                holder.layoutBubbleCell.setGravity(Gravity.RIGHT);


                holder.imgBubblePointer.setBackgroundResource(R.drawable.bubble_pointer_local_user);
                stringResBubbleShapeUser = "local_user_";
                break;

            case OTHER_SIDE_USER:
                textParams.setMargins(0, 0, 0, 0);
                textParams.addRule(RelativeLayout.RIGHT_OF, holder.imgBubblePointer.getId()); // WORKING set right to

                holder.layoutBubbleCell.setGravity(Gravity.LEFT);
                holder.textMsgContent.setGravity(Gravity.LEFT);

                holder.imgBubblePointer.setBackgroundResource(R.drawable.bubble_pointer_other_side_user);
                stringResBubbleShapeUser = "other_side_user_";
                break;

            case QUESTION:
                textParams.setMargins(10, 30, 10, 30);
                stringResBubbleShapeUser = "question_";
                stringResBubbleShapeOrder = "single";
                holder.layoutBubbleCell.setGravity(Gravity.CENTER);

                holder.textMsgContent.setGravity(Gravity.CENTER);
                break;
        }

        holder.imgBubblePointer.setLayoutParams(imageParams);
        holder.textMsgContent.setLayoutParams(textParams);
        int iconResId = context.getResources().getIdentifier("bubble_" + stringResBubbleShapeUser + stringResBubbleShapeOrder, "drawable", context.getPackageName());
        holder.textMsgContent.setBackgroundResource(iconResId);
    }

    private BubbleOrderType whatLocationOfSameSideBubble(int position, BubbleType currentBubbleType) {


        if (currentBubbleType == QUESTION) {
            return null;
        }

        boolean sameTypeAsPrevious = false, sameTypeAsNext = false;
        if (position > 0) {
            if (listConversation.get(position - 1) != null) {
                if (listConversation.get(position - 1).getBubbleType() == currentBubbleType) {
                    sameTypeAsPrevious = true;
                }
            }
        }
        if (position + 1 < listConversation.size()) {
            if (listConversation.get(position + 1) != null) {
                if (listConversation.get(position + 1).getBubbleType() == currentBubbleType) {
                    sameTypeAsNext = true;
                }
            }
        }
        if (!sameTypeAsNext && !sameTypeAsPrevious) {
            return BubbleOrderType.SINGLE;
        }
        if (sameTypeAsNext && !sameTypeAsPrevious) {
            return BubbleOrderType.FIRST;
        }
        if (sameTypeAsNext && sameTypeAsPrevious) {
            return BubbleOrderType.MIDDLE;
        }
        if (!sameTypeAsNext && sameTypeAsPrevious) {
            return BubbleOrderType.LAST;
        }
        return BubbleOrderType.SINGLE;
    }


    private class ViewHolder {
        public TextView textDate, textMsgContent, textSendingTime;
        public ImageView imgBubblePointer;
        public RelativeLayout layoutBubbleCell;

        private ViewHolder(View v) {
            textDate = (TextView) v.findViewById(R.id.textDate);
            textMsgContent = (TextView) v.findViewById(R.id.textMsgContent);
            textSendingTime = (TextView) v.findViewById(R.id.textSendingTime);
            imgBubblePointer = (ImageView) v.findViewById(R.id.imgBubblePointer);
            layoutBubbleCell = (RelativeLayout) v.findViewById(R.id.layoutBubbleCell);

            v.setTag(this);
        }
    }



}
