package rent_a_car.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rent_a_car.funcionalidades.ConexaoBDB;

import java.sql.*;
import java.util.ArrayList;

public class Reserva {
    private int ID;
    private String situacao;
    private String cliente;
    private String viatura;
    private String seguro;
    private String localEntrega;
    private String localDevolucao;
    private Timestamp data_entrega;
    private Timestamp data_devolucao;
    private int extra_segCondutor;
    private int extra_assento;
    private int extra_cadeiraBB;
    private int extra_ovinho;
    private double taxa_aeroporto;
    private double IVA;
    private double misc_taxas;
    private double desconto;
    private double valor_extras;
    private double valor_diario;
    private double valor_total;
    private Date data_reserva;

    public Reserva(int ID, String situacao, String cliente, String viatura, String seguro, String localEntrega, String localDevolucao, Timestamp data_entrega, Timestamp data_devolucao, int extra_segCondutor, int extra_assento, int extra_cadeiraBB, int extra_ovinho, double taxa_aeroporto, double IVA, double misc_taxas, double desconto, double valor_extras, double valor_diario, double valor_total, Date data_reserva) {
        this.ID = ID;
        this.situacao = situacao;
        this.cliente = cliente;
        this.viatura = viatura;
        this.seguro = seguro;
        this.localEntrega = localEntrega;
        this.localDevolucao = localDevolucao;
        this.data_entrega = data_entrega;
        this.data_devolucao = data_devolucao;
        this.extra_segCondutor = extra_segCondutor;
        this.extra_assento = extra_assento;
        this.extra_cadeiraBB = extra_cadeiraBB;
        this.extra_ovinho = extra_ovinho;
        this.taxa_aeroporto = taxa_aeroporto;
        this.IVA = IVA;
        this.misc_taxas = misc_taxas;
        this.desconto = desconto;
        this.valor_extras = valor_extras;
        this.valor_diario = valor_diario;
        this.valor_total = valor_total;
        this.data_reserva = data_reserva;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getViatura() {
        return viatura;
    }

    public void setViatura(String viatura) {
        this.viatura = viatura;
    }

    public String getSeguro() {
        return seguro;
    }

    public void setSeguro(String seguro) {
        this.seguro = seguro;
    }

    public String getLocalEntrega() {
        return localEntrega;
    }

    public String getLocalDevolucao() {
        return localDevolucao;
    }

    public void setLocalDevolucao(String localDevolucao) {
        this.localDevolucao = localDevolucao;
    }

    public void setLocalEntrega(String localEntrega) {
        this.localEntrega = localEntrega;
    }

    public Timestamp getData_entrega() {
        return data_entrega;
    }

    public void setData_entrega(Timestamp data_entrega) {
        this.data_entrega = data_entrega;
    }

    public Timestamp getData_devolucao() {
        return data_devolucao;
    }

    public void setData_devolucao(Timestamp data_devolucao) {
        this.data_devolucao = data_devolucao;
    }

    public int getExtra_segCondutor() {
        return extra_segCondutor;
    }

    public void setExtra_segCondutor(int extra_segCondutor) {
        this.extra_segCondutor = extra_segCondutor;
    }

    public int getExtra_assento() {
        return extra_assento;
    }

    public void setExtra_assento(int extra_assento) {
        this.extra_assento = extra_assento;
    }

    public int getExtra_cadeiraBB() {
        return extra_cadeiraBB;
    }

    public void setExtra_cadeiraBB(int extra_cadeiraBB) {
        this.extra_cadeiraBB = extra_cadeiraBB;
    }

    public int getExtra_ovinho() {
        return extra_ovinho;
    }

    public void setExtra_ovinho(int extra_ovinho) {
        this.extra_ovinho = extra_ovinho;
    }

    public double getTaxa_aeroporto() {
        return taxa_aeroporto;
    }

    public void setTaxa_aeroporto(double taxa_aeroporto) {
        this.taxa_aeroporto = taxa_aeroporto;
    }

    public double getIVA() {
        return IVA;
    }

    public void setIVA(double IVA) {
        this.IVA = IVA;
    }

    public double getMisc_taxas() {
        return misc_taxas;
    }

    public void setMisc_taxas(double misc_taxas) {
        this.misc_taxas = misc_taxas;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getValor_extras() {
        return valor_extras;
    }

    public void setValor_extras(double valor_extras) {
        this.valor_extras = valor_extras;
    }

    public double getValor_diario() {
        return valor_diario;
    }

    public void setValor_diario(double valor_diario) {
        this.valor_diario = valor_diario;
    }

    public double getValor_total() {
        return valor_total;
    }

    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }

    public Date getData_reserva() {
        return data_reserva;
    }

    public void setData_reserva(Date data_reserva) {
        this.data_reserva = data_reserva;
    }


    // Declaracao de outras classes
    private static ConexaoBDB baseDados = new ConexaoBDB();

    public static ObservableList<Reserva> getDadosReservas() {
        // Declaracao de variaveis/objetos
        ObservableList<Reserva> data;
        ResultSet queryResult = null;

        data = FXCollections.observableArrayList();

        try {
            // Executar query para obter dados de todas as reservas
            queryResult = baseDados.executarQuery("SELECT * FROM reservas");

            while(queryResult.next()) {
                // Insere o ID da reserva do resultado
                int reservaID = queryResult.getInt("reservaID");

                // Insere o ID da viatura referente ao reserva do resultado
                int clienteID = queryResult.getInt("clientes_clienteID");

                // Insere o ID da viatura referente ao reserva do resultado
                int viaturaID = queryResult.getInt("viaturas_viaturaID");

                // Insere o ID do seguro referente a reserva do resultado
                int seguroID = queryResult.getInt("seguro_seguroID");

                // Insere o ID do ponto de entrega referente a reserva do resultado
                int pontoID = queryResult.getInt("pontos_entregaID");

                // Insere o ID do ponto de devolucao referente a reserva do resultado
                int pontoDID = queryResult.getInt("pontos_devolucaoID");

                String nomeCliente = getNomeCliente(clienteID);
                String nomeViatura = getNomeViatura(viaturaID);
                String nomeSeguro  = getNomeSeguro(seguroID);
                String nomePontoE  = getNomePontoE(pontoID);
                String nomePontoD  = getNomePontoE(pontoDID);

                ArrayList<Integer> extras = getQtdExtras(reservaID);

                // Cria um objeto Reserva e insere no ArrayList
                data.add(new Reserva(reservaID, queryResult.getString("situacao"), nomeCliente, nomeViatura, nomeSeguro, nomePontoE, nomePontoD, queryResult.getTimestamp("data_entrega"), queryResult.getTimestamp("data_devolucao"), extras.get(0), extras.get(1), extras.get(2), extras.get(3), queryResult.getDouble("taxa_aeroporto"), queryResult.getDouble("IVA"), queryResult.getDouble("misc_taxas"), queryResult.getDouble("desconto"), queryResult.getDouble("valor_extras"), queryResult.getDouble("valor_diario"), queryResult.getDouble("valor_total"), queryResult.getDate("data_reserva")));
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao processar e inserir os dados da query de Reservas:\n " + e);
        }

        return data;
    }

    public static String getNomeCliente(int clienteID) throws SQLException {
        String nome_cliente = "";

        // Busca o nome do cliente e depois insere em uma string
        ResultSet queryResult = baseDados.executarPreparedQuery("SELECT clientes.clienteID, contatos.nome FROM clientes INNER JOIN contatos ON clientes.contatos_contatoID=contatos.contatoID WHERE clienteID=?;", String.valueOf(clienteID));

        if (queryResult.next()) {
            nome_cliente = queryResult.getString(2) + " (" + queryResult.getString(1) + ") ";
        }
        return nome_cliente;
    }

    public static String getNomeViatura(int viaturaID) throws SQLException {
        String nome_viatura = "";

        // Busca a marca, modelo e matricula da viatura e depois insere em uma string
        ResultSet queryResult = baseDados.executarPreparedQuery("SELECT marca, modelo, matricula FROM viaturas WHERE viaturaID=?;", String.valueOf(viaturaID));

        if (queryResult.next()) {
            nome_viatura = queryResult.getString(1) + " " + queryResult.getString(2) + " (" + queryResult.getString(3) + ")";
        }
        return nome_viatura;
    }

    private static String getNomeSeguro(int seguroID) throws SQLException {
        String nome_seguro = "";

        // Busca a marca, modelo e matricula da viatura e depois insere em uma string
        ResultSet queryResult = baseDados.executarPreparedQuery("SELECT nome FROM seguro WHERE seguroID=?;", String.valueOf(seguroID));

        if (queryResult.next()) {
            nome_seguro = queryResult.getString(1);
        }
        return nome_seguro;
    }

    private static String getNomePontoE(int pontoID) throws SQLException {
        String nome_pontoE = "";

        // Busca a marca, modelo e matricula da viatura e depois insere em uma string
        ResultSet queryResult = baseDados.executarPreparedQuery("SELECT nome FROM pontos_entrega WHERE pontoID=?;", String.valueOf(pontoID));

        if (queryResult.next()) {
            nome_pontoE = queryResult.getString(1);
        }
        return nome_pontoE;
    }

    private static ArrayList<Integer> getQtdExtras(int reservaID) throws SQLException {
        ArrayList<Integer> extras = new ArrayList<Integer>();

        // Busca a quantidade dos 4 extras respetivamente e depois insere
        ResultSet queryResult = baseDados.executarPreparedQuery("SELECT quantidade FROM reservas_extras WHERE reservas_reservaID=? ORDER BY extras_extrasID ASC;", String.valueOf(reservaID));

        while(queryResult.next()) {
            extras.add(queryResult.getInt(1));
        }
        return extras;
    }

    public static ObservableList<String> getNClientes() {
        ObservableList<String> listaNClientes = FXCollections.observableArrayList();

        // Executar query para inserir os dados da viatura a ser adicionada
        try {
            ResultSet queryResult;

            // Executar query para obter dados de todos os clientes
            queryResult = baseDados.executarQuery("SELECT contatos.nome, clientes.clienteID FROM clientes INNER JOIN contatos ON clientes.contatos_contatoID=contatos.contatoID;");

            while (queryResult.next()) {
                listaNClientes.add(queryResult.getString(1) + " (" + queryResult.getInt(2) + ")");
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu ao tentar retornar os nomes existentes da tabela clientes!\n" + e);
        }
        return listaNClientes;
    }

    public static ObservableList<String> getNViaturas() {
        ObservableList<String> listaNViaturas = FXCollections.observableArrayList();

        // Executar query para inserir os dados da viatura a ser adicionada
        try {
            String nome_viatura = "";

            // Busca a marca, modelo e matricula da viatura e depois insere em uma string
            ResultSet queryResult = baseDados.executarQuery("SELECT marca, modelo, matricula FROM viaturas;");

            while(queryResult.next()) {
                nome_viatura = queryResult.getString(1) + " " + queryResult.getString(2) + " (" + queryResult.getString(3) + ")";
                listaNViaturas.add((nome_viatura));
            }

        } catch (SQLException e) {
            System.out.println("Ocorreu ao tentar retornar as viaturas existentes da tabela viaturas!\n" + e);
        }
        return listaNViaturas;
    }

    public static ObservableList<String> getNSeguros() {
        ObservableList<String> listaNSeguros = FXCollections.observableArrayList();

        // Executar query para inserir os dados da viatura a ser adicionada
        try {
            // Busca a marca, modelo e matricula da viatura e depois insere em uma string
            ResultSet queryResult = baseDados.executarQuery("SELECT nome FROM seguro;");

            while(queryResult.next()) {
                listaNSeguros.add((queryResult.getString(1)));
            }

        } catch (SQLException e) {
            System.out.println("Ocorreu ao tentar retornar os seguros existentes da tabela seguros!\n" + e);
        }
        return listaNSeguros;
    }

    public static ObservableList<String> getNLocEntregas() {
        ObservableList<String> listaNLocEntregas = FXCollections.observableArrayList();

        // Executar query para inserir os dados da viatura a ser adicionada
        try {
            // Busca a marca, modelo e matricula da viatura e depois insere em uma string
            ResultSet queryResult = baseDados.executarQuery("SELECT nome FROM pontos_entrega;");

            while(queryResult.next()) {
                listaNLocEntregas.add((queryResult.getString(1)));
            }

        } catch (SQLException e) {
            System.out.println("Ocorreu ao tentar retornar os seguros existentes da tabela seguros!\n" + e);
        }
        return listaNLocEntregas;
    }

    public static double getPrecoSeguro(String nomeSeguro) {
        double precoSeguro = 0;

        // Executar query para retornar o preco do seguro recebido
        try {
            // Busca o preco diario do seguro depois insere na variavel precoSeguro
            ResultSet queryResult = baseDados.executarPreparedQuery("SELECT preco_diario FROM seguro WHERE nome = ?;", nomeSeguro);

            if(queryResult.next()) {
                precoSeguro = queryResult.getDouble(1);
            }

        } catch (SQLException e) {
            System.out.println("Ocorreu ao tentar retornar o preco do seguro " + nomeSeguro + " na tabela seguros!\n" + e);
        }
        return precoSeguro;
    }

    public static double getPrecoViatura(String matricula) {
        double precoViatura = 0;

        // Executar query para retornar o preco do seguro recebido
        try {
            // Busca o preco diario do seguro depois insere na variavel precoSeguro
            ResultSet queryResult = baseDados.executarPreparedQuery("SELECT p.valor FROM precario p, viaturas v WHERE v.precario_grupo = p.categoria AND v.matricula= ?;", matricula);

            if(queryResult.next()) {
                precoViatura = queryResult.getDouble(1);
            }

        } catch (SQLException e) {
            System.out.println("Ocorreu ao tentar retornar o preco da viatura de matricula " + matricula + " na tabela viaturas!\n" + e);
        }
        return precoViatura;
    }

    public static int getSeguroID(String nomeSeguro) {
        // Declaracao de variaveis
        ResultSet queryResult;
        int seguroID = 0;

        // Buscar o ID da seguro relacionado com o reserva
        queryResult = baseDados.executarPreparedQuery("SELECT seguroID FROM seguro WHERE nome = ?;", nomeSeguro);

        // Processar o resultado
        try {
            if(queryResult.next()) {
                try {
                    seguroID = queryResult.getInt(1);
                } catch (SQLException e) {
                    e.getCause();
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao tentar dar GET no ID do seguro "+nomeSeguro+"\n");
            e.getCause();
        }
        return seguroID;
    }

    public static int getPontoID(String nome_ponto) {
        // Declaracao de variaveis
        ResultSet queryResult;
        int pontoID = 0;

        // Buscar o ID do ponto relacionado com a reserva
        queryResult = baseDados.executarPreparedQuery("SELECT pontoID FROM pontos_entrega WHERE nome = ?;", nome_ponto);

        // Processar o resultado
        try {
            if(queryResult.next()) {
                try {
                    pontoID = queryResult.getInt(1);
                } catch (SQLException e) {
                    e.getCause();
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao tentar dar GET no ID do ponto "+nome_ponto+"\n");
            e.getCause();
        }
        return pontoID;
    }
}
