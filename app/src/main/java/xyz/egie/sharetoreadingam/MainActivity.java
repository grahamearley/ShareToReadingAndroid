package xyz.egie.sharetoreadingam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String SETTINGS_EXTRAS_URL = "https://www.reading.am/settings/extras";
    PreferencesManager prefs;

    private View hintWebLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.prefs = PreferencesManager.getInstance(this);

        this.hintWebLink = findViewById(R.id.email_hint);
        setHintOnClick();


        final EditText emailInput = (EditText) findViewById(R.id.email_input);
        TextView saveEmailButton = (TextView) findViewById(R.id.save_button);
        saveEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String readingEmail = emailInput.getText().toString();
                prefs.setReadingEmail(readingEmail);
            }
        });
    }

    private void setHintOnClick() {
        hintWebLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hintIntent = new Intent(Intent.ACTION_VIEW);
                hintIntent.setData(Uri.parse(SETTINGS_EXTRAS_URL));
                startActivity(hintIntent);
            }
        });
    }

    // TODO:
    //  - ask for Reading.am email
    //  - show link to the Reading page that gives the email

}
