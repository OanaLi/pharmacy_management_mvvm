package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import viewModel.PharmacyViewModel;
import viewModel.modelDTO.MedicamentDTO;
import viewModel.modelDTO.PharmacyDTO;
import viewModel.modelDTO.StocMedicamentDTO;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class PharmacyView implements Initializable {

    private PharmacyViewModel viewModel;

    //Pharmacy
    @FXML private TableView<PharmacyDTO> pharmacyTable;
    @FXML private TableColumn<PharmacyDTO, Integer> pharmacyIdCol;
    @FXML private TableColumn<PharmacyDTO, String> pharmacyNameCol;
    @FXML private TableColumn<PharmacyDTO, String> pharmacyAddressCol;
    @FXML private TextField pharmacyIdField;
    @FXML private TextField pharmacyNameField;
    @FXML private TextField pharmacyAddressField;
    @FXML private Button addPharmacyButton;
    @FXML private Button updatePharmacyButton;
    @FXML private Button deletePharmacyButton;

    //PharmacyMedicaments
    @FXML private ComboBox<String> filterComboBox;
    @FXML private ComboBox<String> filterProducerComboBox;
    @FXML private Button filterButton;
    @FXML private Button exportCsvButton;
    @FXML private Button exportDocButton;
    @FXML private TableView<StocMedicamentDTO> pharmacyMedicamentTable;
    @FXML private TableColumn<StocMedicamentDTO, Integer> medicamentIdCol;
    @FXML private TableColumn<StocMedicamentDTO, String> medNameCol;
    @FXML private TableColumn<StocMedicamentDTO, Float> medPriceCol;
    @FXML private TableColumn<StocMedicamentDTO, String> medProducerCol;
    @FXML private TableColumn<StocMedicamentDTO, String> medImageCol;
    @FXML private TableColumn<StocMedicamentDTO, Integer> stockCol;
    @FXML private TableColumn<StocMedicamentDTO, LocalDate> expirationCol;
    @FXML private TextField addPharmacyIdField;
    @FXML private TextField addMedicamentIdField;
    @FXML private TextField stockField;
    @FXML private TextField expirationDateField;
    @FXML private Button addPharmacyMedicamentButton;
    @FXML private Button updatePharmacyMedicamentButton;
    @FXML private Button deletePharmacyMedicamentButton;

    //Medicaments
    @FXML private TextField searchMedicamentField;
    @FXML private Button searchMedicamentButton;
    @FXML private Button cancelSearchMedicamentButton;
    @FXML private TableView<MedicamentDTO> medicamentTable;
    @FXML private TableColumn<MedicamentDTO, Integer> medIdCol;
    @FXML private TableColumn<MedicamentDTO, String> medNameOnlyCol;
    @FXML private TableColumn<MedicamentDTO, Float> medPriceOnlyCol;
    @FXML private TableColumn<MedicamentDTO, String> medProducerOnlyCol;
    @FXML private TableColumn<MedicamentDTO, String> medImageOnlyCol;
    @FXML private TextField medicamentIdField;
    @FXML private TextField medicamentNameField;
    @FXML private TextField medicamentPriceField;
    @FXML private ComboBox<String> medicamentProducerComboBox;
    @FXML private TextField medicamentImageField;
    @FXML private Button addMedicamentButton;
    @FXML private Button updateMedicamentButton;
    @FXML private Button deleteMedicamentButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.viewModel = new PharmacyViewModel();

        setupMessageListener();
        setupColumnFactories();
        setupBindings();
        setupListeners();
        setupButtons();

    }


    private void setupColumnFactories() {
        //Pharmacy
        pharmacyIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        pharmacyNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        pharmacyAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        //PharmacyMedicamentTable
        medicamentIdCol.setCellValueFactory(new PropertyValueFactory<>("medicamentId"));
        medNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        medPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        medProducerCol.setCellValueFactory(new PropertyValueFactory<>("producer"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        expirationCol.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        medImageCol.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        medImageCol.setCellFactory(param -> createImageCell());

        //Medicament
        medIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        medNameOnlyCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        medPriceOnlyCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        medProducerOnlyCol.setCellValueFactory(new PropertyValueFactory<>("producer"));
        medImageOnlyCol.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        medImageOnlyCol.setCellFactory(param -> createImageCell());

        medImageOnlyCol.setPrefWidth(120);
        medImageOnlyCol.setMinWidth(120);

        medImageCol.setPrefWidth(120);
        medImageCol.setMinWidth(120);
    }

    private <T> TableCell<T, String> createImageCell() {

        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        TableCell<T, String> cell = new TableCell<>() {


            @Override
            protected void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);

                try {
                    Image img = new Image("file:" + path, true);
                    imageView.setImage(img);
                    setGraphic(imageView);
                } catch (Exception e) {
                    System.err.println("Error loading image: " + path);
                    setGraphic(null);
                }
            }
        };


        return cell;
    }


    private void setupBindings() {
        pharmacyTable.itemsProperty().bind(viewModel.pharmaciesProperty());
        pharmacyMedicamentTable.itemsProperty().bind(viewModel.stocMedicamentsProperty());
        medicamentTable.itemsProperty().bind(viewModel.medicamentsProperty());

        //Pharmacy
        pharmacyIdField.textProperty().bindBidirectional(viewModel.pharmacyIdFormProperty());
        pharmacyNameField.textProperty().bindBidirectional(viewModel.pharmacyNameFormProperty());
        pharmacyAddressField.textProperty().bindBidirectional(viewModel.pharmacyAddressFormProperty());

        //PharmacyMedicament
        addPharmacyIdField.textProperty().bindBidirectional(viewModel.stocPharmacyIdFormProperty());
        addMedicamentIdField.textProperty().bindBidirectional(viewModel.stocMedicamentIdFormProperty());
        stockField.textProperty().bindBidirectional(viewModel.stocStockFormProperty());
        expirationDateField.textProperty().bindBidirectional(viewModel.stocExpirationDateFormProperty());

        //Medicament
        medicamentIdField.textProperty().bindBidirectional(viewModel.medIdFormProperty());
        medicamentNameField.textProperty().bindBidirectional(viewModel.medNameFormProperty());
        medicamentPriceField.textProperty().bindBidirectional(viewModel.medPriceFormProperty());
        medicamentProducerComboBox.valueProperty().bindBidirectional(viewModel.medProducerFormProperty());
        medicamentImageField.textProperty().bindBidirectional(viewModel.medImageFormProperty());
        filterComboBox.valueProperty().bindBidirectional(viewModel.filterTypeProperty());
        filterProducerComboBox.valueProperty().bindBidirectional(viewModel.filterProducerProperty());
        searchMedicamentField.textProperty().bindBidirectional(viewModel.searchMedicamentNameProperty());

        //ComboBox
        filterComboBox.itemsProperty().bind(viewModel.filterTypeOptionsProperty());
        filterProducerComboBox.itemsProperty().bind(viewModel.producerOptionsProperty());
        medicamentProducerComboBox.itemsProperty().bind(viewModel.producerOptionsProperty());
    }

    private void setupListeners() {
        pharmacyTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> viewModel.setSelectedPharmacy(newSelection));
        medicamentTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> viewModel.setSelectedMedicament(newSelection));
        pharmacyMedicamentTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> viewModel.setSelectedStocMedicament(newSelection));
    }


    private void setupButtons(){
        addPharmacyButton.onActionProperty().bind(viewModel.getAddPharmacyCommand());
        updatePharmacyButton.onActionProperty().bind(viewModel.getUpdatePharmacyCommand());
        deletePharmacyButton.onActionProperty().bind(viewModel.getDeletePharmacyCommand());

        addMedicamentButton.onActionProperty().bind(viewModel.getAddMedicamentCommand());
        updateMedicamentButton.onActionProperty().bind(viewModel.getUpdateMedicamentCommand());
        deleteMedicamentButton.onActionProperty().bind(viewModel.getDeleteMedicamentCommand());

        addPharmacyMedicamentButton.onActionProperty().bind(viewModel.getAddStocCommand());
        updatePharmacyMedicamentButton.onActionProperty().bind(viewModel.getUpdateStocCommand());
        deletePharmacyMedicamentButton.onActionProperty().bind(viewModel.getDeleteStocCommand());

        filterButton.onActionProperty().bind(viewModel.getFilterCommand());
        exportCsvButton.onActionProperty().bind(viewModel.getExportCsvCommand());
        exportDocButton.onActionProperty().bind(viewModel.getExportDocCommand());

        searchMedicamentButton.onActionProperty().bind(viewModel.getSearchCommand());
        cancelSearchMedicamentButton.onActionProperty().bind(viewModel.getCancelSearchCommand());
    }

    private void setupMessageListener() {
        viewModel.messageProperty().addListener((obs, oldMsg, newMsg) -> {
            if (newMsg != null && !newMsg.isEmpty()) { //asta se punea in viewmodel
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText(newMsg);
                alert.showAndWait();

                viewModel.messageProperty().set("");
            }
        });
    }



}