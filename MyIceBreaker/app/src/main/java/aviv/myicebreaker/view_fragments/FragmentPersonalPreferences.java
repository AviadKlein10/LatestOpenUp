package aviv.myicebreaker.view_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aviv.myicebreaker.R;

/**
 * Created by Aviad on 15/09/2016.
 */
public class FragmentPersonalPreferences extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.long_screen, container, false);
        return view;

    }
}
