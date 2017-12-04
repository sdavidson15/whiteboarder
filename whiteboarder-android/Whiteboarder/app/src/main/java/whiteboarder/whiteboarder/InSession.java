package whiteboarder.whiteboarder;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InSession extends AppCompatActivity {
    private static final String SHARE_LINK_LABEL = "Share Link";
    private static final String UPDATE_BOARD_LABEL = "Take New Photo";

    private String[] drawerItemLabels = new String[]{
            "Administrative controls coming soon!"
    };
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_session);

        setTitle("Connected to Session");
        ListView drawerList = findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerItemLabels));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,null, R.string.drawer_open, R.string.drawer_close) {};
        drawerLayout.addDrawerListener(drawerToggle);

        ((TextView) findViewById(R.id.meta_session_id)).setText("Session ID: " + SessionInfo.sessionID);

        findViewById(R.id.new_image_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(InSession.this, TakePhoto.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.refresh_user_list).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                refreshConnectedUserList();
            }
        });

        refreshConnectedUserList();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    protected void refreshConnectedUserList() {
        new RESTClient2().getUsers(SessionInfo.sessionID, new RESTClient2.Callback<List<RESTClient2.User>>() {
            @Override
            void success(List<RESTClient2.User> data) {
                TextView textView = (TextView) findViewById(R.id.connected_user_list);
                String text = "";
                for (RESTClient2.User user: data) {
                    text = text + user.username + "\n";
                }
                textView.setText(text);
            }

            @Override
            void fail() {
                Snackbar.make(
                        findViewById(R.id.drawer_layout),
                        "There was an issue fetching the user list :(",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void handleClick(int position) {
        Log.d("InSession.handleClick", "click at position " + position);

        if (SessionInfo.sessionID == null)
            return;

        ArrayList<String> labelsList = (ArrayList<String>) Arrays.asList(drawerItemLabels);
        if (position == labelsList.indexOf(SHARE_LINK_LABEL)) {
            Log.d("InSession.handleClick", "sharing");

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Whiteboarder");
            shareIntent.putExtra(Intent.EXTRA_TEXT, RESTClient2.HOST + "/?sessionID=" + SessionInfo.sessionID);
            startActivity(Intent.createChooser(shareIntent, "some text"));
        } else if (position == labelsList.indexOf(UPDATE_BOARD_LABEL)) {
            // TODO: Navigate to the Take Photo activity. Make sure you
            // can navigate back to this activity by hitting back.
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Log.d("handleClick", "click at position " + position);
            handleClick(position);
        }
    }

    @Override
    protected void onDestroy() {
        SessionInfo.sessionID = null;
        super.onDestroy();
    }
}