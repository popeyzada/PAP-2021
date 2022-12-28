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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rent_a_car.classes.Precario;
import rent_a_car.funcionalidades.ConexaoBDB;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import rent_a_car.funcionalidades.Redirecionamentos;
import rent_a_car.funcionalidades.Validacoes;
import rent_a_car.paginas.menu.sub_paginas.controllers.precario.editPrecarioPageController;

import java.io.IOException;
import java.sql.ResultSet;

import static rent_a_car.funcionalidades.Impressoes.imprimirTableView;


public class PrecarioPageController {
        @FXML
        private TextField searchBar;
        @FXML
        private TableView<Precario> tableView;
        @FXML
        private TableColumn<?, ?> colID;
        @FXML
        private TableColumn<?, ?> colGrupo;
        @FXML
        private TableColumn<?, ?> colValor;
        @FXML
        private Label mensagem;
        @FXML
        private BarChart<?, ?> grafico;


    // Buttons
    @FXML
    public void btn1(ActionEvent event) {  // Atualizar
        refreshTabela();
    }
    @FXML
    public void btn2(ActionEvent event) {  // Editar
       try {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                abrirJanela2("/rent_a_car/paginas/menu/sub_paginas/editPrecarioDialogPage.fxml", "Editar precario");
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de editar Precario\n " + e);
        }
    }
    @FXML
    public void btn3(ActionEvent event) {  // Adicionar
        try {
            redirecionamentos.abrirJanela("/rent_a_car/paginas/menu/sub_paginas/addPrecarioDialogPage.fxml", "Adicionar precario");
            refreshTabela();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de adicionar precario\n " + e);
        }
    }
    @FXML
    public void btn4(ActionEvent event) {  // Eliminar
        // Retorna o ID do grupo selecionada na tabela se tiver algum selecionado
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            String ID = String.valueOf(tableView.getSelectionModel().getSelectedItem().getID());

            if(validacoes.infoBox("Estás prestes a eliminar o grupo de ID " + ID + ", deseja continuar?", "Atenção!")) {
                // Apagar Precario atraves do ID
                if(deletarPrecario(ID)) {
                    data.remove(tableView.getSelectionModel().getSelectedItem());   // Deleta automaticamente da tabela sem precisar dar refresh buscando os dados novamente na DB
                }
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
    private ObservableList<Precario> data;
    private ResultSet queryResult = null;


    public void initialize() {
        refreshTabela();
    }

    public void getDadosPrecarios() {
        data = FXCollections.observableArrayList();

        try {
            // Executar query para obter dados de todos os Precarios
            queryResult = baseDados.executarQuery("SELECT * FROM precario");

            while(queryResult.next()){
                data.add(new Precario(
                        queryResult.getInt("precoID"),
                        queryResult.getString("categoria"),
                        queryResult.getDouble("valor")
                ));
            }

            colID.setCellValueFactory(new PropertyValueFactory("ID"));
            colGrupo.setCellValueFactory(new PropertyValueFactory("grupo"));
            colValor.setCellValueFactory(new PropertyValueFactory("valor"));

            tableView.setItems(data);

        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao processar e inserir os dados da query de Precarios:\n " + e);
        }
    }

    private void refreshGrafico() {
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Valor grupo");

        // Irá inserir um objeto XYChart desde o primeiro até o ultimo grupo existente na tabela
        for(int i = 0; i < data.size(); i++) {
            series1.getData().add(new XYChart.Data(data.get(i).getGrupo(), data.get(i).getValor()));
        }
        grafico.getData().setAll(series1);
    }

    public void refreshTabela() {
        getDadosPrecarios();
        listenSearchBar();
        refreshGrafico();
    }

    private boolean deletarPrecario(String PrecarioID) {
        // Executar query para obter dados de todos as Precarios
        try {
            if(baseDados.executarPreparedQueryUpdate("DELETE FROM precario WHERE precoID = ?;", PrecarioID)) {
                exibirMensagem("O grupo de ID " + PrecarioID + " foi deletado com sucesso!", Color.RED);
                return true;
            } else {
                exibirMensagem("Ocorreu um erro na eliminação do grupo de ID " + PrecarioID + "!\nObs: Para eliminar não pode existir nenhuma viatura com este grupo atribuido", Color.RED);
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao deletar o Precario numero:" + PrecarioID +  "\n " + e);
        }
        return false;
    }

    public void exibirMensagem(String mensagemDisplay, Color cor) {
        try {
            mensagem.setText(mensagemDisplay);
            mensagem.setTextFill(cor);
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println("Ocorreu um erro ao tentar exibir a mensagem: " + mensagemDisplay + " na página de Precarios!");
        }
    }

    private void listenSearchBar() {
        // Transforma a ObservableList em uma FilteredList (inicialmente exibe todos os dados)
        FilteredList<Precario> filteredData = new FilteredList<>(data, p -> true);

        // Define o filtro Predicado sempre que o filtro for alterado.
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(precario -> {
                // Se o texto do filtro estiver vazio, exibe todas as Precarios
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Faz as comparaçoes ignorando maiúsculas e minúsculas
                String lowerCaseFilter = newValue.toLowerCase();

                if (precario.getGrupo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtro corresponde ao grupo
                } else if (String.valueOf(precario.getID()).indexOf(lowerCaseFilter)!=-1) {
                        return true; // Filtro corresponde ao ID
                }
                return false; // Não há correspondencia
            });
        });

        // Transforma a FilteredList em uma SortedList.
        SortedList<Precario> sortedData = new SortedList<>(filteredData);

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
        rootLoader.setLocation(this.getClass().getResource("/rent_a_car/paginas/menu/sub_paginas/editPrecarioDialogPage.fxml"));
        janela = rootLoader.load();

        // Get dos dados da Precario selecionado na tabela
        int ID = tableView.getSelectionModel().getSelectedItem().getID();
        String grupo = tableView.getSelectionModel().getSelectedItem().getGrupo();
        double valor = tableView.getSelectionModel().getSelectedItem().getValor();

        // Define o controlador para que o programa saiba qual é
        editPrecarioPageController controladorCP = rootLoader.getController();

        rootWindow.setScene(new Scene(janela));
        rootWindow.setResizable(true);
        rootWindow.initModality(Modality.APPLICATION_MODAL);

        // Faz com que a tabela seja atualizada automaticamente depois de editado
        rootWindow.setOnHidden(e -> {
            refreshTabela();
        });

        // Insere os dados da Precario selecionada na tabela
        controladorCP.introduzirValoresAuto(ID, grupo, valor);

        rootWindow.show();
    }
}
