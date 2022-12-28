package rent_a_car.paginas.menu.sub_paginas.controllers.reserva;

import javafx.beans.binding.Bindings;
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
import javafx.util.Callback;
import rent_a_car.classes.Reserva;
import rent_a_car.funcionalidades.ConexaoBDB;
import rent_a_car.funcionalidades.Redirecionamentos;
import rent_a_car.funcionalidades.Validacoes;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

import static rent_a_car.classes.Reserva.getDadosReservas;
import static rent_a_car.funcionalidades.Impressoes.imprimirTableView;
import static rent_a_car.funcionalidades.Validacoes.timestampToCalendar;

public class ReservasPageController2 {
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<Reserva> tableView;
    @FXML
    private TableColumn<?, ?> colID;
    @FXML
    private TableColumn<Reserva, String> colSituacao;
    @FXML
    private TableColumn<?, ?> colCliente;
    @FXML
    private TableColumn<?, ?> colViatura;
    @FXML
    private TableColumn<?, ?> colLocalEntrega;
    @FXML
    private TableColumn<Reserva, Timestamp> colDataEntrega;
    @FXML
    private TableColumn<Reserva, Timestamp> colDataDevolucao;
    @FXML
    private TableColumn<Reserva, Double> colValorTotal;
    @FXML
    private TableColumn<Reserva, Date> colDataReserva;
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
                    abrirJanela2("/rent_a_car/paginas/menu/sub_paginas/editReservaDialogPage.fxml", "Editar reserva");
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de editar reservas\n " + e);
        }
    }
    @FXML
    public void btn3(ActionEvent event) {  // Adicionar
       try {
            redirecionamentos.abrirJanela("/rent_a_car/paginas/menu/sub_paginas/addReservaDialogPage.fxml", "Adicionar reserva");
            refreshTabela();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de adicionar clientes\n " + e);
        }
    }
    @FXML
    public void btn4(ActionEvent event) {  // Eliminar
        // Retorna o ID da reserva selecionada na tabela se tiver algum selecionado
      if (tableView.getSelectionModel().getSelectedItem() != null) {
            String ID = String.valueOf(tableView.getSelectionModel().getSelectedItem().getID());

            if(validacoes.infoBox("Estás prestes a eliminar a reserva de ID " + ID + ", deseja continuar?", "Atenção!")) {
                // Apagar cliente atraves do ID
                deletarReserva(ID);
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
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private ObservableList<Reserva> data;
    private ResultSet queryResult = null;

    public void initialize() {
        refreshTabela();
        addBtnDetalhes();
    }

    private void insDadosReservas() {
        data = getDadosReservas();

        colID.setCellValueFactory(new PropertyValueFactory("ID"));
        colSituacao.setCellValueFactory(new PropertyValueFactory("situacao"));
        colCliente.setCellValueFactory(new PropertyValueFactory("cliente"));
        colViatura.setCellValueFactory(new PropertyValueFactory("viatura"));
        colLocalEntrega.setCellValueFactory(new PropertyValueFactory("localEntrega"));
        colDataEntrega.setCellValueFactory(new PropertyValueFactory("data_entrega"));
        colDataDevolucao.setCellValueFactory(new PropertyValueFactory("data_devolucao"));
        colValorTotal.setCellValueFactory(new PropertyValueFactory("valor_total"));
        colDataReserva.setCellValueFactory(new PropertyValueFactory("data_reserva"));

        mudarFormatoTimestamp(colDataEntrega);
        mudarFormatoTimestamp(colDataDevolucao);
        mudarFormatoData(colDataReserva);

        // Adiciona cores na cédula conforme o texto
        colSituacao.setCellFactory(column -> {
            TableCell<Reserva, String> cell = new TableCell<Reserva, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        this.setText(item);
                        if(item.equals("Rejeitado")) {
                            this.setStyle("-fx-background-color: red;-fx-alignment: center; -fx-background-radius: 3px;-fx-end-margin: 2px;");
                        } else if(item.equals("Aceito")) {
                            this.setStyle("-fx-background-color: #00ff37;-fx-alignment: center; -fx-background-radius: 3px;-fx-end-margin: 2px;");
                        } else if(item.equals("Em análise")) {
                            this.setStyle("-fx-background-color: #0073ff;-fx-alignment: center; -fx-background-radius: 3px;-fx-end-margin: 2px;");
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

        // Formata os valores da coluna de valor total para apenas 2 casas decimais
        colValorTotal.setCellFactory(tc -> new TableCell<Reserva, Double>() {

            @Override
            protected void updateItem(Double preco, boolean empty) {
                super.updateItem(preco, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(df2.format(preco));
                }
            }
        });

        tableView.setItems(data);
    }

    private void addBtnDetalhes() {
        TableColumn<Reserva, Void> colBtn = new TableColumn("");

        Callback<TableColumn<Reserva, Void>, TableCell<Reserva, Void>> cellFactory = new Callback<TableColumn<Reserva, Void>, TableCell<Reserva, Void>>() {
            @Override
            public TableCell<Reserva, Void> call(final TableColumn<Reserva, Void> param) {
                final TableCell<Reserva, Void> cell = new TableCell<Reserva, Void>() {

                    private final Button btn = new Button("↗");

                    // Funcao do botao
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            String cliente = getTableView().getItems().get(getIndex()).getCliente();
                            System.out.println(cliente);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                            btn.setStyle("-fx-background-color: #ffc800");
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        colBtn.setMaxWidth(1000);
        tableView.getColumns().add(colBtn);
    }

    private void refreshTabela() {
        insDadosReservas();
        listenSearchBar();
    }

    private void deletarReserva(String reservaID) {
        // Executar query para deletar uma reserva com base no ID
        try {
            if(baseDados.executarPreparedQueryUpdate("DELETE FROM reservas_extras WHERE reservas_reservaID = ?;", reservaID)) {
                if(baseDados.executarPreparedQueryUpdate("DELETE FROM reservas WHERE reservaID = ?;", reservaID)) {
                    exibirMensagem("A reserva de ID " + reservaID + " foi deletada com sucesso!", Color.RED);
                }
            } else {
                exibirMensagem("Ocorreu um erro na eliminação da reserva de ID " + reservaID + "!", Color.RED);
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao deletar a reserva numero:" + reservaID +  "\n " + e);
        }
    }

    private void exibirMensagem(String mensagemDisplay, Color cor) {
        try {
            mensagem.setText(mensagemDisplay);
            mensagem.setTextFill(cor);
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println("Ocorreu um erro ao tentar exibir a mensagem: " + mensagemDisplay + " na página de reservas!");
        }
    }

    private void listenSearchBar() {
        // Transforma a ObservableList em uma FilteredList (inicialmente exibe todos os dados)
        FilteredList<Reserva> filteredData = new FilteredList<>(data, p -> true);

        // Define o filtro Predicado sempre que o filtro for alterado.
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(reserva -> {
                // Se o texto do filtro estiver vazio, exibe todas as reservas
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Faz as comparaçoes ignorando maiúsculas e minúsculas
                String lowerCaseFilter = newValue.toLowerCase();

                if (reserva.getCliente().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtro corresponde ao cliente
                } else if (reserva.getViatura().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtro corresponde a viatura
                } else if (String.valueOf(reserva.getID()).indexOf(lowerCaseFilter)!=-1) {
                    return true; // Filtro corresponde ao ID
                }
                return false; // Não há correspondencia
            });
        });

        // Transforma a FilteredList em uma SortedList.
        SortedList<Reserva> sortedData = new SortedList<>(filteredData);

        // Vincula a SortedList comparadora com a TableView comparada.
        // Caso contrário, ordenar o TableView não teria efeito.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // Adiciona os dados ordenados (e filtrados) à tabela.
        tableView.setItems(sortedData);
    }

    private void mudarFormatoData(TableColumn<Reserva, Date> colData) {
        colData.setCellFactory(column -> {
            TableCell<Reserva, Date> cell = new TableCell<Reserva, Date>() {
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

    private void mudarFormatoTimestamp(TableColumn<Reserva, Timestamp> colTimestamp) {
        colTimestamp.setCellFactory(column -> {
            TableCell<Reserva, Timestamp> cell = new TableCell<Reserva, Timestamp>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                @Override
                protected void updateItem(Timestamp item, boolean empty) {
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
        rootLoader.setLocation(this.getClass().getResource("/rent_a_car/paginas/menu/sub_paginas/editReservaDialogPage.fxml"));
        janela = rootLoader.load();

        // Get dos dados da Precario selecionado na tabela
        int ID = tableView.getSelectionModel().getSelectedItem().getID();
        String situacao = tableView.getSelectionModel().getSelectedItem().getSituacao();
        String cliente  = tableView.getSelectionModel().getSelectedItem().getCliente();
        String viatura  = tableView.getSelectionModel().getSelectedItem().getViatura();
        String seguro   = tableView.getSelectionModel().getSelectedItem().getSeguro();
        String local_entrega = tableView.getSelectionModel().getSelectedItem().getLocalEntrega();
        String local_devolucao = tableView.getSelectionModel().getSelectedItem().getLocalDevolucao();
        Timestamp data_entregaB = tableView.getSelectionModel().getSelectedItem().getData_entrega();
        Timestamp data_devolucaoB  = tableView.getSelectionModel().getSelectedItem().getData_devolucao();
        Date data_entrega  = new Date(data_entregaB.getTime());
        Date data_devolucao = new Date(data_devolucaoB.getTime());
        int segCondutorQtd = tableView.getSelectionModel().getSelectedItem().getExtra_segCondutor();
        int assentoQtd     = tableView.getSelectionModel().getSelectedItem().getExtra_assento();
        int cadeiraBBQtd   = tableView.getSelectionModel().getSelectedItem().getExtra_cadeiraBB();
        int ovinhoQtd      = tableView.getSelectionModel().getSelectedItem().getExtra_ovinho();
        double valor_extras = tableView.getSelectionModel().getSelectedItem().getValor_extras();
        double taxa_aero = tableView.getSelectionModel().getSelectedItem().getTaxa_aeroporto();
        double taxa_misc  = tableView.getSelectionModel().getSelectedItem().getMisc_taxas();
        int desconto   = (int) tableView.getSelectionModel().getSelectedItem().getDesconto();
        double valor_diario = tableView.getSelectionModel().getSelectedItem().getValor_diario();
        double valor_total  = tableView.getSelectionModel().getSelectedItem().getValor_total();
        Date data_reserva = tableView.getSelectionModel().getSelectedItem().getData_reserva();

        // Define o controlador para que o programa saiba qual é
        editReservaPageController controladorCP = rootLoader.getController();

        rootWindow.setScene(new Scene(janela));
        rootWindow.setResizable(true);
        rootWindow.initModality(Modality.APPLICATION_MODAL);

        // Faz com que a tabela seja atualizada automaticamente depois de editado
        rootWindow.setOnHidden(e -> {
            refreshTabela();
        });

        // Insere os dados da Precario selecionada na tabela
        controladorCP.introduzirValoresAuto(ID, situacao, cliente, viatura, seguro, local_entrega, local_devolucao, data_entrega.toString(), data_devolucao.toString(), timestampToCalendar(data_entregaB), timestampToCalendar(data_devolucaoB), segCondutorQtd, assentoQtd, cadeiraBBQtd, ovinhoQtd, String.valueOf(taxa_aero), String.valueOf(taxa_misc), valor_extras, desconto, String.valueOf(data_reserva));

        rootWindow.show();
    }
}
