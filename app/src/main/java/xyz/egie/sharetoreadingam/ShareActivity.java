package xyz.egie.sharetoreadingam;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ShareActivity extends AppCompatActivity {

    private static final String YEP = " yep ";
    private static final String NOPE = " nope ";

    // Stores yeps and nopes:
    private String opinionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // TODO: Handle case where user doesn't have an email set yet
        // TODO: Let user choose whether to show the YEP/NOPE section (if not, then just jump to send)

        View parentView = findViewById(R.id.activity_share);
        parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Let the user know if the link was not posted to Reading!
                finish();
            }
        });

        final TextView yepButton = (TextView) findViewById(R.id.yep_button);
        final TextView nopeButton = (TextView) findViewById(R.id.nope_button);
        TextView sendButton = (TextView) findViewById(R.id.send_button);

        yepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opinionText = YEP;
                nopeButton.setPaintFlags(nopeButton.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                yepButton.setSelected(true);
            }
        });

        nopeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opinionText = NOPE;
            }
        });

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShareActivity.this, "Opinion: " + opinionText, Toast.LENGTH_LONG).show();
                finish();
            }
        });

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

        String opinionatedString = body + this.opinionText;
        emailIntent.putExtra(Intent.EXTRA_TEXT, opinionatedString);

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
