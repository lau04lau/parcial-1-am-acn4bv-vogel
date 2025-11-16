package com.example.psicopedagogiaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListaPacientesActivity extends AppCompatActivity {

    private ArrayList<Paciente> pacientes;
    private LinearLayout contenedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pacientes);
        contenedor = findViewById(R.id.listaContainer);
        pacientes = (ArrayList<Paciente>) getIntent().getSerializableExtra("pacientes");
        if (pacientes == null) pacientes = new ArrayList<>();
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            Intent i = new Intent(this, cargarPaciente.class);
            i.putExtra("pacientes", pacientes);
            startActivity(i);
        });
        renderTabla();
    }

    private void renderTabla() {
        if (contenedor == null) return;
        contenedor.removeAllViews();

        int colorBorde = android.graphics.Color.parseColor("#edf8f9");

        TextView title = new TextView(this);
        title.setText("Listado de Pacientes");
        title.setTextColor(colorBorde);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(null, Typeface.BOLD);
        title.setPadding(8, 8, 8, 24);
        title.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        contenedor.addView(title);

        TableLayout table = new TableLayout(this);
        table.setStretchAllColumns(true);
        table.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        table.setPadding(2, 2, 2, 2);
        table.setBackgroundColor(colorBorde);

        TableRow header = new TableRow(this);
        header.setBackgroundColor(android.graphics.Color.parseColor("#3d5a80"));
        String[] hs = {"Nombre","Apellido","Teléfono"};
        for (String h : hs) {
            TextView tv = new TextView(this);
            tv.setText(h);
            tv.setPadding(8,8,8,8);
            tv.setTextColor(colorBorde);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            header.addView(tv);
        }
        table.addView(header);

        View headerDivider = new View(this);
        int stroke = (int) Math.ceil(2 * getResources().getDisplayMetrics().density);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, stroke);
        headerDivider.setLayoutParams(lp);
        headerDivider.setBackgroundColor(colorBorde);
        table.addView(headerDivider);

        if (pacientes != null) {
            for (Paciente p : pacientes) {
                TableRow row = new TableRow(this);
                row.setBackgroundColor(android.graphics.Color.parseColor("#3d5a80"));
                row.setPadding(1,1,1,1);

                String nombre = p.getNombre() != null ? p.getNombre() : "";
                String apellido = p.getApellido() != null ? p.getApellido() : "";
                String telefono = p.getTelefono() != null ? p.getTelefono() : "";

                TextView tvNombre = new TextView(this);
                tvNombre.setText(nombre);
                tvNombre.setPadding(8,8,8,8);
                tvNombre.setTextColor(colorBorde);
                tvNombre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvNombre.setOnClickListener(v -> {
                    Intent i = new Intent(this, DetallePacienteActivity.class);
                    i.putExtra("paciente", p);
                    startActivity(i);
                });
                row.addView(tvNombre);

                TextView tvApellido = new TextView(this);
                tvApellido.setText(apellido);
                tvApellido.setPadding(8,8,8,8);
                tvApellido.setTextColor(colorBorde);
                tvApellido.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                row.addView(tvApellido);

                TextView tvTelefono = new TextView(this);
                tvTelefono.setText(telefono);
                tvTelefono.setPadding(8,8,8,8);
                tvTelefono.setTextColor(colorBorde);
                tvTelefono.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                row.addView(tvTelefono);

                View divider = new View(this);
                divider.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, stroke));
                divider.setBackgroundColor(colorBorde);

                table.addView(row);
                table.addView(divider);
            }
        }
        contenedor.addView(table);
    }
}