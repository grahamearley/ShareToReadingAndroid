package xyz.egie.sharetoreadingam;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ShareToReadingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Set the font to be Roboto light!
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
