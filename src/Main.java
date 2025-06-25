import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        Parent root = FXMLLoader.load(getClass().getResource("src\\UI\\StartScene.fxml"));

        // HostServicesProvider is used to access host services like opening URLs
        HostServicesProvider.setHostServices(getHostServices());

        Scene scene = new Scene(root);
        primaryStage.setTitle("Bounty Hunter Store Terminal - Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        DatabaseInitializer.Setup();
        launch(args);

    }
}