package it.flashlov.utility;

import android.app.Application;

/**
 * Created by Karshima on 1/2/2017.
 */

public class AppClass extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FontsOverride.setDefaultFont(this, "DEFAULT", "proxima.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "proxima.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "proxima.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "proxima.ttf");

    }

}
