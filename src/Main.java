import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import Database.DatabaseInitializer;
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