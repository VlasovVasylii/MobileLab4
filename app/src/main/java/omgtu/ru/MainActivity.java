package omgtu.ru;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResultDate;
    private ImageView imageViewReceived;

    private final ActivityResultLauncher<Intent> activity2Launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    long dateMillis = result.getData().getLongExtra("SELECTED_DATE", 0);
                    if (dateMillis != 0) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                        String dateString = sdf.format(new Date(dateMillis));
                        textViewResultDate.setText("Выбранная дата: " + dateString);
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        if (imageBitmap != null) {
                            imageViewReceived.setVisibility(View.VISIBLE);
                            imageViewReceived.setImageBitmap(imageBitmap);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editTextName = findViewById(R.id.editTextName);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        Button buttonOpenMarket = findViewById(R.id.buttonOpenMarket);
        Button buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        textViewResultDate = findViewById(R.id.textViewResultDate);
        imageViewReceived = findViewById(R.id.imageViewReceived);

        buttonSubmit.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            Intent intent = new Intent(MainActivity.this, Activity2.class);
            intent.putExtra("USER_NAME", name);
            activity2Launcher.launch(intent);
        });

        buttonOpenMarket.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
            // Если Play Market не установлен, откроем в браузере
            if (intent.resolveActivity(getPackageManager()) == null) {
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            }
            startActivity(intent);
        });

        buttonTakePhoto.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(takePictureIntent);
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri imageUri = intent.getData();
                if (imageUri != null) {
                    imageViewReceived.setVisibility(View.VISIBLE);
                    imageViewReceived.setImageURI(imageUri);
                }
            }
        }
    }
}