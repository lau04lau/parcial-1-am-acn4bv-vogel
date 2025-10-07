package com.example.psicopedagogiaandroid;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Objects;

public class Paciente  {

    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private Date fechaNac;
    private String motivoConsulta;
    private int gradoCurso;
    private String nivelEducativo;

    public Paciente() {

    }

    public Paciente(String nombre,
                    String apellido,
                    String dni,
                    String telefono,
                    Date fechaNac,
                    String motivoConsulta,
                    int gradoCurso,
                    String nivelEducativo) {

        setNombre(nombre);
        setApellido(apellido);
        setDni(dni);
        setTelefono(telefono);
        setFechaNac(fechaNac);
        setMotivoConsulta(motivoConsulta);
        setGradoCurso(gradoCurso);
        setNivelEducativo(nivelEducativo);

    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() { return apellido; }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        this.apellido = apellido.trim();
    }

    public String getDni() { return dni; }

    public void setDni(String dni) {
        if (dni == null || !dni.matches("\\d{7,10}")) {
            throw new IllegalArgumentException("DNI inválido (debe contener 7 a 10 dígitos)");
        }
        this.dni = dni;
    }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) {
        if (telefono == null || !telefono.matches("[+\\d()\\s-]{6,20}")) {
            throw new IllegalArgumentException("Teléfono inválido");
        }
        this.telefono = telefono.trim();
    }

    public Date getFechaNac() { return fechaNac; }

    public void setFechaNac(Date fechaNac) {
        if (fechaNac != null && fechaNac.after(new Date())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
        }
        this.fechaNac = fechaNac;
    }

    public String getMotivoConsulta() { return motivoConsulta; }

    public void setMotivoConsulta(String motivoConsulta) {
        if (motivoConsulta == null) motivoConsulta = "";
        motivoConsulta = motivoConsulta.trim();
        if (motivoConsulta.length() > 255) {
            throw new IllegalArgumentException("El motivo de consulta es demasiado largo");
        }
        this.motivoConsulta = motivoConsulta;
    }

    public int getGradoCurso() { return gradoCurso; }

    public void setGradoCurso(int gradoCurso) {
        if (gradoCurso < 0 || gradoCurso > 12) {
            throw new IllegalArgumentException("Grado/curso fuera de rango (0 a 12)");
        }
        this.gradoCurso = gradoCurso;
    }

    public String getNivelEducativo() { return nivelEducativo; }

    public void setNivelEducativo(String nivelEducativo) {
        if (nivelEducativo == null || nivelEducativo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nivel educativo no puede estar vacío");
        }
        this.nivelEducativo = nivelEducativo.trim();
    }


    @Override
    public String toString() {
        // Útil para logs y depuración
        return "Paciente{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                ", fechaNac=" + (fechaNac != null ? fechaNac : "null") +
                ", motivoConsulta='" + motivoConsulta + '\'' +
                ", gradoCurso=" + gradoCurso +
                ", nivelEducativo='" + nivelEducativo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente)) return false;
        Paciente paciente = (Paciente) o;
        return Objects.equals(dni, paciente.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

    protected Paciente(Parcel in) {
        nombre = in.readString();
        apellido = in.readString();
        dni = in.readString();
        telefono = in.readString();
        long fechaMillis = in.readLong();
        fechaNac = (fechaMillis == -1L) ? null : new Date(fechaMillis);
        motivoConsulta = in.readString();
        gradoCurso = in.readInt();
        nivelEducativo = in.readString();
    }




}
