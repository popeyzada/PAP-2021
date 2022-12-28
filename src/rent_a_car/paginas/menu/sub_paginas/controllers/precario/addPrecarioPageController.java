package rent_a_car.paginas.menu.sub_paginas.controllers.precario;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import rent_a_car.funcionalidades.ComboBoxAutoComplete;
import rent_a_car.funcionalidades.ConexaoBDB;

import java.sql.ResultSet;
import java.sql.SQLException;

import static rent_a_car.funcionalidades.Validacoes.*;

public class addPrecarioPageController {
    @FXML
    private Label erro;
    @FXML
    private TextField valor;
    @FXML
    private TextField grupo;

    @FXML
    void btn1(ActionEvent event) {   // Cancelar
        try {
            fecharJanela(event);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar fechar a janela de adicionar grupos\n " + e);
        }
    }

    @FXML
    void btn2(ActionEvent event) {   // Submeter
        if(grupo.getText().isEmpty() || valor.getText().isEmpty()) {
            erro.setText("Há campos obrigatórios em branco!");
        } else {
            erro.setText("");

            if(addGrupo()) {
                try {
                    // Mensagem de sucesso
                    fecharJanela(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Mensagem de erro
            }
        }
    }

    // Declaracao de outras classes
    private ConexaoBDB baseDados = new ConexaoBDB();

    public void initialize() {
        // Forçar TextFields para aceitar um length máximo e/ou numeros
        setCampoStringLength(grupo, 6);
        setCampoDouble(valor);
    }

    private void fecharJanela(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        dialog.close();
    }

    private boolean addGrupo() {
        // Retirar e guardar os dados dos campos em variaveis (Depois de mudados)
        String Grupo = grupo.getText();
        String Valor = valor.getText();


        // Executar query para inserir os dados da viatura a ser adicionada
        if(baseDados.executarPreparedQueryUpdate("INSERT INTO precario(categoria, valor) VALUES (?, ?);", Grupo, Valor)) {
            return true;
        } else {
            erro.setText("Ocorreu um erro ao tentar inserir este grupo!");
        }
        return false;
    }
}


