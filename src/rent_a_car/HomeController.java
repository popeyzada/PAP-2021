package rent_a_car;

import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import rent_a_car.paginas.menu.controllers.HomePageController;

import java.io.IOException;

public class HomeController {
    // Objetos
    @FXML
    private BorderPane janelaPrincipal;
    @FXML
    private AnchorPane menu;
    @FXML
    private JFXButton btn1;
    @FXML
    private JFXButton btn2;
    @FXML
    private JFXButton btn3;
    @FXML
    private JFXButton btn5;
    @FXML
    private JFXButton btn6;
    @FXML
    private JFXButton btn4;
    @FXML
    private JFXButton btn7;
    @FXML
    private JFXButton btn8;

    public void initialize() {
        carregarPaginaHome();
    }

    private void carregarPaginaHome() {
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("paginas/menu/home_page-page.fxml"));
            root = loader.load();
            janelaPrincipal.setCenter(root);

            // Recebe o controller da HomePageController
            HomePageController controladorHomePage = loader.getController();

            // Chama a funcao para atualizar os valores
            controladorHomePage.pI_updateDados();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao carregar a pagina Home");
        }
    }

    private void carregarPagina(String paginaPath) {
        Task<Parent> loadTask = new Task<Parent>() {
            @Override
            public Parent call() throws IOException {
                Parent root = null;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(paginaPath));
                    root = loader.load();
                } catch (IOException e) {
                    System.out.println("Ocorreu um erro ao carregar a pagina " + paginaPath);
                }
                return root;
            }
        };

        loadTask.setOnSucceeded(e -> {
            janelaPrincipal.setCenter(loadTask.getValue());
        });

        loadTask.setOnFailed(e -> loadTask.getException().printStackTrace());
        Thread thread = new Thread(loadTask);
        thread.start();
    }


    @FXML
    void botao1(ActionEvent event) {
        carregarPaginaHome();
    }

    @FXML
    void botao2(ActionEvent event) {
        carregarPagina("paginas/menu/client-page.fxml");
    }

    @FXML
    void botao3(ActionEvent event) {
        carregarPagina("paginas/menu/viaturas-page.fxml");
    }

    @FXML
    void botao4(ActionEvent event) {
        carregarPagina("paginas/menu/reservas-homepage.fxml");
    }

    @FXML
    void botao5(ActionEvent event) {
        carregarPagina("paginas/menu/precario-page.fxml");
    }

    @FXML
    void botao6(ActionEvent event) {
        carregarPagina("paginas/menu/seguro-page.fxml");
    }

    @FXML
    void botao7(ActionEvent event) {
        carregarPagina("paginas/menu/acidentes-page.fxml");
    }

    @FXML
    void botao8(ActionEvent event) {
    }
}
