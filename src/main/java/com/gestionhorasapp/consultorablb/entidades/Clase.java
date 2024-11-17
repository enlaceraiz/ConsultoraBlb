package com.gestionhorasapp.consultorablb.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "clases")  // Asegúrate de que Hibernate mapee esta clase a la tabla 'clases'
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase")  // La columna primaria en la tabla 'clases'
    private Long id;

    @Column(name = "duracion")  // La columna 'duracion' está en la tabla 'clases'
    private int duracion;

    // Relación con Alumno (id_alumno es la clave foránea en la tabla 'clases')
    @ManyToOne
    @JoinColumn(name = "id_alumno")  // Mapeamos la columna de clave foránea
    private Alumno alumno;

    // Relación con Profesor (id_profesor es la clave foránea en la tabla 'clases')
    @ManyToOne
    @JoinColumn(name = "id_profesor")
    private Profesor profesor;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    // Acceder al campo 'grupo' a través de la relación con 'Alumno'
    public String getGrupo() {
        return alumno != null ? alumno.getGrupo() : null;
    }

    // Acceder al campo 'empresa' a través de la relación con 'Alumno'
    public String getEmpresa() {
        return alumno != null ? alumno.getEmpresa() : null;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
}