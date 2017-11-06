package whiteboarder.whiteboarder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Camera camera;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doPermissionsCheck() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
        }

        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.INTERNET}, 0);
        }
    }

    private final View.OnClickListener takePhotoButtonOnClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override

        public void onClick(final View view) {
            Log.d("takePhotoButton", "clicked");

            if (camera == null) {
                return;
            }

            camera.takePicture(null, null, null, new Camera.PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), data);

                    // Post to the server
                    new RESTClient2().postImage(SessionInfo.sessionID, requestBody, new RESTClient2.Callback<Void>() {
                        void success(Void v) {
                            Snackbar.make(view, "Image posted!", Snackbar.LENGTH_LONG).show();
                        }

                        void fail() {
                            Snackbar.make(view, "Failed to post image.", Snackbar.LENGTH_LONG).show();
                        }
                    });

                    // You have to restart the preview-- otherwise the camera freezes.
                    camera.startPreview();
                }
            });
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openCamera() {
        doPermissionsCheck();

        if (camera != null) {
            // camera is already open.
            return;
        }

        camera = Camera.open(0);

        // hack to make the camera show up as rotated correctly.
        // see https://stackoverflow.com/questions/19176038/camera-setdisplayorientation-function-is-not-working-for-samsung-galaxy-ace-wi#19257186
        Camera.Parameters parameters = camera.getParameters();
        parameters.set("orientation", "portrait"); // why is this necessary?
        parameters.setRotation(0);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cameraPreviewSurface);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d("openCamera", holder.toString());
                try {
                    camera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    Log.d("openCamera", "unable to setPreviewDisplay on camera");
                    e.printStackTrace();
                }
                camera.startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {}
        });
    }

    private final View.OnClickListener backstageButtonOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), Backstage.class);
            startActivity(intent);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button takePhotoButton = (Button) findViewById(R.id.takePhotoButton);
        takePhotoButton.setOnClickListener(takePhotoButtonOnClickListener);

        Button backstageButton = (Button) findViewById(R.id.backstageButton);
        backstageButton.setOnClickListener(backstageButtonOnClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        openCamera();
    }
}
