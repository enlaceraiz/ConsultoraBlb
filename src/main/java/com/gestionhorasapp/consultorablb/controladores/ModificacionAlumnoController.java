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

import java.util.List;

public class ModificacionAlumnoController {

    @FXML
    private ComboBox<String> empresaComboBox;

    @FXML
    private ComboBox<String> grupoComboBox;

    @FXML
    private ComboBox<Alumno> alumnoComboBox;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField apellidoField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> nivelIdiomaComboBox;

    @FXML
    private Button guardarButton;

    private AlumnoRepository alumnoRepository;

    public ModificacionAlumnoController() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        alumnoRepository = new AlumnoRepository(entityManager);
    }

    @FXML
    public void initialize() {
        cargarEmpresas();
        cargarNivelesDeIdioma();

        // Listeners para filtrar alumnos
        empresaComboBox.setOnAction(event -> cargarGruposPorEmpresa());
        grupoComboBox.setOnAction(event -> cargarAlumnosPorGrupo());
        alumnoComboBox.setOnAction(event -> cargarDatosAlumnoSeleccionado());
    }

    private void cargarEmpresas() {
        List<String> empresas = alumnoRepository.listarEmpresas();
        ObservableList<String> empresasObservable = FXCollections.observableArrayList(empresas);
        empresaComboBox.setItems(empresasObservable);
    }

    private void cargarGruposPorEmpresa() {
        String empresaSeleccionada = empresaComboBox.getValue();
        if (empresaSeleccionada != null) {
            List<String> grupos = alumnoRepository.listarGruposPorEmpresa(empresaSeleccionada);
            ObservableList<String> gruposObservable = FXCollections.observableArrayList(grupos);
            grupoComboBox.setItems(gruposObservable);
        } else {
            grupoComboBox.getItems().clear();
        }
    }

    private void cargarAlumnosPorGrupo() {
        String empresaSeleccionada = empresaComboBox.getValue();
        String grupoSeleccionado = grupoComboBox.getValue();

        if (empresaSeleccionada != null && grupoSeleccionado != null) {
            List<Alumno> alumnos = alumnoRepository.listarAlumnosPorEmpresaYGrupo(empresaSeleccionada, grupoSeleccionado);
            ObservableList<Alumno> alumnosObservable = FXCollections.observableArrayList(alumnos);
            alumnoComboBox.setItems(alumnosObservable);

            // Configuración para mostrar nombre y apellido en el ComboBox
            alumnoComboBox.setConverter(new javafx.util.StringConverter<Alumno>() {
                @Override
                public String toString(Alumno alumno) {
                    if (alumno != null) {
                        return alumno.getNombre() + " " + alumno.getApellido();
                    }
                    return "";
                }

                @Override
                public Alumno fromString(String string) {
                    return null;
                }
            });
        } else {
            alumnoComboBox.getItems().clear();
        }
    }

    private void cargarNivelesDeIdioma() {
        ObservableList<String> niveles = FXCollections.observableArrayList("Básico", "Intermedio", "Avanzado");
        nivelIdiomaComboBox.setItems(niveles);
    }

    private void cargarDatosAlumnoSeleccionado() {
        Alumno alumnoSeleccionado = alumnoComboBox.getValue();
        if (alumnoSeleccionado != null) {
            nombreField.setText(alumnoSeleccionado.getNombre());
            apellidoField.setText(alumnoSeleccionado.getApellido());
            emailField.setText(alumnoSeleccionado.getEmail());
            nivelIdiomaComboBox.setValue(alumnoSeleccionado.getNivelIdioma());
        }
    }

    @FXML
    public void guardarCambios() {
        Alumno alumnoSeleccionado = alumnoComboBox.getValue();
        if (alumnoSeleccionado == null) {
            mostrarAlerta("Error", "Debes seleccionar un alumno para modificar.");
            return;
        }

        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String email = emailField.getText();
        String nivelIdioma = nivelIdiomaComboBox.getValue();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || nivelIdioma == null) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        alumnoSeleccionado.setNombre(nombre);
        alumnoSeleccionado.setApellido(apellido);
        alumnoSeleccionado.setEmail(email);
        alumnoSeleccionado.setNivelIdioma(nivelIdioma);

        try {
            alumnoRepository.actualizarAlumno(alumnoSeleccionado);
            mostrarAlerta("Éxito", "Alumno modificado correctamente.");
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