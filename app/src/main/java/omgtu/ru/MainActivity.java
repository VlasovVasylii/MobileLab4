package omgtu.ru;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
        textViewResultDate = findViewById(R.id.textViewResultDate);

        buttonSubmit.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            Intent intent = new Intent(MainActivity.this, Activity2.class);
            intent.putExtra("USER_NAME", name);
            activity2Launcher.launch(intent);
        });
    }
}