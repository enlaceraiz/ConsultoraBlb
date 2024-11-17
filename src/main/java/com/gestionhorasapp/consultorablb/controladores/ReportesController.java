package com.gestionhorasapp.consultorablb.controladores;

import com.gestionhorasapp.consultorablb.entidades.Alumno;
import com.gestionhorasapp.consultorablb.entidades.Clase;
import com.gestionhorasapp.consultorablb.entidades.Profesor;
import com.gestionhorasapp.consultorablb.repository.AlumnoRepository;
import com.gestionhorasapp.consultorablb.repository.ClaseRepository;
import com.gestionhorasapp.consultorablb.repository.ProfesorRepository;
import com.gestionhorasapp.consultorablb.util.JPAUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import jakarta.persistence.EntityManager;
import java.util.List;

public class ReportesController {

    @FXML
    private ComboBox<Alumno> alumnoComboBox;

    @FXML
    private ComboBox<Profesor> profesorComboBox;

    @FXML
    private TableView<Clase> reporteTable;

    @FXML
    private TableColumn<Clase, Alumno> alumnoColumn;

    @FXML
    private TableColumn<Clase, Profesor> profesorColumn;

    @FXML
    private TableColumn<Clase, String> fechaColumn;

    @FXML
    private TableColumn<Clase, String> horaColumn;

    @FXML
    private TableColumn<Clase, Integer> duracionColumn;

    @FXML
    private TableColumn<Clase, String> empresaColumn;

    @FXML
    private TableColumn<Clase, String> grupoColumn;

    private AlumnoRepository alumnoRepository;
    private ProfesorRepository profesorRepository;
    private ClaseRepository claseRepository;

    public ReportesController() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        alumnoRepository = new AlumnoRepository(entityManager);
        profesorRepository = new ProfesorRepository(entityManager);
        claseRepository = new ClaseRepository(entityManager);
    }

    @FXML
    public void initialize() {
        configurarTabla(); // Configuración de las columnas de la tabla
        cargarDatos();     // Poblado de ComboBox
        configurarComboBoxes(); // Configurar los ComboBox para mostrar nombre y apellido
    }

    private void configurarTabla() {
        // Personalización de la columna Alumno
        alumnoColumn.setCellValueFactory(new PropertyValueFactory<>("alumno"));
        alumnoColumn.setCellFactory(column -> new TableCell<Clase, Alumno>() {
            @Override
            protected void updateItem(Alumno item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNombre() + " " + item.getApellido());
                }
            }
        });

        // Personalización de la columna Profesor
        profesorColumn.setCellValueFactory(new PropertyValueFactory<>("profesor"));
        profesorColumn.setCellFactory(column -> new TableCell<Clase, Profesor>() {
            @Override
            protected void updateItem(Profesor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNombre() + " " + item.getApellido());
                }
            }
        });

        // Configuración estándar para fecha y hora
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        horaColumn.setCellValueFactory(new PropertyValueFactory<>("hora"));

        // Configuración de la columna Duración
        duracionColumn.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        // Configuración de la columna Empresa
        empresaColumn.setCellValueFactory(new PropertyValueFactory<>("empresa"));

        // Configuración de la columna Grupo
        grupoColumn.setCellValueFactory(new PropertyValueFactory<>("grupo"));
    }

    private void configurarComboBoxes() {
        // Configuración del ComboBox de Alumnos
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
                return null; // No se necesita implementación aquí
            }
        });

        // Configuración del ComboBox de Profesores
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
                return null; // No se necesita implementación aquí
            }
        });
    }

    private void cargarDatos() {
        List<Alumno> alumnos = alumnoRepository.listarAlumnos();
        alumnoComboBox.setItems(FXCollections.observableArrayList(alumnos));
        List<Profesor> profesores = profesorRepository.listarProfesores();
        profesorComboBox.setItems(FXCollections.observableArrayList(profesores));
    }

    @FXML
    public void generarReporteAlumno() {
        Alumno alumno = alumnoComboBox.getValue(); // Obtener el alumno seleccionado

        if (alumno != null) {
            Long alumnoId = alumno.getId(); // Asegúrate de que el ID sea del tipo correcto
            List<Clase> clases = claseRepository.buscarClasesPorAlumno(alumnoId); // Consulta en el repositorio
            reporteTable.setItems(FXCollections.observableArrayList(clases)); // Cargar datos en la tabla
        } else {
            mostrarAlerta("Error", "Seleccione un alumno para generar el reporte.");
        }
    }

    @FXML
    public void generarReporteProfesor() {
        Profesor profesor = profesorComboBox.getValue();

        if (profesor != null) {
            Long profesorId = profesor.getId(); // Conversión explícita de Integer a Long
            List<Clase> clases = claseRepository.buscarClasesPorProfesor(profesorId);
            reporteTable.setItems(FXCollections.observableArrayList(clases));
        } else {
            mostrarAlerta("Error", "Seleccione un profesor para generar el reporte.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        // Implementación de alerta para mostrar mensajes al usuario
        System.out.println(titulo + ": " + mensaje); // Reemplazar con Alert de JavaFX
    }
}