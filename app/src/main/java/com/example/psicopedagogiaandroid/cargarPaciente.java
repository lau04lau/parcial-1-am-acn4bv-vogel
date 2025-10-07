package com.example.psicopedagogiaandroid;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class cargarPaciente extends AppCompatActivity {

    private AutoCompleteTextView nivelEdu;
    private EditText fechaNac;
    private EditText nombre;
    private EditText apellido;
    private EditText dni;
    private EditText telefono;
    private EditText fechaNacimiento;
    private EditText motivoConsulta;
    private EditText gradoCurso;
    private EditText nivelEducativo;
    private ArrayList<Paciente> pacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        pacientes = (ArrayList<Paciente>) bundle.get("pacientes");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cargar_paciente);

        nivelEdu = findViewById(R.id.nivelEdu);
        fechaNac = findViewById(R.id.fechaNac);

        String[] opciones = {"Inicial","Primario","Secundario","Terciario","Universitario","Posgrado"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, opciones);
        nivelEdu.setAdapter(adaptador);

        fechaNac.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dp = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String txt = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        fechaNac.setText(txt);
                    },
                    y, m, d
            );
            dp.getDatePicker().setMaxDate(System.currentTimeMillis());
            dp.show();
        });

        findViewById(R.id.btnAgregar).setOnClickListener(v -> agregarPaciente());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void agregarPaciente() {
        Paciente p = new Paciente();
        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        dni = findViewById(R.id.dni);
        telefono = findViewById(R.id.telefono);
        fechaNacimiento = findViewById(R.id.fechaNac);
        motivoConsulta = findViewById(R.id.motivoconsulta);
        gradoCurso = findViewById(R.id.curso);
        nivelEducativo = findViewById(R.id.nivelEdu);

        p.setNombre(nombre.getText().toString());
        p.setApellido(apellido.getText().toString());
        p.setDni(dni.getText().toString());
        p.setTelefono(telefono.getText().toString());
        p.setMotivoConsulta(motivoConsulta.getText().toString());

        int grado;
        try {
            String gradoTxt = gradoCurso.getText().toString().trim();
            grado = Integer.parseInt(gradoTxt);
        } catch (NumberFormatException e) {
            gradoCurso.setError("Ingrese un número entero");
            return;
        }
        p.setGradoCurso(grado);
        p.setNivelEducativo(nivelEducativo.getText().toString());

        String fechaTxt = fechaNacimiento.getText().toString().trim();
        if (!fechaTxt.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            sdf.setLenient(false);
            Date fecha;
            try {
                fecha = sdf.parse(fechaTxt);
            } catch (ParseException e) {
                fechaNacimiento.setError("Fecha inválida (dd/MM/yyyy)");
                return;
            }
            if (fecha.after(new Date())) {
                fechaNacimiento.setError("La fecha no puede ser futura");
                return;
            }
            p.setFechaNac(fecha);
        } else {
            fechaNacimiento.setError("Complete la fecha de nacimiento");
            return;
        }

        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String resumen =
                "Nombre: " + p.getNombre() + "\n" +
                        "Apellido: " + p.getApellido() + "\n" +
                        "DNI: " + p.getDni() + "\n" +
                        "Teléfono: " + p.getTelefono() + "\n" +
                        "Fecha nac.: " + (p.getFechaNac() != null ? out.format(p.getFechaNac()) : "") + "\n" +
                        "Motivo: " + p.getMotivoConsulta() + "\n" +
                        "Nivel educativo: " + p.getNivelEducativo() + "\n" +
                        "Grado/Curso: " + p.getGradoCurso();

        new AlertDialog.Builder(this)
                .setTitle("Confirmar paciente")
                .setMessage(resumen)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    pacientes.add(p);
                    Toast.makeText(this, "Paciente guardado", Toast.LENGTH_SHORT).show();
                    limpiarFormulario();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void limpiarFormulario() {
        nombre.setText("");
        apellido.setText("");
        dni.setText("");
        telefono.setText("");
        fechaNacimiento.setText("");
        motivoConsulta.setText("");
        gradoCurso.setText("");
        nivelEducativo.setText("");
        nivelEdu.setText("", false);
    }
}
