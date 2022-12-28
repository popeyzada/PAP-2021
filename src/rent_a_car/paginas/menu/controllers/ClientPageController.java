package rent_a_car.paginas.menu.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rent_a_car.classes.Cliente;
import rent_a_car.funcionalidades.ConexaoBDB;
import rent_a_car.funcionalidades.Redirecionamentos;
import rent_a_car.funcionalidades.Validacoes;
import rent_a_car.paginas.menu.sub_paginas.controllers.cliente.editClientPageController;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static rent_a_car.funcionalidades.Impressoes.imprimirTableView;

public class ClientPageController implements Initializable {
    // Text Fields
    @FXML
    private TextField searchBar;

    // Tabela
    @FXML
    public TableView<Cliente> tableView;
    @FXML
    private TableColumn<Cliente, Integer> colID;
    @FXML
    private TableColumn<Cliente, String> colNome;
    @FXML
    private TableColumn<Cliente, Integer> colContato;
    @FXML
    private TableColumn<Cliente, Date> colDataNascimento;
    @FXML
    private TableColumn<?, ?> colGenero;
    @FXML
    private TableColumn<Cliente, String> colCidade;
    @FXML
    private TableColumn<Cliente, String> colMorada;
    @FXML
    private TableColumn<Cliente, String> colCodigoPostal;
    @FXML
    private TableColumn<Cliente, String> colPais;
    @FXML
    private TableColumn<Cliente, Integer> colNCartaDeConducao;
    @FXML
    private TableColumn<Cliente, String> colCartaDeConducalVAL;
    @FXML
    private TableColumn<Cliente, Integer> colNumPassaporte;
    @FXML
    private TableColumn<Cliente, Integer> colContribuinte;
    @FXML
    private TableColumn<Cliente, String> colEmail;
    @FXML
    private TableColumn<Cliente, String> colEmpresa;
    @FXML
    public Label mensagem;


    // Buttons
    @FXML
    public void btn1(ActionEvent event) {  // Atualizar
        refreshTabela();
    }
    @FXML
    public void btn2(ActionEvent event) {  // Editar
        try {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                abrirJanela2("/rent_a_car/paginas/menu/sub_paginas/editClientDialogPage.fxml", "Editar cliente");
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de editar clientes\n " + e);
        }
    }
    @FXML
    public void btn3(ActionEvent event) {  // Adicionar
        try {
            redirecionamentos.abrirJanela("/rent_a_car/paginas/menu/sub_paginas/addClientDialogPage.fxml", "Adicionar cliente");
            refreshTabela();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao tentar abrir a janela de adicionar clientes\n " + e);
        }
    }
    @FXML
    public void btn4(ActionEvent event) {  // Eliminar
        // Retorna o ID do cliente selecionado na tabela se tiver algum selecionado
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            String ID = String.valueOf(tableView.getSelectionModel().getSelectedItem().getID());

            if(validacoes.infoBox("Estás prestes a eliminar o cliente de ID " + ID + ", deseja continuar?", "Atenção!")) {
                // Apagar cliente atraves do ID
                deletarCliente(ID);
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
    private ObservableList<Cliente> data;
    private ResultSet queryResult = null;


    public void initialize(URL url, ResourceBundle rb) {
        refreshTabela();
    }

    public void getDadosClientes() {
        data = FXCollections.observableArrayList();

        try {
            // Executar query para obter dados de todos os clientes
            queryResult = baseDados.executarQuery("SELECT * FROM clientes");

            while(queryResult.next()){
                data.add(new Cliente(
                        queryResult.getInt("clienteID"),
                        "",
                        -1,
                        queryResult.getDate("data_nascimento"),
                        queryResult.getString("genero"),
                        queryResult.getString("cidade"),
                        queryResult.getString("morada"),
                        queryResult.getString("codigo_postal"),
                        queryResult.getString("pais"),
                        queryResult.getString("carta_conducao"),
                        queryResult.getString("validade_carta_conducao"),
                        queryResult.getString("ID_passaporte"),
                        queryResult.getInt("contribuinte"),
                        queryResult.getString("email"),
                        queryResult.getString("empresa")
                ));
            }

            // Insirá os nomes e os contatos apartir de outra query dentro do arrayList
            // Executar query para obter dados de todos os clientes exclusivamente sobre nome e numero de contato
            queryResult = baseDados.executarQuery("SELECT contatos.nome, contatos.contato FROM clientes INNER JOIN contatos ON clientes.contatos_contatoID=contatos.contatoID;");

            int i = 0;
            while(queryResult.next()){
                data.get(i).setNome(queryResult.getString("nome"));
                data.get(i).setContato(queryResult.getInt("contato"));
                i++;
            }

            // Executar query para obter os clientes blacklisteds e pegar seus respetivos IDs
            queryResult = baseDados.executarQuery("SELECT clienteID FROM lista_negra;");

            ArrayList<Integer> IDclientesBL = new ArrayList<Integer>();

            i = 0;
            while(queryResult.next()) {
                IDclientesBL.add(queryResult.getInt("clienteID"));
                i++;
            }

            colID.setCellValueFactory(new PropertyValueFactory("ID"));
            colNome.setCellValueFactory(new PropertyValueFactory("nome"));
            colContato.setCellValueFactory(new PropertyValueFactory("contato"));
            colDataNascimento.setCellValueFactory(new PropertyValueFactory("data_nascimento"));
            colGenero.setCellValueFactory(new PropertyValueFactory("genero"));
            colCidade.setCellValueFactory(new PropertyValueFactory("cidade"));
            colMorada.setCellValueFactory(new PropertyValueFactory("morada"));
            colCodigoPostal.setCellValueFactory(new PropertyValueFactory("codigo_postal"));
            colPais.setCellValueFactory(new PropertyValueFactory("pais"));
            colNCartaDeConducao.setCellValueFactory(new PropertyValueFactory("num_carta_conducao"));
            colCartaDeConducalVAL.setCellValueFactory(new PropertyValueFactory("carta_conducaoVAL"));
            colNumPassaporte.setCellValueFactory(new PropertyValueFactory("num_passaporte"));
            colContribuinte.setCellValueFactory(new PropertyValueFactory("contribuinte"));
            colEmail.setCellValueFactory(new PropertyValueFactory("email"));
            colEmpresa.setCellValueFactory(new PropertyValueFactory("empresa"));

            mudarFormatoData(colDataNascimento);

            tableView.setItems(data);

            // Mudará a cor da linha caso o cliente esteja na lista negra
            tableView.setRowFactory(tv -> new TableRow<Cliente>() {
                @Override
                protected void updateItem(Cliente item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || item.getNome() == null) {
                        setStyle("");
                    } else if (item != null) {
                        for(int x = 0; x < IDclientesBL.size(); x++) {
                            if(item.getID() == IDclientesBL.get(x)) {
                                setStyle("-fx-background-color: #ffbbbb");
                            }
                        }
                    } else {
                        setStyle("");
                    }
                }
            });

        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao processar e inserir os dados da query de Clientes:\n " + e);
        }
    }

    public void refreshTabela() {
        getDadosClientes();
        listenSearchBar();
    }

    private void deletarCliente(String clienteID) {
        // Executar query para obter dados de todos os clientes
        try {
            if(baseDados.executarPreparedQueryUpdate("DELETE FROM clientes WHERE clienteID = ?;", clienteID)) {
                exibirMensagem("O cliente de ID " + clienteID + " foi deletado com sucesso!", Color.RED);
            } else {
                exibirMensagem("Ocorreu um erro na eliminação do cliente de ID " + clienteID + "!", Color.RED);
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao deletar o Cliente numero:" + clienteID +  "\n " + e);
        }
    }

    public void exibirMensagem(String mensagemDisplay, Color cor) {
        try {
            mensagem.setText(mensagemDisplay);
            mensagem.setTextFill(cor);
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println("Ocorreu um erro ao tentar exibir a mensagem: " + mensagemDisplay + " na página de clientes!");
        }
    }

    private void listenSearchBar() {
        // Transforma a ObservableList em uma FilteredList (inicialmente exibe todos os dados)
        FilteredList<Cliente> filteredData = new FilteredList<>(data, p -> true);

        // Define o filtro Predicado sempre que o filtro for alterado.
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cliente -> {
                // Se o texto do filtro estiver vazio, exibe todas os clientes
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Faz as comparaçoes ignorando maiúsculas e minúsculas
                String lowerCaseFilter = newValue.toLowerCase();

                if (cliente.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtro corresponde ao nome.
                } else if (String.valueOf(cliente.getContribuinte()).indexOf(lowerCaseFilter)!=-1) {
                    return true; // Filtro corresponde ao contribuinte
                } else if (String.valueOf(cliente.getContato()).indexOf(lowerCaseFilter)!=-1) {
                    return true; // Filtro corresponde ao contato
                } else if (String.valueOf(cliente.getID()).indexOf(lowerCaseFilter)!=-1) {
                    return true; // Filtro corresponde ao ID
                } else if (cliente.getNum_passaporte().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtro corresponde ao passaporte
                }
                return false; // Não há correspondencia
            });
        });

        // Transforma a FilteredList em uma SortedList.
        SortedList<Cliente> sortedData = new SortedList<>(filteredData);

        // Vincula a SortedList comparadora com a TableView comparada.
        // Caso contrário, ordenar o TableView não teria efeito.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // Adiciona os dados ordenados (e filtrados) à tabela.
        tableView.setItems(sortedData);
    }

    private void mudarFormatoData(TableColumn<Cliente, Date> colData) {
        colData.setCellFactory(column -> {
            TableCell<Cliente, Date> cell = new TableCell<Cliente, Date>() {
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
        rootLoader.setLocation(this.getClass().getResource("/rent_a_car/paginas/menu/sub_paginas/editClientDialogPage.fxml"));
        janela = rootLoader.load();

        // Get dos dados do cliente selecionado na tabela
        int ID = tableView.getSelectionModel().getSelectedItem().getID();
        String nomeCompleto = tableView.getSelectionModel().getSelectedItem().getNome();
        int contato = tableView.getSelectionModel().getSelectedItem().getContato();
        Date data_nascimento = tableView.getSelectionModel().getSelectedItem().getData_nascimento();
        String gen = tableView.getSelectionModel().getSelectedItem().getGenero();
        String cidade = tableView.getSelectionModel().getSelectedItem().getCidade();
        String morada =tableView.getSelectionModel().getSelectedItem().getMorada();
        String codigo_postal = tableView.getSelectionModel().getSelectedItem().getCodigo_postal();
        String pais = tableView.getSelectionModel().getSelectedItem().getPais();
        String num_carta_conducao = tableView.getSelectionModel().getSelectedItem().getNum_carta_conducao();
        String carta_conducaoVAL = tableView.getSelectionModel().getSelectedItem().getCarta_conducaoVAL();
        String num_passaporte = tableView.getSelectionModel().getSelectedItem().getNum_passaporte();
        int contribuinte = tableView.getSelectionModel().getSelectedItem().getContribuinte();
        String email = tableView.getSelectionModel().getSelectedItem().getEmail();

        // Define o controlador para que o programa saiba qual é
        editClientPageController controladorCP = rootLoader.getController();

        rootWindow.setScene(new Scene(janela));
        rootWindow.setResizable(true);
        rootWindow.initModality(Modality.APPLICATION_MODAL);

        // Faz com que a tabela seja atualizada automaticamente depois de editado
        rootWindow.setOnHidden(e -> {
            refreshTabela();
        });

        // Insere os dados do cliente selecionado na tabela
        controladorCP.introduzirValoresAuto(ID, nomeCompleto, contato, data_nascimento, gen, cidade, morada, codigo_postal, pais, num_carta_conducao, carta_conducaoVAL, num_passaporte, contribuinte, email);

        rootWindow.show();
    }
}
