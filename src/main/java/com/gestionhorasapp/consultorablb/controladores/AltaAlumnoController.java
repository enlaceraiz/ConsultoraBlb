package com.gestionhorasapp.consultorablb.controladores;

import com.gestionhorasapp.consultorablb.entidades.Alumno;
import com.gestionhorasapp.consultorablb.repository.AlumnoRepository;
import com.gestionhorasapp.consultorablb.util.JPAUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import jakarta.persistence.EntityManager;

public class AltaAlumnoController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField apellidoField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> nivelIdiomaComboBox;

    @FXML
    private TextField empresaField;

    @FXML
    private TextField grupoField;

    @FXML
    private Button guardarButton;

    private AlumnoRepository alumnoRepository;

    public AltaAlumnoController() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        alumnoRepository = new AlumnoRepository(entityManager);
    }

    @FXML
    public void initialize() {
        cargarNivelesDeIdioma();
    }

    private void cargarNivelesDeIdioma() {
        ObservableList<String> niveles = FXCollections.observableArrayList("Básico", "Intermedio", "Avanzado");
        nivelIdiomaComboBox.setItems(niveles);
    }

    @FXML
    public void guardarAlumno() {
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String email = emailField.getText();
        String nivelIdioma = nivelIdiomaComboBox.getValue();
        String empresa = empresaField.getText();
        String grupo = grupoField.getText();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || nivelIdioma == null || empresa.isEmpty() || grupo.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        Alumno alumno = new Alumno();
        alumno.setNombre(nombre);
        alumno.setApellido(apellido);
        alumno.setEmail(email);
        alumno.setNivelIdioma(nivelIdioma);
        alumno.setEmpresa(empresa);
        alumno.setGrupo(grupo);

        try {
            alumnoRepository.guardarAlumno(alumno);
            mostrarAlerta("Éxito", "Alumno registrado correctamente.");
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al guardar el alumno.");
        }
    }

    private void limpiarCampos() {
        nombreField.clear();
        apellidoField.clear();
        emailField.clear();
        nivelIdiomaComboBox.getSelectionModel().clearSelection();
        empresaField.clear();
        grupoField.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}