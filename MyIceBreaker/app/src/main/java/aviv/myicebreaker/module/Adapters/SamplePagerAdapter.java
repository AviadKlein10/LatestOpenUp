package aviv.myicebreaker.module.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import aviv.myicebreaker.view_fragments.FragmentActiveChatsList;
import aviv.myicebreaker.view_fragments.FragmentMatchesList;

/**
 * Created by Aviad on 26/09/2016.
 */
public class SamplePagerAdapter extends FragmentPagerAdapter {
    private FragmentActiveChatsList fragmentActiveChatsList;
    private FragmentMatchesList fragmentMatchesList;
    public SamplePagerAdapter(FragmentManager fm,FragmentActiveChatsList fragmentActiveChatsList, FragmentMatchesList fragmentMatchesList) {
        super(fm);
        this.fragmentActiveChatsList = fragmentActiveChatsList;
        this.fragmentMatchesList = fragmentMatchesList;
    }

    @Override
    public Fragment getItem(int position) {
        /** Show a Fragment based on the position of the current screen */
        if (position == 0) {
            return fragmentActiveChatsList;
        } else
            return fragmentMatchesList;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }



}
