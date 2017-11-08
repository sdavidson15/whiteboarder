package whiteboarder.whiteboarder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Backstage extends AppCompatActivity {
    private final View.OnClickListener createSessionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            RESTClient2 restClient2 = new RESTClient2();
            restClient2.createSession(new RESTClient2.Callback<String>() {
                @Override
                public void success(String data) {
                    Snackbar.make(view, "session created successfully. ID = " + data, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    SessionInfo.sessionID = data;
                    updateViewsWithSessionInfo();
                }

                @Override
                public void fail() {
                    Snackbar.make(view, "error creating session", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backstage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupListeners();

        updateViewsWithSessionInfo();
    }

    private void setupListeners() {
        Button createSession = findViewById(R.id.createSessionButton);
        createSession.setOnClickListener(createSessionOnClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doPermissionsCheck() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);

        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 0);
    }

    private void updateViewsWithSessionInfo() {
        ((TextView) findViewById(R.id.sessionID)).setText(SessionInfo.sessionID);
    }
}
