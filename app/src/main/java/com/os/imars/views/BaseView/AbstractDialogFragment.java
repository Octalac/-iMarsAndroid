package com.os.imars.views.BaseView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * Created by Raj Kumar Kumawat on 10/13/2015.
 */
public abstract class AbstractDialogFragment extends DialogFragment implements View.OnClickListener {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Env.currentActivity = getActivity();

        return onCreateDialogPost(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Env.currentActivity = getActivity();
    }

    protected abstract Dialog onCreateDialogPost(Bundle savedInstanceState);


}
