package aviv.myicebreaker.module.Adapters;

import android.widget.BaseAdapter;

/**
 * Created by Aviad on 05/09/2016.
 */
public abstract class BaseSwipeListAdapter extends BaseAdapter {

    public boolean getSwipEnableByPosition(int position){
        return true;
    }



}