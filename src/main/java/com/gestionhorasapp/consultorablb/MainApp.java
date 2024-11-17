package com.gestionhorasapp.consultorablb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;


public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);

        // Agregar ícono al Stage
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/ICO-FINAL.jpg")));

        stage.setTitle("Consultora de Idiomas BLB - Sistema de Gestión Interna");
        stage.show();

    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}