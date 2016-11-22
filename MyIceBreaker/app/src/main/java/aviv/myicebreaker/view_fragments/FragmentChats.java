package aviv.myicebreaker.view_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.Locale;

import aviv.myicebreaker.R;
import aviv.myicebreaker.module.Adapters.SamplePagerAdapter;
import aviv.myicebreaker.module.CustomObjects.NonSwipeableViewPager;
import aviv.myicebreaker.module.Listeners.DrawerListener;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Aviad on 28/08/2016.
 */
public class FragmentChats extends Fragment implements View.OnClickListener {
    private Context context;
    private FragmentActiveChatsList fragmentActiveChatsList;
    private FragmentMatchesList fragmentMatchesList;
    private ImageButton imgBtnActiveChats, imgBtnMatches, btnDrawer;
    private FloatingActionButton fabNewMatch;
    private DrawerListener drawerListener;
    private FragmentChats callback;
    private TextView txtTitleActionBar;
    private NonSwipeableViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("how many", " justonce");
        context = container.getContext();
        final View view = inflater.inflate(R.layout.fragment_chats, container, false);

        fabNewMatch = (FloatingActionButton) view.findViewById(R.id.fabNewMatch);
        fragmentActiveChatsList = new FragmentActiveChatsList();
        fragmentMatchesList = new FragmentMatchesList();
        txtTitleActionBar = (TextView) view.findViewById(R.id.txtTitleActionBar);
        imgBtnActiveChats = (ImageButton) view.findViewById(R.id.imgBtnActiveChats);
        imgBtnMatches = (ImageButton) view.findViewById(R.id.imgBtnMatches);
        btnDrawer = (ImageButton) view.findViewById(R.id.btnDrawer);

        viewPager = (NonSwipeableViewPager) view.findViewById(R.id.containerLists);
/** set the adapter for ViewPager */
        viewPager.setAdapter(new SamplePagerAdapter(getFragmentManager(),fragmentActiveChatsList,fragmentMatchesList));

        initTypeFace();
   //     displayFragment(fragmentActiveChatsList, AnimationSide.NON);

        view.findViewById(R.id.layoutActiveChats).setOnClickListener(this);
        view.findViewById(R.id.layoutMatches).setOnClickListener(this);
        imgBtnMatches.setOnClickListener(this);
        imgBtnActiveChats.setOnClickListener(this);
        btnDrawer.setOnClickListener(this);
        loadLastNotifications();
        deleteNotifications();
        return view;
    }

    private void initTypeFace() {
        AssetManager am = getContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "GothamRounded-Medium.otf"));


        if (txtTitleActionBar != null) {
            txtTitleActionBar.setTypeface(typeface);
        } else {
            Log.e("fChats", "problam with typeface");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutActiveChats:
            case R.id.imgBtnActiveChats:
             //   displayFragment(fragmentActiveChatsList, AnimationSide.LEFT);
viewPager.setCurrentItem(0);
fragmentActiveChatsList.initFab();
                        imgBtnActiveChats.setImageResource(R.drawable.active_chats_pressed);
                        imgBtnMatches.setImageResource(R.drawable.matches_unpressed);


                break;
            case R.id.layoutMatches:
            case R.id.imgBtnMatches:
             //  displayFragment(fragmentMatchesList, AnimationSide.RIGHT);
                viewPager.setCurrentItem(1);


                        imgBtnMatches.setImageResource(R.drawable.matches_pressed);
                        imgBtnActiveChats.setImageResource(R.drawable.active_chats_unpressed);


                break;
            case R.id.btnDrawer:
                drawerListener.openOrCloseDrawer();
        }
    }

    private void displayFragment(Fragment nextFragment, AnimationSide whichSide) {

        String backStateName = nextFragment.getClass().getName();


        FragmentManager fragmentManager = this.getChildFragmentManager();
      // boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 2);
      // Log.d("backStack: " + backStateName + " ", fragmentPopped + " ");
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (whichSide == AnimationSide.LEFT) {
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (whichSide == AnimationSide.RIGHT) {
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }


Log.d("isadded ", backStateName+ " and " + nextFragment.isAdded());
Log.d("isadded2 ", backStateName+ " and " + fragmentManager.findFragmentById(R.id.containerLists));


            ft.add(R.id.containerLists, nextFragment);

            ft.addToBackStack("replaced");
            ft.commit();



    }

    private void displayFragment2(Fragment nextFragment) {
        Log.d("switch", "fragment");
        String strNextFragment = nextFragment.toString();
        android.support.v4.app.FragmentManager fragmentManager = this.getFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        Log.d("switchNotNull ", strNextFragment + "so null");
        ft.replace(R.id.containerLists, nextFragment)
                .addToBackStack(strNextFragment)
                .commit();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("save ", "fragment");
        //  getActivity().getSupportFragmentManager().getFragment()

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        Log.d("save ", "fragment");

        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DrawerListener) {
            drawerListener = (DrawerListener) context;

            Log.d("hello", "drawerlistener");
        } else {
            throw new ClassCastException(context.toString() + " must implement OnRageComicSelected.");
        }
    }

    private void loadLastNotifications(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("refreshed_fb_token",MODE_PRIVATE);
        String restoredUserFBToken = sharedPreferences.getString("refreshed_fb_token", "");
        Log.d("refreshed_fb_token",restoredUserFBToken);
    }

    private enum AnimationSide {
        LEFT, RIGHT, NON;
    }

    private void deleteNotifications(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("notification_key2",MODE_PRIVATE);
        String restoredUserFBToken = sharedPreferences.getString("notification_key2", "");
        Log.d("notification_key2",restoredUserFBToken+" NOT nothing");
        sharedPreferences.edit().remove("notification_key2").apply();

    }
}
