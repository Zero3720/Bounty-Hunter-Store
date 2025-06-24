package UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Models.*;
import Database.DatabaseHandler;

public class Scene1Ctrl {

    // Blasters table components
    @FXML private TableView<Blaster> BlastersTable;
    @FXML private TableColumn<Blaster, String> BlstrNameClmn;
    @FXML private TableColumn<Blaster, String> BlstrColorClmn;
    @FXML private TableColumn<Blaster, Integer> BlstrRangeClmn;
    @FXML private TableColumn<Blaster, Integer> BlstrCapacityClmn;
    @FXML private TableColumn<Blaster, String> BlstrManufcturerClmn;
    @FXML private TableColumn<Blaster, Integer> BlstrPriceClmn;
    
    // Equipment table components
    @FXML private TableView<Item> EquipmentTable;
    @FXML private TableColumn<Item, String> EquipNameClmn;
    @FXML private TableColumn<Item, Integer> EquipQuantityClmn;
    @FXML private TableColumn<Item, String> EquipManufacturerClmn;
    @FXML private TableColumn<Item, String> EquipDescriptionClmn;
    @FXML private TableColumn<Item, Integer> EquipPriceClmn;
    
    // Hunter info labels
    @FXML private Label cfpiValueLabel;
    @FXML private Label nameValueLabel;
    @FXML private Label creditsValueLabel;
    @FXML private Label affiliationValueLabel;
    
    // Buttons and other controls
    @FXML private Button BuyButtom;
    @FXML private Button LoanButtom;
    @FXML private CheckMenuItem MusicToggle;
    @FXML private Hyperlink Hyperlink;
    @FXML private Tab TabBlstrs;
    @FXML private Tab TabEquip;
    
    private Hunter currentHunter;
    private Object selectedItem; // Can be Blaster or Item

    public void initialize() {
        // Initialize blasters table
        BlstrNameClmn.setCellValueFactory(new PropertyValueFactory<>("name"));
        BlstrColorClmn.setCellValueFactory(new PropertyValueFactory<>("color"));
        BlstrRangeClmn.setCellValueFactory(new PropertyValueFactory<>("range"));
        BlstrCapacityClmn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        BlstrManufcturerClmn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        BlstrPriceClmn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // Initialize equipment table
        EquipNameClmn.setCellValueFactory(new PropertyValueFactory<>("name"));
        EquipQuantityClmn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        EquipManufacturerClmn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        EquipDescriptionClmn.setCellValueFactory(new PropertyValueFactory<>("description"));
        EquipPriceClmn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // Add selection listeners
        BlastersTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                selectedItem = newVal;
                updateButtonState();
            });
        
        EquipmentTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                selectedItem = newVal;
                updateButtonState();
            });
        
        // Initially disable buttons
        BuyButtom.setDisable(true);
        LoanButtom.setDisable(true);
    }
    
    public void setCurrentHunter(Hunter hunter) {
        this.currentHunter = hunter;
        updateHunterInfo();
        loadEquipmentData();
    }
    
    private void updateHunterInfo() {
        if (currentHunter != null) {
            cfpiValueLabel.setText(String.valueOf(currentHunter.getCfpi()));
            nameValueLabel.setText(currentHunter.getName());
            creditsValueLabel.setText(currentHunter.getCredits() + " CR");
            affiliationValueLabel.setText(currentHunter.getAffiliation());
        }
    }
    
    private void loadEquipmentData() {
        // Load blasters
        BlastersTable.getItems().setAll(DatabaseHandler.getAvailableBlasters());
        
        // Load items
        EquipmentTable.getItems().setAll(DatabaseHandler.getAvailableItems());
    }
    
    private void updateButtonState() {
        boolean isSelected = selectedItem != null;
        BuyButtom.setDisable(!isSelected);
        LoanButtom.setDisable(!isSelected);
    }
    
    @FXML
    void Buy(ActionEvent event) {
        if (selectedItem != null) {
            if (selectedItem instanceof Blaster) {
                processBlasterPurchase((Blaster) selectedItem);
            } else if (selectedItem instanceof Item) {
                processItemPurchase((Item) selectedItem);
            }
        }
    }
    
    @FXML
    void Loan(ActionEvent event) {
        showAlert("Loan Option", "Loan feature coming soon!");
    }
    
    private void processBlasterPurchase(Blaster blaster) {
        boolean success = DatabaseHandler.purchaseBlaster(
            currentHunter.getCfpi(),
            blaster.getId()
        );
        
        if (success) {
            // Update hunter credits
            currentHunter.setCredits(currentHunter.getCredits() - blaster.getPrice());
            updateHunterInfo();
            showAlert("Purchase Complete", "Acquired: " + blaster.getName());
        } else {
            showAlert("Purchase Failed", "Insufficient credits!");
        }
        
        // Clear selection
        BlastersTable.getSelectionModel().clearSelection();
        selectedItem = null;
        updateButtonState();
    }
    
    private void processItemPurchase(Item item) {
        // Add item purchase method to DatabaseHandler
        boolean success = DatabaseHandler.purchaseItem(
            currentHunter.getCfpi(),
            item.getIdItem()
        );
        
        if (success) {
            // Update hunter credits
            currentHunter.setCredits(currentHunter.getCredits() - item.getPrice());
            updateHunterInfo();
            showAlert("Purchase Complete", "Acquired: " + item.getName());
        } else {
            showAlert("Purchase Failed", "Insufficient credits!");
        }
        
        // Clear selection
        EquipmentTable.getSelectionModel().clearSelection();
        selectedItem = null;
        updateButtonState();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void MusicToggle(ActionEvent event) {
        // Toggle music state
        boolean musicOn = MusicToggle.isSelected();
        // Implement your music toggle logic here
    }

    @FXML
    void OpenGithub(ActionEvent event) {
        // Open GitHub link
        HostServicesProvider.getHostServices().showDocument("https://github.com/Zero3720/Bounty-Hunter-Store");
    }
}