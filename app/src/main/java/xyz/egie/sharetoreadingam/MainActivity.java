package xyz.egie.sharetoreadingam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String SETTINGS_EXTRAS_URL = "https://www.reading.am/settings/extras";
    PreferencesManager prefs;

    private View hintWebLink;

    private View saveEmailButton;
    private EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.prefs = PreferencesManager.getInstance(this);

        this.hintWebLink = findViewById(R.id.email_hint);
        setHintOnClick();


        this.emailInput = (EditText) findViewById(R.id.email_input);
        this.saveEmailButton = (TextView) findViewById(R.id.save_button);

        // Fill in the input field if there is an address already stored:
        String storedEmail = prefs.getReadingEmail();
        if (!TextUtils.isEmpty(storedEmail)) {
            emailInput.setText(storedEmail);
        }

        setSaveButtonOnClick();
        setEmailInputOnEnterKey();
    }

    private void setSaveButtonOnClick() {
        saveEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String readingEmail = emailInput.getText().toString();
                prefs.setReadingEmail(readingEmail);
                hideKeyboard();
            }
        });
    }

    private void setEmailInputOnEnterKey() {
        emailInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    saveEmailButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void setHintOnClick() {
        hintWebLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));

                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(MainActivity.this, Uri.parse(SETTINGS_EXTRAS_URL));
            }
        });
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
