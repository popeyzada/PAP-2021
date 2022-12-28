package rent_a_car.paginas.menu.sub_paginas.controllers.reserva;

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

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import static rent_a_car.classes.Acidente.getViaturaID;
import static rent_a_car.classes.Extra.getExtras;
import static rent_a_car.classes.Extra.getPrecoExtras;
import static rent_a_car.classes.Reserva.*;
import static rent_a_car.funcionalidades.Validacoes.*;

public class editReservaPageController {
    @FXML
    private ComboBox<String> cliente;
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

    // Dados iniciais da reserva (Quando não foram editados ainda)
    private int reservaID;
    private String _Situacao;
    private String _Cliente;
    private String _Viatura;
    private String _Seguro;
    private String _Local_entrega;
    private String _Local_devolucao;
    private String _Data_entrega;
    private String _Data_devolucao;
    private Calendar _Hora_entrega;
    private Calendar _Hora_devolucao;
    private int _extraSegCond = 0;
    private int _extraAssento = 0;
    private int _extraCadeiraBB = 0;
    private int _extraOvinho = 0;
    private int _PercentagemDesc;
    private String _Data_reserva;

    @FXML
    void btn1(ActionEvent event) {   // Cancelar
        try {
            fecharJanela(event);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar fechar a janela de adicionar reservas\n " + e);
        }
    }

    @FXML
    void btn2(ActionEvent event) {   // Redefinir
        introduzirValoresNE(_Situacao, _Cliente, _Viatura, _Seguro, _Local_entrega, _Local_devolucao, _Data_entrega, _Data_devolucao, _Hora_entrega, _Hora_devolucao, _extraSegCond, _extraAssento, _extraCadeiraBB, _extraOvinho, extras_total, String.valueOf(valor_taxaaero), String.valueOf(valor_taxasdiv), _PercentagemDesc, _Data_reserva);
        insDadosExtras();
    }

    @FXML
    void btn3(ActionEvent event) {   // Submeter
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

        setCampoDouble(taxa_aeroporto);
        setCampoDouble(taxa_diversas);

        // Desabilitar IVA pois os valores já são incluidos com 18% de IVA
        this.percentagemIVA.setDisable(true);

        // Desabilitar a reordem da tableview pois pode ocorrer confunsões nas quantidades de cada extra
        extra.setSortable(false);

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

    public void introduzirValoresAuto(int ID, String situacao, String cliente, String viatura, String seguro, String local_entrega, String local_devolucao, String data_entrega, String data_devolucao, Calendar hora_entrega, Calendar hora_devolucao, int extraSegCond, int extraAssento, int extraCadeiraBB, int extraOvinho, String taxa_aero, String taxa_div, double valor_extras, int percentagemDesc, String data_reserva) {
        reservaID = ID;
        introduzirValoresNE(situacao, cliente, viatura, seguro, local_entrega, local_devolucao, data_entrega, data_devolucao, hora_entrega, hora_devolucao, extraSegCond, extraAssento, extraCadeiraBB, extraOvinho, valor_extras, taxa_aero, taxa_div, percentagemDesc, data_reserva);
        introduzirValoresString();
        insDadosExtras();
        insSpnQtd();
    }

    public void introduzirValoresNE(String situacao, String cliente, String viatura, String seguro, String local_entrega, String local_devolucao, String data_entrega, String data_devolucao, Calendar hora_entrega, Calendar hora_devolucao, int extraSegCond, int extraAssento, int extraCadeiraBB, int extraOvinho, double valor_extras, String taxa_aero, String taxa_div, int percentagemDesc, String data_reserva) {
        this.situacao.setValue(situacao);
        this.cliente.setValue(cliente);
        this.viatura.setValue(viatura);
        this.seguro.setValue(seguro);
        this.local_entrega.setValue(local_entrega);
        this.local_devolucao.setValue(local_devolucao);
        this.data_entrega.setValue(LocalDate.parse(data_entrega));
        this.data_devolucao.setValue(LocalDate.parse(data_devolucao));
        this.hora_entrega.setCalendar(hora_entrega);
        this.hora_devolucao.setCalendar(hora_devolucao);

        this.valor_extra.setText(String.valueOf(valor_extras) + " €");
        this.taxa_aeroporto.setText(taxa_aero);
        this.taxa_diversas.setText(taxa_div);
        this.percentagemdesconto.getValueFactory().setValue(percentagemDesc);
        this.data_reserva.setValue(LocalDate.parse(data_reserva));

        valor_diario_total -= extras_total;
        extras_total = valor_extras;
        valor_diario_total += extras_total;

        _extraSegCond = extraSegCond;
        _extraAssento = extraAssento;
        _extraCadeiraBB = extraCadeiraBB;
        _extraOvinho = extraOvinho;

        this.valor_diario.setText(df2.format(valor_diario_total) + " €");

        // Adicionar o cliente existente na combobox cliente (O cliente de uma reserva existente não pode mudar)
        ObservableList<String> listaClientes = FXCollections.observableArrayList();
        listaClientes.add(_Cliente);
        this.cliente.setItems(listaClientes);
    }

    public void introduzirValoresString() {
        _Situacao = this.situacao.getValue().toString();
        _Cliente = this.cliente.getValue();
        _Viatura = this.viatura.getValue().toString();
        _Seguro = this.seguro.getValue().toString();
        _Local_entrega = this.local_entrega.getValue().toString();
        _Local_devolucao = this.local_devolucao.getValue().toString();
        _Data_entrega = this.data_entrega.getValue().toString();
        _Data_devolucao = this.data_devolucao.getValue().toString();
        _Hora_entrega = this.hora_entrega.getCalendar();
        _Hora_devolucao = this.hora_devolucao.getCalendar();
        valor_taxaaero = Double.parseDouble(this.taxa_aeroporto.getText());
        valor_taxasdiv = Double.parseDouble(this.taxa_diversas.getText());
        _PercentagemDesc = this.percentagemdesconto.getValueFactory().getValue();
        _Data_reserva = this.data_reserva.getValue().toString();
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
            if(i == 0) {
                qtdExtra.set(i, _extraSegCond);
            } else if (i == 1) {
                qtdExtra.set(i, _extraAssento);
            } else if (i == 2) {
                qtdExtra.set(i, _extraCadeiraBB);
            } else if (i == 3) {
                qtdExtra.set(i, _extraOvinho);
            }
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

    private int extraNum = 0;
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
                            if (!"".equals(newValue) && this.getIndex() != -1) {
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


                            if(extraNum == 2) {
                                spn.getValueFactory().setValue(qtdExtra.get(0));
                            } else if(extraNum == 3) {
                                spn.getValueFactory().setValue(qtdExtra.get(1));
                            } else if(extraNum == 4) {
                                spn.getValueFactory().setValue(qtdExtra.get(2));
                            } else if(extraNum == 5) {
                                spn.getValueFactory().setValue(qtdExtra.get(3));
                            }
                        spn.setEditable(false);
                        extraNum++;
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
        if(baseDados.executarPreparedQueryUpdate("UPDATE reservas SET situacao = ?, clientes_clienteID = ?, viaturas_viaturaID = ?, seguro_seguroID = ?, pontos_entregaID = ?, pontos_devolucaoID = ?, data_entrega = ?, data_devolucao = ?, taxa_aeroporto = ?, IVA = ?, misc_taxas = ?, desconto = ?, valor_extras = ?, valor_diario = ?, valor_total = ?, data_reserva = ? WHERE reservaID = ?;", Situacao, String.valueOf(ClienteIdINS), String.valueOf(ViaturaIdINS), String.valueOf(SeguroIdINS), String.valueOf(PontoEntIdINS), String.valueOf(PontoDevIdINS), Data_entrega, Data_devolucao, Taxa_aeroporto, IVA, Taxa_diversas, Desconto, Valor_extras, Valor_diario, Valor_total, Data_reserva, String.valueOf(reservaID))) {
            for(int i = 0; i < data.size(); i++) {
                // Adicionar query para inserir cada extra referido a reserva e sua quantidade respetivamente
                String extraNome = data.get(i).getNome();
                int extraQtd = qtdExtra.get(i);
                int extraId = getExtraID(extraNome);

                if(!baseDados.executarPreparedQueryUpdate("UPDATE reservas_extras SET quantidade = ? WHERE reservas_reservaID = ? AND extras_extrasID = ?;", String.valueOf(extraQtd), String.valueOf(reservaID), String.valueOf(extraId))) {
                    erro.setText("Ocorreu um erro ao tentar editar esta reserva!");
                }
            }
            return true;
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


