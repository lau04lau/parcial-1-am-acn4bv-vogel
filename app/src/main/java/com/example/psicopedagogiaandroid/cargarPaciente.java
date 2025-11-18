package com.example.psicopedagogiaandroid;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class cargarPaciente extends AppCompatActivity {

    private AutoCompleteTextView nivelEdu;
    private AutoCompleteTextView gradoCurso;
    private EditText fechaNac;
    private EditText nombre;
    private EditText apellido;
    private EditText dni;
    private EditText telefono;
    private EditText motivoConsulta;

    private ArrayList<Paciente> pacientes;

    private Paciente pacienteEditar;
    private int indiceEditar = -1;
    private boolean modoEdicion = false;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_paciente);

        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        dni = findViewById(R.id.dni);
        telefono = findViewById(R.id.telefono);
        fechaNac = findViewById(R.id.fechaNac);
        motivoConsulta = findViewById(R.id.motivoconsulta);
        nivelEdu = findViewById(R.id.nivelEdu);
        gradoCurso = findViewById(R.id.curso);
        Button btnAgregar = findViewById(R.id.btnAgregar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get("pacientes") instanceof ArrayList) {
            pacientes = (ArrayList<Paciente>) bundle.get("pacientes");
        } else {
            pacientes = new ArrayList<>();
        }

        if (bundle != null) {
            pacienteEditar = (Paciente) bundle.getSerializable("paciente");
            indiceEditar = bundle.getInt("indice", -1);
            modoEdicion = pacienteEditar != null && indiceEditar >= 0 && indiceEditar < pacientes.size();
        }

        String[] niveles = new String[]{"Inicial", "Primaria", "Secundaria", "Terciario"};
        ArrayAdapter<String> adaptNivel = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, niveles);
        nivelEdu.setAdapter(adaptNivel);
        nivelEdu.setOnClickListener(v -> nivelEdu.showDropDown());

        String[] cursos = new String[]{"1", "2", "3", "4", "5", "6", "7"};
        ArrayAdapter<String> adaptCurso = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, cursos);
        gradoCurso.setAdapter(adaptCurso);
        gradoCurso.setOnClickListener(v -> gradoCurso.showDropDown());

        fechaNac.setOnClickListener(v -> mostrarDatePicker());

        if (modoEdicion && pacienteEditar != null) {
            nombre.setText(pacienteEditar.getNombre());
            apellido.setText(pacienteEditar.getApellido());
            dni.setText(pacienteEditar.getDni());
            telefono.setText(pacienteEditar.getTelefono());
            motivoConsulta.setText(pacienteEditar.getMotivoConsulta());
            if (pacienteEditar.getNivelEducativo() != null) {
                nivelEdu.setText(pacienteEditar.getNivelEducativo(), false);
            }
            if (pacienteEditar.getGradoCurso() != 0) {
                gradoCurso.setText(String.valueOf(pacienteEditar.getGradoCurso()), false);
            }
            if (pacienteEditar.getFechaNac() != null) {
                fechaNac.setText(sdf.format(pacienteEditar.getFechaNac()));
            }
            btnAgregar.setText("Guardar cambios");
        }

        btnAgregar.setOnClickListener(v -> agregarPaciente());
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
                    fechaNac.setText(txt);
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
        dp.getButton(DatePickerDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
        dp.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
    }

    private void agregarPaciente() {
        String vNombre = nombre.getText().toString().trim();
        String vApellido = apellido.getText().toString().trim();
        String vDni = dni.getText().toString().trim();
        String vTelefono = telefono.getText().toString().trim();
        String vFecha = fechaNac.getText().toString().trim();
        String vNivel = nivelEdu.getText().toString().trim();
        String vGrado = gradoCurso.getText().toString().trim();
        String vMotivo = motivoConsulta.getText().toString().trim();

        StringBuilder errores = new StringBuilder();

        if (vNombre.isEmpty()) errores.append("• El nombre es obligatorio\n");
        if (vApellido.isEmpty()) errores.append("• El apellido es obligatorio\n");
        if (vDni.isEmpty()) errores.append("• El DNI es obligatorio\n");
        if (vTelefono.isEmpty()) errores.append("• El teléfono es obligatorio\n");
        if (vFecha.isEmpty()) errores.append("• La fecha de nacimiento es obligatoria\n");
        if (vNivel.isEmpty()) errores.append("• El nivel educativo es obligatorio\n");
        if (vGrado.isEmpty()) errores.append("• El grado/curso es obligatorio\n");
        if (vMotivo.isEmpty()) errores.append("• El motivo de consulta es obligatorio\n");

        String vDniDigits = vDni.replaceAll("\\D", "");
        if (!vDniDigits.isEmpty()) {
            boolean dniExiste = false;
            for (int i = 0; i < pacientes.size(); i++) {
                if (modoEdicion && i == indiceEditar) continue;
                Paciente px = pacientes.get(i);
                if (px != null && px.getDni() != null) {
                    String pd = px.getDni().replaceAll("\\D", "");
                    if (!pd.isEmpty() && pd.equals(vDniDigits)) {
                        dniExiste = true;
                        break;
                    }
                }
            }
            if (dniExiste) errores.append("• El DNI ya está registrado\n");
        }

        Date fecha = null;
        if (!vFecha.isEmpty()) {
            try {
                fecha = sdf.parse(vFecha);
            } catch (ParseException e) {
                errores.append("• La fecha no tiene formato válido (dd/MM/aaaa)\n");
            }
        }

        int grado = 0;
        if (!vGrado.isEmpty()) {
            try {
                grado = Integer.parseInt(vGrado);
            } catch (NumberFormatException e) {
                errores.append("• El grado debe ser numérico\n");
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

        Paciente p;
        if (modoEdicion && pacienteEditar != null) {
            p = pacienteEditar;
        } else {
            p = new Paciente();
        }

        try {
            p.setNombre(vNombre);
            p.setApellido(vApellido);
            p.setDni(vDniDigits.isEmpty() ? vDni : vDniDigits);
            p.setTelefono(vTelefono);
            p.setFechaNac(fecha);
            p.setMotivoConsulta(vMotivo);
            p.setGradoCurso(grado);
            p.setNivelEducativo(vNivel);
        } catch (IllegalArgumentException ex) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(ex.getMessage())
                    .setPositiveButton("Aceptar", null)
                    .show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(modoEdicion ? "Confirmar cambios" : "Confirmar paciente")
                .setMessage(
                        "Nombre: " + vNombre + "\n" +
                                "Apellido: " + vApellido + "\n" +
                                "DNI: " + vDni + "\n" +
                                "Teléfono: " + vTelefono + "\n" +
                                "Fecha: " + vFecha + "\n" +
                                "Nivel educativo: " + vNivel + "\n" +
                                "Grado: " + vGrado + "\n" +
                                "Motivo: " + vMotivo
                )
                .setPositiveButton("Guardar", (dialog, which) -> {
                    if (modoEdicion && indiceEditar >= 0 && indiceEditar < pacientes.size()) {
                        pacientes.set(indiceEditar, p);
                        Toast.makeText(this, "Paciente actualizado", Toast.LENGTH_SHORT).show();
                    } else {
                        pacientes.add(p);
                        Toast.makeText(this, "Paciente guardado", Toast.LENGTH_SHORT).show();
                    }
                    Intent i = new Intent(this, ListaPacientesActivity.class);
                    i.putExtra("pacientes", pacientes);
                    startActivity(i);
                    finish();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
