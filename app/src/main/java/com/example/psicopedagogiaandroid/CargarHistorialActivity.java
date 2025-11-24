package com.example.psicopedagogiaandroid;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CargarHistorialActivity extends AppCompatActivity {

    private AutoCompleteTextView selectorTipo;
    private EditText selectorFecha;
    private EditText descripcion;

    private ArrayList<Historial> historial;
    private ArrayList<Paciente> pacientes;
    private Paciente pacienteSeleccionado;
    private Historial historialItem;
    private int indice = -1;
    private boolean modoEdicion = false;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_historial);

        selectorFecha = findViewById(R.id.fechaHistorial);
        selectorTipo = findViewById(R.id.tipoRegistro);
        descripcion = findViewById(R.id.descripcionHistorial);
        Button btnGuardar = findViewById(R.id.btnGuardarHistorial);
        ImageButton btnBackHistorial = findViewById(R.id.btnBackHistorial);

        historial = (ArrayList<Historial>) getIntent().getSerializableExtra("historial");
        pacientes = (ArrayList<Paciente>) getIntent().getSerializableExtra("pacientes");
        pacienteSeleccionado = (Paciente) getIntent().getSerializableExtra("pacienteSeleccionado");
        historialItem = (Historial) getIntent().getSerializableExtra("historialItem");
        indice = getIntent().getIntExtra("indice", -1);

        if (historial == null) {
            historial = new ArrayList<>();
        }
        if (pacientes == null) {
            pacientes = new ArrayList<>();
        }

        if (pacienteSeleccionado == null && historialItem != null && historialItem.getPaciente() != null) {
            pacienteSeleccionado = historialItem.getPaciente();
        }

        modoEdicion = historialItem != null && indice >= 0 && indice < historial.size();

        String[] tipos = {
                "Sesión",
                "Primera entrevista",
                "Reunión con familia",
                "Reunión con escuela",
                "Reunión con terapeuta"
        };
        ArrayAdapter<String> adaptTipo = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, tipos);
        selectorTipo.setAdapter(adaptTipo);
        selectorTipo.setOnClickListener(v -> selectorTipo.showDropDown());

        selectorFecha.setOnClickListener(v -> mostrarDatePicker());

        if (modoEdicion && historialItem != null) {
            if (historialItem.getFecha() != null) {
                selectorFecha.setText(sdf.format(historialItem.getFecha()));
            }
            if (historialItem.getTipoRegistro() != null) {
                selectorTipo.setText(historialItem.getTipoRegistro(), false);
            }
            if (historialItem.getDescripcion() != null) {
                descripcion.setText(historialItem.getDescripcion());
            }
            Button btn = findViewById(R.id.btnGuardarHistorial);
            btn.setText("Editar");
            TextView titulo = findViewById(R.id.tvTituloAgregarHistorial);
            if (titulo != null) {
                titulo.setText("EDITAR HISTORIAL");
            }
        }

        btnGuardar.setOnClickListener(v -> guardarHistorial());
        btnBackHistorial.setOnClickListener(v -> volverAListaHistorial());
    }

    private void mostrarDatePicker() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(
                this,
                R.style.Theme_PsicopedagogiaAndroid_DatePicker,
                (view, year, month, dayOfMonth) -> {
                    String txt = String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%04d",
                            dayOfMonth,
                            month + 1,
                            year
                    );
                    selectorFecha.setText(txt);
                },
                y, m, d
        );

        dp.getDatePicker().setMaxDate(System.currentTimeMillis());
        dp.show();

        int azul = android.graphics.Color.parseColor("#3d5a80");
        dp.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(azul);
        dp.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(azul);
        dp.getButton(DatePickerDialog.BUTTON_POSITIVE).setText("OK");
        dp.getButton(DatePickerDialog.BUTTON_NEGATIVE).setText("Cancelar");
    }

    private void guardarHistorial() {
        String tTexto = selectorTipo.getText().toString().trim();
        String fTexto = selectorFecha.getText().toString().trim();
        String desc = descripcion.getText().toString().trim();

        StringBuilder errores = new StringBuilder();

        if (pacienteSeleccionado == null) {
            errores.append("• No se encontró un paciente válido para asociar el historial\n");
        }
        if (tTexto.isEmpty()) {
            errores.append("• Debés seleccionar un tipo de registro\n");
        }
        if (fTexto.isEmpty()) {
            errores.append("• Debés seleccionar una fecha\n");
        }
        if (desc.isEmpty()) {
            errores.append("• La descripción es obligatoria\n");
        }

        Date fecha = null;
        if (!fTexto.isEmpty()) {
            try {
                fecha = sdf.parse(fTexto);
            } catch (Exception e) {
                errores.append("• La fecha no tiene un formato válido (dd/MM/aaaa)\n");
            }
        }

        if (errores.length() > 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Revisar datos")
                    .setMessage(errores.toString())
                    .setPositiveButton("Aceptar", null)
                    .show();
            return;
        }

        if (modoEdicion && historialItem != null && indice >= 0 && indice < historial.size()) {
            historialItem.setPaciente(pacienteSeleccionado);
            historialItem.setFecha(fecha);
            historialItem.setTipoRegistro(tTexto);
            historialItem.setDescripcion(desc);
            historial.set(indice, historialItem);
        } else {
            Historial h = new Historial(pacienteSeleccionado, fecha, tTexto, desc);
            historial.add(h);
        }

        new AlertDialog.Builder(this)
                .setTitle("Guardado")
                .setMessage("El registro fue guardado exitosamente.")
                .setPositiveButton("OK", (d, w) -> volverAListaHistorial())
                .show();
    }

    private void volverAListaHistorial() {
        Intent i = new Intent(this, ListaHistorialActivity.class);
        i.putExtra("historial", historial);
        i.putExtra("pacientes", pacientes);
        if (pacienteSeleccionado != null) {
            i.putExtra("pacienteSeleccionado", pacienteSeleccionado);
        }
        startActivity(i);
        finish();
    }
}
