package xyz.egie.sharetoreadingam;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * A class for managing the SharedPreferences of the app.
 */

public class PreferencesManager {

    private static final String DEFAULT_PREFS_NAME = "DEFAULT_PREFS_NAME";
    private static final String READING_EMAIL = "READING_EMAIL";
    private static final String OPINION_OPTION = "OPINION_OPTION";

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

    public void setUserOpinionOption(boolean shouldShowOpinionOption) {
        prefs.edit().putBoolean(OPINION_OPTION, shouldShowOpinionOption).apply();
    }

    public boolean userWantsOpinionOption() {
        // By default, we'll show the opinion options (so default is true)
        return prefs.getBoolean(OPINION_OPTION, true);
    }
}
