package rent_a_car.paginas.menu.controllers;

import javafx.collections.FXCollections;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import rent_a_car.classes.Viatura;
import rent_a_car.funcionalidades.ConexaoBDB;
import rent_a_car.funcionalidades.Redirecionamentos;
import rent_a_car.funcionalidades.Validacoes;
import rent_a_car.paginas.menu.sub_paginas.controllers.viatura.editViaturaPageController;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import static rent_a_car.funcionalidades.Impressoes.imprimirTableView;


public class ViaturasPageController {

    @FXML
    private TextField searchBar;
    @FXML
    private TableView<Viatura> tableView;
    @FXML
    private TableColumn<?, ?> colID;
    @FXML
    private TableColumn<?, ?> colGrupo;
    @FXML
    private TableColumn<?, ?> colMarca;
    @FXML
    private TableColumn<?, ?> colModelo;
    @FXML
    private TableColumn<?, ?> colAno;
    @FXML
    private TableColumn<?, ?> colCombustivel;
    @FXML
    private TableColumn<?, ?> colCaixaVel;
    @FXML
    private TableColumn<Viatura, Date> colFimGarantia;
    @FXML
    private TableColumn<?, ?> colPortas;
    @FXML
    private TableColumn<?, ?> colLugares;
    @FXML
    private TableColumn<?, ?> colMatricula;
    @FXML
    private TableColumn<Viatura, Date> colProxInspecao;
    @FXML
    private TableColumn<?, ?> colTanqueComb;
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
                abrirJanela2("/rent_a_car/paginas/menu/sub_paginas/editViaturaDialogPage.fxml", "Editar viatura");
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de editar viatura\n " + e);
        }
    }
    @FXML
    public void btn3(ActionEvent event) {  // Adicionar
        try {
            redirecionamentos.abrirJanela("/rent_a_car/paginas/menu/sub_paginas/addViaturaDialogPage.fxml", "Adicionar viatura");
            refreshTabela();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de adicionar viaturas\n " + e);
        }
    }
    @FXML
    public void btn4(ActionEvent event) {  // Eliminar
        // Retorna o ID da viatura selecionada na tabela se tiver algum selecionado
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            String ID = String.valueOf(tableView.getSelectionModel().getSelectedItem().getID());

            if(validacoes.infoBox("Estás prestes a eliminar a viatura de ID " + ID + ", deseja continuar?", "Atenção!")) {
                // Apagar viatura atraves do ID
                deletarViatura(ID);
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
    private ObservableList<Viatura> data;
    private ResultSet queryResult = null;


    public void initialize() {
        refreshTabela();
    }

    public void getDadosViaturas() {
        data = FXCollections.observableArrayList();

        try {
            // Executar query para obter dados de todos os viaturas
            queryResult = baseDados.executarQuery("SELECT * FROM viaturas");

            while(queryResult.next()){
                data.add(new Viatura(
                        queryResult.getInt("viaturaID"),
                        queryResult.getString("precario_grupo"),
                        queryResult.getString("marca"),
                        queryResult.getString("modelo"),
                        queryResult.getInt("ano"),
                        queryResult.getString("tipo_combustivel"),
                        queryResult.getString("caixa_velocidades"),
                        queryResult.getDate("data_garantia"),
                        queryResult.getInt("portas"),
                        queryResult.getInt("lugares"),
                        queryResult.getString("matricula"),
                        queryResult.getDate("prox_inspecao"),
                        queryResult.getString("tanque_combustivel")
                ));
            }

            colID.setCellValueFactory(new PropertyValueFactory("ID"));
            colGrupo.setCellValueFactory(new PropertyValueFactory("grupo"));
            colMarca.setCellValueFactory(new PropertyValueFactory("marca"));
            colModelo.setCellValueFactory(new PropertyValueFactory("modelo"));
            colAno.setCellValueFactory(new PropertyValueFactory("ano"));
            colCombustivel.setCellValueFactory(new PropertyValueFactory("combustivel"));
            colCaixaVel.setCellValueFactory(new PropertyValueFactory("caixa_vel"));
            colFimGarantia.setCellValueFactory(new PropertyValueFactory("fim_garantia"));
            colPortas.setCellValueFactory(new PropertyValueFactory("portas"));
            colLugares.setCellValueFactory(new PropertyValueFactory("lugares"));
            colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
            colProxInspecao.setCellValueFactory(new PropertyValueFactory("prox_inspecao"));
            colTanqueComb.setCellValueFactory(new PropertyValueFactory("tanque_combustivel"));

            mudarFormatoData(colFimGarantia);
            mudarFormatoData(colProxInspecao);

            tableView.setItems(data);

        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao processar e inserir os dados da query de Viaturas:\n " + e);
        }
    }

    public void refreshTabela() {
        getDadosViaturas();
        listenSearchBar();
    }

    private void deletarViatura(String viaturaID) {
        // Executar query para deletar uma viatura com base no ID
        try {
            if(baseDados.executarPreparedQueryUpdate("DELETE FROM viaturas WHERE viaturaID = ?;", viaturaID)) {
                exibirMensagem("A viatura de ID " + viaturaID + " foi deletada com sucesso!", Color.RED);
            } else {
                exibirMensagem("Ocorreu um erro na eliminação da viatura de ID " + viaturaID + "!", Color.RED);
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao deletar a viatura numero:" + viaturaID +  "\n " + e);
        }
    }

    public void exibirMensagem(String mensagemDisplay, Color cor) {
        try {
            mensagem.setText(mensagemDisplay);
            mensagem.setTextFill(cor);
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println("Ocorreu um erro ao tentar exibir a mensagem: " + mensagemDisplay + " na página de viaturas!");
        }
    }

    private void listenSearchBar() {
        // Transforma a ObservableList em uma FilteredList (inicialmente exibe todos os dados)
        FilteredList<Viatura> filteredData = new FilteredList<>(data, p -> true);

        // Define o filtro Predicado sempre que o filtro for alterado.
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(viatura -> {
                // Se o texto do filtro estiver vazio, exibe todas as viaturas
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Faz as comparaçoes ignorando maiúsculas e minúsculas
                String lowerCaseFilter = newValue.toLowerCase();

                if (viatura.getModelo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtro corresponde ao modelo
                } else if (String.valueOf(viatura.getAno()).indexOf(lowerCaseFilter)!=-1) {
                    return true; // Filtro corresponde ao ano
                } else if (viatura.getMatricula().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtro corresponde a matricula
                } else if (String.valueOf(viatura.getID()).indexOf(lowerCaseFilter)!=-1) {
                        return true; // Filtro corresponde ao ID
                }
                return false; // Não há correspondencia
            });
        });

        // Transforma a FilteredList em uma SortedList.
        SortedList<Viatura> sortedData = new SortedList<>(filteredData);

        // Vincula a SortedList comparadora com a TableView comparada.
        // Caso contrário, ordenar o TableView não teria efeito.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // Adiciona os dados ordenados (e filtrados) à tabela.
        tableView.setItems(sortedData);
    }

    private void mudarFormatoData(TableColumn<Viatura, Date> colData) {
        colData.setCellFactory(column -> {
            TableCell<Viatura, Date> cell = new TableCell<Viatura, Date>() {
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
        rootLoader.setLocation(this.getClass().getResource("/rent_a_car/paginas/menu/sub_paginas/editViaturaDialogPage.fxml"));
        janela = rootLoader.load();

        // Get dos dados da viatura selecionado na tabela
        int ID = tableView.getSelectionModel().getSelectedItem().getID();
        String grupo = tableView.getSelectionModel().getSelectedItem().getGrupo();
        String marca = tableView.getSelectionModel().getSelectedItem().getMarca();
        String modelo = tableView.getSelectionModel().getSelectedItem().getModelo();
        int ano = tableView.getSelectionModel().getSelectedItem().getAno();
        String tipo_comb = tableView.getSelectionModel().getSelectedItem().getCombustivel();
        String caixa_vel = tableView.getSelectionModel().getSelectedItem().getCaixa_vel();
        Date data_garantia = tableView.getSelectionModel().getSelectedItem().getFim_garantia();
        int num_portas = tableView.getSelectionModel().getSelectedItem().getPortas();
        int num_lugares = tableView.getSelectionModel().getSelectedItem().getLugares();
        String matricula = tableView.getSelectionModel().getSelectedItem().getMatricula();
        Date prox_inspecao = tableView.getSelectionModel().getSelectedItem().getProx_inspecao();
        String tanque_combustivel = tableView.getSelectionModel().getSelectedItem().getTanque_combustivel();

        // Define o controlador para que o programa saiba qual é
        editViaturaPageController controladorCP = rootLoader.getController();

        rootWindow.setScene(new Scene(janela));
        rootWindow.setResizable(true);
        rootWindow.initModality(Modality.APPLICATION_MODAL);

        // Faz com que a tabela seja atualizada automaticamente depois de editado
        rootWindow.setOnHidden(e -> {
            refreshTabela();
        });

        // Insere os dados da viatura selecionada na tabela
        controladorCP.introduzirValoresAuto(ID, grupo, marca, modelo, ano, tipo_comb, caixa_vel, data_garantia, num_portas, num_lugares, matricula, prox_inspecao, tanque_combustivel);

        rootWindow.show();
    }
}
