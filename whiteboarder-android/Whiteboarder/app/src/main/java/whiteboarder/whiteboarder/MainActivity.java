package whiteboarder.whiteboarder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private final View.OnClickListener scanQRButtonOnClicklistener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, ReadQR.class);
            startActivity(intent);
        }
    };

    private final View.OnClickListener photoActivityButtonOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, TakePhoto.class);
            startActivity(intent);
        }
    };

    private final View.OnClickListener backstageButtonOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, Backstage.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupListeners();
    }

    private void setupListeners() {
        Button photoActivityButton = findViewById(R.id.photoActivityButton);
        photoActivityButton.setOnClickListener(photoActivityButtonOnClickListener);

        Button scanQR = findViewById(R.id.scanQRButton);
        scanQR.setOnClickListener(scanQRButtonOnClicklistener);

        Button backstageButton = findViewById(R.id.backstageButton);
        backstageButton.setOnClickListener(backstageButtonOnClickListener);
    }
}
