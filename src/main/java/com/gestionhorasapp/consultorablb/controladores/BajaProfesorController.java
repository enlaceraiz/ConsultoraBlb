package com.gestionhorasapp.consultorablb.controladores;

import com.gestionhorasapp.consultorablb.entidades.Profesor;
import com.gestionhorasapp.consultorablb.repository.ProfesorRepository;
import com.gestionhorasapp.consultorablb.util.JPAUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import jakarta.persistence.EntityManager;

import java.util.List;

public class BajaProfesorController {

    @FXML
    private ComboBox<Profesor> profesorComboBox;

    @FXML
    private Button eliminarButton;

    private ProfesorRepository profesorRepository;

    public BajaProfesorController() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        profesorRepository = new ProfesorRepository(entityManager);
    }

    @FXML
    public void initialize() {
        cargarProfesores();
    }

    private void cargarProfesores() {
        List<Profesor> profesores = profesorRepository.listarProfesores();
        ObservableList<Profesor> profesoresObservable = FXCollections.observableArrayList(profesores);
        profesorComboBox.setItems(profesoresObservable);

        // Configuración para mostrar nombre y apellido del profesor en el ComboBox
        profesorComboBox.setConverter(new javafx.util.StringConverter<Profesor>() {
            @Override
            public String toString(Profesor profesor) {
                if (profesor != null) {
                    return profesor.getNombre() + " " + profesor.getApellido();
                }
                return "";
            }

            @Override
            public Profesor fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    public void eliminarProfesor() {
        Profesor profesorSeleccionado = profesorComboBox.getValue();

        if (profesorSeleccionado == null) {
            mostrarAlerta("Error", "Debes seleccionar un profesor para eliminar.");
            return;
        }

        try {
            profesorRepository.eliminarProfesor(profesorSeleccionado);
            mostrarAlerta("Éxito", "Profesor eliminado correctamente.");
            cargarProfesores(); // Recarga la lista de profesores después de eliminar
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al eliminar el profesor.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}