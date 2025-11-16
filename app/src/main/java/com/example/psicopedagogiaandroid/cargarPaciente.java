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
    private LinearLayout agregadosContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        pacientes = (ArrayList<Paciente>) bundle.get("pacientes");
        if (bundle != null) pacientes = (ArrayList<Paciente>) bundle.get("pacientes");
        if (pacientes == null) pacientes = new ArrayList<>();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cargar_paciente);
        agregadosContainer = findViewById(R.id.agregadoslineal);
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


        try {
            p.setNombre(nombre.getText().toString());
            p.setApellido(apellido.getText().toString());
            p.setDni(dni.getText().toString());
            p.setTelefono(telefono.getText().toString());
            p.setMotivoConsulta(motivoConsulta.getText().toString());
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle("Datos inválidos")
                    .setMessage("Revise que los valores seleccionados sean correctos o no estén vacíos")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        String vNombre = nombre.getText().toString().trim();
        String vApellido = apellido.getText().toString().trim();
        String vDni = dni.getText().toString().replaceAll("\\D","");
        String vTelefono = telefono.getText().toString().trim();
        String vMotivo = motivoConsulta.getText().toString().trim();

        StringBuilder errores = new StringBuilder();
        if (!vNombre.matches("[A-Za-zÁÉÍÓÚÑáéíóúñ\\s'-]{2,}")) errores.append("• Nombre inválido\n");
        if (!vApellido.matches("[A-Za-zÁÉÍÓÚÑáéíóúñ\\s'-]{2,}")) errores.append("• Apellido inválido\n");
        if (!vDni.matches("\\d{7,10}")) errores.append("• DNI inválido\n");
        else {
            boolean dniExiste = false;
            if (pacientes != null) {
                for (Paciente px : pacientes) {
                    String pd = px != null && px.getDni() != null ? px.getDni().replaceAll("\\D","") : "";
                    if (vDni.equals(pd)) { dniExiste = true; break; }
                }
            }
            if (dniExiste) errores.append("• DNI ya registrado\n");
        }
        String telDigits = vTelefono.replaceAll("\\D","");
        if (telDigits.length() < 7 || telDigits.length() > 20) errores.append("• Teléfono inválido\n");
        if (vMotivo.length() < 3) errores.append("• Motivo de consulta inválido\n");

        if (errores.length() > 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Datos inválidos")
                    .setMessage(errores.toString())
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }


        int grado;
        try {
            String gradoTxt = gradoCurso.getText().toString().trim();
            grado = Integer.parseInt(gradoTxt);
        } catch (NumberFormatException e) {
            gradoCurso.setError("Ingrese un numero entero");
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
                fechaNacimiento.setError("Fecha invalida (dd/MM/yyyy)");
                return;
            }

                if (fecha != null && fecha.after(new Date())) {
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
                        android.content.Intent i = new android.content.Intent(this, ListaPacientesActivity.class);
                        i.putExtra("pacientes", pacientes);
                        startActivity(i);
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

        private void renderTabla() {
            if (agregadosContainer == null) return;
            agregadosContainer.removeAllViews();

            int colorBorde = android.graphics.Color.parseColor("#edf8f9");

            TableLayout table = new TableLayout(this);
            table.setStretchAllColumns(true);
            table.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            table.setPadding(2, 2, 2, 2);
            table.setBackgroundColor(colorBorde);


            TableRow header = new TableRow(this);
            header.setBackgroundColor(android.graphics.Color.parseColor("#3d5a80"));
            String[] hs = {"Nombre", "Apellido", "DNI"};
            for (String h : hs) {
                TextView tv = new TextView(this);
                tv.setText(h);
                tv.setPadding(8, 8, 8, 8);
                tv.setTextColor(colorBorde);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                header.addView(tv);
            }
            table.addView(header);

            View headerDivider = new View(this);
            headerDivider.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
            headerDivider.setBackgroundColor(colorBorde);
            table.addView(headerDivider);



            if (pacientes != null) {
                for (Paciente p : pacientes) {
                    TableRow row = new TableRow(this);
                    row.setBackgroundColor(android.graphics.Color.parseColor("#3d5a80"));
                    row.setPadding(1, 1, 1, 1);

                    String nombre = p.getNombre() != null ? p.getNombre() : "";
                    String apellido = p.getApellido() != null ? p.getApellido() : "";
                    String dni = p.getDni() != null ? p.getDni() : "";

                    String[] cols = {nombre, apellido, dni};
                    for (String c : cols) {
                        TextView tv = new TextView(this);
                        tv.setText(c);
                        tv.setPadding(8, 8, 8, 8);
                        tv.setTextColor(colorBorde);
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        row.addView(tv);
                    }


                    View divider = new View(this);
                    divider.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT, 1));
                    divider.setBackgroundColor(colorBorde);

                    table.addView(row);
                    table.addView(divider);
                }
            }

            agregadosContainer.addView(table);
        }


    }