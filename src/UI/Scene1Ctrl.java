package UI;

import java.net.URL;

import Database.DatabaseHandler;
import Models.Blaster;
import Models.Hunter;
import Models.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

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


    private MediaPlayer mediaPlayer;
    private boolean mediaInitialized = false;
    private boolean shouldPlayOnReady = false;

    public void initialize() {

        initializeMediaPlayer();
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
            (_obs, oldVal, newVal) -> {
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

    private void initializeMediaPlayer() {
    try {
        // Use direct Google Drive download link
        String directDownloadUrl = "https://drive.google.com/uc?export=download&id=1x-tJOFaQ4v5oztXyMosXA_0_eckOhrIn";
        
        // Create media from URL
        Media media = new Media(directDownloadUrl);
        mediaPlayer = new MediaPlayer(media);
        
        // Set up looping
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        
        // Handle playback errors
        mediaPlayer.setOnError(() -> {
            System.err.println("Media error: " + mediaPlayer.getError());
            showAlert("Playback Error", "Failed to play audio: " + mediaPlayer.getError().getMessage());
            mediaInitialized = false;
        });
        
        // Setup ready handler
        mediaPlayer.setOnReady(() -> {
            mediaInitialized = true;
            System.out.println("Media loaded successfully!");
            if (shouldPlayOnReady) {
                mediaPlayer.play();
                shouldPlayOnReady = false;
            }
        });
        
        // Add buffering feedback
        mediaPlayer.bufferProgressTimeProperty().addListener((obs, oldVal, newVal) -> {
            double progress = mediaPlayer.getBufferProgressTime().toSeconds() / 
                             mediaPlayer.getTotalDuration().toSeconds();
            System.out.printf("Buffering: %.1f%%%n", progress * 100);
        });
        
    } catch (Exception e) {
        System.err.println("Error initializing media player: " + e.getMessage());
        e.printStackTrace();
        showAlert("Initialization Error", "Failed to initialize audio: " + e.getMessage());
        mediaInitialized = false;
    }
}

    @FXML
    void MusicToggle(ActionEvent event) {
        if (!mediaInitialized) {
            showAlert("Music Error", "Audio system not initialized");
            return;
        }
        
        boolean musicOn = MusicToggle.isSelected();
        
        if (musicOn) {
            // Handle case where media is still loading
            if (mediaPlayer.getStatus() == MediaPlayer.Status.UNKNOWN) {
                shouldPlayOnReady = true;
            } else {
                mediaPlayer.play();
            }
        } else {
            mediaPlayer.pause();
        }
    }


    @FXML
    void OpenGithub(ActionEvent event) {
        // Open GitHub link
        HostServicesProvider.getHostServices().showDocument("https://github.com/Zero3720/Bounty-Hunter-Store");
    }
}