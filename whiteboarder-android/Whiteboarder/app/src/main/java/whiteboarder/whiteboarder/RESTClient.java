package whiteboarder.whiteboarder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;

class RESTClient {
    // TODO: change this to point to the permanent location of our server,
    // or at least make it configurable.
    //
    // The permanent location is probably
    //      "https://proj-309-yt-c-1.cs.iastate.edu/"
    private static final String HOST = "http://79814b7d.ngrok.io:80";
    private static final String SESSION_ID = "levelheadedness2";
    private static final String WB_ID = "whiteboard_id";

    private static HttpURLConnection getClient(final String path) throws IOException {
        final URL url = new URL(path);
        final HttpURLConnection client = (HttpURLConnection) url.openConnection();
        return client;
    }

    private class CreateSessionTask extends AsyncTask<byte [], Void, String> {
        private class SessionPOSTRequest {
            byte []bytes;
            String wbID;
            String filename;

            SessionPOSTRequest(byte []imageData, String wbID, String filename) {
                this.bytes = imageData;
                this.wbID = wbID;
                this.filename = filename;
            }
        }

        private IOException exception;
        private Activity activity;

        CreateSessionTask(Activity activity) {
            super();
            this.activity = activity;
        }

        protected String doInBackground(byte []... imageDatas) {
            assert imageDatas.length == 1;
            byte []imageData = imageDatas[0];
            Log.d("CreateSessionTask", "beginning doInBackground");

            SessionPOSTRequest request = new SessionPOSTRequest(imageData, WB_ID, "filename.jpg");
            String serialized = new Gson().toJson(request);
            Log.d("CreateSessionTask", "serialized: " + serialized);

            try {
                String url = HOST + "/whiteboarder/wb/image/" + SESSION_ID;
                Log.d("CreateSessionTask", "POST " + url);
                final HttpURLConnection client = getClient(url);
                client.setDoOutput(true);
                client.setFixedLengthStreamingMode(serialized.length());
                client.setConnectTimeout(700);

                Log.d("CreateSessionTask", "client initialized & configured");
                OutputStream outputPost = client.getOutputStream();
                Log.d("CreateSessionTask", "got output stream");
                outputPost.write(serialized.getBytes());
                outputPost.flush();
                Log.d("CreateSessionTask", "wrote & flushed data");
                outputPost.close();
                Log.d("CreateSessionTask", "closed output");

                client.disconnect();
                return SESSION_ID;
            } catch (IOException e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String sessionID) {
            Log.d("onPostExecute", "activity = " + activity.toString());
            if (this.exception != null) {
                Toast.makeText(activity, "error creating session at " + HOST, Toast.LENGTH_SHORT).show();
                this.exception.printStackTrace();
                return;
            }

            Toast.makeText(activity, "created session " + sessionID, Toast.LENGTH_SHORT).show();
            String url = HOST + "/whiteboarder/wb/image/" + SESSION_ID;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            activity.startActivity(i);
        }
    }

    /*
     Return the Session ID of the new Whiteboard Session.
     */
    String createWhiteboardSession(Activity activity, byte []imageData) {
        CreateSessionTask task = new CreateSessionTask(activity);
        task.execute(imageData);
        return null;
    }
}
