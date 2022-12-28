package rent_a_car.paginas.menu.controllers;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rent_a_car.classes.Acidente;
import rent_a_car.funcionalidades.ConexaoBDB;
import rent_a_car.funcionalidades.Redirecionamentos;
import rent_a_car.funcionalidades.Validacoes;
import rent_a_car.paginas.menu.sub_paginas.controllers.acidente.editAcidentePageController;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import static rent_a_car.classes.Acidente.getDadosAcidentes;
import static rent_a_car.funcionalidades.Impressoes.imprimirTableView;

public class AcidentesPageController {
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<Acidente> tableView;
    @FXML
    private TableColumn<?, ?> colID;
    @FXML
    private TableColumn<Acidente, String> colCategoria;
    @FXML
    private TableColumn<?, ?> colViatura;
    @FXML
    private TableColumn<Acidente, String> colDescricao;
    @FXML
    private TableColumn<Acidente, Date> colData;
    @FXML
    private Label mensagem;

    // Buttons
    @FXML
    public void btn1(ActionEvent event) {  // Atualizar
        refreshTabela();
    }
    @FXML
    public void btn2(ActionEvent event) {  // Editar
        try {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                abrirJanela2("/rent_a_car/paginas/menu/sub_paginas/editAcidenteDialogPage.fxml", "Editar acidente");
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de editar acidentes\n " + e);
        }
    }
    @FXML
    public void btn3(ActionEvent event) {  // Adicionar
        try {
            redirecionamentos.abrirJanela("/rent_a_car/paginas/menu/sub_paginas/addAcidenteDialogPage.fxml", "Adicionar acidente");
            refreshTabela();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de adicionar acidentes\n " + e);
        }
    }
    @FXML
    public void btn4(ActionEvent event) {  // Eliminar
        // Retorna o ID do acidente selecionado na tabela se tiver algum selecionado
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            String ID = String.valueOf(tableView.getSelectionModel().getSelectedItem().getID());

            if(validacoes.infoBox("Estás prestes a eliminar o acidente de ID " + ID + ", deseja continuar?", "Atenção!")) {
                // Apagar acidente atraves do ID
                deletarAcidente(ID);
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
    private ObservableList<Acidente> data;
    private ResultSet queryResult = null;


    public void initialize() {
        refreshTabela();
    }

    private void insDadosAcidentes() {
        data = getDadosAcidentes();

        colID.setCellValueFactory(new PropertyValueFactory("ID"));
        colCategoria.setCellValueFactory(new PropertyValueFactory("categoria"));
        colViatura.setCellValueFactory(new PropertyValueFactory("viatura"));
        colDescricao.setCellValueFactory(new PropertyValueFactory("descricao"));
        colData.setCellValueFactory(new PropertyValueFactory("data"));

        mudarFormatoData(colData);

        // Ajusta o tamanho da cedula conforme o tamanho do texto
        colDescricao.setCellFactory(tc -> {
            TableCell<Acidente, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(colDescricao.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });

        colCategoria.setCellFactory(column -> {
            TableCell<Acidente, String> cell = new TableCell<Acidente, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                        setStyle("");
                    }
                    else {
                        this.setText(item);
                        if(item.equals("Muito grave")) {
                            this.setStyle("-fx-background-color: red;-fx-alignment: center; -fx-background-radius: 3px;-fx-end-margin: 2px;");
                        } else if(item.equals("Grave")) {
                            this.setStyle("-fx-background-color: orange;-fx-alignment: center; -fx-background-radius: 3px;-fx-end-margin: 2px;");
                        } else if(item.equals("Leve")) {
                            this.setStyle("-fx-background-color: yellow;-fx-alignment: center; -fx-background-radius: 3px;-fx-end-margin: 2px;");
                        } else if(item.equals("Outro (a)")) {
                            this.setStyle("-fx-background-color: #00ff88;-fx-alignment: center; -fx-background-radius: 3px;-fx-end-margin: 2px;");
                        } else {
                            this.setStyle("");
                        }

                    }
                }
            };
            return cell;
        });

        tableView.setItems(data);
    }

    private void refreshTabela() {
        insDadosAcidentes();
        listenSearchBar();
    }

    private void deletarAcidente(String acidenteID) {
        // Executar query para remover um acidente com base no ID
        try {
            if(baseDados.executarPreparedQueryUpdate("DELETE FROM acidentes WHERE acidenteID = ?;", acidenteID)) {
                exibirMensagem("O acidente de ID " + acidenteID + " foi deletado com sucesso!", Color.RED);
            } else {
                exibirMensagem("Ocorreu um erro na eliminação do acidente de ID " + acidenteID + "!", Color.RED);
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao deletar o acidente numero:" + acidenteID +  "\n " + e);
        }
    }

    private void exibirMensagem(String mensagemDisplay, Color cor) {
        try {
            mensagem.setText(mensagemDisplay);
            mensagem.setTextFill(cor);
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println("Ocorreu um erro ao tentar exibir a mensagem: " + mensagemDisplay + " na página de acidentes!");
        }
    }

    private void listenSearchBar() {
        // Transforma a ObservableList em uma FilteredList (inicialmente exibe todos os dados)
        FilteredList<Acidente> filteredData = new FilteredList<>(data, p -> true);

        // Define o filtro Predicado sempre que o filtro for alterado.
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(acidente -> {
                // Se o texto do filtro estiver vazio, exibe todas os clientes
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Faz as comparaçoes ignorando maiúsculas e minúsculas
                String lowerCaseFilter = newValue.toLowerCase();

                if (acidente.getCategoria().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtro corresponde a categoria
                } else if (acidente.getViatura().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtro corresponde a viatura
                } else if (String.valueOf(acidente.getID()).indexOf(lowerCaseFilter)!=-1) {
                    return true; // Filtro corresponde ao ID
                }
                return false; // Não há correspondencia
            });
        });

        // Transforma a FilteredList em uma SortedList.
        SortedList<Acidente> sortedData = new SortedList<>(filteredData);

        // Vincula a SortedList comparadora com a TableView comparada.
        // Caso contrário, ordenar o TableView não teria efeito.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // Adiciona os dados ordenados (e filtrados) à tabela.
        tableView.setItems(sortedData);
    }

    private void mudarFormatoData(TableColumn<Acidente, Date> colData) {
        colData.setCellFactory(column -> {
            TableCell<Acidente, Date> cell = new TableCell<Acidente, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        this.setText(format.format(item));
                    }
                }
            };
            return cell;
        });
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
        rootLoader.setLocation(this.getClass().getResource("/rent_a_car/paginas/menu/sub_paginas/editAcidenteDialogPage.fxml"));
        janela = rootLoader.load();

        // Get dos dados da viatura selecionado na tabela
        int ID = tableView.getSelectionModel().getSelectedItem().getID();
        String categoria = tableView.getSelectionModel().getSelectedItem().getCategoria();
        String viatura = tableView.getSelectionModel().getSelectedItem().getViatura();
        String descricao = tableView.getSelectionModel().getSelectedItem().getDescricao();
        Date data = tableView.getSelectionModel().getSelectedItem().getData();

        // Define o controlador para que o programa saiba qual é
        editAcidentePageController controladorCP = rootLoader.getController();

        rootWindow.setScene(new Scene(janela));
        rootWindow.setResizable(true);
        rootWindow.initModality(Modality.APPLICATION_MODAL);

        // Faz com que a tabela seja atualizada automaticamente depois de editado
        rootWindow.setOnHidden(e -> {
            refreshTabela();
        });

        // Insere os dados da viatura selecionada na tabela
        controladorCP.introduzirValoresAuto(ID, categoria, viatura, descricao, data);

        rootWindow.show();
    }
}

