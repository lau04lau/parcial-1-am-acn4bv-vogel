package com.example.psicopedagogiaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<Paciente> pacientes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnPacientes(View v) {
        Intent i = new Intent(MainActivity.this, ListaPacientesActivity.class);
        i.putExtra("pacientes", pacientes);
        startActivity(i);
    }
}
