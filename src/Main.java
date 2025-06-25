import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import Database.DatabaseHandler;
import Database.DatabaseInitializer;
import UI.HostServicesProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Get absolute path to FXML
        Path fxmlPath = Paths.get("src", "UI", "MainScene.fxml").toAbsolutePath();

        // Convert to URL
        URL fxmlUrl = fxmlPath.toUri().toURL();

        // Load FXML
        Parent root = FXMLLoader.load(fxmlUrl);

        //
        HostServicesProvider.setHostServices(getHostServices());

        Scene scene = new Scene(root);
        primaryStage.setTitle("Bounty Hunter Store Terminal - Login Page");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            handleApplicationExit();
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            handleApplicationExit();
        }));
        DatabaseInitializer.Setup();
        launch(args);

    }

    private static void handleApplicationExit() {
        System.out.println("Application is closing...");
        DatabaseHandler.resetDatabase();
        DatabaseHandler.closeConnection();
        System.out.println("Cleanup completed. Exiting...");
    }
}