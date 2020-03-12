package com.os.imars.views.BaseView;

import android.content.Context;

/**
 * Created by Raj Kumar Kumawat on 8/10/2017.
 */

public class Env {

    public static Context appContext;
    public static Context currentActivity;

    public static void init(Context appContext) {
        Env.appContext = appContext;

    }
}
