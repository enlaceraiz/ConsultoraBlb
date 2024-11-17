package com.gestionhorasapp.consultorablb.controladores;

import com.gestionhorasapp.consultorablb.MainApp;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;


public class MainController {

    @FXML
    public void mostrarAltaAlumno() {
        // Lógica para cargar la vista de alta de alumnos
        cargarVista("AltaAlumno.fxml", "Alta de Alumnos");
    }

    @FXML
    public void mostrarBajaAlumno() {
        cargarVista("BajaAlumno.fxml", "Baja de Alumnos");
    }

    @FXML
    public void mostrarModificacionAlumno() {
        cargarVista("ModificacionAlumno.fxml", "Modificación de Alumnos");
    }

    @FXML
    public void mostrarAltaProfesor() {
        cargarVista("AltaProfesor.fxml", "Alta de Profesores");
    }

    @FXML
    public void mostrarBajaProfesor() {
        cargarVista("BajaProfesor.fxml", "Baja de Profesores");
    }

    @FXML
    public void mostrarModificacionProfesor() {
        cargarVista("ModificacionProfesor.fxml", "Modificación de Profesores");
    }

    @FXML
    public void mostrarAgendarClase() {
        cargarVista("AgendarClase.fxml", "Agendar Clase");
    }

    @FXML
    public void mostrarEliminarClase() {
        cargarVista("EliminarClase.fxml", "Eliminar Clase");
    }

    @FXML
    public void mostrarReportes() {
        cargarVista("Reportes.fxml", "Reportes");
    }

    @FXML
    public void mostrarAyuda() {
        cargarVista("Ayuda.fxml", "Ayuda");
    }

    @FXML
    public void salirAplicacion() {
        Stage stage = (Stage) MainApp.getPrimaryStage().getScene().getWindow();
        stage.close();
    }

    private void cargarVista(String archivoFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + archivoFXML));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}