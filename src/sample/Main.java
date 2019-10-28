package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    /**
     * Passes the stage and control to Controller.
     * @param primaryStage takes in a Stage given by the compiler.
     * @throws IOException is thrown only when FXML file is unable to load.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        // closes
        primaryStage.setOnCloseRequest(t -> {
            Cycling.invertIsAppAlive();
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setTitle("TTC Signage");
        primaryStage.setScene(new Scene(root, 886, 366));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
