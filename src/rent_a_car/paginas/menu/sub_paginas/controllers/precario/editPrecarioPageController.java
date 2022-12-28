package rent_a_car.paginas.menu.sub_paginas.controllers.precario;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rent_a_car.funcionalidades.ConexaoBDB;

import java.net.URL;
import java.util.ResourceBundle;

import static rent_a_car.funcionalidades.Validacoes.setCampoDouble;
import static rent_a_car.funcionalidades.Validacoes.setCampoStringLength;


public class editPrecarioPageController implements Initializable {
    @FXML
    private Label erro;
    @FXML
    private TextField valor;
    @FXML
    private TextField grupo;

    // Dados iniciais do cliente (Quando não foram editados ainda)
    private int precoID;
    private String Grupo;
    private String Valor;

    @FXML
    void btn1(ActionEvent event) {   // Cancelar
        try {
            fecharJanela(event);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar fechar a janela de editar um grupo\n " + e);
        }
    }

    @FXML
    void btn2(ActionEvent event) {   // Redefinir
        introduzirValoresNE(Grupo, Valor);
    }

    @FXML
    void btn3(ActionEvent event) {  // Submeter
        if(grupo.getText().isEmpty() || valor.getText().isEmpty()) {
            erro.setText("Há campos obrigatórios em branco!");
        } else {
            erro.setText("");

            if(updateGrupo()) {
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

    public void initialize(URL url, ResourceBundle rb) {
        // Forçar TextFields para aceitar um length máximo e/ou numeros
        setCampoStringLength(grupo, 6);
        setCampoDouble(valor);
    }

    public void introduzirValoresAuto(int ID, String grupo, double valor) {
        precoID = ID;
        introduzirValoresNE(grupo, String.valueOf(valor));
        introduzirValoresString();
    }

    public void introduzirValoresNE(String grupo, String valor) {
        this.grupo.setText(grupo);
        this.valor.setText(valor);
    }

    public void introduzirValoresString() {
        Grupo = grupo.getText();
        Valor = valor.getText();
    }

    private boolean updateGrupo() {
        // Retirar e guardar os dados dos campos em variaveis (Depois de mudados)
        String Grupo = grupo.getText();
        String Valor = valor.getText();

        // Executar query para inserir os dados do grupo a ser adicionado
            if(baseDados.executarPreparedQueryUpdate("UPDATE precario SET categoria = ?, valor = ? WHERE precoID = ?;", Grupo, Valor, String.valueOf(precoID))) {
               return true;
            } else {
                erro.setText("Ocorreu um erro ao tentar editar este grupo!");
            }
        return false;
    }

    private void fecharJanela(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        dialog.close();
    }
}

