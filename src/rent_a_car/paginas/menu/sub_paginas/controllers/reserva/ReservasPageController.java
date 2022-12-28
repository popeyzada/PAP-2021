package rent_a_car.paginas.menu.sub_paginas.controllers.reserva;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.AgendaSkinSwitcher;
import rent_a_car.funcionalidades.ConexaoBDB;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

import static rent_a_car.classes.Reserva.getNomeCliente;
import static rent_a_car.classes.Reserva.getNomeViatura;

public class ReservasPageController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Agenda agenda;


    // Declaracao de outras classes
    private static ConexaoBDB baseDados = new ConexaoBDB();

    public void initialize() {
        // Retorna todas as reservas da BDB
        ObservableList<Agenda.AppointmentImplLocal> listaReservas = getReservas();

        // Adicionar reservas
        agenda.appointments().addAll(listaReservas);

        AgendaSkinSwitcher agendaSkinSwitcher = new AgendaSkinSwitcher(agenda);
        anchorPane.getChildren().add(agendaSkinSwitcher);
    }

    private ObservableList<Agenda.AppointmentImplLocal> getReservas() {
        ObservableList<Agenda.AppointmentImplLocal> reservas = FXCollections.observableArrayList();

        try {
            // Busca o nome do cliente e depois insere em uma string
            ResultSet queryResult = baseDados.executarQuery("SELECT data_entrega, data_devolucao, clientes_clienteID, viaturas_viaturaID, situacao FROM reservas;");

            while(queryResult.next()) {
                int clienteID = queryResult.getInt(3);
                int viaturaID = queryResult.getInt(4);

                // Retorna o timestamp
                Timestamp hora_entregaB = queryResult.getTimestamp(1);
                Timestamp hora_devolucaoB = queryResult.getTimestamp(2);

                // Transforma em data
                Date data_entregaB  = new Date(hora_entregaB.getTime());
                Date data_devolucaoB = new Date(hora_devolucaoB.getTime());

                String data_entrega = data_entregaB.toString();
                String data_devolucao = data_devolucaoB.toString();

                // Transforma no objeto LocalTime para introduzir abaixo
                LocalTime hora_entrega = LocalTime.from(hora_entregaB.toLocalDateTime());
                LocalTime hora_devolucao = LocalTime.from(hora_devolucaoB.toLocalDateTime());

                reservas.add(
                        new Agenda.AppointmentImplLocal()
                                .withStartLocalDateTime(LocalDate.parse((CharSequence) data_entrega).atTime(hora_entrega))
                                .withEndLocalDateTime(LocalDate.parse((CharSequence) data_devolucao).atTime(hora_devolucao))
                                .withSummary(getNomeViatura(viaturaID)+ "\n" + getNomeCliente(clienteID))
                                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass(queryResult.getString(5).substring(0, 2))) // Retorna apenas as duas primeiras letras pois no css o grupo não pode conter espaços
                );
            }
        } catch (SQLException e) {
            e.getCause();
        }


        return reservas;
    }
}
