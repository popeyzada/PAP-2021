package rent_a_car.funcionalidades;

import javafx.scene.control.Alert;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static rent_a_car.DialogController.mostrarErro;

// Importar outras classes e aceder aos demais métodos

public class ConexaoBDB {
    private Connection con = null;
    private Statement stmt = null;
    private PreparedStatement pstmt = null;
    private ResultSet qr = null;

        public ResultSet executarQuery(String query) {
            try {
                if(abrirConexao()) {
                    // Criar uma variavel como statement (declaracao)
                    stmt=con.createStatement();

                    // Envia a query na base dados e retorna os resultados
                    qr=stmt.executeQuery(query);
                }
            } catch (Exception e) {
                System.out.println("Ocorreu um erro ao tentar retornar a query "+query+":\n " + e);
            }
        return qr;
        }

    public ResultSet executarPreparedQuery(String query, String ... args) {
        try {
            if(abrirConexao()) {
                // Criar uma variavel como statement (declaracao)
                pstmt = con.prepareStatement(query);

                // Definir variaveis dentro da String query
                int nArgumentos = 0;
                for (String arg : args) {
                    nArgumentos++;
                    pstmt.setString(nArgumentos, arg);
                } // O setString "Substitui os ?" com as variaveis setadas acima

                // Envia a query na base dados e retorna os resultados
                qr = pstmt.executeQuery();
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar retornar a Prepared query "+query+":\n " + e);
        }
       return qr;
    }

    public boolean executarPreparedQueryUpdate(String query, String ... args) {
        try {
            if(abrirConexao()) {
                // Criar uma variavel como statement (declaracao)
                pstmt = con.prepareStatement(query);

                // Definir variaveis dentro da String query
                int nArgumentos = 0;
                for (String arg : args) {
                    nArgumentos++;
                    pstmt.setString(nArgumentos, arg);
                } // O setString "Substitui os ?" com as variaveis setadas acima

                // Envia a query na base dados e retorna os resultados
                int linhasAfetadas = pstmt.executeUpdate();

                if(linhasAfetadas > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar executar a Prepared query update "+query+":\n " + e);
        }
        return false;
    }

        private boolean abrirConexao() {
            try {
            	  Properties property = new Properties();
                  InputStream is = new FileInputStream("dbConfig.properties");
                  property.load(is);

                // Define a classe do driver para o mySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Dados para conexao da base de dados
                con = DriverManager.getConnection(property.getProperty("url"), property.getProperty("username"), property.getProperty("password"));
                return true;
            } catch (Exception e) {
                System.out.println("Ocorreu um erro ao conectar na base de dados:\n " + e);
                mostrarErro(Alert.AlertType.ERROR, "ERRO", "Ocorreu um erro ao tentar conectar-se com a base de dados", "Verifique sua conexão ou contacte um administrador caso o erro persista");
                return false;
            }
        }

        public void closeConexaoBDB() {
            try {
                if (qr != null) {
                    qr.close();
                } else if (stmt != null) {
                    stmt.close();
                } else if (con != null) {
                    con.close();
                }

            } catch (Exception e) {
                System.out.println("Ocorreu um erro ao tentar fechar a conexao com a base de dados:\n " + e);
            }
        }
}