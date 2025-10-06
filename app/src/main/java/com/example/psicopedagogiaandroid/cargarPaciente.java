package com.example.psicopedagogiaandroid;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Locale;

public class cargarPaciente extends AppCompatActivity {
    private Spinner nivelEdu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cargar_paciente);

        nivelEdu = findViewById(R.id.nivelEdu);
        String []opciones = {"Inicial", "Primario", "Secundario", "Terciario", "Universitario", "Posgrado"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        nivelEdu.setAdapter(adaptador);

        EditText fechaNac = findViewById(R.id.fechaNac);
        Spinner nivelEdu = findViewById(R.id.nivelEdu);

// DatePicker
        fechaNac.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dp = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        // formateo simple DD/MM/YYYY
                        String txt = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                                dayOfMonth, month + 1, year);
                        fechaNac.setText(txt);
                    },
                    y, m, d
            );
            dp.show();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}