package com.os.imars.views.BaseView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

/**
 * Created by Raj Kumar Kumawat on 16/06/16.
 */
public abstract class AbstractListFragment extends ListFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Env.currentActivity=getActivity();
        return onCreateViewPost(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Env.currentActivity=getActivity();
    }

    protected abstract View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

}
