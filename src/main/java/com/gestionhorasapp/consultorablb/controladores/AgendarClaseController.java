package com.gestionhorasapp.consultorablb.controladores;

import com.gestionhorasapp.consultorablb.entidades.Alumno;
import com.gestionhorasapp.consultorablb.entidades.Profesor;
import com.gestionhorasapp.consultorablb.entidades.Clase;
import com.gestionhorasapp.consultorablb.repository.AlumnoRepository;
import com.gestionhorasapp.consultorablb.repository.ProfesorRepository;
import com.gestionhorasapp.consultorablb.repository.ClaseRepository;
import com.gestionhorasapp.consultorablb.util.JPAUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AgendarClaseController {

    @FXML
    private ComboBox<String> empresaComboBox;

    @FXML
    private ComboBox<String> grupoComboBox;

    @FXML
    private ComboBox<Alumno> alumnoComboBox;

    @FXML
    private ComboBox<Profesor> profesorComboBox;

    @FXML
    private DatePicker fechaPicker;

    @FXML
    private TextField duracionTextField;

    @FXML
    private TextField horaField;

    private AlumnoRepository alumnoRepository;
    private ProfesorRepository profesorRepository;
    private ClaseRepository claseRepository;

    public AgendarClaseController() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        alumnoRepository = new AlumnoRepository(entityManager);
        profesorRepository = new ProfesorRepository(entityManager);
        claseRepository = new ClaseRepository(entityManager);
    }

    @FXML
    public void initialize() {
        cargarEmpresas();

        // Listeners para actualizar dinámicamente grupos y alumnos
        empresaComboBox.setOnAction(event -> cargarGruposPorEmpresa());
        grupoComboBox.setOnAction(event -> filtrarAlumnos());

        cargarProfesores();
        configurarComboBoxes();
    }

    private void cargarEmpresas() {
        List<String> empresas = alumnoRepository.listarEmpresas(); // Obtiene las empresas únicas
        ObservableList<String> empresasObservable = FXCollections.observableArrayList(empresas);
        empresaComboBox.setItems(empresasObservable);
    }

    private void cargarGruposPorEmpresa() {
        String empresaSeleccionada = empresaComboBox.getValue();
        if (empresaSeleccionada != null) {
            List<String> grupos = alumnoRepository.listarGruposPorEmpresa(empresaSeleccionada); // Grupos por empresa
            ObservableList<String> gruposObservable = FXCollections.observableArrayList(grupos);
            grupoComboBox.setItems(gruposObservable);
        } else {
            grupoComboBox.getItems().clear();
        }
    }

    private void filtrarAlumnos() {
        String empresaSeleccionada = empresaComboBox.getValue();
        String grupoSeleccionado = grupoComboBox.getValue();

        if (empresaSeleccionada != null && grupoSeleccionado != null) {
            List<Alumno> alumnos = alumnoRepository.findByEmpresaAndGrupo(empresaSeleccionada, grupoSeleccionado);
            ObservableList<Alumno> alumnosObservable = FXCollections.observableArrayList(alumnos);
            alumnoComboBox.setItems(alumnosObservable);
        } else {
            alumnoComboBox.getItems().clear();
        }
    }

    private void cargarProfesores() {
        List<Profesor> profesores = profesorRepository.listarProfesores(); // Método existente en el repositorio
        ObservableList<Profesor> profesoresObservable = FXCollections.observableArrayList(profesores);
        profesorComboBox.setItems(profesoresObservable);
    }

    private void configurarComboBoxes() {
        // Configuración del ComboBox de Alumnos para mostrar nombre y apellido
        alumnoComboBox.setConverter(new StringConverter<Alumno>() {
            @Override
            public String toString(Alumno alumno) {
                if (alumno != null) {
                    return alumno.getNombre() + " " + alumno.getApellido();
                }
                return "";
            }

            @Override
            public Alumno fromString(String string) {
                return null; // No se necesita implementación
            }
        });

        // Configuración del ComboBox de Profesores para mostrar nombre y apellido
        profesorComboBox.setConverter(new StringConverter<Profesor>() {
            @Override
            public String toString(Profesor profesor) {
                if (profesor != null) {
                    return profesor.getNombre() + " " + profesor.getApellido();
                }
                return "";
            }

            @Override
            public Profesor fromString(String string) {
                return null; // No se necesita implementación
            }
        });
    }

    @FXML
    public void agendarClase() {
        Alumno alumno = alumnoComboBox.getValue();
        Profesor profesor = profesorComboBox.getValue();
        LocalDate fecha = fechaPicker.getValue();
        String horaString = horaField.getText();
        String duracionString = duracionTextField.getText();

        if (alumno == null || profesor == null || fecha == null || horaString.isEmpty() || duracionString.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        // Conversión de hora y duración
        LocalTime hora;
        int duracion;
        try {
            hora = LocalTime.parse(horaString); // Asegúrate de que el formato sea "HH:mm"
            duracion = Integer.parseInt(duracionString);
        } catch (Exception e) {
            mostrarAlerta("Error", "Formato inválido en hora o duración.");
            return;
        }

        // Validar disponibilidad del profesor
        if (!claseRepository.estaDisponible(profesor, fecha, hora)) {
            mostrarAlerta("Error", "El profesor no está disponible en ese horario.");
            return;
        }

        // Crear y guardar la clase
        Clase clase = new Clase();
        clase.setAlumno(alumno);
        clase.setProfesor(profesor);
        clase.setFecha(fecha);
        clase.setHora(hora);
        clase.setDuracion(duracion);

        claseRepository.guardarClase(clase);
        mostrarAlerta("Éxito", "Clase agendada correctamente.");
        limpiarCampos();
    }

    private void limpiarCampos() {
        alumnoComboBox.getSelectionModel().clearSelection();
        profesorComboBox.getSelectionModel().clearSelection();
        fechaPicker.setValue(null);
        horaField.clear();
        duracionTextField.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}