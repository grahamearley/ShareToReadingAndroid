package xyz.egie.sharetoreadingam;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * A custom base activity for performing logic that should
 *  happen with all activities in this app
 *  (for example, setting up Calligraphy!)
 */

public abstract class ReadingActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
