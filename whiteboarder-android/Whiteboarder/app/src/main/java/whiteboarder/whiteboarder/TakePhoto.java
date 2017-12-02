package whiteboarder.whiteboarder;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TakePhoto extends AppCompatActivity {
    private Camera camera;

    private final View.OnClickListener takePhotoButtonOnClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(final View view) {
            Log.d("takePhotoButton", "clicked");

            if (camera == null)
                return;

            camera.takePicture(null, null, null, new Camera.PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), data);
                    Snackbar.make(view, "Uploading...", Snackbar.LENGTH_SHORT).show();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto);
        setupListeners();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        openCamera();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        openCamera();
    }

    private void setupListeners() {
        Button takePhotoButton = findViewById(R.id.takePhotoButton);
        takePhotoButton.setOnClickListener(takePhotoButtonOnClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doPermissionsCheck() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);

        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openCamera() {
        doPermissionsCheck();

        if (camera != null) {
            // camera is already open.
            return;
        }

        camera = Camera.open(0);
        setCameraDisplayOrientation();

        SurfaceView surfaceView = findViewById(R.id.cameraPreviewSurface);
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
                camera.autoFocus(null);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {}
        });
    }

    private void setCameraDisplayOrientation() {
        if (camera == null)
            return;

        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(0, info);
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
