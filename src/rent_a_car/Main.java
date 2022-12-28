package rent_a_car;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("paginas/login_page.fxml"));

        // Adicionar ficheiros css
        root.getStylesheets().add(getClass().getResource("/css/fancy-buttons.css").toString());
        root.getStylesheets().add(getClass().getResource("/css/menu_fancy-buttons.css").toString());


        // Icones e nome do programa
        primaryStage.getIcons().add(new Image("/imagens/icon.png"));
        primaryStage.setTitle("Rent-a-car 2000");

        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
