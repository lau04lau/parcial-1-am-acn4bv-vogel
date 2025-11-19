package com.example.psicopedagogiaandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DetallePacienteActivity extends AppCompatActivity {

    private Paciente paciente;
    private ArrayList<Paciente> pacientes;
    private ArrayList<Historial> historial;
    private int indice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_paciente);

        Intent intent = getIntent();
        paciente = (Paciente) intent.getSerializableExtra("paciente");
        pacientes = (ArrayList<Paciente>) intent.getSerializableExtra("pacientes");
        historial = (ArrayList<Historial>) intent.getSerializableExtra("historial");
        indice = intent.getIntExtra("indice", -1);

        if (pacientes == null) {
            pacientes = new ArrayList<>();
        }
        if (historial == null) {
            historial = new ArrayList<>();
        }

        TextView tvNombre = findViewById(R.id.tvNombre);
        TextView tvApellido = findViewById(R.id.tvApellido);
        TextView tvDni = findViewById(R.id.tvDni);
        TextView tvTelefono = findViewById(R.id.tvTelefono);
        TextView tvNivel = findViewById(R.id.tvNivel);
        TextView tvCurso = findViewById(R.id.tvCurso);
        TextView tvFecha = findViewById(R.id.tvFecha);
        TextView tvMotivo = findViewById(R.id.tvMotivo);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (paciente != null) {
            tvNombre.setText(paciente.getNombre() != null ? paciente.getNombre() : "");
            tvApellido.setText(paciente.getApellido() != null ? paciente.getApellido() : "");
            tvDni.setText(paciente.getDni() != null ? paciente.getDni() : "");
            tvTelefono.setText(paciente.getTelefono() != null ? paciente.getTelefono() : "");
            tvNivel.setText(paciente.getNivelEducativo() != null ? paciente.getNivelEducativo() : "");
            tvCurso.setText(String.valueOf(paciente.getGradoCurso()));
            if (paciente.getFechaNac() != null) {
                tvFecha.setText(sdf.format(paciente.getFechaNac()));
            } else {
                tvFecha.setText("");
            }
            tvMotivo.setText(paciente.getMotivoConsulta() != null ? paciente.getMotivoConsulta() : "");
        }

        View btnAtras = findViewById(R.id.fabAtras);
        View btnHist = findViewById(R.id.fabHistorial);
        View btnEditar = findViewById(R.id.btnEditar);
        View btnEliminar = findViewById(R.id.btnEliminar);

        btnAtras.setOnClickListener(v -> volverALista());
        btnHist.setOnClickListener(this::onVerHistorial);
        btnEditar.setOnClickListener(this::onEditar);
        btnEliminar.setOnClickListener(this::onEliminar);
    }

    private void volverALista() {
        Intent i = new Intent(this, ListaPacientesActivity.class);
        i.putExtra("pacientes", pacientes);
        i.putExtra("historial", historial);
        startActivity(i);
        finish();
    }

    public void onVerHistorial(View v) {
        if (paciente == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Sin paciente")
                    .setMessage("No se encontró el paciente para ver su historial.")
                    .setPositiveButton("Aceptar", null)
                    .show();
            return;
        }
        Intent i = new Intent(this, ListaHistorialActivity.class);
        i.putExtra("historial", historial);
        i.putExtra("pacientes", pacientes);
        i.putExtra("pacienteSeleccionado", paciente);
        startActivity(i);
        finish();
    }

    public void onEditar(View v) {
        if (paciente == null) {
            return;
        }
        Intent i = new Intent(this, cargarPaciente.class);
        i.putExtra("pacientes", pacientes);
        i.putExtra("paciente", paciente);
        i.putExtra("indice", indice);
        startActivity(i);
        finish();
    }

    public void onEliminar(View v) {
        if (indice < 0 || indice >= pacientes.size()) {
            volverALista();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Eliminar paciente")
                .setMessage("¿Deseás eliminar a este paciente?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    pacientes.remove(indice);
                    volverALista();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
