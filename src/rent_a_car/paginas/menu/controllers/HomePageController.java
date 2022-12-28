package rent_a_car.paginas.menu.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import rent_a_car.funcionalidades.ConexaoBDB;


import java.sql.ResultSet;
import java.sql.SQLException;

public class HomePageController {
    // Objetos
    @FXML
    private Label numero_viaturasT;

    @FXML
    private Label numero_clientesT;

    @FXML
    private Label numero_reservasT;

    @FXML
    private Label numero_incidentesT;

    @FXML
    private Label numero_reservasM;

    // Declaracao de outras classes
    private ConexaoBDB baseDados = new ConexaoBDB();

    // Declaracao de variaveis
    private ResultSet queryResult;


    public void pI_updateDados() {
        int numeroReservasT = 0, numeroClientesT = 0, numeroViaturasT = 0, numeroIncidentesT = 0, numeroReservasM = 0;

        String query[] = {"SELECT COUNT(reservaID) FROM reservas;",
                          "SELECT COUNT(clienteID) FROM clientes;",
                          "SELECT COUNT(viaturaID) FROM viaturas;",
                          "SELECT COUNT(acidenteID) FROM acidentes;",
                          "SELECT COUNT(reservaID) FROM reservas WHERE MONTH(data_reserva) = MONTH(sysdate());",
        };

        // Retorna e insere o valor obtido na base de dados nas variaveis
        numeroReservasT   = getQuantidade(query[0]);
        numeroClientesT   = getQuantidade(query[1]);
        numeroViaturasT   = getQuantidade(query[2]);
        numeroIncidentesT = getQuantidade(query[3]);
        numeroReservasM   = getQuantidade(query[4]);


        // Define o texto das labels para inserir na interface gráfica
        numero_reservasT.setText(String.valueOf(numeroReservasT));
        numero_clientesT.setText(String.valueOf(numeroClientesT));
        numero_viaturasT.setText(String.valueOf(numeroViaturasT));
        numero_incidentesT.setText(String.valueOf(numeroIncidentesT));
        numero_reservasM.setText(String.valueOf(numeroReservasM));

    }

    private int getQuantidade(String query) {
        int qtd = 0;

        try {
            // Executar uma query
            queryResult = baseDados.executarPreparedQuery(query);

            // Processar o resultado
            if (queryResult.next()) {
                qtd = queryResult.getInt(1);
            }

            // Fechar conexao com a BDB
            baseDados.closeConexaoBDB();
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao processar a query get numeros para a Pagina Inicial na base de dados:\n " + e);
        }
        return qtd;
    }
}
