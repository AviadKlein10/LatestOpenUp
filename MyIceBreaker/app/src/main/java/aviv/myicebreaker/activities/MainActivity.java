package aviv.myicebreaker.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.util.Locale;

import aviv.myicebreaker.R;
import aviv.myicebreaker.Singleton;
import aviv.myicebreaker.module.CustomObjects.NewUser;
import aviv.myicebreaker.module.Listeners.DrawerListener;
import aviv.myicebreaker.module.Listeners.ListenerActiveChatsList;
import aviv.myicebreaker.network.Connectivity;
import aviv.myicebreaker.network.ConnectivityError;
import aviv.myicebreaker.network.OpenGalleryListener;
import aviv.myicebreaker.network.ResponseObject;
import aviv.myicebreaker.network.SearchBtnListener;
import aviv.myicebreaker.view_fragments.FragmentChangeProfilePic;
import aviv.myicebreaker.view_fragments.FragmentChats;
import aviv.myicebreaker.view_fragments.FragmentFacebookGallery;
import aviv.myicebreaker.view_fragments.FragmentGalleries;
import aviv.myicebreaker.view_fragments.FragmentPersonalPreferences;

import aviv.myicebreaker.view_fragments.FragmentSomeText;
import aviv.myicebreaker.view_fragments.GalleryListener;


/**
 * Created by Aviv on 12/07/2016.
 */
public class MainActivity extends AppCompatActivity implements DrawerListener, ListenerActiveChatsList, SearchBtnListener, OpenGalleryListener,GalleryListener {

    public static final String TAG = Connectivity.class.getSimpleName();
    private FragmentChats fragmentChats;
    private FragmentChangeProfilePic fragmentChangeProfilePic;
    private NewUser localUser;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton btnChangePic;
    private Connectivity connectivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                localUser = new NewUser(null);
                localUser = new NewUser(Singleton.getInstance().getNewUser());
                Log.d("wtff", " " + localUser.getFirstName());
            }
        });
        setContentView(R.layout.main_activity);
        initActionBar();
        initDrawerNavigation();

        connectivity = new Connectivity(this);
       Log.d("Splash? ",SplashActivity.TAG);

        fragmentChats = new FragmentChats();

        if (findViewById(R.id.fragmentContainer) != null) {

            if (savedInstanceState != null) {
                return;
            }
            Log.d("initFragment", " Chats");
            initFragmentChats();
        }

        /*Intent intent = getIntent();
        String message = intent.getStringExtra("Hello");
        Log.d("message", message + "killmyslef");*/

    }


    private void initDrawerNavigation() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.inflateHeaderView(R.layout.drawer_header);
        View headerview = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        //  String urlImg = "https://assets.entrepreneur.com/content/16x9/822/20150406145944-dos-donts-taking-perfect-linkedin-profile-picture-selfie-mobile-camera-2.jpeg";


        ImageView profileImgDrawer = (ImageView) headerview.findViewById(R.id.drawerImgUser);
        btnChangePic = (FloatingActionButton) headerview.findViewById(R.id.btnChagnePic);
        btnChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentChangeProfilePic = new FragmentChangeProfilePic();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragmentContainer, fragmentChangeProfilePic).commit();
                openOrCloseDrawer();
            }
        });
        String urlProfilePic= Singleton.getInstance().getNewUser().getProfileImageUrl();
        Glide.with(this).load(urlProfilePic).centerCrop().into(profileImgDrawer);
        navigationView.setCheckedItem(R.id.home);
        navigationView.setTextDirection(View.TEXT_DIRECTION_RTL);
        navigationView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        navigationView.setItemTextAppearance(R.style.item_navigation);

    }

    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {

            mDrawerLayout.closeDrawer(GravityCompat.START);

            Fragment fragment = null;
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.userPreferences:
                    fragment = new FragmentPersonalPreferences();
                    break;
                case R.id.termsOfUse:
                    fragment = new FragmentSomeText();
                    break;
                case R.id.contactUs:
                //    fragment = new FragmentPictureReplace();
                    break;
                case R.id.questionsAndAnswers:
               //     fragment = new FragmentPictureReplace();
                    break;
                case R.id.share:
              //      fragment = new FragmentPictureReplace();
                    break;
            }

            ft.replace(R.id.fragmentContainer, fragment).commit();
            return true;
        }
    };

    private void initActionBar() {

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//TODO change status bar lower than 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            //TODO change color status bar
        }


        AssetManager am = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "GothamRounded-Medium.otf"));


        TextView txtTitleActionBar = (TextView) findViewById(R.id.txtTitleActionBar);
        if (txtTitleActionBar != null) {
            txtTitleActionBar.setTypeface(typeface);
        } else {
            Log.e(TAG, "problam with typeface");
        }
    }


    private void initFragmentChats() {
        Log.d("initFragment", " Chats");


        fragmentChats.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragmentChats).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_items, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            initFragmentChats();
            super.onBackPressed();
        }
    }

    @Override
    public void openOrCloseDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void createActivityPrivateChat() {


        Intent mainIntent = new Intent(getApplicationContext(), ActivityPrivateChat.class);
        mainIntent.putExtra("Hello", "fck");
        startActivity(mainIntent);
    }

    @Override
    public void onSearchClick() {
        connectivity.testSearchForMatch();
        connectivity.albumTry();
    }

    @Override
    public void receiveServerResponse(ConnectivityError error, ResponseObject response) {

    }

    @Override
    public void onOpenGalleryClicked() {
        FragmentGalleries fragmentGalleries = new FragmentGalleries();
        replaceFragment(fragmentGalleries);
    }

    @Override
    public void uploadChosenImage(String userId, int imageIndexInArray, File imageFile) {
        connectivity.uploadImageToServer(userId,String.valueOf(imageIndexInArray),imageFile);
    }

    @Override
    public void initFacebookGalleryFragment() {
        FragmentFacebookGallery fragmentFacebookGallery = new FragmentFacebookGallery();
        replaceFragment(fragmentFacebookGallery);
    }
    private void replaceFragment(Fragment nextFragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, nextFragment).commit();
    }
}
