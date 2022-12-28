package rent_a_car.funcionalidades;

import javafx.event.ActionEvent;
import javafx.print.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;



public class Impressoes {
    public static void imprimirTableView(TableView tableView, ActionEvent event) {

        Rectangle rect = new Rectangle(0, 0, tableView.getWidth(), tableView.getHeight());
        tableView.setClip(rect);
        WritableImage writableImage;
        writableImage = new WritableImage((int) tableView.getPrefWidth(), (int) tableView.getPrefHeight());
        tableView.snapshot(null, writableImage);
        Button btn = (Button) event.getSource();
        Stage dialog = (Stage) btn.getScene().getWindow();

        print(writableImage, dialog);
    }


    private static void print(WritableImage writableImage, Stage primaryStage) {
        ImageView imageView = new ImageView(writableImage);
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        double scaleX = pageLayout.getPrintableWidth() / imageView.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / imageView.getBoundsInParent().getHeight();
        imageView.getTransforms().add(new Scale(scaleX, scaleY));
        imageView.setPreserveRatio(true);

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean successPrintDialog = job.showPrintDialog(primaryStage.getOwner());
            if(successPrintDialog){
                boolean success = job.printPage(pageLayout,imageView);
                if (success) {
                    job.endJob();
                }
            }
        }
    }
}
