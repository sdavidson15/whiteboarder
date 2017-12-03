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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupListeners();
    }

    private void setupListeners() {
        Button scanQR = findViewById(R.id.scanQRButton);
        scanQR.setOnClickListener(scanQRButtonOnClicklistener);
    }
}
