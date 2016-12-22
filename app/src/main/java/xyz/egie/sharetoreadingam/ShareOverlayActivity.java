package xyz.egie.sharetoreadingam;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShareOverlayActivity extends ReadingActivity {

    // Intent key:
    public static final String SENT_FROM_SHARE = "SENT_FROM_SHARE";

    private static final String YEP = " yep ";
    private static final String NOPE = " nope ";
    private static final String NO_OPINION = "";

    private TextView errorText;
    private TextView yepButton;
    private TextView nopeButton;
    private TextView sendButton;

    private PreferencesManager prefs;

    // Stores yeps and nopes (so we can pin those onto our email):
    private String opinionText;

    // Store body of Share email
    private String shareBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_overlay);
        this.prefs = PreferencesManager.getInstance(this);

        // Start off with no yep or nope opinion:
        this.opinionText = NO_OPINION;

        // Clicking outside of the overlay will close the activity:
        View parentView = findViewById(R.id.activity_share);
        parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.link_was_not_posted_to_reading, Toast.LENGTH_LONG).show();
                finish();
            }
        });

        this.yepButton = (TextView) findViewById(R.id.yep_button);
        this.nopeButton = (TextView) findViewById(R.id.nope_button);
        this.sendButton = (TextView) findViewById(R.id.send_button);
        this.errorText = (TextView) findViewById(R.id.error_text);

        setOpinionButtonOnClicks();

        // Get share intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (action.equals(Intent.ACTION_SEND) && type != null && type.equals("text/plain")) {
            this.shareBody = intent.getStringExtra(Intent.EXTRA_TEXT);
            setSendToReading();
        } else {
            finish();
        }

        // If user doesn't want yep/nope options, then skip that:
        if (!prefs.userWantsOpinionOption()) {
            postBodyToReading();
            finish();
        }
    }

    private void setOpinionButtonOnClicks() {
        yepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.equals(opinionText, YEP)) {
                    // If opinion is already yep, then reset
                    clearOpinionUi();
                    opinionText = NO_OPINION;
                } else {
                    markUiAsYep();
                    opinionText = YEP;
                }
            }
        });

        nopeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.equals(opinionText, NOPE)) {
                    // If opinion is already nope, then reset
                    clearOpinionUi();
                    opinionText = NO_OPINION;
                } else {
                    markUiAsNope();
                    opinionText = NOPE;
                }
            }
        });
    }

    /**
     * Set the Send button to send the email to Reading
     */
    private void setSendToReading() {
        this.sendButton.setText(R.string.share_to_reading);
        this.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postBodyToReading();
                finish();
            }
        });
    }

    /**
     * Set the Send button to open the Settings activity.
     *
     *  We use this when the user needs to set up their email;
     *   this way we don't need to have two buttons
     *   (we just repurpose one of them).
     */
    private void setSendToOpenSettings() {
        this.sendButton.setText(R.string.set_up);
        this.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(ShareOverlayActivity.this, SettingsActivity.class);

                // Let the Settings activity know that we want to get back
                //  to this activity after we've set an email!
                settingsIntent.putExtra(SENT_FROM_SHARE, true);

                startActivity(settingsIntent);
            }
        });
    }

    private void postBodyToReading() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this

        String readingEmail = this.prefs.getReadingEmail();

        // Set email to send to Reading.am address
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{readingEmail});

        // Set email to send with link (from app that is sharing) and opinion (yep/nope)
        String opinionatedString = shareBody + this.opinionText;
        emailIntent.putExtra(Intent.EXTRA_TEXT, opinionatedString);

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

    private void markUiAsYep() {
        clearOpinionUi();
        nopeButton.setPaintFlags(nopeButton.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        yepButton.setSelected(true);
    }

    private void markUiAsNope() {
        clearOpinionUi();
        yepButton.setPaintFlags(nopeButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        nopeButton.setSelected(true);
    }

    private void clearOpinionUi() {
        yepButton.setPaintFlags(0);
        nopeButton.setPaintFlags(0);

        yepButton.setSelected(false);
        nopeButton.setSelected(false);
    }

    /**
     * When the overlay activity resumes, we will check whether
     *  there is an email saved. Update the UI accordingly based
     *  on that change
     *
     *  (show yep/nope if there's an email; otherwise show the
     *   error message and the link to the Settings activity)
     */
    @Override
    public void onResume() {
        super.onResume();

        String readingEmail = this.prefs.getReadingEmail();

        if (!TextUtils.isEmpty(readingEmail)) {
            yepButton.setVisibility(View.VISIBLE);
            nopeButton.setVisibility(View.VISIBLE);

            setSendToReading();

            errorText.setVisibility(View.GONE);
        } else {
            yepButton.setVisibility(View.GONE);
            nopeButton.setVisibility(View.GONE);

            setSendToOpenSettings();

            errorText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finish() {
        super.finish();

        // Finish up without a big ol transition:
        overridePendingTransition(0, 0);
    }
}
