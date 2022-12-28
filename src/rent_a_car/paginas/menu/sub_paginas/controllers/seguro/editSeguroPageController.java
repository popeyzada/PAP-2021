package rent_a_car.paginas.menu.sub_paginas.controllers.seguro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rent_a_car.funcionalidades.ConexaoBDB;


import static rent_a_car.funcionalidades.Validacoes.setCampoDouble;
import static rent_a_car.funcionalidades.Validacoes.setCampoStringLength;

public class editSeguroPageController {

    @FXML
    private Label erro;
    @FXML
    private TextField valor;
    @FXML
    private TextField nome;

    @FXML
    public void btn1(ActionEvent event) {    // Cancelar
        fecharJanela(event);
    }

    @FXML
    public void btn2(ActionEvent event) {    // Redefinir
        introduzirValoresNE(Nome, Valor);
    }

    @FXML
    public void btn3(ActionEvent event) {    // Submeter
        if(nome.getText().isEmpty() || valor.getText().isEmpty()) {
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


    // Dados iniciais do cliente (Quando não foram editados ainda)
    private int seguroID;
    private String Nome;
    private String Valor;

    // Declaracao de outras classes
    private ConexaoBDB baseDados = new ConexaoBDB();


    public void initialize() {
        // Forçar TextFields para aceitar um length máximo e/ou numeros
        setCampoStringLength(nome, 45);
        setCampoDouble(valor);
    }


    public void introduzirValoresAuto(int ID, String nome, double valor) {
        seguroID = ID;
        introduzirValoresNE(nome, String.valueOf(valor));
        introduzirValoresString();
    }

    public void introduzirValoresNE(String grupo, String valor) {
        this.nome.setText(grupo);
        this.valor.setText(valor);
    }

    public void introduzirValoresString() {
        Nome = nome.getText();
        Valor = valor.getText();
    }

    private boolean updateGrupo() {
        // Retirar e guardar os dados dos campos em variaveis (Depois de mudados)
        String Nome = nome.getText();
        String Valor = valor.getText();

        // Executar query para inserir os dados do seguro a ser adicionado
        if(baseDados.executarPreparedQueryUpdate("UPDATE seguro SET nome = ?, preco_diario = ? WHERE seguroID = ?;", Nome, Valor, String.valueOf(seguroID))) {
            return true;
        } else {
            erro.setText("Ocorreu um erro ao tentar editar este seguro!");
        }
        return false;
    }

    private void fecharJanela(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        dialog.close();
    }

}
