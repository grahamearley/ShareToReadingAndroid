package xyz.egie.sharetoreadingam;

import android.app.Activity;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
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

    private TextView saveEmailButton;
    private EditText emailInput;
    private TextView yepOpinionSettingButton;
    private TextView nopeOpinionSettingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.prefs = PreferencesManager.getInstance(this);

        // Flatten out the action bar:
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
        }

        // Get view references:
        this.hintWebLink = findViewById(R.id.email_hint);
        this.emailInput = (EditText) findViewById(R.id.email_input);
        this.saveEmailButton = (TextView) findViewById(R.id.save_button);
        this.yepOpinionSettingButton = (TextView) findViewById(R.id.yep_button_opinion_option);
        this.nopeOpinionSettingButton = (TextView) findViewById(R.id.nope_button_opinion_option);

        // Fill in the input field if there is an address already stored:
        String storedEmail = prefs.getReadingEmail();
        if (!TextUtils.isEmpty(storedEmail)) {
            emailInput.setText(storedEmail);
        }

        Boolean openedFromShareOverlay = getIntent().getBooleanExtra(ShareActivity.SENT_FROM_SHARE, false);
        if (openedFromShareOverlay) {
            saveEmailButton.setText(R.string.save_and_return_to_link);
        }

        // Set OnClickListeners:
        setSaveButtonOnClick(openedFromShareOverlay);
        setEmailInputOnEnterKey();
        setWebHintOnClick();
        setOpinionSettingsOnClicks();

        // Set opinion settings from saved prefs:
        setOpinionSettingsUiFromPreferences();
    }

    private void setSaveButtonOnClick(final boolean shouldFinishAfterSave) {
        saveEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String readingEmail = emailInput.getText().toString();
                prefs.setReadingEmail(readingEmail);
                hideKeyboard();

                if (shouldFinishAfterSave) {
                    finish();
                }
            }
        });
    }

    private void setEmailInputOnEnterKey() {
        emailInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    saveEmailButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void setWebHintOnClick() {
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

    private void setOpinionSettingsOnClicks() {
        yepOpinionSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowOptionsToSelected();
                prefs.setUserOpinionOption(true);
            }
        });

        nopeOpinionSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowOptionsToUnselected();
                prefs.setUserOpinionOption(false);
            }
        });
    }

    private void setShowOptionsToSelected() {
        yepOpinionSettingButton.setSelected(true);
        nopeOpinionSettingButton.setSelected(false);

        nopeOpinionSettingButton.setPaintFlags(nopeOpinionSettingButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        yepOpinionSettingButton.setPaintFlags(0);
    }

    private void setShowOptionsToUnselected() {
        nopeOpinionSettingButton.setSelected(true);
        yepOpinionSettingButton.setSelected(false);

        yepOpinionSettingButton.setPaintFlags(yepOpinionSettingButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        nopeOpinionSettingButton.setPaintFlags(0);
    }

    private void setOpinionSettingsUiFromPreferences() {
        boolean shouldShowOpinionOption = prefs.userWantsOpinionOption();

        if (shouldShowOpinionOption) {
            yepOpinionSettingButton.performClick();
        } else {
            nopeOpinionSettingButton.performClick();
        }
    }

    private void hideKeyboard() {
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
