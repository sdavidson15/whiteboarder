package whiteboarder.whiteboarder;

import android.util.Log;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

// RESTClient2 is a replacement for RESTClient that makes use of the Retrofit library.
//
// See https://square.github.io/retrofit/ for information on the library in use.
// RESTClient2 is *unaware* of the SessionInfo singleton.
public class RESTClient2 {
    // TODO: change this to point to the permanent location of our server,
    // or at least make it configurable.
    //
    // The permanent location is probably
    //      "http://proj-309-yt-c-1.cs.iastate.edu"
    public static final String HOST = "http://proj-309-yt-c-1.cs.iastate.edu";

    public abstract static class Callback<T> {
        abstract void success(T data);
        abstract void fail();
    }

    public class User {
        public String wbID;
        public String username;
    }


    private interface WhiteboarderServer {
        @POST("/whiteboarder/session")
        Call<String> createSesssion();

        @Multipart
        @POST("/whiteboarder/image/{sessionID}")
        Call<Void> submitImage(@Path("sessionID") String sessionID, @Part("file") RequestBody requestBody);

        @GET("/whiteboarder/users/{sessionID}")
        Call<List<User>> getUsers(@Path("sessionID") String sessionID);
    }

    private static final Retrofit retrofit = new Retrofit.Builder().baseUrl(HOST)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final WhiteboarderServer service = retrofit.create(WhiteboarderServer.class);

    public void createSession(final Callback<String> callback) {
        Call<String> call = service.createSesssion();

        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() >= 200 && response.code() < 300) {
                    callback.success(response.body());
                } else {
                    callback.fail();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("createSession failure", t.toString());
                callback.fail();
            }
        });
    }

    public void postImage(String sessionID, RequestBody requestBody, final Callback<Void> callback) {
        Call<Void> call = service.submitImage(sessionID, requestBody);
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.success(null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.fail();
            }
        });
    }

    public void getUsers(String sessionID, final Callback<List<User>> callback) {
        Call<List<User>> call = service.getUsers(sessionID);
        call.enqueue(new retrofit2.Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code() >= 200 && response.code() < 300) {
                    callback.success(response.body());
                } else {
                    callback.fail();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("getUsers failure", t.toString());
                callback.fail();
            }
        });
    }
}
