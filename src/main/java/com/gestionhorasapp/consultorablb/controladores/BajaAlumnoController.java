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

import jakarta.persistence.EntityManager;

import java.util.List;

public class BajaAlumnoController {

    @FXML
    private ComboBox<String> empresaComboBox;

    @FXML
    private ComboBox<String> grupoComboBox;

    @FXML
    private ComboBox<Alumno> alumnoComboBox;

    @FXML
    private Button eliminarButton;

    private AlumnoRepository alumnoRepository;

    public BajaAlumnoController() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        alumnoRepository = new AlumnoRepository(entityManager);
    }

    @FXML
    public void initialize() {
        cargarEmpresas();

        // Listeners para actualizar grupos y alumnos dinámicamente
        empresaComboBox.setOnAction(event -> cargarGruposPorEmpresa());
        grupoComboBox.setOnAction(event -> cargarAlumnosPorGrupo());
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

            // Configuración para mostrar nombre y apellido del alumno en el ComboBox
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

    @FXML
    public void eliminarAlumno() {
        Alumno alumnoSeleccionado = alumnoComboBox.getValue();

        if (alumnoSeleccionado == null) {
            mostrarAlerta("Error", "Debes seleccionar un alumno para eliminar.");
            return;
        }

        try {
            alumnoRepository.eliminarAlumno(alumnoSeleccionado);
            mostrarAlerta("Éxito", "Alumno eliminado correctamente.");
            cargarAlumnosPorGrupo(); // Recarga la lista de alumnos después de eliminar
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al eliminar el alumno.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}