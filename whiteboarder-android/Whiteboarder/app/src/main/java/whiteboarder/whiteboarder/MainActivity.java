package whiteboarder.whiteboarder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
        }

        if (checkSelfPermission(Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

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

    private final View.OnClickListener cameraButtonOnClickListener =  new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {
            doPermissionsCheck();

            if (camera != null) {
                // camera is already open.
                return;
            }

            camera = Camera.open(0);
            camera.setDisplayOrientation(0);
            camera.startPreview();

            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cameraPreviewSurface);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private final View.OnClickListener backstageButtonOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), Backstage.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openCameraButton = (Button) findViewById(R.id.cameraButton);
        openCameraButton.setOnClickListener(cameraButtonOnClickListener);
        Button takePhotoButton = (Button) findViewById(R.id.takePhotoButton);
        takePhotoButton.setOnClickListener(takePhotoButtonOnClickListener);

        Button backstageButton = (Button) findViewById(R.id.backstageButton);
        backstageButton.setOnClickListener(backstageButtonOnClickListener);
    }
}
