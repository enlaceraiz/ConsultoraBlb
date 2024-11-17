package com.gestionhorasapp.consultorablb.repository;

import com.gestionhorasapp.consultorablb.entidades.Clase;
import com.gestionhorasapp.consultorablb.entidades.Profesor;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ClaseRepository {

    private final EntityManager entityManager;

    public ClaseRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean estaDisponible(Profesor profesor, LocalDate fecha, LocalTime hora) {
        String query = "SELECT c FROM Clase c WHERE c.profesor = :profesor AND c.fecha = :fecha AND c.hora = :hora";
        List<Clase> clases = entityManager.createQuery(query, Clase.class)
                .setParameter("profesor", profesor)
                .setParameter("fecha", fecha)
                .setParameter("hora", hora)
                .getResultList();
        return clases.isEmpty(); // Devuelve true si no hay clases en ese horario
    }

    public void guardarClase(Clase clase) {
        entityManager.getTransaction().begin();
        entityManager.persist(clase);
        entityManager.getTransaction().commit();
    }

    public List<Clase> buscarClasesPorAlumno(Long alumnoId) {
        String query = "SELECT c FROM Clase c WHERE c.alumno.id = :alumnoId";
        return entityManager.createQuery(query, Clase.class)
                .setParameter("alumnoId", alumnoId)
                .getResultList();
    }

    public List<Clase> buscarClasesPorProfesor(Long profesorId) {
        String query = "SELECT c FROM Clase c WHERE c.profesor.id = :profesorId";
        return entityManager.createQuery(query, Clase.class)
                .setParameter("profesorId", profesorId)
                .getResultList();
    }
    public List<Clase> listarClases() {
        return entityManager.createQuery("SELECT c FROM Clase c", Clase.class).getResultList();
    }

    public void eliminarClase(Clase clase) {
        entityManager.getTransaction().begin();
        clase = entityManager.find(Clase.class, clase.getId());
        if (clase != null) {
            entityManager.remove(clase);
        }
        entityManager.getTransaction().commit();
    }
    public List<Clase> listarClasesPorFecha(LocalDate fecha) {
        return entityManager.createQuery(
                        "SELECT c FROM Clase c WHERE c.fecha = :fecha", Clase.class)
                .setParameter("fecha", fecha)
                .getResultList();
    }


}
