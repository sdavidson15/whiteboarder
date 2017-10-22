package whiteboarder.whiteboarder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class RESTClient2 {
    // TODO: change this to point to the permanent location of our server,
    // or at least make it configurable.
    //
    // The permanent location is probably
    //      "https://proj-309-yt-c-1.cs.iastate.edu/"
    private static final String HOST = "http://79814b7d.ngrok.io:80";
    private static final String SESSION_ID = "levelheadedness2";
    private static final String WB_ID = "whiteboard_id";

    public interface WhiteboarderServer {
        @FormUrlEncoded
        @POST("/image/{sessionID}")
        Call<Integer> submitImage(String sessionID);
    }

    static final Retrofit retrofit = new Retrofit.Builder().baseUrl(HOST).build();
}
