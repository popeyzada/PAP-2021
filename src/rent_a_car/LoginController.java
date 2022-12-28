package rent_a_car;

import com.google.common.hash.Hashing;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;

// Importar outras classes e aceder aos demais métodos
import rent_a_car.funcionalidades.Login;

public class LoginController {
    // Declaracao da variavel dos inputs
    @FXML
    private JFXTextField inputUtilizador;
    @FXML
    private JFXPasswordField inputPassword;
    @FXML
    private Label blankLabel;
    @FXML
    private JFXButton btn;

    // Outros controladores
    private final DialogController dialog = new DialogController();

    // Funcoes
    @FXML
    void efetuarLogin(ActionEvent event) throws Exception {
        if(validarLogin()) {
            dialog.loginSucesso(event);
            fecharJanela();
        } else {
            dialog.loginErro(event);
        }
    }

    public void initialize() {
        // Adicionar um listener para checar se o enter é pressionado no texfield da password
        inputPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                if (key.getCode().equals(KeyCode.ENTER)) {
                    // Simula que o utilizador tenha pressionado o botão
                    btn.fire();
                }
            }
        });
    }

    private boolean validarLogin() {
        String utilizador = inputUtilizador.getText();
        String password = inputPassword.getText();

        if(utilizador.isEmpty() || password.isEmpty()) {
            blankLabel.setVisible(true);
        } else {
            if (Login.checarCredenciais(utilizador, converterSha256hex(password))) {
                return true;
            }
        }
        return false;
    }

    private static String converterSha256hex(String password) {
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }

    private void fecharJanela() {
        Stage login_page = (Stage) btn.getScene().getWindow();

        login_page.close();
    }
}

