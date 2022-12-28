package rent_a_car.paginas.menu.sub_paginas.controllers.reserva;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.CalendarTimeTextField;
import rent_a_car.classes.Extra;
import rent_a_car.funcionalidades.ConexaoBDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import static rent_a_car.classes.Acidente.getViaturaID;
import static rent_a_car.classes.Extra.getExtras;
import static rent_a_car.classes.Extra.getPrecoExtras;
import static rent_a_car.classes.Reserva.*;
import static rent_a_car.funcionalidades.Validacoes.DataHoraToTimeStamp;
import static rent_a_car.funcionalidades.Validacoes.setCampoDouble;

public class addReservaPageController {
    @FXML
    private Label generoLabel;
    @FXML
    private ComboBox<String> cliente;
    @FXML
    private JFXButton cancelar;
    @FXML
    private JFXButton submeter;
    @FXML
    private Label erro;
    @FXML
    private DatePicker data_devolucao;
    @FXML
    private ComboBox<String> local_entrega;
    @FXML
    private ComboBox<String> local_devolucao;
    @FXML
    private ComboBox<String> viatura;
    @FXML
    private ComboBox<String> seguro;
    @FXML
    private ComboBox<String> situacao;
    @FXML
    private DatePicker data_entrega;
    @FXML
    private CalendarTimeTextField hora_entrega;
    @FXML
    private CalendarTimeTextField hora_devolucao;
    @FXML
    private TableView<Extra> extras;
    @FXML
    private TableColumn<Extra, String> extra;
    @FXML
    private Spinner<Integer> percentagemdesconto;
    @FXML
    private Spinner<Integer> percentagemIVA;
    @FXML
    private TextField taxa_diversas;
    @FXML
    private TextField taxa_aeroporto;
    @FXML
    private DatePicker data_reserva;
    @FXML
    private Label valor_extra;
    @FXML
    private Label valor_diario;
    @FXML
    private Label valorTotal_sDesconto;
    @FXML
    private Label valor_total;

    @FXML
    void btn1(ActionEvent event) {   // Cancelar
        try {
            fecharJanela(event);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar fechar a janela de adicionar reservas\n " + e);
        }
    }

    @FXML
    void btn2(ActionEvent event) {   // Submeter
        if(situacao.getValue() == null || cliente.getValue() == null || viatura.getValue() == null || seguro.getValue() == null || local_entrega.getValue() == null || local_devolucao.getValue() == null || data_entrega.getValue() == null || data_devolucao.getValue() == null || hora_entrega.dateFormatProperty().toString().isEmpty() || hora_devolucao.dateFormatProperty().toString().isEmpty() || data_reserva.getValue() == null) {
            erro.setText("Há campos obrigatórios em branco!");
        } else {
            erro.setText("");

            if(addReserva()) {
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

    // Declaracao de variaveis/objetos
    private ObservableList<Extra> data;
    private ObservableList<Double> precoExtras;
    private ArrayList<Integer> qtdExtra = new ArrayList<Integer>();
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    // Variaveis para o programa calcular nos listeners
    private double extras_total        = 0;
    private double valor_diario_total  = 0;
    private double valor_total_total   = 0;
    private double valorsd_total_total = 0;
    private double valor_desconto      = 0;
    private double valor_taxaaero      = 0;
    private double valor_taxasdiv      = 0;
    private double valor_viatura       = 0;
    private double valor_seguro        = 0;
    private double valor_dias_total    = 0;

    // Declaracao de outras classes
    private static ConexaoBDB baseDados = new ConexaoBDB();

    public void initialize() {
        listenersValorTotal();
        insDadosExtras();
        insSpnQtd();

        setCampoDouble(taxa_aeroporto);
        setCampoDouble(taxa_diversas);

        // Desabilitar a reordem da tableview pois pode ocorrer confunsões nas quantidades de cada extra
        extra.setSortable(false);

        // Adicionar clientes existentes com o ID entre parentes na combobox cliente
        ObservableList<String> listaClientes = getNClientes();
        cliente.setItems(listaClientes);

        // Adicionar viaturas existentes com a matricula entre parentes na combobox viatura
        ObservableList<String> listaViaturas = getNViaturas();
        viatura.setItems(listaViaturas);

        // Adicionar seguros existentes na combobox seguro
        ObservableList<String> listaSeguros = getNSeguros();
        seguro.setItems(listaSeguros);

        // Adicionar situacoes na combobox situacoes
        ObservableList<String> listaSituacao = FXCollections.observableArrayList();

        listaSituacao.setAll("Em análise", "Aceito", "Rejeitado", "Outro (a)");
        situacao.setItems(listaSituacao);

        // Adicionar locais de entrega existentes na combobox local_entrega
        ObservableList<String> listaLocEntrega = getNLocEntregas();
        local_entrega.setItems(listaLocEntrega);
        local_devolucao.setItems(listaLocEntrega);

        this.percentagemIVA.getValueFactory().setValue(18);
        this.percentagemdesconto.getValueFactory().setValue(0);
    }

    private void insDadosExtras() {
        data = getExtras();

        extra.setCellValueFactory(new PropertyValueFactory<>("nome"));
        extras.setItems(data);

        // Insere os precos no arrayList para o programa ter o valor para calcular depois
        precoExtras = getPrecoExtras();

        // Definir como 0 a quantidade de cada extra inicialmente
        for(int i = 0; i < precoExtras.size(); i++) {
            qtdExtra.add(i);
            qtdExtra.set(i, 0);
        }
    }

    private void listenersValorTotal() {
        // Listener no valor diario que faz mudar o preco no valor total sem desconto
        valor_diario.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                // Formata as ',' em '.' para o programa conseguir converter a string em double
                String newV = newValue.replaceAll(",", ".");
                String oldV = oldValue.replaceAll(",", ".");

                double nV = Double.parseDouble(newV.replaceAll(" €", "")), oV = Double.parseDouble(oldV.replaceAll(" €", ""));
                if(nV > oV) {
                   valor_diario_total  = nV;
                   valorsd_total_total -= oV;
                   valorsd_total_total += valor_diario_total;
               } else if(nV < oV) {
                   valor_diario_total  = nV;
                   valorsd_total_total -= oV;
                   valorsd_total_total += valor_diario_total;
               }
                valorTotal_sDesconto.setText(df2.format(valorsd_total_total) + " €");
                atualizarCampos();
            }
        });

        // Listener no valor total sem desconto que faz mudar o preco no valor total
        valorTotal_sDesconto.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                // Formata as ',' em '.' para o programa conseguir converter a string em double
                String newV = newValue.replaceAll(",", ".");
                String oldV = oldValue.replaceAll(",", ".");

                double nV = Double.parseDouble(newV.replaceAll(" €", "")), oV = Double.parseDouble(oldV.replaceAll(" €", ""));

                if(nV > oV) {
                    valor_total_total = (valor_total_total - oV) + nV;
                } else if(nV < oV) {
                    valor_total_total = (valor_total_total - nV) + oV;
                }
                valor_total.setText(df2.format(valor_total_total) + " €");
                atualizarCampos();
            }
        });

        // Listener na taxa de aeroporto que faz mudar o preco no valor total sem desconto
        taxa_aeroporto.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                try {
                    if(!newValue.isEmpty()) {
                        valorsd_total_total -= valor_taxaaero;
                        valor_taxaaero = Double.parseDouble(taxa_aeroporto.getText());
                        valorsd_total_total += valor_taxaaero;

                        valorTotal_sDesconto.setText(df2.format(valorsd_total_total) + " €");
                    }

                } catch(NumberFormatException e) {
                    e.getCause();
                }
            }
        });

        // Listener nas taxas diversas que faz mudar o preco no valor total sem desconto
        taxa_diversas.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                try {
                    if(!newValue.isEmpty()) {
                        valorsd_total_total -= valor_taxasdiv;
                        valor_taxasdiv = Double.parseDouble(taxa_diversas.getText());
                        valorsd_total_total += valor_taxasdiv;

                        valorTotal_sDesconto.setText(df2.format(valorsd_total_total) + " €");
                    }

                } catch(NumberFormatException e) {
                    e.getCause();
                }
            }
        });

        // Listener na combobox viatura para obter o preço de cada viatura selecionada que faz mudar o preco diário
        viatura.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                String matricula = newValue.substring(newValue.indexOf("(")+1, newValue.indexOf(")"));

                valor_diario_total -= valor_viatura;
                valor_viatura = getPrecoViatura(matricula);
                valor_diario_total += valor_viatura;
                valor_diario.setText(df2.format(valor_diario_total) + " €");

                atualizarCampos();
                }
        );

        // Listener na combobox seguro para obter o preço de cada seguro selecionado que faz mudar o preco diário
        seguro.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                valor_diario_total -= valor_seguro;
                valor_seguro = getPrecoSeguro(newValue);
                valor_diario_total += valor_seguro;
                valor_diario.setText(df2.format(valor_diario_total) + " €");

                atualizarCampos();
            }
        );

        // Listener no datepicker data_entrega que desabilita as datas de devolucao que são impossíveis para uma reserva e tenta o calculo dos dias
        data_entrega.valueProperty().addListener((ov, oldValue, newValue) -> {
            disableData(newValue, data_devolucao, false);
            calcDias();
        });

        // Listener no datepicker data_devolucao que desabilita as datas de entrega que são impossíveis para uma reserva e tenta o calculo dos dias
        data_devolucao.valueProperty().addListener((ov, oldValue, newValue) -> {
            disableData(newValue, data_entrega, true);
            calcDias();
        });


        // Padrao para os spinners
        final SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        final SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);

        percentagemIVA.setValueFactory(valueFactory1);
        percentagemdesconto.setValueFactory(valueFactory2);

        // Listener no desconto que faz mudar o preco no valor total
        percentagemdesconto.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            double nV, oV;

            nV = Double.parseDouble(newValue);
            oV = Double.parseDouble(oldValue);

            if(nV == 100) {
                valor_total_total = 0;
            } else if(nV == 0) {
                valor_total_total = valorsd_total_total;
            } else if (nV > oV) {
                valor_desconto = (valorsd_total_total * (nV/100));
                valor_total_total = valorsd_total_total - valor_desconto;
            } else if(nV < oV) {
                valor_desconto = (valorsd_total_total * (oV/100));
                valor_total_total = valorsd_total_total - valor_desconto;
            }

            valor_total.setText(df2.format(valor_total_total) + " €");
        });
    }

    private void insSpnQtd() {
        TableColumn<Extra, Void> colSpn = new TableColumn("Quantidade");
        Callback<TableColumn<Extra, Void>, TableCell<Extra, Void>> cellFactory = new Callback<TableColumn<Extra, Void>, TableCell<Extra, Void>>() {
            @Override
            public TableCell<Extra, Void> call(final TableColumn<Extra, Void> param) {
                final TableCell<Extra, Void> cell = new TableCell<Extra, Void>() {

                    private final SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5);
                    private final Spinner spn = new Spinner<>(valueFactory);

                    // Funcao do spinner
                    {
                        spn.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
                            if (!"".equals(newValue)) {
                                int index = this.getIndex();

                                qtdExtra.set(index, Integer.valueOf(newValue));
                                if(Integer.parseInt(newValue) > Integer.parseInt(oldValue)) {
                                    extras_total += precoExtras.get(index);
                                    valor_diario_total += precoExtras.get(index);
                                } else {
                                    extras_total -= precoExtras.get(index);
                                    valor_diario_total -= precoExtras.get(index);
                                }
                                valor_extra.setText(df2.format(extras_total) + " €");
                                valor_diario.setText(df2.format(valor_diario_total) + " €");
                            }
                        });
                        spn.getValueFactory().setValue(0);
                        spn.setEditable(false);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(spn);
                        }
                    }
                };
                return cell;
            }
        };
        colSpn.setCellFactory(cellFactory);
        colSpn.setEditable(true);
        colSpn.setMaxWidth(130);
        extras.getColumns().add(colSpn);
    }

    private void disableData(LocalDate data, DatePicker datepicker, boolean forward) {
        Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        LocalDate today = data;
                        if(forward) {
                            setDisable(empty || item.compareTo(today) > 0);
                        } else {
                            setDisable(empty || item.compareTo(today) < 0);
                        }

                    }
                };
            }
        };
        datepicker.setDayCellFactory(callB);
    }

    private void calcDias() {
        if(data_entrega.getValue() != null && data_devolucao.getValue() != null) {
            long data_ent = data_entrega.getValue().toEpochDay();
            long data_dev = data_devolucao.getValue().toEpochDay();

            int dias = (int) Math.abs(data_ent - data_dev);

            // Define como zero sempre que uma data nova for definida para que não ocorra erros de cálculos
            taxa_aeroporto.setText("0");
            taxa_diversas.setText("0");

            valorsd_total_total =- valor_dias_total;
            valor_dias_total += (valor_diario_total * (dias+1));
            valorsd_total_total += valor_dias_total;
            valorTotal_sDesconto.setText(df2.format(valorsd_total_total) + " €");

            atualizarCampos();
        }
    }

    private void atualizarCampos() {
        // Truque para calcular o valor total com desconto
        int pDesconto = percentagemdesconto.getValue();
        percentagemdesconto.getValueFactory().setValue(1);
        percentagemdesconto.getValueFactory().setValue(pDesconto);

        String taxaA = taxa_aeroporto.getText();
        String taxaD = taxa_diversas.getText();
        if(!taxaA.isEmpty()) {
            taxa_aeroporto.setText(taxaA);
        } else if(!taxaD.isEmpty()) {
            taxa_diversas.setText(taxaD);
        }
    }

    private void fecharJanela(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        dialog.close();
    }

    private boolean addReserva() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Retirar e guardar os dados dos campos em variaveis
        String Situacao = situacao.getValue();
        String Cliente = cliente.getValue();
        String Viatura = viatura.getValue();
        String Seguro = seguro.getValue();
        String Ponto_entrega = local_entrega.getValue();
        String Ponto_devolucao = local_devolucao.getValue();
        String Data_entrega = String.valueOf(DataHoraToTimeStamp(data_entrega.getValue().toString(), hora_entrega));
        String Data_devolucao = String.valueOf(DataHoraToTimeStamp(data_devolucao.getValue().toString(), hora_devolucao));
        String Taxa_aeroporto = taxa_aeroporto.getText();
        String Taxa_diversas  = taxa_diversas.getText();
        String IVA      = percentagemIVA.getValue().toString();
        String Desconto = percentagemdesconto.getValue().toString();
        String Valor_extras = String.valueOf(extras_total);
        String Valor_diario = String.valueOf(valor_diario_total);
        String Valor_total  = String.valueOf(valor_total_total);
        String Data_reserva = data_reserva.getValue().toString();

        int ViaturaIdINS = getViaturaID(Viatura);
        int ClienteIdINS = Integer.parseInt(Cliente.substring(Cliente.indexOf("(")+1, Cliente.indexOf(")")));
        int SeguroIdINS  = getSeguroID(Seguro);
        int PontoEntIdINS = getPontoID(Ponto_entrega);
        int PontoDevIdINS = getPontoID(Ponto_devolucao);


        // Executar query para inserir os dados do acidente a ser adicionado
        if(baseDados.executarPreparedQueryUpdate("INSERT INTO reservas(situacao, clientes_clienteID, viaturas_viaturaID, seguro_seguroID, pontos_entregaID, pontos_devolucaoID, data_entrega, data_devolucao, taxa_aeroporto, IVA, misc_taxas, desconto, valor_extras, valor_diario, valor_total, data_reserva) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", Situacao, String.valueOf(ClienteIdINS), String.valueOf(ViaturaIdINS), String.valueOf(SeguroIdINS), String.valueOf(PontoEntIdINS), String.valueOf(PontoDevIdINS), Data_entrega, Data_devolucao, Taxa_aeroporto, IVA, Taxa_diversas, Desconto, Valor_extras, Valor_diario, Valor_total, Data_reserva)) {
            try {
                // Buscar o ID da reserva inserida
                ResultSet queryResult = baseDados.executarQuery("SELECT reservaID FROM reservas ORDER BY reservaID DESC LIMIT 1");

                if(queryResult.next()) {
                    int reservaID = queryResult.getInt(1);

                    for(int i = 0; i < data.size(); i++) {
                        // Adicionar query para inserir cada extra referido a reserva e sua quantidade respetivamente
                        String extraNome = data.get(i).getNome();
                        int extraQtd = qtdExtra.get(i);
                        int extraId = getExtraID(extraNome);

                        if(!baseDados.executarPreparedQueryUpdate("INSERT INTO reservas_extras VALUES (?, ?, ?);", String.valueOf(reservaID), String.valueOf(extraId), String.valueOf(extraQtd))) {
                            erro.setText("Ocorreu um erro ao tentar inserir esta reserva!");
                        }
                    }
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("Ocorreu ao tentar inserir os extras de uma reserva na tabela reservas_extras!\n" + e);
            }
        } else {
            erro.setText("Ocorreu um erro ao tentar inserir esta reserva!");
        }
        return false;
    }

    private int getExtraID(String nomeExtra) {
        // Declaracao de variaveis
        ResultSet queryResult;
        int extraID = 0;

        // Buscar o ID da seguro relacionado com o reserva
        queryResult = baseDados.executarPreparedQuery("SELECT extrasID FROM extras WHERE nome = ?;", nomeExtra);

        // Processar o resultado
        try {
            if(queryResult.next()) {
                try {
                    extraID = queryResult.getInt(1);
                } catch (SQLException e) {
                    e.getCause();
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao tentar dar GET no ID do extra "+nomeExtra+"\n");
            e.getCause();
        }
        return extraID;
    }
}


