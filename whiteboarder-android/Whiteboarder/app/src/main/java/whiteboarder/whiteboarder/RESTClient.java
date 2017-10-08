package whiteboarder.whiteboarder;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
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

class RESTClient {
    // TODO: change this to point to the permanent location of our server,
    // or at least make it configurable.
    //
    // The permanent location is probably
    //      "https://proj-309-yt-c-1.cs.iastate.edu/"
    private static final String HOST = "http://192.168.1.58:8080";

    private static HttpURLConnection getClient(final String path) throws IOException {
        final URL url = new URL(path);
        final HttpURLConnection client = (HttpURLConnection) url.openConnection();
        return client;
    }

    private class CreateSessionTask extends AsyncTask<byte [], Void, String> {
        private IOException exception;
        private Activity activity;

        public CreateSessionTask(Activity activity) {
            super();
            this.activity = activity;
        }

        protected String doInBackground(byte []... imageData) {
            assert imageData.length == 1;
            Log.d("CreateSessionTask", "beginning doInBackground");

            try {
                String url = HOST + "/image/levelheadedness";
                Log.d("CreateSessionTask", "POST " + url);
                final HttpURLConnection client = getClient(url);
                client.setDoOutput(true);
                client.setFixedLengthStreamingMode(imageData.length);
                client.setConnectTimeout(700);

                Log.d("CreateSessionTask", "client initialized & configured");
                OutputStream outputPost = client.getOutputStream();
                Log.d("CreateSessionTask", "got output stream");
                outputPost.write(imageData[0]);
                outputPost.flush();
                Log.d("CreateSessionTask", "wrote & flushed data");
                outputPost.close();
                Log.d("CreateSessionTask", "closed output");

                BufferedInputStream response = new BufferedInputStream(client.getInputStream());
                Scanner s = new Scanner(response);
                String sessionID = s.next();

                Log.d("CreateSessionTask", "read sessionID " + sessionID);

                client.disconnect();
                return sessionID;
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

            Toast.makeText(activity, sessionID, Toast.LENGTH_SHORT).show();
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
