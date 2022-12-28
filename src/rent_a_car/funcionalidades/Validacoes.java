package rent_a_car.funcionalidades;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import jfxtras.scene.control.CalendarTimeTextField;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;

public class Validacoes {
    public static void setCampoNumerico(TextField tf, int digitos)  {
        tf.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,"+digitos+"}([\\.]\\d{0,4})?")) {
                    tf.setText(oldValue);
                }
            }
        });
    }

    public static void setCampoDouble(TextField tf)  {
        tf.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*|\\d+\\.\\d*")) {
                    tf.setText(oldValue);
                }
            }
        });
    }

    public static void setCampoStringLength(TextField tf, int digitos)  {
        tf.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (tf.getText().length() >= digitos ) // limita para x caracteres
                    tf.setText(oldValue);;
            }
        });
    }

    // Retornar a data formatada para inserir no depois no DatePicker
    public static final LocalDate formatarData(Date data){
        String date = new SimpleDateFormat("dd/MM/yyyy").format(data);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date , formatter);
        return localDate;
    }

    public static final ObservableList<String> getObservableItems(String ... args) {
        ObservableList<String> observableList = FXCollections.observableArrayList();

        for (String arg : args) {
            observableList.add(arg);
        }
        return observableList;
    }

    public boolean infoBox(String infoMessage, String titleBar) {
        ButtonType confirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                infoMessage,
                confirmar,
                cancelar);

        alert.setTitle(null);
        alert.setHeaderText(titleBar);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/css/fancy-buttons.css").toExternalForm());

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initStyle(StageStyle.UNDECORATED);

        Optional<ButtonType> result = alert.showAndWait();

        if(result.orElse(cancelar) == confirmar) {
            return true;
        }
        return false;
    }

    public static Timestamp DataHoraToTimeStamp(String timestamp, CalendarTimeTextField hora) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        int h = hora.getCalendar().getTime().getHours();
        int m = hora.getCalendar().getTime().getMinutes();

        timestamp += " "+ h +":"+ m+":00";

        java.sql.Timestamp ts = java.sql.Timestamp.valueOf(timestamp) ;
        return ts;
    }

    public static Calendar timestampToCalendar(final Timestamp timestamp) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        return cal;
    }
}
