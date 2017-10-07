package whiteboarder.whiteboarder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RESTClient {
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

    /*
     Return the Session ID of the new Whiteboard Session.
     */
    public static String createWhiteboardSession(byte []imageData) throws IOException {
        System.out.println("made it to here");
        HttpURLConnection client = getClient(HOST + "/wb/create");
        client.setRequestMethod("POST");
        client.setFixedLengthStreamingMode(imageData.length);
        OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
        BufferedInputStream response = new BufferedInputStream(client.getInputStream());

        outputPost.write(imageData);
        outputPost.flush();
        outputPost.close();

        Scanner s = new Scanner(response);
        String sessionID = s.next();

        client.disconnect();

        return sessionID;
    }
}
