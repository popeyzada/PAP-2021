package rent_a_car.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rent_a_car.funcionalidades.ConexaoBDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Extra {
    private int ID;
    private String nome;
    private Double preco;

    public Extra(int ID, String nome, Double preco) {
        this.ID = ID;
        this.nome = nome;
        this.preco = preco;
    }

    public Extra(String nome) {
        this.nome = nome;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }


    // Declaracao de outras classes
    private static ConexaoBDB baseDados = new ConexaoBDB();

    public static ObservableList<Extra> getExtras() {
        ObservableList<Extra> listaExtras = FXCollections.observableArrayList();

        try {
            ResultSet queryResult;

            // Executar query para obter dados de todos os clientes
            queryResult = baseDados.executarQuery("SELECT nome FROM extras;");

            while(queryResult.next()){
                listaExtras.add(new Extra(queryResult.getString(1)));
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu ao tentar retornar nome dos extras existentes da tabela extras!\n" + e);
        }

        return listaExtras;
    }

    public static ObservableList<Double> getPrecoExtras() {
        ObservableList<Double> listaPreco = FXCollections.observableArrayList();

        try {
            ResultSet queryResult;

            // Executar query para obter dados de todos os clientes
            queryResult = baseDados.executarQuery("SELECT preco FROM extras;");

            while(queryResult.next()){
                listaPreco.add(queryResult.getDouble(1));
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu ao tentar retornar o preco dos extras existentes da tabela extras!\n" + e);
        }

        return listaPreco;
    }
}
