package aviv.myicebreaker.module.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import aviv.myicebreaker.view_fragments.FragmentActiveChatsList;
import aviv.myicebreaker.view_fragments.FragmentMatchesList;

/**
 * Created by Aviad on 27/11/2016.
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private FragmentActiveChatsList fragmentActiveChatsList;
    private FragmentMatchesList fragmentMatchesList;
    public FragmentPagerAdapter(FragmentManager fm,FragmentActiveChatsList fragmentActiveChatsList, FragmentMatchesList fragmentMatchesList) {
        super(fm);
        this.fragmentActiveChatsList = fragmentActiveChatsList;
        this.fragmentMatchesList = fragmentMatchesList;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("getItem ", "fragment" + position);
        if (position==0){
            return fragmentActiveChatsList;
        }if(position == 1){
            return fragmentMatchesList;
        }
        return null;
    }
/*
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("instantiateItem ", "fragment" + position);

        if (position==0){
            return fragmentActiveChatsList;
        }if(position == 1){
            return fragmentMatchesList;
        }
        return super.instantiateItem(container, position);
    }*/

    @Override
    public int getCount() {
        return 2;
    }
}
