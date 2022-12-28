package rent_a_car.paginas.menu.sub_paginas.controllers.viatura;

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

public class addViaturaPageController {

    @FXML
    private ComboBox<String> marca;
    @FXML
    private TextField num_portas;
    @FXML
    private TextField matricula;
    @FXML
    private JFXButton cancelar;
    @FXML
    private JFXButton submeter;
    @FXML
    private Label erro;
    @FXML
    private DatePicker fim_garantia;
    @FXML
    private TextField modelo;
    @FXML
    private TextField ano;
    @FXML
    private ComboBox<String> combustivel;
    @FXML
    private ComboBox<String> caixa_velocidades;
    @FXML
    private TextField num_lugares;
    @FXML
    private ComboBox<String> tanque_combustivel;
    @FXML
    private Label generoLabel1;
    @FXML
    private ComboBox<String> grupo;
    @FXML
    private DatePicker prox_inspecao;

    @FXML
    void btn1(ActionEvent event) {   // Cancelar
        try {
            fecharJanela(event);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar fechar a janela de adicionar clientes\n " + e);
        }
    }

    @FXML
    void btn2(ActionEvent event) {   // Submeter
        if(marca.getValue() == null || modelo.getText().isEmpty() || ano.getText().isEmpty() || combustivel.getValue() == null || caixa_velocidades.getValue() == null || matricula.getText().isEmpty() || grupo.getValue() == null || fim_garantia.getValue() == null || prox_inspecao.getValue() == null) {
            erro.setText("Há campos obrigatórios em branco!");
        } else {
            erro.setText("");

            try {
                if(addViatura()) {
                    try {
                        fecharJanela(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //Mensagem de erro na janela principal
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Declaracao de outras classes
    private ConexaoBDB baseDados = new ConexaoBDB();

    @FXML
    public void initialize() {
        // Adicionar lista de marcas de carros na ComboBox marca
        ObservableList<String> carrosMarcas = FXCollections.observableArrayList();

        carrosMarcas.setAll("Abarth", "Alfa Romeo", "Aston Martin", "Audi", "Bentley", "BMW", "Bugatti", "Cadillac", "Chevrolet", "Chrysler", "Citroën", "Dacia", "Daewoo", "Daihatsu", "Dodge", "Donkervoort", "DS", "Ferrari", "Fiat", "Fisker", "Ford", "Honda", "Hummer", "Hyundai", "Infiniti", "Iveco", "Jaguar", "Jeep", "Kia", "KTM", "Lada", "Lamborghini", "Lancia", "Land Rover", "Landwind", "Lexus", "Lotus", "Maserati", "Maybach", "Mazda", "McLaren", "Mercedes-Benz", "MG", "Mini", "Mitsubishi", "Morgan", "Nissan", "Opel", "Peugeot", "Porsche", "Renault", "Rolls-Royce", "Rover", "Saab", "Seat", "Skoda", "Smart", "SsangYong", "Subaru", "Suzuki", "Tesla", "Toyota", "Volkswagen", "Volvo");
        marca.setItems(carrosMarcas);

        // Função de auto-completar no ComboBox marca
        marca.setTooltip(new Tooltip());
        new ComboBoxAutoComplete(marca);


        // Adicionar lista de combustiveis (Gasoleo ou gasolina ou eletrico) na ComboBox combustivel
        combustivel.setItems(getObservableItems("Gasóleo", "Gasolina", "Elétrico"));


        // Adicionar lista de caixa de velocidades (Automática ou manual) na ComboBox caixa_velocidades
        caixa_velocidades.setItems(getObservableItems("Automática", "Manual"));


        // Adicionar lista do nivel do tanque de combustivel que o carro tem que voltar (1/4, 1/3, 1/2 ou CHEIO) na ComboBox tanque_combustivel
        tanque_combustivel.setItems(getObservableItems("CHEIO", "1/2", "1/3", "1/4"));


        // Adicionar lista de grupos existentes na Base De Dados (Faz uma query na DB e retorna e adiciona todos os possíveis) na ComboBox grupo
        ObservableList<String> listaGrupos = FXCollections.observableArrayList();

        // Executar query para inserir os dados da viatura a ser adicionada
        try {
            ResultSet queryResult;

            // Executar query para obter dados de todos os clientes
            queryResult = baseDados.executarQuery("SELECT categoria FROM precario ORDER BY categoria ASC;");

            while(queryResult.next()){
                listaGrupos.add(
                            queryResult.getString("categoria")
                    );
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu ao tentar retornar os grupos existentes da tabela precario!\n" + e);
        }
        grupo.setItems(listaGrupos);


        // Forçar TextFields para aceitar um length máximo e/ou numeros
        setCampoStringLength(matricula, 9);
        setCampoNumerico(num_lugares, 2);
        setCampoNumerico(num_portas, 2);
        setCampoNumerico(ano, 4);
    }

    private void fecharJanela(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        dialog.close();
    }

    private boolean addViatura() {
        // Retirar e guardar os dados dos campos em variaveis
        String Grupo = (String) grupo.getValue();
        String Marca = marca.getValue();
        String Modelo = modelo.getText();
        String Ano = ano.getText();
        String Tipo_comb = (String) combustivel.getValue();
        String Caixa_vel = caixa_velocidades.getValue();
        String Data_garantia = ""; if(fim_garantia.getValue() != null) { Data_garantia = fim_garantia.getValue().toString(); }
        String Num_portas = num_portas.getText();
        String Num_lugares = num_lugares.getText();
        String Matricula = matricula.getText();
        String Prox_inspecao = ""; if(prox_inspecao.getValue() != null) { Prox_inspecao = prox_inspecao.getValue().toString(); }
        String Tanque_combustivel = tanque_combustivel.getValue();

        // Executar query para inserir os dados da viatura a ser adicionada
        if(baseDados.executarPreparedQueryUpdate("INSERT INTO viaturas(precario_grupo, marca, modelo, ano, tipo_combustivel, caixa_velocidades, data_garantia, portas, lugares, matricula, prox_inspecao, tanque_combustivel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", Grupo, Marca, Modelo, Ano, Tipo_comb, Caixa_vel, Data_garantia, Num_portas, Num_lugares, Matricula, Prox_inspecao, Tanque_combustivel)) {
            return true;
        } else {
            erro.setText("Ocorreu um erro ao tentar inserir esta viatura!");
        }

        return false;
    }
}


