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

public class MainSceneController {

    @FXML
    private Button MainButton;

    @FXML
    private TextField TxtFld1;

    private int enteredCFPI;  // Store the entered CFPI value

    // Called when user presses Enter in text field
    @FXML
    void Cpfi_Input(ActionEvent event) {
        try {
            // Capture and store the CFPI value
            enteredCFPI = Integer.parseInt(TxtFld1.getText().trim());
        } catch (NumberFormatException e) {
            // Handle invalid input immediately
            showAlert("Input Error", "CFPI must be a numeric value");
            enteredCFPI = -1;  // Set invalid value
        }
    }

    // Called when login button is pressed
    @FXML
    void LoginHunter(ActionEvent event) {
        // Check if we have a valid CFPI
        if (enteredCFPI <= 0) {
            showAlert("Input Error", "Please enter a valid CFPI first");
            return;
        }
        
        try {
            // Use the stored CFPI for authentication
            Hunter hunter = DatabaseHandler.authenticateHunter(enteredCFPI);
            
            if (hunter != null) {
                // Successful login - load main menu
                loadMainMenu(hunter);
            } else {
                // Invalid CFPI
                showAlert("Access Denied", "No hunter found with CFPI: " + enteredCFPI);
            }
        } catch (Exception e) {
            // Handle other errors
            showAlert("System Error", "Failed to authenticate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadMainMenu(Hunter hunter) {
        try {
            // Load FXML for main menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/Scene1.fxml"));
            Parent root = loader.load();
            
            // Get controller and pass hunter data
            /*Scene1Ctrl mainController = loader.getController();
            mainController.initializeWithHunter(hunter);*/
            
            // Get current stage and switch scenes
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
