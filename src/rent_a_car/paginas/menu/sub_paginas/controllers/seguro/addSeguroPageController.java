package rent_a_car.paginas.menu.sub_paginas.controllers.seguro;


import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import rent_a_car.funcionalidades.ConexaoBDB;

import static rent_a_car.funcionalidades.Validacoes.setCampoDouble;
import static rent_a_car.funcionalidades.Validacoes.setCampoStringLength;


public class addSeguroPageController {

    @FXML
    private Label erro;
    @FXML
    private JFXButton submeter;
    @FXML
    private TextField valor;
    @FXML
    private TextField nome;

    @FXML
    void btn1(ActionEvent event) {
        try {
            fecharJanela(event);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar fechar a janela de adicionar seguro\n " + e);
        }
    }

    @FXML
    void btn2(ActionEvent event) {
        if(nome.getText().isEmpty() || valor.getText().isEmpty()) {
            erro.setText("Há campos obrigatórios em branco!");
        } else {
            erro.setText("");

            if(addSeguro()) {
                try {
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
        setCampoStringLength(nome, 45);
        setCampoDouble(valor);
    }

    private void fecharJanela(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        dialog.close();
    }

    private boolean addSeguro() {
        // Retirar e guardar os dados dos campos em variaveis (Depois de mudados)
        String Nome = nome.getText();
        String Valor = valor.getText();


        // Executar query para inserir os dados da viatura a ser adicionada
        if(baseDados.executarPreparedQueryUpdate("INSERT INTO seguro(nome, preco_diario) VALUES (?, ?);", Nome, Valor)) {
            return true;
        } else {
            erro.setText("Ocorreu um erro ao tentar inserir este seguro!");
        }
        return false;
    }

}
