package rent_a_car.funcionalidades;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class Redirecionamentos {
    public void redirecionarHomePage() throws Exception {
        Stage rootWindow = new Stage();
        Parent root = null;

        Parent janela = FXMLLoader.load(getClass().getResource("../paginas/menu.fxml"));

        // Adicionar ficheiros css
        janela.getStylesheets().add(getClass().getResource("/css/menu_fancy-buttons.css").toString());

        // Icones e nome do programa
        rootWindow.getIcons().add(new Image("/imagens/icon.png"));
        rootWindow.setTitle("Página Inicial");

        rootWindow.setScene(new Scene(janela));
        rootWindow.setResizable(true);

        rootWindow.show();
    }

    public void abrirJanela(String FXMLpath, String titulo) throws IOException {
        Stage rootWindow = new Stage();
        Parent root = null;

        Parent janela = FXMLLoader.load(getClass().getResource(FXMLpath));

        // Adicionar ficheiros css
        janela.getStylesheets().add(getClass().getResource("/css/menu_fancy-buttons.css").toString());

        // Icones e nome do programa
        rootWindow.getIcons().add(new Image("/imagens/icon.png"));
        rootWindow.setTitle(titulo);

        rootWindow.setScene(new Scene(janela));
        rootWindow.setResizable(true);
        rootWindow.initModality(Modality.APPLICATION_MODAL);

        rootWindow.showAndWait();
    }
}
