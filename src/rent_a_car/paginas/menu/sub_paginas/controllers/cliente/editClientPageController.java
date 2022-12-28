package rent_a_car.paginas.menu.sub_paginas.controllers.cliente;

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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import static rent_a_car.funcionalidades.Validacoes.setCampoNumerico;
import static rent_a_car.funcionalidades.Validacoes.setCampoStringLength;

// Importar funcoes de outras classes


public class editClientPageController implements Initializable {
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

    // Dados iniciais do cliente (Quando não foram editados ainda)
        private int clienteID;
        private String nome;
        private String codDiscagem1;
        private String num1;
        private String Data_nascimento;
        private String Gen;
        private String Morada;
        private String Cidade;
        private String Pais;
        private String Codigo_Postal;
        private String NumCartaCond;
        private String CartaCondVAL;
        private String NumPassaporte;
        private String Contribuinte;
        private String Email;

    @FXML
        void btn1(ActionEvent event) {   // Cancelar
            try {
                fecharJanela(event);
            } catch (Exception e) {
                System.out.println("Ocorreu um erro ao tentar fechar a janela de adicionar clientes\n " + e);
            }
        }

        @FXML
        void btn2(ActionEvent event) {   // Redefinir
            introduzirValoresNE(nome, codDiscagem1, num1, Data_nascimento, Gen, Cidade, Morada, Codigo_Postal, Pais, NumCartaCond, CartaCondVAL, NumPassaporte, Contribuinte, Email);
        }

        @FXML
        void btn3(ActionEvent event) {  // Submeter
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
                if(updateCliente()) {
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

    public void introduzirValoresAuto(int ID, String nomeCompleto, int contato, Date data_nascimento, String gen, String cidade, String morada, String codigo_postal, String pais, String num_carta_conducao, String carta_conducaoVAL, String num_passaporte, int contribuinte, String email) {
        clienteID = ID;
        introduzirValoresNE(nomeCompleto, String.valueOf(getDDI1(String.valueOf(contato))), String.valueOf(contato), data_nascimento.toString(), gen, cidade, morada, codigo_postal, pais, num_carta_conducao, carta_conducaoVAL, num_passaporte, String.valueOf(contribuinte), email);
        introduzirValoresString();
    }

    public void introduzirValoresNE(String nomeCompleto, String codDiscagem1, String num1, String data_nascimento, String gen, String cidade, String morada, String codigo_postal, String pais, String num_carta_conducao, String carta_conducaoVAL, String num_passaporte, String contribuinte, String email) {
        this.nomeCompleto.setText(nomeCompleto);
        this.dob.setValue(LocalDate.parse(data_nascimento));
        this.empresa.setSelected(false); if(gen.equals("M")) { this.masculino.setSelected(true); } else if(gen.equals("F")) { this.feminino.setSelected(true); } else { this.empresa.setSelected(true); }
        this.morada.setText(morada);
        this.cidade.setText(cidade);
        this.pais.setValue((String) pais);
        this.codigo_postal.setText(codigo_postal);
        this.cartadeconducao.setText(num_carta_conducao);
        this.cartadeconducaoVALdia.setText(carta_conducaoVAL.substring(0, 2));
        this.cartadeconducaoVALmes.setText(carta_conducaoVAL.substring(3, 5));
        this.cartadeconducaoVALano.setText(carta_conducaoVAL.substring(6, 10));
        this.num_passaporte.setText(num_passaporte);
        this.contribuinte.setText(String.valueOf(contribuinte));
        this.email.setText(email);
        this.numero1.setText(String.valueOf(num1));
        this.ddiN1.setText(codDiscagem1);
    }

    public void introduzirValoresString() {
        nome = nomeCompleto.getText();
        codDiscagem1 = ddiN1.getText();
        num1 = numero1.getText();
        Data_nascimento = dob.getValue().toString();
        if(masculino.isSelected()){ Gen = "M"; } else if(feminino.isSelected()){ Gen = "F"; }
        Morada = morada.getText();
        Cidade = cidade.getText();
        Pais = (String) pais.getValue();
        Codigo_Postal = codigo_postal.getText();
        NumCartaCond = null; if(!cartadeconducao.getText().isEmpty()) { NumCartaCond = cartadeconducao.getText(); }
        CartaCondVAL = null; if(!cartadeconducao.getText().isEmpty()) { CartaCondVAL = cartadeconducaoVALdia.getText() + "/" + cartadeconducaoVALmes.getText() + "/" + cartadeconducaoVALano.getText(); }
        NumPassaporte = num_passaporte.getText();
        Contribuinte = null; if(!contribuinte.getText().isEmpty()) { Contribuinte = contribuinte.getText(); }
        Email = email.getText();
        String empresa1;
        if(empresa.isSelected()) { empresa1 = "S"; Gen = "I"; } else { empresa1 = "N"; }
    }

    private boolean updateCliente() {
        // Retirar e guardar os dados dos campos em variaveis (Depois de mudados)
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
            if(baseDados.executarPreparedQueryUpdate("UPDATE clientes SET data_nascimento = ?, genero = ?, cidade = ?, morada = ?, codigo_postal = ?, pais = ?, carta_conducao = ?, validade_carta_conducao = ?, ID_passaporte = ?, contribuinte = ?, email = ?, empresa = ? WHERE clienteID = ?;", Data_nascimento, Gen, Cidade, Morada, Codigo_Postal, Pais, NumCartaCond, CartaCondVAL, NumPassaporte, Contribuinte, Email, Empresa, String.valueOf(clienteID))) {
                // Declaracao de variaveis
                ResultSet queryResult;

                // Buscar o ID do contato do cliente pela db para atribuir a uma variavel depois
                queryResult = baseDados.executarPreparedQuery("SELECT contatos_contatoID FROM clientes WHERE clienteID = ? ORDER BY contatos_contatoID DESC LIMIT 1;", String.valueOf(clienteID));

                // Processar o resultado
                if(queryResult.next()) {
                    int contatoID = queryResult.getInt(1);
                    // Dar update dos dados do cliente na tabela contatos
                    if(baseDados.executarPreparedQueryUpdate("UPDATE contatos SET nome = ?, codigo_discagem = ?, contato = ? WHERE contatoID = ?;", nome, codDiscagem1, num1, String.valueOf(contatoID))) {
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

    private int getDDI1(String numero1) {
        int ddi1 = 0;
        try {
            ResultSet queryResult = null;

            // Executar query para obter dados de todos os clientes exclusivamente sobre nome e numero de contato
            queryResult = baseDados.executarPreparedQuery("SELECT codigo_discagem FROM contatos WHERE contato = ?;", numero1);

            while(queryResult.next()) {
                ddi1 = queryResult.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao retornar o DDI1" + e);
        }
        return ddi1;
    }

    private void fecharJanela(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        dialog.close();
    }

    // Retornar a data formatada para inserir no depois no DatePicker
    public static final LocalDate formatarData(Date data){
        String date = new SimpleDateFormat("dd/MM/yyyy").format(data);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date , formatter);
        return localDate;
    }
}

