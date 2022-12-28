package rent_a_car.paginas.menu.controllers;

import com.jfoenix.controls.JFXTabPane;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

import java.io.IOException;

public class ReservasHomePageController {
    @FXML
    private JFXTabPane tabPane;

    public void initialize() {
        // Menus
        Tab tab1 = new Tab();
        Tab tab2 = new Tab();

        // Setar popriedades de cada tab
        tab1.setText("Visão geral");
        tabPane.getTabs().add(tab1);
        carregarPagina("../sub_paginas/reservas-page.fxml", tab1);

        tab2.setText("Tabela");
        tabPane.getTabs().add(tab2);
        carregarPagina("../sub_paginas/reservas-page2.fxml", tab2);
    }

    private void carregarPagina(String paginaPath, Tab tabPane) {
        Task<Parent> loadTask = new Task<Parent>() {
            @Override
            public Parent call() throws IOException {
                Parent root = null;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(paginaPath));
                    root = loader.load();
                } catch (IOException e) {
                    System.out.println("Ocorreu um erro ao carregar a pagina " + paginaPath);
                    e.printStackTrace();
                }
                return root;
            }
        };

        loadTask.setOnSucceeded(e -> {
            tabPane.setContent(loadTask.getValue());
        });

        loadTask.setOnFailed(e -> loadTask.getException().printStackTrace());
        Thread thread = new Thread(loadTask);
        thread.start();
    }
}
