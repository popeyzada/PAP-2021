package rent_a_car.funcionalidades;

import java.sql.ResultSet;

public class Login {
    public static boolean checarCredenciais(String inputUtilizador, String inputPassword) {
        // Declaracao de outras classes
        ConexaoBDB baseDados = new ConexaoBDB();

        // Declaracao de variaveis
        ResultSet queryResult;
        boolean login_valido = false;

        try {
            // Executar uma query
            queryResult = baseDados.executarPreparedQuery("SELECT loginID FROM login_funcionarios WHERE usuario = ? AND password = ?;", inputUtilizador, inputPassword);

            // Processar o resultado
            if (queryResult.next()) {
                login_valido = true;
            }

            // Fechar conexao com a BDB
            baseDados.closeConexaoBDB();
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao processar a query de Login na base de dados:\n " + e);
        }
        return login_valido;
    }
}
