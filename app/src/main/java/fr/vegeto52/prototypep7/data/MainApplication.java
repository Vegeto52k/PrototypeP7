package fr.vegeto52.prototypep7.data;

import android.app.Application;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public class MainApplication extends Application {

    private static final String TAG = "MainApplication";
    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static Application getApplication() {
        return mApplication;
    }
}
