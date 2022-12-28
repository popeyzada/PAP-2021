package rent_a_car.paginas.menu.sub_paginas.controllers.acidente;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import rent_a_car.funcionalidades.ConexaoBDB;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static rent_a_car.classes.Acidente.getViaturaID;
import static rent_a_car.classes.Acidente.getViaturas;
import static rent_a_car.funcionalidades.Validacoes.getObservableItems;

public class editAcidentePageController {
    @FXML
    private ComboBox<String> categoria;
    @FXML
    private Label erro;
    @FXML
    private DatePicker data;
    @FXML
    private ComboBox<String> viatura;
    @FXML
    private TextArea descricao;


    @FXML
    void btn1(ActionEvent event) {   // Cancelar
        try {
            fecharJanela(event);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar fechar a janela de adicionar acidentes\n " + e);
        }
    }

    @FXML
    void btn2(ActionEvent event) {   // Redefinir
        introduzirValoresNE(Categoria, Viatura, Descricao, Data);
    }


    @FXML
    void btn3(ActionEvent event) {   // Submeter
        if(descricao.getText().isEmpty() || categoria.getValue() == null || viatura.getValue() == null || data.getValue() == null) {
            erro.setText("Há campos obrigatórios em branco!");
        } else {
            erro.setText("");

            if(editAcidente()) {
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

    private int acidenteID;
    private String Categoria;
    private String Viatura;
    private String Descricao;
    private String Data;

    // Declaracao de outras classes
    private ConexaoBDB baseDados = new ConexaoBDB();

    public void initialize() {
        // Adicionar lista de viaturas existentes na Base De Dados (Faz uma query na DB e retorna e adiciona todos os possíveis) na ComboBox viatura
        ObservableList<String> listaViaturas = getViaturas();
        viatura.setItems(listaViaturas);

        // Adicionar categorias de acidentes na combobox categoria
        categoria.setItems(getObservableItems("Leve", "Grave", "Muito grave", "Outro (a)"));
    }

    public void introduzirValoresAuto(int ID, String categoria, String viatura, String descricao, Date data) {
        acidenteID = ID;
        introduzirValoresNE(categoria, viatura, descricao, data.toString());
        introduzirValoresString();
    }

    public void introduzirValoresNE(String categoria, String viatura, String descricao, String data) {
        this.categoria.setValue(categoria);
        this.viatura.setValue(viatura);
        this.descricao.setText(descricao);
        this.data.setValue(LocalDate.parse(data));
    }

    public void introduzirValoresString() {
        Categoria = (String) categoria.getValue();
        Viatura = (String) viatura.getValue();
        Descricao = descricao.getText();
        Data = data.getValue().toString();
    }

    private void fecharJanela(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        dialog.close();
    }

    private boolean editAcidente() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Retirar e guardar os dados dos campos em variaveis
        String Categoria = categoria.getValue().toString();
        String NomeViatura = viatura.getValue().toString();
        String Descricao = descricao.getText().toString();
        String Data = data.getValue().toString();

        int ViaturaIdINS = getViaturaID(NomeViatura);


        // Executar query para inserir os dados do acidente a ser adicionado
        if(baseDados.executarPreparedQueryUpdate("UPDATE acidentes SET viaturas_viaturaID = ?, categoria = ?, descricao = ?, data = ? WHERE acidenteID = ?;", String.valueOf(ViaturaIdINS), Categoria, Descricao, Data, String.valueOf(acidenteID))) {
            return true;
        } else {
            erro.setText("Ocorreu um erro ao tentar editar este acidente!");
        }
        return false;
    }


}
