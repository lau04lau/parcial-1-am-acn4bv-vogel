package com.example.psicopedagogiaandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetallePacienteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_paciente);

        Paciente p = (Paciente) getIntent().getSerializableExtra("paciente");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        ((TextView) findViewById(R.id.tvNombre)).setText(p != null && p.getNombre()!=null ? p.getNombre() : "");
        ((TextView) findViewById(R.id.tvApellido)).setText(p != null && p.getApellido()!=null ? p.getApellido() : "");
        ((TextView) findViewById(R.id.tvDni)).setText(p != null && p.getDni()!=null ? p.getDni() : "");
        ((TextView) findViewById(R.id.tvTelefono)).setText(p != null && p.getTelefono()!=null ? p.getTelefono() : "");
        ((TextView) findViewById(R.id.tvNivel)).setText(p != null && p.getNivelEducativo()!=null ? p.getNivelEducativo() : "");
        ((TextView) findViewById(R.id.tvCurso)).setText(p != null ? String.valueOf(p.getGradoCurso()) : "");
        ((TextView) findViewById(R.id.tvFecha)).setText(p != null && p.getFechaNac()!=null ? sdf.format(p.getFechaNac()) : "");
        ((TextView) findViewById(R.id.tvMotivo)).setText(p != null && p.getMotivoConsulta()!=null ? p.getMotivoConsulta() : "");

        View btnAtras = findViewById(R.id.fabAtras);
        View btnHist = findViewById(R.id.fabHistorial);
        View btnEditar = findViewById(R.id.btnEditar);
        View btnEliminar = findViewById(R.id.btnEliminar);

        btnAtras.setOnClickListener(v -> finish());
        btnHist.setOnClickListener(this::onVerHistorial);
        btnEditar.setOnClickListener(this::onEditar);
        btnEliminar.setOnClickListener(this::onEliminar);
    }

    public void onVerHistorial(View v) { }

    public void onEditar(View v) { }

    public void onEliminar(View v) { }
}
