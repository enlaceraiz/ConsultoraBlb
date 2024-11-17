package com.gestionhorasapp.consultorablb.repository;

import com.gestionhorasapp.consultorablb.entidades.Alumno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import jakarta.persistence.TypedQuery;


public class AlumnoRepository {
    private EntityManager entityManager;

    public AlumnoRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void guardarAlumno(Alumno alumno) {
        entityManager.getTransaction().begin();
        entityManager.persist(alumno);
        entityManager.getTransaction().commit();
    }

    public List<Alumno> listarAlumnos() {
        return entityManager.createQuery("SELECT a FROM Alumno a", Alumno.class).getResultList();
    }

    public List<String> listarEmpresas() {
        String jpql = "SELECT DISTINCT a.empresa FROM Alumno a WHERE a.empresa IS NOT NULL";
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
        return query.getResultList();
    }

    // Método para obtener los grupos únicos según la empresa seleccionada
    public List<String> listarGruposPorEmpresa(String empresa) {
        String jpql = "SELECT DISTINCT a.grupo FROM Alumno a WHERE a.empresa = :empresa AND a.grupo IS NOT NULL";
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
        query.setParameter("empresa", empresa);
        return query.getResultList();
    }

    // Método para filtrar alumnos por empresa y grupo
    public List<Alumno> findByEmpresaAndGrupo(String empresa, String grupo) {
        String jpql = "SELECT a FROM Alumno a WHERE a.empresa = :empresa AND a.grupo = :grupo";
        TypedQuery<Alumno> query = entityManager.createQuery(jpql, Alumno.class);
        query.setParameter("empresa", empresa);
        query.setParameter("grupo", grupo);
        return query.getResultList();
    }

    public List<Alumno> listarAlumnosPorEmpresaYGrupo(String empresa, String grupo) {
        return entityManager.createQuery(
                        "SELECT a FROM Alumno a WHERE a.empresa = :empresa AND a.grupo = :grupo", Alumno.class)
                .setParameter("empresa", empresa)
                .setParameter("grupo", grupo)
                .getResultList();
    }

    public void eliminarAlumno(Alumno alumno) {
        entityManager.getTransaction().begin();
        alumno = entityManager.find(Alumno.class, alumno.getId());
        if (alumno != null) {
            entityManager.remove(alumno);
        }
        entityManager.getTransaction().commit();
    }

    public void actualizarAlumno(Alumno alumno) {
        entityManager.getTransaction().begin();
        entityManager.merge(alumno);
        entityManager.getTransaction().commit();
    }


}



