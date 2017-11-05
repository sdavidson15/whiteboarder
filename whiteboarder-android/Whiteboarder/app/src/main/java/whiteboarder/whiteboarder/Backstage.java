package whiteboarder.whiteboarder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Backstage extends AppCompatActivity {
    String sessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backstage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button b = (Button) findViewById(R.id.createSessionButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                RESTClient2 restClient2 = new RESTClient2();
                restClient2.createSession(new RESTClient2.Callback<String>() {
                    @Override
                    public void success(String data) {
                        Snackbar.make(view, "session created successfully. ID = " + data, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        ((TextView) findViewById(R.id.sessionID)).setText(data);
                    }

                    @Override
                    public void fail() {
                        Snackbar.make(view, "error creating session", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        });
    }

}
