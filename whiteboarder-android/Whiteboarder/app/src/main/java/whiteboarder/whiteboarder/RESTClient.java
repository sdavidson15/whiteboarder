package whiteboarder.whiteboarder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

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
    private static final String HOST = "http://192.168.1.58:8000";

    private static HttpURLConnection getClient(String path) throws IOException {
        HttpURLConnection client;
        final URL url = new URL(path);
        client = (HttpURLConnection) url.openConnection();
        return client;
    }

    private class CreateSessionTask extends AsyncTask<byte [], Void, String> {
        private IOException exception;

        protected String doInBackground(byte []... imageData) {
            assert imageData.length == 1;

            try {
                HttpURLConnection client = getClient(HOST + "/wb/create");
                client.setRequestMethod("POST");
                client.setFixedLengthStreamingMode(imageData.length);
                OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
                BufferedInputStream response = new BufferedInputStream(client.getInputStream());

                outputPost.write(imageData[0]);
                outputPost.flush();
                outputPost.close();

                Scanner s = new Scanner(response);
                String sessionID = s.next();

                client.disconnect();
                return sessionID;
            } catch (IOException e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String sessionID) {
            if (this.exception != null) {
                this.exception.printStackTrace();
            }
        }
    }

    /*
     Return the Session ID of the new Whiteboard Session.
     */
    String createWhiteboardSession(byte []imageData) throws IOException {
        CreateSessionTask task = new CreateSessionTask();
        task.execute(imageData);
        try {
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
