package cl.stomas.agendauniversitaria.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Asignatura implements Serializable {
    private long id;
    private String nombre;
    private String descripcion;
    private String color;
    private String docente;
    private Semestre semestre;
    private ArrayList<Bloque> bloques;
    private ArrayList<Actividad> actividades;

    public Asignatura(long id, String nombre, String descripcion, String color, String docente, ArrayList<Bloque> bloques, ArrayList<Actividad> actividades) {
        this(id, nombre, descripcion, color, docente);
        this.addBloques(bloques);
        this.addActividades(actividades);
    }

    public Asignatura(String nombre, String descripcion, String color, String docente, ArrayList<Bloque> bloques, ArrayList<Actividad> actividades) {
        this(nombre, descripcion, color, docente);
        this.addBloques(bloques);
        this.addActividades(actividades);
    }

    public Asignatura(long id, String nombre, String descripcion, String color, String docente) {
        this(nombre, descripcion, color, docente);
        this.id = id;
    }

    public Asignatura(String nombre, String descripcion, String color, String docente) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.color = color;
        this.docente = docente;
        this.id = -1;
        this.bloques = new ArrayList<>();
        this.actividades = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public ArrayList<Bloque> getBloques() {
        return bloques;
    }

    public void addBloques(ArrayList<Bloque> bloques) {
        this.bloques.addAll(bloques);
        this.bloques.forEach(new Consumer<Bloque>() {
            @Override
            public void accept(Bloque bloque) {
                bloque.setAsignatura(Asignatura.this);
            }
        });
    }

    public ArrayList<Actividad> getActividades() {
        return actividades;
    }

    public void addActividades(ArrayList<Actividad> actividades) {
        this.actividades.addAll(actividades);
        this.actividades.forEach(new Consumer<Actividad>() {
            @Override
            public void accept(Actividad actividad) {
                actividad.setAsignatura(Asignatura.this);
            }
        });
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }
}
