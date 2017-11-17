package whiteboarder.whiteboarder;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class InSession extends AppCompatActivity {

    private static final String CHANGE_USERNAME_LABEL = "Change Username";
    private static final String CHANGE_SESSION_NAME_LABEL = "Change Session Name";
    private static final String VIEW_USERS_LABEL = "View Online Users";
    private static final String SHARE_LINK_LABEL = "Share Link";
    private static final String UPDATE_BOARD_LABEL = "Update Board";

    private String[] drawerItemLabels;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_session);

        // TODO:
        // 1. Make a GET request for the session's image.
        // 2. Make a GET request for the session's data and update SessionInfo.
        // 3. Render the background image.
        // 4. Render the edits.
        // 5. Connect to the web socket, and log in as "Anonymous".

        setTitle((SessionInfo.sessionName == null) ? "No session connected" : SessionInfo.sessionName);
        if (SessionInfo.username == null) {
            Snackbar viewAsAnon = Snackbar.make(findViewById(R.id.content_frame), R.string.view_as_anon, Snackbar.LENGTH_LONG);
            viewAsAnon.show();
        }

        drawerItemLabels = new String[]{
                CHANGE_USERNAME_LABEL,
                CHANGE_SESSION_NAME_LABEL,
                VIEW_USERS_LABEL,
                SHARE_LINK_LABEL,
                UPDATE_BOARD_LABEL
        };
        ListView drawerList = findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerItemLabels));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,null, R.string.drawer_open, R.string.drawer_close) {};
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
        if (SessionInfo.sessionID == null)
            return;

        ArrayList<String> labelsList = (ArrayList<String>) Arrays.asList(drawerItemLabels);
        if (position == labelsList.indexOf(CHANGE_USERNAME_LABEL)) {
            // TODO: Let the user type a new username, send the PUT
            // request to the server, then update it in SessionInfo.
        } else if (position == labelsList.indexOf(CHANGE_SESSION_NAME_LABEL)) {
            // TODO: Let the user type a new session name, send the PUT
            // request to the server, then update it in SessionInfo.
        } else if (position == labelsList.indexOf(VIEW_USERS_LABEL)) {
            // TODO: Navigate to an Activity that simply lists the list
            // of users stored in SessionInfo
        } else if (position == labelsList.indexOf(SHARE_LINK_LABEL)) {
            // TODO: Somehow share the link.
        } else if (position == labelsList.indexOf(UPDATE_BOARD_LABEL)) {
            // TODO: Navigate to the Take Photo activity. Make sure you
            // can navigate back to this activity by hitting back.
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            handleClick(position);
        }
    }

}