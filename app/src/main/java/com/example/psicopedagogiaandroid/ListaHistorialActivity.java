package com.example.psicopedagogiaandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListaHistorialActivity extends AppCompatActivity {

    private ArrayList<Historial> historial;
    private ArrayList<Paciente> pacientes;
    private Paciente pacienteSeleccionado;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_historial);

        Intent intent = getIntent();
        historial = (ArrayList<Historial>) intent.getSerializableExtra("historial");
        pacientes = (ArrayList<Paciente>) intent.getSerializableExtra("pacientes");
        pacienteSeleccionado = (Paciente) intent.getSerializableExtra("pacienteSeleccionado");

        if (historial == null) {
            historial = new ArrayList<>();
        }
        if (pacientes == null) {
            pacientes = new ArrayList<>();
        }

        ImageButton btnAdd = findViewById(R.id.btnAddHistorial);
        btnAdd.setOnClickListener(v -> {
            Intent i = new Intent(this, CargarHistorialActivity.class);
            i.putExtra("historial", historial);
            i.putExtra("pacientes", pacientes);
            if (pacienteSeleccionado != null) {
                i.putExtra("pacienteSeleccionado", pacienteSeleccionado);
            }
            startActivity(i);
        });

        renderTabla();
    }

    private boolean coincidePaciente(Historial h) {
        if (pacienteSeleccionado == null) {
            return true;
        }
        if (h.getPaciente() == null) {
            return false;
        }
        String dniSel = pacienteSeleccionado.getDni() != null ? pacienteSeleccionado.getDni() : "";
        String dniHist = h.getPaciente().getDni() != null ? h.getPaciente().getDni() : "";
        return !dniSel.isEmpty() && dniSel.equals(dniHist);
    }

    private void renderTabla() {
        TableLayout table = findViewById(R.id.tableHistorial);
        table.removeAllViews();
        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(false);

        int colorTexto = Color.parseColor("#edf8f9");
        int colorFondo = Color.parseColor("#3d5a80");
        int colorBorde = Color.parseColor("#edf8f9");

        TableRow header = new TableRow(this);
        header.setBackgroundColor(colorFondo);

        String[] titulos = {"Paciente", "Fecha", "Tipo"};
        for (String t : titulos) {
            TextView tv = new TextView(this);
            tv.setText(t);
            tv.setTextColor(colorTexto);
            tv.setPadding(8, 8, 8, 8);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new TableRow.LayoutParams(
                    0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            header.addView(tv);
        }

        table.addView(header);

        android.view.View headerDivider = new android.view.View(this);
        headerDivider.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, 1));
        headerDivider.setBackgroundColor(colorBorde);
        table.addView(headerDivider);

        for (int i = 0; i < historial.size(); i++) {
            Historial h = historial.get(i);

            if (!coincidePaciente(h)) {
                continue;
            }

            TableRow row = new TableRow(this);
            row.setBackgroundColor(colorFondo);
            row.setPadding(1, 1, 1, 1);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            String pacienteTxt = "";
            if (h.getPaciente() != null) {
                String nom = h.getPaciente().getNombre() != null ? h.getPaciente().getNombre() : "";
                String ape = h.getPaciente().getApellido() != null ? h.getPaciente().getApellido() : "";
                pacienteTxt = (nom + " " + ape).trim();
            }
            String fechaTxt = h.getFecha() != null ? sdf.format(h.getFecha()) : "";
            String tipoTxt = h.getTipoRegistro() != null ? h.getTipoRegistro() : "";

            String[] cols = {pacienteTxt, fechaTxt, tipoTxt};

            for (int c = 0; c < cols.length; c++) {
                TextView tv = new TextView(this);
                tv.setText(cols[c]);
                tv.setTextColor(colorTexto);
                tv.setPadding(8, 8, 8, 8);
                tv.setGravity(Gravity.CENTER);
                tv.setLayoutParams(new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                row.addView(tv);

                if (c == 0) {
                    final int index = i;
                    tv.setOnClickListener(v -> {
                        Intent d = new Intent(this, DetalleHistorialActivity.class);
                        d.putExtra("historial", historial);
                        d.putExtra("historialItem", historial.get(index));
                        d.putExtra("indice", index);
                        d.putExtra("pacientes", pacientes);
                        startActivity(d);
                        finish();
                    });
                }
            }

            android.view.View divider = new android.view.View(this);
            divider.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, 1));
            divider.setBackgroundColor(colorBorde);

            table.addView(row);
            table.addView(divider);
        }
    }
}
