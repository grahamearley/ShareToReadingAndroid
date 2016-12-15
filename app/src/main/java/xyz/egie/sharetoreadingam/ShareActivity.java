package xyz.egie.sharetoreadingam;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ShareActivity extends AppCompatActivity {

    // TODO: Handle case where user doesn't have an email set yet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        View parentView = findViewById(R.id.activity_share);
        parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Let the user know if the link was not posted to Reading!
                finish();
            }
        });

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

//        if (Intent.ACTION_SEND.equals(action) && type != null) {
//            if ("text/plain".equals(type)) {
//                String body = intent.getStringExtra(Intent.EXTRA_TEXT);
//                postBodyToReading(body); // Handle text being sent
//            }
//        }
    }

    private void postBodyToReading(String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this

        String readingEmail = ""; // todo: store this.

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{readingEmail});
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

    @Override
    public void finish() {
        super.finish();

        // Finish up without a big ol transition:
        overridePendingTransition(0, 0);
    }
}
