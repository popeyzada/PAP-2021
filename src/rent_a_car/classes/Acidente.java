package rent_a_car.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rent_a_car.funcionalidades.ConexaoBDB;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Acidente {
    private int ID;
    private String categoria;
    private String viatura;
    private String descricao;
    private Date data;

    public Acidente(int ID, String categoria, String viatura, String descricao, Date data) {
        this.ID = ID;
        this.categoria = categoria;
        this.viatura = viatura;
        this.descricao = descricao;
        this.data = data;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getViatura() {
        return viatura;
    }

    public void setViatura(String viatura) {
        this.viatura = viatura;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }


    // Declaracao de outras classes
    private static ConexaoBDB baseDados = new ConexaoBDB();

    public static ObservableList<Acidente> getDadosAcidentes() {
        // Declaracao de variaveis/objetos
        ObservableList<Acidente> data;
        ResultSet queryResult = null;

        data = FXCollections.observableArrayList();

        try {
            // Executar query para obter dados de todos os acidentes
            queryResult = baseDados.executarQuery("SELECT * FROM acidentes");

            while (queryResult.next()) {
                // Insere o ID da viatura referente ao acidente do resultado
                int viaturaID = queryResult.getInt("viaturas_viaturaID");
                String viatura_matricula = "";

                // Busca o modelo e matricula da viatura e depois insere em uma string
                ResultSet queryResult2 = baseDados.executarPreparedQuery("SELECT modelo, matricula FROM viaturas WHERE ?=viaturaID;", String.valueOf(viaturaID));

                if(queryResult2.next()) {
                    viatura_matricula = queryResult2.getString(1) + " (" + queryResult2.getString(2) + ")";
                }

                // Cria um objeto Acidente e insere no ArrayList
                data.add(new Acidente(queryResult.getInt("acidenteID"), queryResult.getString("categoria"), viatura_matricula, queryResult.getString("descricao"), queryResult.getDate("data")));
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao processar e inserir os dados da query de Acidentes:\n " + e);
        }

        return data;
    }

    public static ObservableList<String> getViaturas() {
        ObservableList<String> listaViaturas = FXCollections.observableArrayList();

        // Executar query para inserir os dados da viatura a ser adicionada
        try {
            ResultSet queryResult;

            // Executar query para obter dados de todos os clientes
            queryResult = baseDados.executarQuery("SELECT V.modelo, V.matricula FROM viaturas V;");

            String viatura_matricula;
            while(queryResult.next()){
                viatura_matricula = queryResult.getString(1) + " (" + queryResult.getString(2) + ")";
                listaViaturas.add(
                        viatura_matricula
                );
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu ao tentar retornar as viaturas existentes da tabela viaturas!\n" + e);
        }

    return listaViaturas;
    }

    public static int getViaturaID(String nomeViatura) {
        // Declaracao de variaveis
        ResultSet queryResult;
        int viaturaID = -1;

        String matricula = getMatricula(nomeViatura);

        // Buscar o ID da viatura relacionada com o acidente
        queryResult = baseDados.executarPreparedQuery("SELECT viaturaID FROM viaturas WHERE matricula = ?;", matricula);

        // Processar o resultado
        try {
            if(queryResult.next()) {
                try {
                    viaturaID = queryResult.getInt(1);
                } catch (SQLException e) {
                    e.getCause();
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao tentar dar GET no ID da viatura "+nomeViatura+"\n");
            e.getCause();
        }
        return viaturaID;
    }

    public static String getMatricula(String nomeViatura) {
        String matricula = "";

        for(int i = 0; i < nomeViatura.length(); i++) {
            if(nomeViatura.charAt(i) == '(') {
                for(int x = i+1; x < nomeViatura.length(); x++) {
                    if(nomeViatura.charAt(x) != ')') {
                        matricula += nomeViatura.charAt(x);
                    } else {
                        break;
                    }
                }
            }
        }
        return matricula;
    }
}