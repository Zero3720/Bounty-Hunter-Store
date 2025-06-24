package UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Scene1Ctrl {

    @FXML
    private TableColumn<?, ?> BlstrCapacityClmn;

    @FXML
    private TableColumn<?, ?> BlstrColorClmn;

    @FXML
    private TableColumn<?, ?> BlstrManufcturerClmn;

    @FXML
    private TableColumn<?, ?> BlstrNameClmn;

    @FXML
    private TableColumn<?, ?> BlstrPriceClmn;

    @FXML
    private TableColumn<?, ?> BlstrRangeClmn;

    @FXML
    private Button BuyButtom;

    @FXML
    private TableColumn<?, ?> EquipDescriptionClmn;

    @FXML
    private TableColumn<?, ?> EquipManufacturerClmn;

    @FXML
    private TableColumn<?, ?> EquipNameClmn;

    @FXML
    private TableColumn<?, ?> EquipPriceClmn;

    @FXML
    private TableColumn<?, ?> EquipQuantityClmn;

    @FXML
    private TableView<?> EquipmentTable;

    @FXML
    private TableView<?> EquipmentTable2;

    @FXML
    private Button LoanButtom;

    @FXML
    private CheckMenuItem MusicToggle;

    private String link = "https://github.com/Zero3720/Bounty-Hunter-Store";

    @FXML
    void Buy(ActionEvent event) {

    }

    @FXML
    void Loan(ActionEvent event) {

    }

    @FXML
    void MusicToggle(ActionEvent event) {

    }

}
