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

import java.util.List;

public class ModificacionProfesorController {

    @FXML
    private ComboBox<Profesor> profesorComboBox;

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

    public ModificacionProfesorController() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        profesorRepository = new ProfesorRepository(entityManager);
    }

    @FXML
    public void initialize() {
        cargarProfesores();
        cargarEspecialidades();

        // Listener para cargar datos del profesor seleccionado
        profesorComboBox.setOnAction(event -> cargarDatosProfesorSeleccionado());
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

    private void cargarEspecialidades() {
        ObservableList<String> especialidades = FXCollections.observableArrayList(
                "Conversación", "Gramática", "Preparación exámenes"
        );
        especialidadComboBox.setItems(especialidades);
    }

    private void cargarDatosProfesorSeleccionado() {
        Profesor profesorSeleccionado = profesorComboBox.getValue();
        if (profesorSeleccionado != null) {
            nombreField.setText(profesorSeleccionado.getNombre());
            apellidoField.setText(profesorSeleccionado.getApellido());
            emailField.setText(profesorSeleccionado.getEmail());
            especialidadComboBox.setValue(profesorSeleccionado.getEspecialidad());
        }
    }

    @FXML
    public void guardarCambios() {
        Profesor profesorSeleccionado = profesorComboBox.getValue();

        if (profesorSeleccionado == null) {
            mostrarAlerta("Error", "Debes seleccionar un profesor para modificar.");
            return;
        }

        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String email = emailField.getText();
        String especialidad = especialidadComboBox.getValue();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || especialidad == null) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        profesorSeleccionado.setNombre(nombre);
        profesorSeleccionado.setApellido(apellido);
        profesorSeleccionado.setEmail(email);
        profesorSeleccionado.setEspecialidad(especialidad);

        try {
            profesorRepository.actualizarProfesor(profesorSeleccionado);
            mostrarAlerta("Éxito", "Profesor modificado correctamente.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al guardar los cambios.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}