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
import javafx.scene.control.TextField;

import jakarta.persistence.EntityManager;

public class AltaProfesorController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField apellidoField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> especialidadComboBox;

    @FXML
    private Button guardarButton;

    private ProfesorRepository profesorRepository;

    public AltaProfesorController() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        profesorRepository = new ProfesorRepository(entityManager);
    }

    @FXML
    public void initialize() {
        cargarEspecialidades();
    }

    private void cargarEspecialidades() {
        ObservableList<String> especialidades = FXCollections.observableArrayList(
                "Conversación", "Gramática", "Preparación exámenes"
        );
        especialidadComboBox.setItems(especialidades);
    }

    @FXML
    public void guardarProfesor() {
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String email = emailField.getText();
        String especialidad = especialidadComboBox.getValue();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || especialidad == null) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        Profesor profesor = new Profesor();
        profesor.setNombre(nombre);
        profesor.setApellido(apellido);
        profesor.setEmail(email);
        profesor.setEspecialidad(especialidad);

        try {
            profesorRepository.guardarProfesor(profesor);
            mostrarAlerta("Éxito", "Profesor registrado correctamente.");
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al guardar el profesor.");
        }
    }

    private void limpiarCampos() {
        nombreField.clear();
        apellidoField.clear();
        emailField.clear();
        especialidadComboBox.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}