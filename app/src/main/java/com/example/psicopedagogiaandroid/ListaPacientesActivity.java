import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ListaPacientesActivity extends AppCompatActivity {

    private ArrayList<Paciente> pacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pacientes);

        pacientes = (ArrayList<Paciente>) getIntent().getSerializableExtra("pacientes");
        if (pacientes == null) pacientes = new ArrayList<>();
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {

            ImageButton btnAdd = findViewById(R.id.btnAddPatient);
            btnAdd.setOnClickListener(v -> {
                Intent i = new Intent(this, cargarPaciente.class);
                i.putExtra("pacientes", pacientes);
                startActivity(i);
                finish();
            });

            renderTabla();
        }

        private void renderTabla() {

            TableLayout table = findViewById(R.id.tablePacientes);
            table.removeAllViews();

            int colorTexto = android.graphics.Color.parseColor("#edf8f9");
            int colorFondo = android.graphics.Color.parseColor("#3d5a80");
            int colorBorde = android.graphics.Color.parseColor("#edf8f9");


            TableRow header = new TableRow(this);
            header.setBackgroundColor(colorFondo);
            String[] hs = {"Nombre", "Apellido", "Teléfono"};
            for (String h : hs) {
                TextView tv = new TextView(this);
                tv.setText(h);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(colorTexto);
                tv.setPadding(8, 8, 8, 8);
                tv.setTextAlignment(android.view.View.TEXT_ALIGNMENT_CENTER);
                header.addView(tv);
            }
            table.addView(header);


                    android.view.View separator = new android.view.View(this);
                    separator.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    separator.setBackgroundColor(colorBorde);
                    table.addView(separator);

                    for (Paciente p : pacientes) {
                        TableRow row = new TableRow(this);
                        row.setBackgroundColor(colorFondo);
                        row.setPadding(1, 1, 1, 1);

                        String nombre = p.getNombre() != null ? p.getNombre() : "";
                        String apellido = p.getApellido() != null ? p.getApellido() : "";
                        String telefono = p.getTelefono() != null ? p.getTelefono() : "";

                        String[] cols = {nombre, apellido, telefono};
                        for (int i = 0; i < cols.length; i++) {
                            TextView tv = new TextView(this);
                            tv.setText(cols[i]);
                            tv.setTextColor(colorTexto);
                            tv.setPadding(8, 8, 8, 8);
                            tv.setTextAlignment(android.view.View.TEXT_ALIGNMENT_CENTER);
                            row.addView(tv);
                            if (i == 0) {
                                final Paciente seleccionado = p;
                                tv.setOnClickListener(v -> {
                                    Intent d = new Intent(this, DetallePacienteActivity.class);
                                    d.putExtra("paciente", seleccionado);
                                    startActivity(d);
                                });
                            }
                        }

                        android.view.View divider = new android.view.View(this);
                        divider.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                        divider.setBackgroundColor(colorBorde);

                        table.addView(row);
                        table.addView(divider);
                    }
                }
            }