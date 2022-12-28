package rent_a_car.paginas.menu.sub_paginas.controllers.cliente;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import rent_a_car.funcionalidades.ComboBoxAutoComplete;
import rent_a_car.funcionalidades.ConexaoBDB;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import static rent_a_car.funcionalidades.Validacoes.setCampoNumerico;
import static rent_a_car.funcionalidades.Validacoes.setCampoStringLength;

// Importar funcoes de outras classes

public class addClientPageController implements Initializable {
        // Labels
        @FXML
        private Label erro;
        @FXML
        private Label generoLabel;

        // Date Pickers
        @FXML
        private DatePicker dob;

        // Radio Buttons
        @FXML
        private RadioButton masculino;
        @FXML
        private RadioButton feminino;
        @FXML
        private ToggleGroup genero;

        // ComboBoxes
        @FXML
        private ComboBox<String> pais;

        // Checkboxes
        @FXML
        private CheckBox empresa;


        // Textfields
        @FXML
        private TextField nomeCompleto;
        @FXML
        private TextField morada;
        @FXML
        private TextField cidade;
        @FXML
        private TextField codigo_postal;
        @FXML
        private TextField cartadeconducao;
        @FXML
        private TextField cartadeconducaoVALdia;
        @FXML
        private TextField cartadeconducaoVALmes;
        @FXML
        private TextField cartadeconducaoVALano;
        @FXML
        private TextField num_passaporte;
        @FXML
        private TextField contribuinte;
        @FXML
        private TextField email;
        @FXML
        private TextField ddiN1;
        @FXML
        private TextField numero1;
        @FXML
        private TextField ddiN2;
        @FXML
        private TextField numero2;

        // Buttons
        @FXML
        private JFXButton cancelar;
        @FXML
        private JFXButton submeter;

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

            if(nomeCompleto.getText().isEmpty()  || genero.getSelectedToggle() == null || morada.getText().isEmpty() || cidade.getText().isEmpty() || pais.getValue() == null || email.getText().isEmpty() || ddiN1.getText().isEmpty() || numero1.getText().isEmpty()) {
                erro.setText("Há campos obrigatórios em branco!");
            } else if ((dob.getValue().getYear()) > (Calendar.getInstance().get(Calendar.YEAR) - 18)) {
                erro.setText("Cliente com menos de 18 anos!");
            } else if ((Integer.valueOf(cartadeconducaoVALmes.getText()) < 1) || (Integer.valueOf(cartadeconducaoVALmes.getText()) > 12 || cartadeconducaoVALmes.getText().length() < 2)) {
                erro.setText("O mês da validade da carta de condução é invalido!");
            } else if ((Integer.valueOf(cartadeconducaoVALdia.getText()) < 1) || (Integer.valueOf(cartadeconducaoVALdia.getText()) > 31 || cartadeconducaoVALdia.getText().length() < 2)) {
                erro.setText("O dia da validade da carta de condução é invalido!");
            } else if (cartadeconducaoVALano.getText().length() < 4 || (Integer.valueOf(cartadeconducaoVALano.getText()) == 0)) {
                erro.setText("O ano da validade da carta de condução é invalido!");
            } else if (cartadeconducao.getText().length() < 9) {
                erro.setText("O número da carta condução é invalido!");
            } else if (numero1.getText().length() < 9) {
                erro.setText("O número de contato é invalido!");
            } else if (!contribuinte.getText().isEmpty() && !contribuinte.getText().contains("0") && contribuinte.getText().length() < 9) {
                erro.setText("O contribuinte é invalido!");
            } else if (!num_passaporte.getText().isEmpty() && num_passaporte.getText().length() < 9) {
                erro.setText("O número de passaporte é invalido!");
            } else {
                erro.setText("");

                try {
                    if(addCliente()) {
                        try {
                            fecharJanela(event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    // Declaracao de outras classes
    private ConexaoBDB baseDados = new ConexaoBDB();

    public void initialize(URL url, ResourceBundle rb) {
        // Adicionar listener na CheckBox empresa para desativar/ativar o genero
        generoLabel.disableProperty().bind(empresa.selectedProperty());
        feminino.disableProperty().bind(empresa.selectedProperty());
        masculino.disableProperty().bind(empresa.selectedProperty());

        // Adicionar países na ComboBox pais
        ObservableList<String> cities = FXCollections.observableArrayList();

        String[] locales1 = Locale.getISOCountries();
        for (String countrylist : locales1) {
            Locale obj = new Locale("", countrylist);
            String[] city = { obj.getDisplayCountry() };
            for (int x = 0; x < city.length; x++) {
                cities.add(obj.getDisplayCountry());
            }
        }
        pais.setItems(cities);

        // Função de auto-completar no ComboBox paises
        pais.setTooltip(new Tooltip());
        new ComboBoxAutoComplete(pais);

        // Forçar TextFields para aceitar um length máximo e/ou numeros
        setCampoStringLength(codigo_postal, 10);
        setCampoStringLength(cartadeconducao, 15);
        setCampoStringLength(nomeCompleto, 75);
        setCampoStringLength(cidade, 30);
        setCampoStringLength(morada, 50);
        setCampoStringLength(email, 50);
        setCampoNumerico(cartadeconducaoVALano, 4);
        setCampoNumerico(cartadeconducaoVALmes, 2);
        setCampoNumerico(cartadeconducaoVALdia, 2);
        setCampoNumerico(num_passaporte, 9);
        setCampoNumerico(contribuinte, 9);
        setCampoNumerico(ddiN1, 3);
        setCampoNumerico(numero1, 9);

    }

    private void fecharJanela(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        dialog.close();
    }

    private boolean addCliente() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Retirar e guardar os dados dos campos em variaveis
        String nome = nomeCompleto.getText();
        String codDiscagem1 = ddiN1.getText();
        String num1 = numero1.getText();
        String Data_nascimento = dob.getValue().toString();
        String Gen = ""; if(masculino.isSelected()){ Gen = "M"; } else if(feminino.isSelected()){ Gen = "F"; }
        String Morada = morada.getText();
        String Cidade = cidade.getText();
        String Pais = (String) pais.getValue();
        String Codigo_Postal = codigo_postal.getText();
        String NumCartaCond = null; if(!cartadeconducao.getText().isEmpty()) { NumCartaCond = cartadeconducao.getText(); }
        String CartaCondVAL = null; if(!cartadeconducao.getText().isEmpty()) { CartaCondVAL = cartadeconducaoVALdia.getText() + "/" + cartadeconducaoVALmes.getText() + "/" + cartadeconducaoVALano.getText(); }
        String NumPassaporte = num_passaporte.getText();
        String Contribuinte = null; if(!contribuinte.getText().isEmpty()) { Contribuinte = contribuinte.getText(); }
        String Email = email.getText();
        String Empresa; if(empresa.isSelected()) { Empresa = "S"; Gen = "I"; } else { Empresa = "N"; }

        // Executar query para inserir os dados do cliente a ser adicionado
        try {
            if(baseDados.executarPreparedQueryUpdate("INSERT INTO contatos(nome, codigo_discagem, contato) VALUES (?, ?, ?);", nome, codDiscagem1, num1)) {
                // Declaracao de variaveis
                ResultSet queryResult;
                String contatoID = "-1";

                // Buscar o ID do contato adicionado automaticamente acima pela db para atribuir a tabela clientes depois
                queryResult = baseDados.executarPreparedQuery("SELECT contatoID FROM contatos ORDER BY contatoID DESC LIMIT 1;");

                // Processar o resultado
                if(queryResult.next()) {
                    contatoID = queryResult.getString("contatoID");

                    // Insere o resto dos dados na tabela clientes e retorna true para que o programa feche a janela automaticamente
                    if(baseDados.executarPreparedQueryUpdate("INSERT INTO clientes(contatos_contatoID, data_nascimento, genero, cidade, morada, codigo_postal, pais, carta_conducao, validade_carta_conducao, ID_passaporte, contribuinte, email, empresa) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", contatoID, Data_nascimento, Gen, Cidade, Morada, Codigo_Postal, Pais, NumCartaCond, CartaCondVAL, NumPassaporte, Contribuinte, Email, Empresa)) {
                        return true;
                    }
                }
            } else {
                erro.setText("Ocorreu um erro ao tentar inserir este cliente!");
            }

        } catch (SQLException e) {
            System.out.println("Ocorreu ao tentar inserir um cliente!\n" + e);
        }
        return false;
    }
}

