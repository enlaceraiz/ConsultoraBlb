package com.gestionhorasapp.consultorablb.repository;

import com.gestionhorasapp.consultorablb.entidades.Profesor;

import jakarta.persistence.EntityManager;
import java.util.List;

public class ProfesorRepository {
    private EntityManager entityManager;

    public ProfesorRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Profesor> listarProfesores() {
        String query = "SELECT p FROM Profesor p";
        return entityManager.createQuery(query, Profesor.class).getResultList();
    }

    public void guardarProfesor(Profesor profesor) {
        entityManager.getTransaction().begin();
        entityManager.persist(profesor);
        entityManager.getTransaction().commit();
    }
    public void eliminarProfesor(Profesor profesor) {
        entityManager.getTransaction().begin();
        profesor = entityManager.find(Profesor.class, profesor.getId());
        if (profesor != null) {
            entityManager.remove(profesor);
        }
        entityManager.getTransaction().commit();
    }
    public void actualizarProfesor(Profesor profesor) {
        entityManager.getTransaction().begin();
        entityManager.merge(profesor);
        entityManager.getTransaction().commit();
    }

}