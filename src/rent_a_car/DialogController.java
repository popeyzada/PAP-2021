package rent_a_car;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

// Importar outras classes e aceder aos demais métodos
import rent_a_car.funcionalidades.Redirecionamentos;

public class DialogController {

    // Definicao de classes externas
    private final Redirecionamentos redirecionamentos = new Redirecionamentos();
    private final Main main = new Main();

    public void loginSucesso(ActionEvent actionEvent) throws Exception {
        Parent ficheiro = FXMLLoader.load(getClass().getResource("paginas/dialogs/loginSucesso.fxml"));
        Stage dialog = new Stage();

        dialog.initModality(Modality.APPLICATION_MODAL);    // Faz com que seja obrigado a fechar para continuar
        dialog.setScene(new Scene(ficheiro));
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.showAndWait();
    }

    public static void mostrarErro(Alert.AlertType type, String titulo, String header_texto, String content_texto) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(header_texto);
        alert.setContentText(content_texto);
        alert.showAndWait();
    }

    public void loginErro(ActionEvent actionEvent) throws IOException {
        Parent ficheiro = FXMLLoader.load(getClass().getResource("paginas/dialogs/loginErro.fxml"));
        Stage dialog = new Stage();

        dialog.initModality(Modality.APPLICATION_MODAL);    // Faz com que seja obrigado a fechar para continuar
        dialog.setScene(new Scene(ficheiro));
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.showAndWait();
    }

    public void fechar(ActionEvent actionEvent) throws Exception {
        Button btn = (Button) actionEvent.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        dialog.close();
    }

    public void fecharSucesso(ActionEvent actionEvent) throws Exception {
        Button btn = (Button) actionEvent.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        dialog.close();

        redirecionamentos.redirecionarHomePage();
    }
}
