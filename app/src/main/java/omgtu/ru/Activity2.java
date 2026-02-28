package omgtu.ru;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textViewDisplayName = findViewById(R.id.textViewDisplayName);
        DatePicker datePicker = findViewById(R.id.datePicker);
        Button buttonOk = findViewById(R.id.buttonOk);

        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null) {
            textViewDisplayName.setText(userName);
        }

        buttonOk.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            long dateMillis = calendar.getTimeInMillis();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("SELECTED_DATE", dateMillis);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}