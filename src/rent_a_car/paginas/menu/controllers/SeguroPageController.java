package rent_a_car.paginas.menu.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rent_a_car.classes.Seguro;
import rent_a_car.funcionalidades.ConexaoBDB;
import rent_a_car.funcionalidades.Redirecionamentos;
import rent_a_car.funcionalidades.Validacoes;
import rent_a_car.paginas.menu.sub_paginas.controllers.seguro.editSeguroPageController;

import java.io.IOException;
import java.sql.ResultSet;

import static rent_a_car.funcionalidades.Impressoes.imprimirTableView;


public class SeguroPageController {
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<Seguro> tableView;
    @FXML
    private TableColumn<?, ?> colID;
    @FXML
    private TableColumn<?, ?> colNome;
    @FXML
    private TableColumn<?, ?> ColPrecoDiario;
    @FXML
    public Label mensagem;

    @FXML
    public void btn1(ActionEvent event) {   // Atualizar
        refreshTabela();
    }

    @FXML
    public void btn2(ActionEvent event) {   // Editar
        try {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                abrirJanela2("/rent_a_car/paginas/menu/sub_paginas/editSeguroDialogPage.fxml", "Editar seguro");
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de editar Seguro\n " + e);
        }
    }

    @FXML
    public void btn3(ActionEvent event) {   // Adicionar
        try {
            redirecionamentos.abrirJanela("/rent_a_car/paginas/menu/sub_paginas/addSeguroDialogPage.fxml", "Adicionar seguro");
            refreshTabela();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de adicionar seguro\n " + e);
        }
    }

    @FXML
    public void btn4(ActionEvent event) {   // Eliminar
        // Retorna o ID do seguro selecionado na tabela se tiver algum selecionado
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            String ID = String.valueOf(tableView.getSelectionModel().getSelectedItem().getID());

            if(validacoes.infoBox("Estás prestes a eliminar a viatura de ID " + ID + ", deseja continuar?", "Atenção!")) {
                // Apagar viatura atraves do ID
                deletarSeguro(ID);
                data.remove(tableView.getSelectionModel().getSelectedItem());   // Deleta automaticamente da tabela sem precisar dar refresh buscando os dados novamente na DB
            }
        }
    }

    @FXML
    public void btnImprimir(ActionEvent event) {
        imprimirTableView(tableView, event);
    }

    // Declaracao de outras classes
    private ConexaoBDB baseDados = new ConexaoBDB();
    private Redirecionamentos redirecionamentos = new Redirecionamentos();
    private Validacoes validacoes = new Validacoes();

    // Declaracao de variaveis/objetos
    private ObservableList<Seguro> data;
    private ResultSet queryResult = null;


    public void initialize() {
        refreshTabela();
    }


    public void getDadosSeguros() {
        data = FXCollections.observableArrayList();

        try {
            // Executar query para obter dados de todos os viaturas
            queryResult = baseDados.executarQuery("SELECT * FROM seguro");

            while(queryResult.next()){
                data.add(new Seguro(
                        queryResult.getInt("seguroID"),
                        queryResult.getString("nome"),
                        queryResult.getDouble("preco_diario")
                ));
            }

            colID.setCellValueFactory(new PropertyValueFactory("ID"));
            colNome.setCellValueFactory(new PropertyValueFactory("nome"));
            ColPrecoDiario.setCellValueFactory(new PropertyValueFactory("preco_diario"));

            tableView.setItems(data);

        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao processar e inserir os dados da query de Seguros:\n " + e);
        }
    }

    public void refreshTabela() {
        getDadosSeguros();
        listenSearchBar();
    }

    private void listenSearchBar() {
        // Transforma a ObservableList em uma FilteredList (inicialmente exibe todos os dados)
        FilteredList<Seguro> filteredData = new FilteredList<>(data, p -> true);

        // Define o filtro Predicado sempre que o filtro for alterado.
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(seguro -> {
                // Se o texto do filtro estiver vazio, exibe todas as viaturas
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Faz as comparaçoes ignorando maiúsculas e minúsculas
                String lowerCaseFilter = newValue.toLowerCase();

                if (seguro.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtro corresponde ao nome
                } else if (String.valueOf(seguro.getID()).indexOf(lowerCaseFilter) != -1) {
                    return true; // Filtro corresponde ao ID
                }
                return false; // Não há correspondencia
            });
        });

        // Transforma a FilteredList em uma SortedList.
        SortedList<Seguro> sortedData = new SortedList<>(filteredData);

        // Vincula a SortedList comparadora com a TableView comparada.
        // Caso contrário, ordenar o TableView não teria efeito.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // Adiciona os dados ordenados (e filtrados) à tabela.
        tableView.setItems(sortedData);
    }

    private void abrirJanela2(String FXMLpath, String titulo) throws IOException {
        Stage rootWindow = new Stage();
        Parent root = null;

        Parent janela = FXMLLoader.load(getClass().getResource(FXMLpath));

        // Adicionar ficheiros css
        janela.getStylesheets().add(getClass().getResource("/css/menu_fancy-buttons.css").toString());

        // Icones e nome do programa
        rootWindow.getIcons().add(new Image("/imagens/icon.png"));
        rootWindow.setTitle(titulo);

        // Carregar controlador para poder utilizar os métodos
        FXMLLoader rootLoader = new FXMLLoader();
        rootLoader.setLocation(this.getClass().getResource("/rent_a_car/paginas/menu/sub_paginas/editSeguroDialogPage.fxml"));
        janela = rootLoader.load();

        // Get dos dados da Precario selecionado na tabela
        int ID = tableView.getSelectionModel().getSelectedItem().getID();
        String nome = tableView.getSelectionModel().getSelectedItem().getNome();
        double valor = tableView.getSelectionModel().getSelectedItem().getPreco_diario();

        // Define o controlador para que o programa saiba qual é
        editSeguroPageController controladorCP = rootLoader.getController();

        rootWindow.setScene(new Scene(janela));
        rootWindow.setResizable(true);
        rootWindow.initModality(Modality.APPLICATION_MODAL);

        // Faz com que a tabela seja atualizada automaticamente depois de editado
        rootWindow.setOnHidden(e -> {
            refreshTabela();
        });

        // Insere os dados da Precario selecionada na tabela
        controladorCP.introduzirValoresAuto(ID, nome, valor);

        rootWindow.show();
    }

    private void deletarSeguro(String seguroID) {
        // Executar query para obter dados de todos as Viaturas
        try {
            if(baseDados.executarPreparedQueryUpdate("DELETE FROM seguro WHERE seguroID = ?;", seguroID)) {
                exibirMensagem("O seguro de ID " + seguroID + " foi deletado com sucesso!", Color.RED);
            } else {
                exibirMensagem("Ocorreu um erro na eliminação do seguro de ID " + seguroID + "!", Color.RED);
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao deletar o seguro numero:" + seguroID +  "\n " + e);
        }
    }

    public void exibirMensagem(String mensagemDisplay, Color cor) {
        try {
            mensagem.setText(mensagemDisplay);
            mensagem.setTextFill(cor);
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println("Ocorreu um erro ao tentar exibir a mensagem: " + mensagemDisplay + " na página de seguros!");
        }
    }


}
