package whiteboarder.whiteboarder;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

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
                    // You have to restart the preview-- otherwise the camera freezes.
                    camera.startPreview();
                    new RESTClient().createWhiteboardSession(MainActivity.this, data);
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
            camera.setDisplayOrientation(90);
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

    private final View.OnClickListener scanQRButtonOnClicklistener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {
            doPermissionsCheck();
            Intent i = new Intent(MainActivity.this, ReadQR.class);
            startActivity(i);
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
        Button scanQR = (Button) findViewById(R.id.scanQRButton);
        scanQR.setOnClickListener(scanQRButtonOnClicklistener);
    }
}
