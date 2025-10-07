package com.example.psicopedagogiaandroid;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class cargarPaciente extends AppCompatActivity {

    private AutoCompleteTextView nivelEdu;
    private EditText fechaNac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cargar_paciente);

        // Referencias
        nivelEdu = findViewById(R.id.nivelEdu);
        fechaNac = findViewById(R.id.fechaNac);

        // Opciones del dropdown
        String[] opciones = {
                "Inicial",
                "Primario",
                "Secundario",
                "Terciario",
                "Universitario",
                "Posgrado"
        };

        // Adaptador con tu diseño personalizado
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                this,
                R.layout.spinner_dropdown_item,
                opciones
        );
        nivelEdu.setAdapter(adaptador);

        // DatePicker para fecha de nacimiento
        fechaNac.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dp = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String txt = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                                dayOfMonth, month + 1, year);
                        fechaNac.setText(txt);
                    },
                    y, m, d
            );
            dp.show();
        });

        // Ajuste para pantallas edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
