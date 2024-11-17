module com.gestionhorasapp.consultorablb {
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.gestionhorasapp.consultorablb.entidades to org.hibernate.orm.core, javafx.base;


    exports com.gestionhorasapp.consultorablb;
    exports com.gestionhorasapp.consultorablb.controladores;
    opens com.gestionhorasapp.consultorablb.controladores to javafx.fxml;
}
