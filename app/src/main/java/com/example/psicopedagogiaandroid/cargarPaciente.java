package com.example.psicopedagogiaandroid;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.ViewGroup;

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
import android.content.Intent;

public class cargarPaciente extends AppCompatActivity {

    private AutoCompleteTextView nivelEdu;
    private EditText fechaNac;
    private EditText nombre;
    private EditText apellido;
    private EditText dni;
    private EditText telefono;
    private EditText fechaNacimiento;
    private EditText motivoConsulta;
    private AutoCompleteTextView gradoCurso;
    private EditText nivelEducativo;
    private ArrayList<Paciente> pacientes;
    private LinearLayout agregadosContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.get("pacientes") instanceof ArrayList) {
            pacientes = (ArrayList<Paciente>) bundle.get("pacientes");
        } else {
            pacientes = new ArrayList<>();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cargar_paciente);
        agregadosContainer = null;
        nivelEdu = findViewById(R.id.nivelEdu);
        fechaNac = findViewById(R.id.fechaNac);
        gradoCurso = findViewById(R.id.curso);

        String[] opciones = {"Inicial", "Primario", "Secundario", "Terciario", "Universitario", "Posgrado"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, opciones);
        nivelEdu.setAdapter(adaptador);
        nivelEdu.setOnClickListener(v -> nivelEdu.showDropDown());

        String[] cursos = {"1", "2", "3", "4", "5", "6", "7"};
        ArrayAdapter<String> adaptCurso = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, cursos);
        gradoCurso.setAdapter(adaptCurso);
        gradoCurso.setOnClickListener(v -> gradoCurso.showDropDown());

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


        String vNombre = nombre.getText() != null ? nombre.getText().toString().trim() : "";
        String vApellido = apellido.getText() != null ? apellido.getText().toString().trim() : "";
        String vDni = dni.getText() != null ? dni.getText().toString().replaceAll("\\D", "") : "";
        String vTelefono = telefono.getText() != null ? telefono.getText().toString().trim() : "";
        String vMotivo = motivoConsulta.getText() != null ? motivoConsulta.getText().toString().trim() : "";
        String vNivel = nivelEducativo.getText() != null ? nivelEducativo.getText().toString().trim() : "";
        String vCurso = gradoCurso.getText() != null ? gradoCurso.getText().toString().trim() : "";
        String vFechaTxt = fechaNacimiento.getText() != null ? fechaNacimiento.getText().toString().trim() : "";

        StringBuilder errores = new StringBuilder();

        if (vNombre.isEmpty() || !vNombre.matches("[A-Za-zÁÉÍÓÚÑáéíóúñ\\s'-]{2,}"))
            errores.append("• Nombre inválido\n");
        if (vApellido.isEmpty() || !vApellido.matches("[A-Za-zÁÉÍÓÚÑáéíóúñ\\s'-]{2,}"))
            errores.append("• Apellido inválido\n");
        if (vDni.isEmpty() || !vDni.matches("\\d{7,10}")) errores.append("• DNI inválido\n");
        if (!vDni.isEmpty()) {
            boolean dniExiste = false;
            if (pacientes != null) {
                for (Paciente px : pacientes) {
                    String pd = px != null && px.getDni() != null ? px.getDni().replaceAll("\\D", "") : "";
                    if (!pd.isEmpty() && vDni.equals(pd)) {
                        dniExiste = true;
                        break;
                    }
                }
            }
            if (dniExiste) errores.append("• DNI ya registrado\n");
        }
        String telDigits = vTelefono.replaceAll("\\D", "");
        if (vTelefono.isEmpty() || telDigits.length() < 7 || telDigits.length() > 20)
            errores.append("• Teléfono inválido\n");
        if (vMotivo.length() < 3) errores.append("• Motivo de consulta inválido\n");
        if (vNivel.isEmpty()) errores.append("• Seleccione el nivel educativo\n");
        if (vCurso.isEmpty() || !vCurso.matches("[1-7]"))
            errores.append("• Seleccione un curso entre 1 y 7\n");
        if (vFechaTxt.isEmpty()) errores.append("• Complete la fecha de nacimiento\n");

        Date fechaValida = null;
        if (!vFechaTxt.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            sdf.setLenient(false);
            try {
                fechaValida = sdf.parse(vFechaTxt);
                if (fechaValida != null && fechaValida.after(new Date())) {
                    errores.append("• La fecha no puede ser futura\n");
                }
            } catch (ParseException e) {
                errores.append("• Fecha inválida (dd/MM/yyyy)\n");
            }
        }

        if (errores.length() > 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Datos inválidos")
                    .setMessage(errores.toString())
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        p.setNombre(vNombre);
        p.setApellido(vApellido);
        p.setDni(vDni);
        p.setTelefono(vTelefono);
        p.setMotivoConsulta(vMotivo);
        p.setNivelEducativo(vNivel);
        p.setGradoCurso(Integer.parseInt(vCurso));
        p.setFechaNac(fechaValida);

        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String resumen =
                "Nombre: " + p.getNombre() + "\n" +
                        "Apellido: " + p.getApellido() + "\n" +
                        "DNI: " + p.getDni() + "\n" +
                        "Telefono: " + p.getTelefono() + "\n" +
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
                    Intent i = new Intent(this, ListaPacientesActivity.class);
                    i.putExtra("pacientes", pacientes);
                    startActivity(i);
                    limpiarFormulario();
                    finish();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

}