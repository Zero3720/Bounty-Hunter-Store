package UI;

import Database.DatabaseHandler;
import Models.Hunter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartSceneCtrl {

    @FXML
    private Button MainButton;

    @FXML
    private TextField TxtFld1;

    @FXML
    void LoginHunter(ActionEvent event) {
        String input = TxtFld1.getText().trim();

        // Validate input exists
        if (input.isEmpty()) {
            showAlert("Input Error", "Please enter a CFPI");
            return;
        }

        int cfpi;
        try {
            // Parse input directly when login is attempted
            cfpi = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "CFPI must be a numeric value");
            return;
        }

        try {
            Hunter hunter = DatabaseHandler.authenticateHunter(cfpi);
            if (hunter != null) {
                loadMainMenu(hunter);
            } else {
                showAlert("Access Denied", "No hunter found with CFPI: " + cfpi);
            }
        } catch (Exception e) {
            showAlert("System Error", "Failed to authenticate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadMainMenu(Hunter hunter) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src\\UI\\MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuCtrl controller = loader.getController();
            controller.setCurrentHunter(hunter);

            Stage stage = (Stage) MainButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bounty Hunter Store Terminal - Main Menu");

        } catch (Exception e) {
            showAlert("Navigation Error", "Failed to load main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}