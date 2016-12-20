package xyz.egie.sharetoreadingam;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by grahamearley on 12/20/16.
 */

public class PreferencesManager {

    private static final String DEFAULT_PREFS_NAME = "DEFAULT_PREFS_NAME";
    private static final String READING_EMAIL = "READING_EMAIL";

    private SharedPreferences prefs;
    private static PreferencesManager instance;

    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    private PreferencesManager(Context context) {
        this.prefs = context.getSharedPreferences(DEFAULT_PREFS_NAME, 0);
    }

    public void setReadingEmail(String email) {
        prefs.edit().putString(READING_EMAIL, email).apply();
    }

    public String getReadingEmail() {
        return prefs.getString(READING_EMAIL, null);
    }
}
