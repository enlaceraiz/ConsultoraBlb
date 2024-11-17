package com.gestionhorasapp.consultorablb.controladores;

import com.gestionhorasapp.consultorablb.entidades.Clase;
import com.gestionhorasapp.consultorablb.repository.ClaseRepository;
import com.gestionhorasapp.consultorablb.util.JPAUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class EliminarClaseController {

    @FXML
    private DatePicker fechaDatePicker;

    @FXML
    private ComboBox<Clase> claseComboBox;

    @FXML
    private Button eliminarButton;

    private ClaseRepository claseRepository;

    public EliminarClaseController() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        claseRepository = new ClaseRepository(entityManager);
    }

    @FXML
    public void initialize() {
        // Inicializa la fecha con la fecha actual
        fechaDatePicker.setValue(LocalDate.now());
        filtrarClasesPorFecha();
    }

    @FXML
    public void filtrarClasesPorFecha() {
        LocalDate fechaSeleccionada = fechaDatePicker.getValue();
        if (fechaSeleccionada != null) {
            cargarClasesPorFecha(fechaSeleccionada);
        }
    }

    private void cargarClasesPorFecha(LocalDate fecha) {
        List<Clase> clases = claseRepository.listarClasesPorFecha(fecha);
        ObservableList<Clase> clasesObservable = FXCollections.observableArrayList(clases);
        claseComboBox.setItems(clasesObservable);

        // Configuración para mostrar detalles de la clase en el ComboBox
        claseComboBox.setConverter(new javafx.util.StringConverter<Clase>() {
            @Override
            public String toString(Clase clase) {
                if (clase != null) {
                    return "Alumno: " + clase.getAlumno().getNombre() + " " + clase.getAlumno().getApellido() +
                            ", Profesor: " + clase.getProfesor().getNombre() + " " + clase.getProfesor().getApellido() +
                            ", Hora: " + clase.getHora().toString();
                }
                return "";
            }

            @Override
            public Clase fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    public void eliminarClase() {
        Clase claseSeleccionada = claseComboBox.getValue();

        if (claseSeleccionada == null) {
            mostrarAlerta("Error", "Debes seleccionar una clase para eliminar.");
            return;
        }

        try {
            claseRepository.eliminarClase(claseSeleccionada);
            mostrarAlerta("Éxito", "Clase eliminada correctamente.");
            filtrarClasesPorFecha(); // Recarga la lista de clases después de eliminar
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al eliminar la clase.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}