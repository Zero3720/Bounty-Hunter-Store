import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("D:\\FILES\\]-FACULDADE\\--FACULDADE-- 2025\\C07\\Trabalho\\Bounty-Hunter-Store\\src\\UI\\MainScene.fxml"));
        primaryStage.setTitle("Bounty Hunter Store");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        
    }
}