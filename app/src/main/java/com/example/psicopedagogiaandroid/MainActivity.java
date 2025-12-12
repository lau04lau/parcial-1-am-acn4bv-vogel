package com.example.psicopedagogiaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";

    private final ArrayList<Paciente> pacientes = new ArrayList<>();
    private final ArrayList<Historial> historial = new ArrayList<>();

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Log.d(TAG, "No hay sesión, redirigiendo a Login");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    public void btnPacientes(View v) {
        Intent i = new Intent(MainActivity.this, ListaPacientesActivity.class);
        i.putExtra("pacientes", pacientes);
        i.putExtra("historial", historial);
        startActivity(i);
    }
}
