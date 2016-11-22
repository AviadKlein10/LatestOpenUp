package aviv.myicebreaker.view_fragments;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Locale;

import aviv.myicebreaker.R;
import aviv.myicebreaker.module.Adapters.SwipeListViewAdapter;
import aviv.myicebreaker.module.CustomObjects.UserInfoForChatCell;

/**
 * Created by Aviad on 01/09/2016.
 */
public class FragmentMatchesList extends Fragment {
    private TextView txtMatchesTitle;
    private SwipeMenuListView listViewMatchesList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_matches_list, container, false);
        txtMatchesTitle = (TextView) view.findViewById(R.id.txtMatchesTitle);
        listViewMatchesList = (SwipeMenuListView) view.findViewById(R.id.listViewMatches);

        setFontType();

        ArrayList<UserInfoForChatCell> userInfoForChatCellsArr = createArrayUserInfoForCell();
        SwipeListViewAdapter swipeListViewAdapter = new SwipeListViewAdapter(getContext(),userInfoForChatCellsArr);
        listViewMatchesList.setAdapter(swipeListViewAdapter);

        createSwipeListview();
        onClickSwipeListview();

        listViewMatchesList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        return view;
    }

    private void setFontType() {
        AssetManager am = getContext().getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "OpenSansHebrew-Bold.ttf"));


        if (txtMatchesTitle != null) {
            txtMatchesTitle.setTypeface(typeface);
        } else {
            Log.e("FragmentActiveChat", "problam with typeface");
        }
    }
    private ArrayList<UserInfoForChatCell> createArrayUserInfoForCell() {
        ArrayList<UserInfoForChatCell> userInfoForChatCellsTemp = new ArrayList<>();
        String urlImg = "https://assets.entrepreneur.com/content/16x9/822/20150406145944-dos-donts-taking-perfect-linkedin-profile-picture-selfie-mobile-camera-2.jpeg";
        for (int i = 0; i < 11; i++) {
            UserInfoForChatCell newUser = new UserInfoForChatCell("Aviad", "24", 8, 0, "היי מה נשמע ממי שלי? חשבתי אולי לקפוץ לראות איך", urlImg,true);
            userInfoForChatCellsTemp.add(newUser);
        }

        return userInfoForChatCellsTemp;
    }

    private void onClickSwipeListview() {
        listViewMatchesList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.d("item ", "notification");
                        break;
                    case 1:
                        Log.d("item ", "block");
                        break;
                    case 2:
                        Log.d("item ", "report");
                        break;
                    case 3:
                        Log.d("item ", "delete");
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    private void createSwipeListview() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem silenceUserItem = new SwipeMenuItem(
                        getContext());
                // set item background
                silenceUserItem.setBackground(Drawable.createFromPath("#01FFFFFF"));
                // set item width
                silenceUserItem.setWidth(dp2px(75));
                // set item title
                silenceUserItem.setIcon(R.drawable.notification);
                // add to menu
                menu.addMenuItem(silenceUserItem);

                SwipeMenuItem blockUserItem = new SwipeMenuItem(
                        getContext());
                // set item background
                blockUserItem.setBackground(Drawable.createFromPath("#01FFFFFF"));
                // set item width
                blockUserItem.setWidth(dp2px(75));
                // set item title
                blockUserItem.setIcon(R.drawable.block);
                // add to menu
                menu.addMenuItem(blockUserItem);

                SwipeMenuItem reportUserItem = new SwipeMenuItem(
                        getContext());
                // set item background
                reportUserItem.setBackground(Drawable.createFromPath("#01FFFFFF"));
                // set item width
                reportUserItem.setWidth(dp2px(75));
                // set item title
                reportUserItem.setIcon(R.drawable.report_user);
                // add to menu
                menu.addMenuItem(reportUserItem);

                SwipeMenuItem deleteUserItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteUserItem.setBackground(Drawable.createFromPath("#01FFFFFF"));
                // set item width
                deleteUserItem.setWidth(dp2px(75));
                // set item title
                deleteUserItem.setIcon(R.drawable.delete);
                // add to menu
                menu.addMenuItem(deleteUserItem);
            }
        };

// set creator
        listViewMatchesList.setMenuCreator(creator);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
