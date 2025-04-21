package viewModel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import model.Medicament;
import model.Pharmacy;
import model.Producer;
import model.StocMedicament;
import model.repository.MedicamentRepository;
import model.repository.PharmacyRepository;
import model.repository.StocMedicamentRepository;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import viewModel.modelDTO.MedicamentDTO;
import viewModel.modelDTO.PharmacyDTO;
import viewModel.modelDTO.StocMedicamentDTO;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import viewModel.commands.PharmacyCommand;


public class PharmacyViewModel {

    private final MedicamentRepository medicamentRepository;
    private final PharmacyRepository pharmacyRepository;
    private final StocMedicamentRepository stocMedicamentRepository;

    private final ListProperty<PharmacyDTO> pharmacies = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<MedicamentDTO> medicaments = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<StocMedicamentDTO> stocMedicaments = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final ObjectProperty<PharmacyDTO> selectedPharmacy = new SimpleObjectProperty<>();
    private final ObjectProperty<MedicamentDTO> selectedMedicament = new SimpleObjectProperty<>();
    private final ObjectProperty<StocMedicamentDTO> selectedStocMedicament = new SimpleObjectProperty<>();

    //Pharmacy
    private final StringProperty pharmacyIdForm = new SimpleStringProperty("");
    private final StringProperty pharmacyNameForm = new SimpleStringProperty("");
    private final StringProperty pharmacyAddressForm = new SimpleStringProperty("");

    //Medicament
    private final StringProperty medIdForm = new SimpleStringProperty("");
    private final StringProperty medNameForm = new SimpleStringProperty("");
    private final StringProperty medPriceForm = new SimpleStringProperty("");
    private final StringProperty medImageForm = new SimpleStringProperty("");
    private final StringProperty medProducerForm = new SimpleStringProperty();
    private final StringProperty searchMedicamentName = new SimpleStringProperty("");


    //StocMedicament
    private final StringProperty stocPharmacyIdForm = new SimpleStringProperty("");
    private final StringProperty stocMedicamentIdForm = new SimpleStringProperty("");
    private final StringProperty stocStockForm = new SimpleStringProperty("");
    private final StringProperty stocExpirationDateForm = new SimpleStringProperty("");
    private final StringProperty filterType = new SimpleStringProperty();
    private final StringProperty filterProducer = new SimpleStringProperty();
    private final ListProperty<String> producerOptions = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<String> filterTypeOptions = new SimpleListProperty<>(FXCollections.observableArrayList("Availability", "Validity", "Producer"));

    private final StringProperty message = new SimpleStringProperty("");


    private final PharmacyCommand addPharmacyCommand;
    private final PharmacyCommand updatePharmacyCommand;
    private final PharmacyCommand deletePharmacyCommand;
    private final PharmacyCommand addMedicamentCommand;
    private final PharmacyCommand updateMedicamentCommand;
    private final PharmacyCommand deleteMedicamentCommand;
    private final PharmacyCommand addStocCommand;
    private final PharmacyCommand updateStocCommand;
    private final PharmacyCommand deleteStocCommand;
    private final PharmacyCommand filterCommand;
    private final PharmacyCommand exportCsvCommand;
    private final PharmacyCommand exportDocCommand;
    private final PharmacyCommand searchCommand;
    private final PharmacyCommand cancelSearchCommand;




    public PharmacyViewModel() {
        this.medicamentRepository = new MedicamentRepository();
        this.pharmacyRepository = new PharmacyRepository();
        this.stocMedicamentRepository = new StocMedicamentRepository();

        //initializare comenzi
        addPharmacyCommand = new PharmacyCommand(this::addPharmacy);
        updatePharmacyCommand = new PharmacyCommand(this::updatePharmacy);
        deletePharmacyCommand = new PharmacyCommand(this::deletePharmacy);
        addMedicamentCommand = new PharmacyCommand(this::addMedicament);
        updateMedicamentCommand = new PharmacyCommand(this::updateMedicament);
        deleteMedicamentCommand = new PharmacyCommand(this::deleteMedicament);
        addStocCommand = new PharmacyCommand(this::addPharmacyMedicament);
        updateStocCommand = new PharmacyCommand(this::updatePharmacyMedicament);
        deleteStocCommand = new PharmacyCommand(this::deletePharmacyMedicament);
        filterCommand = new PharmacyCommand(this::filterMedicaments);
        exportCsvCommand = new PharmacyCommand(this::exportToCsv);
        exportDocCommand = new PharmacyCommand(this::exportToDoc);
        searchCommand = new PharmacyCommand(this::searchMedicament);
        cancelSearchCommand = new PharmacyCommand(this::cancelSearch);


        //LISTENERE CLICK IN TABELE
        selectedPharmacy.addListener((obs, oldVal, newVal) -> {populatePharmacyForm(newVal); if (newVal != null) { loadStocMedicamentsInternal(newVal.getId());}});
        selectedMedicament.addListener((obs, oldVal, newVal) -> populateMedicamentForm(newVal));
        selectedStocMedicament.addListener((obs, oldVal, newVal) -> populateStocMedicamentForm(newVal));

        loadProducersInternal();
        loadPharmaciesInternal();
        loadMedicamentsInternal();
    }

    //GETTERS
    public ListProperty<PharmacyDTO> pharmaciesProperty() { return pharmacies; }
    public ListProperty<MedicamentDTO> medicamentsProperty() { return medicaments; }
    public ListProperty<StocMedicamentDTO> stocMedicamentsProperty() { return stocMedicaments; }
    public ObjectProperty<PharmacyDTO> selectedPharmacyProperty() { return selectedPharmacy; }
    public ObjectProperty<MedicamentDTO> selectedMedicamentProperty() { return selectedMedicament; }
    public ObjectProperty<StocMedicamentDTO> selectedStocMedicamentProperty() { return selectedStocMedicament; }
    public StringProperty pharmacyIdFormProperty() { return pharmacyIdForm; }
    public StringProperty pharmacyNameFormProperty() { return pharmacyNameForm; }
    public StringProperty pharmacyAddressFormProperty() { return pharmacyAddressForm; }
    public StringProperty medIdFormProperty() { return medIdForm; }
    public StringProperty medNameFormProperty() { return medNameForm; }
    public StringProperty medPriceFormProperty() { return medPriceForm; }
    public StringProperty medImageFormProperty() { return medImageForm; }
    public StringProperty medProducerFormProperty() { return medProducerForm; }
    public StringProperty stocPharmacyIdFormProperty() { return stocPharmacyIdForm; }

    public StringProperty stocMedicamentIdFormProperty() { return stocMedicamentIdForm; }
    public StringProperty stocStockFormProperty() { return stocStockForm; }
    public StringProperty stocExpirationDateFormProperty() { return stocExpirationDateForm; }
    public StringProperty filterTypeProperty() { return filterType; }
    public StringProperty filterProducerProperty() { return filterProducer; }
    public StringProperty searchMedicamentNameProperty() { return searchMedicamentName; }
    public ListProperty<String> producerOptionsProperty() { return producerOptions; }
    public ListProperty<String> filterTypeOptionsProperty() { return filterTypeOptions; }
    public StringProperty messageProperty() { return message; }

    //SETTERS
    public void setSelectedPharmacy(PharmacyDTO pharmacy) { this.selectedPharmacy.set(pharmacy); }
    public void setSelectedMedicament(MedicamentDTO medicament) { this.selectedMedicament.set(medicament); }
    public void setSelectedStocMedicament(StocMedicamentDTO stocMedicament) { this.selectedStocMedicament.set(stocMedicament); }




    ////////////////////
    public PharmacyCommand getAddPharmacyCommand() { return addPharmacyCommand; }
    public PharmacyCommand getUpdatePharmacyCommand() { return updatePharmacyCommand; }
    public PharmacyCommand getDeletePharmacyCommand() { return deletePharmacyCommand; }
    public PharmacyCommand getAddMedicamentCommand() { return addMedicamentCommand; }
    public PharmacyCommand getUpdateMedicamentCommand() { return updateMedicamentCommand; }
    public PharmacyCommand getDeleteMedicamentCommand() { return deleteMedicamentCommand; }
    public PharmacyCommand getAddStocCommand() { return addStocCommand; }
    public PharmacyCommand getUpdateStocCommand() { return updateStocCommand; }
    public PharmacyCommand getDeleteStocCommand() { return deleteStocCommand; }
    public PharmacyCommand getFilterCommand() { return filterCommand; }
    public PharmacyCommand getExportCsvCommand() { return exportCsvCommand; }
    public PharmacyCommand getExportDocCommand() { return exportDocCommand; }
    public PharmacyCommand getSearchCommand() { return searchCommand; }
    public PharmacyCommand getCancelSearchCommand() { return cancelSearchCommand; }




    //LOADERE
    private void loadProducersInternal() {
        try {
            List<String> producers = Producer.getProducersList();
            this.producerOptions.set(FXCollections.observableArrayList(producers));
        } catch (Exception e) {
            message.set("Error loading producers: " + e.getMessage());
        }
    }

    private void loadPharmaciesInternal() {
        try {
            List<Pharmacy> loadedPharmacies = pharmacyRepository.getAllPharmacies();
            this.pharmacies.set(FXCollections.observableArrayList(pharmacyToPharmacyDTO(loadedPharmacies)));
        } catch (Exception e) {
            message.set("Error loading pharmacies: " + e.getMessage());
        }
    }

    private void loadMedicamentsInternal() {
        try {
            List<Medicament> loadedMedicaments = medicamentRepository.getAllMedicaments();
            this.medicaments.set(FXCollections.observableArrayList(medicamentToMedicamentDTO(loadedMedicaments)));
        } catch (Exception e) {
            message.set("Error loading medicaments: " + e.getMessage());
        }
    }

    private void loadStocMedicamentsInternal(int pharmacyId) {
        if (pharmacyId <= 0) {
            return;
        }
        try {
            List<StocMedicament> stoc = stocMedicamentRepository.getAllPharmacyMedicaments(pharmacyId);
            this.stocMedicaments.set(FXCollections.observableArrayList(stocMedicamentToStocMedicamentDTO(stoc)));
        } catch (Exception e) {
            message.set("Error loading stock for pharmacy " + pharmacyId + ": " + e.getMessage());
        }
    }


    //POPULATE FORMS
    private void populatePharmacyForm(PharmacyDTO pharmacy) {
        if (pharmacy != null) {
            pharmacyIdForm.set(String.valueOf(pharmacy.getId()));
            pharmacyNameForm.set(pharmacy.getName());
            pharmacyAddressForm.set(pharmacy.getAddress());
            stocPharmacyIdForm.set(String.valueOf(pharmacy.getId())); // Also set for the stock form
        }
    }


    private void populateMedicamentForm(MedicamentDTO medicament) {
        if (medicament != null) {
            medIdForm.set(String.valueOf(medicament.getId()));
            medNameForm.set(medicament.getName());
            medPriceForm.set(String.valueOf(medicament.getPrice()));
            medProducerForm.set(medicament.getProducer());
            medImageForm.set(medicament.getImagePath()); //Cum ia likul?

            stocMedicamentIdForm.set(String.valueOf(medicament.getId()));
        }

    }

    private void populateStocMedicamentForm(StocMedicamentDTO stoc) {
        if (stoc != null) {
            stocPharmacyIdForm.set(String.valueOf(stoc.getPharmacyId()));
            stocMedicamentIdForm.set(String.valueOf(stoc.getMedicamentId()));
            stocStockForm.set(String.valueOf(stoc.getStock()));
            stocExpirationDateForm.set(stoc.getExpirationDate() != null ? stoc.getExpirationDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "");
        }
    }



    //PHARMACY
    public void addPharmacy() {
        try {
        Pharmacy pharmacy = validatePharmacyInput("ADD");
            if (pharmacy != null) {

                pharmacyRepository.addPharmacy(pharmacy);
                loadPharmaciesInternal();
                message.set("Pharmacy added successfully.");
            }
        } catch (Exception e) {
            message.set("Error adding pharmacy: " + e.getMessage());
        }
    }

    public void updatePharmacy() {
        try {
            Pharmacy pharmacy = validatePharmacyInput("UPDATE");
            if (pharmacy != null) {

                pharmacyRepository.updatePharmacy(pharmacy);
                loadPharmaciesInternal();
                message.set("Pharmacy updated successfully.");
            }
        } catch (Exception e) {
            message.set("Error updating pharmacy: " + e.getMessage());
        }
    }

    public void deletePharmacy() {
        try {
            int id = Integer.parseInt(pharmacyIdForm.get());
            if (id <= 0) {
                message.set("Invalid pharmacy ID.");
                return;
            }
            pharmacyRepository.deletePharmacy(id);

            loadPharmaciesInternal();
            stocMedicaments.clear();
            message.set("Pharmacy deleted successfully.");

        } catch (Exception e) {
            message.set("No pharmacy was selected!");
        }
    }


    //MEDICAMENT
    public void addMedicament() {
        try {
        Medicament medicament = validateMedicamentInput("ADD");
            if (medicament != null) {

                medicamentRepository.addMedicament(medicament);
                loadMedicamentsInternal();
                message.set("Medicament added successfully.");
            }
        } catch (Exception e) {
                message.set("Error adding medicament: " + e.getMessage());
        }

    }

    public void updateMedicament() {
        try {
            Medicament medicament = validateMedicamentInput("UPDATE");
            if (medicament != null) {

                medicamentRepository.updateMedicament(medicament);
                loadMedicamentsInternal();
                if (selectedPharmacy.get() != null) {
                    loadStocMedicamentsInternal(selectedPharmacy.get().getId());
                }
                message.set("Medicament updated successfully.");
            }
        }catch (Exception e) {
                message.set("Error updating medicament: " + e.getMessage());
        }
    }

    public void deleteMedicament() {
        try {
            int id = Integer.parseInt(medIdForm.get());
            if (id <= 0) {
                message.set("Invalid Medicament ID.");
                return;
            }
            medicamentRepository.deleteMedicament(id);
            loadMedicamentsInternal();
            if (selectedPharmacy.get() != null) {
                loadStocMedicamentsInternal(selectedPharmacy.get().getId());
            }
            message.set("Medicament deleted successfully.");

        } catch (Exception e) {
            message.set("Error deleting medicament: " + e.getMessage());
        }
    }


    ////////////////////
    public void addPharmacyMedicament() {
        try {
            StocMedicament stocMedicament = validateStocMedicamentInput();
            if (stocMedicament != null) {

                stocMedicamentRepository.addStocMedicament(stocMedicament);
                loadStocMedicamentsInternal(stocMedicament.getPharmacyId());
                message.set("Medicament stock added successfully.");
            }
        } catch (Exception e) {
            message.set("Error adding medicament stock: " + e.getMessage());
        }
    }

    public void updatePharmacyMedicament() {
        try {
            StocMedicament stocMedicament = validateStocMedicamentInput();
            if (stocMedicament != null) {

            stocMedicamentRepository.updateStocMedicament(stocMedicament);
            loadStocMedicamentsInternal(stocMedicament.getPharmacyId());
            message.set("Medicament stock updated successfully.");
            }
        } catch (Exception e) {
            message.set("Error updating medicament stock: " + e.getMessage());
        }
    }

    public void deletePharmacyMedicament() {
        try {
            int pharmacyId = Integer.parseInt(stocPharmacyIdForm.get());
            int medicamentId = Integer.parseInt(stocMedicamentIdForm.get());

            if (pharmacyId <= 0 || medicamentId <= 0) {
                message.set("Invalid Pharmacy or Medicament ID.");
                return;
            }
            stocMedicamentRepository.deleteStocMedicament(pharmacyId, medicamentId);
            loadStocMedicamentsInternal(pharmacyId);
            message.set("Medicament deleted successfully.");

        } catch (Exception e) {
            message.set("Error deleting medicament stock: " + e.getMessage());
        }
    }

    //FILTER
    public void filterMedicaments() {
        int pharmacyId = selectedPharmacy.get() != null ? selectedPharmacy.get().getId() : -1;
        if (pharmacyId < 0) {
            message.set("Please select a pharmacy first.");
            return;
        }

        String type = filterType.get();
        List<StocMedicament> filteredList = new ArrayList<>();

        if (type == null || type.isEmpty()) {
            message.set("Please select a filter type.");
            return;
        }


        try {
            switch (type) {
                case "Availability":
                    filteredList = stocMedicamentRepository.getAvailableMedicaments(pharmacyId);
                    break;
                case "Validity":
                    filteredList = stocMedicamentRepository.getValidMedicaments(pharmacyId);
                    break;
                case "Producer":
                    String producer = filterProducer.get();
                    if (producer == null || producer.isEmpty()) {
                        message.set("Please select a producer.");
                        return;
                    }
                    filteredList = stocMedicamentRepository.getMedicamentsByProducer(pharmacyId, producer);
                    break;
            }

            this.stocMedicaments.set(FXCollections.observableArrayList(stocMedicamentToStocMedicamentDTO(filteredList)));
            message.set("Filter applied successfully.");

        } catch (Exception e) {
            message.set("Error applying filter: " + e.getMessage());
        }
    }



    public void exportToCsv() {
        int pharmacyId = selectedPharmacy.get() != null ? selectedPharmacy.get().getId() : 0;
        if (pharmacyId <= 0) {
            message.set("Please select a pharmacy to export data for.");
            return;
        }

        List<StocMedicament> allMedicaments = stocMedicamentRepository.getAllPharmacyMedicaments(pharmacyId);

        List<StocMedicament> expiredOrOutOfStock = allMedicaments.stream()
                .filter(med -> med.getStock() == 0 || (med.getExpirationDate() != null && med.getExpirationDate().isBefore(LocalDate.now())))
                .toList();

        if (expiredOrOutOfStock.isEmpty()) {
            message.set("No expired or out-of-stock medicaments to export for this pharmacy.");
            return;
        }

        String filePath = "unavailable_medicaments_" + pharmacyId + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Pharmacy ID,Medicament ID,Producer,Name,Stock,Expiration Date\n");

            for (StocMedicament med : expiredOrOutOfStock) {
                writer.write(med.getPharmacyId() + "," +
                        med.getMedicamentId() + "," +
                        med.getProducer() + "," +
                        med.getName() + "," +
                        med.getStock() + "," +
                        (med.getExpirationDate() != null ? med.getExpirationDate().format(DateTimeFormatter.ISO_LOCAL_DATE)  : "") +
                        "\n");
            }
            message.set("Data exported successfully to: " + filePath);
        } catch (IOException e) {
            message.set("Error exporting data to CSV: " + e.getMessage());
        }
    }



    public void exportToDoc() {
        int pharmacyId = selectedPharmacy.get() != null ? selectedPharmacy.get().getId() : -1;
        if (pharmacyId < 0) {
            message.set("Please select a pharmacy to export data for.");
            return;
        }

        List<StocMedicament> allMedicaments = stocMedicamentRepository.getAllPharmacyMedicaments(pharmacyId);

        List<StocMedicament> expiredOrOutOfStock = allMedicaments.stream()
                .filter(med -> med.getStock() == 0 || (med.getExpirationDate() != null && med.getExpirationDate().isBefore(LocalDate.now())))
                .toList();

        if (expiredOrOutOfStock.isEmpty()) {
            message.set("No expired or out-of-stock medicaments to export for this pharmacy.");
            return;
        }

        String filePath = "unavailable_medicaments_" + pharmacyId + ".docx"; // Include pharmacy ID

        try (XWPFDocument document = new XWPFDocument(); FileOutputStream out = new FileOutputStream(filePath)) {
            XWPFParagraph title = document.createParagraph();
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Expired or Out of Stock Medicaments for Pharmacy ID: " + pharmacyId);
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.addBreak();

            for (StocMedicament med : expiredOrOutOfStock) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText("Medicament ID: " + med.getMedicamentId()); run.addBreak();
                run.setText("Name: " + med.getName()); run.addBreak();
                run.setText("Producer: " + med.getProducer()); run.addBreak();
                run.setText("Stock: " + med.getStock()); run.addBreak();
                run.setText("Expiration Date: " + (med.getExpirationDate() != null ? med.getExpirationDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "N/A"));
                run.addBreak();
            }
            document.write(out);

            message.set("Data exported successfully to: " + filePath);
        } catch (IOException e) {
            message.set("Error exporting data to DOCX: " + e.getMessage());
        }
    }


    public void searchMedicament() {
        String name = searchMedicamentName.get();
        if (name.isEmpty()) {
            message.set("Please enter a medicament name.");
            return;
        }

        try {
            List<Medicament> results = medicamentRepository.searchMedicamentsByName(name.trim());
            if (results.isEmpty()) {
                message.set("No medicament found with the name: " + name.trim());
            }
            else{
                this.medicaments.set(FXCollections.observableArrayList(medicamentToMedicamentDTO(results)));
            }
        } catch (Exception e) {
            message.set("Error searching medicaments: " + e.getMessage());
        }
    }


    public void cancelSearch() {
        searchMedicamentName.set("");
        loadMedicamentsInternal();
    }


    //VALIDATOARE
    private StocMedicament validateStocMedicamentInput() {
        int medicamentId = Integer.parseInt(stocMedicamentIdForm.get());
        if (medicamentId < 0) {
            message.set("Invalid Medicament ID");
            return null;
        }

        int pharmacyId = Integer.parseInt(stocPharmacyIdForm.get());
        if (pharmacyId < 0) {
            message.set("Invalid Pharmacy ID");
            return null;
        }

        int stock = Integer.parseInt(stocStockForm.get());
        if (stock < 0) {
            message.set("Invalid Stock");
            return null;
        }

        LocalDate expirationDate;
        try {
            expirationDate = LocalDate.parse(stocExpirationDateForm.get());
        } catch (DateTimeParseException e) {
            message.set("Invalid Expiration Date");
            return null;
        }

        return new StocMedicament(pharmacyId, medicamentId, stock, expirationDate);
    }



    //VALIDATOARE
    private Medicament validateMedicamentInput(String validationType) {

        int id = -1;
        if ("UPDATE".equals(validationType)) {
            id = Integer.parseInt(medIdForm.get());
            if (id <= 0) {
                message.set("Invalid Medicament ID.");
                return null;
            }
        }

        String name = medNameForm.get();
        if (name == null || name.trim().isEmpty()) {
            message.set("Medicament name cannot be empty.");
            return null;
        }

        String producerStr = medProducerForm.get();
        if (producerStr == null || producerStr.isEmpty()) {
            message.set("Medicament producer must be selected.");
            return null;
        }

        String imagePath = medImageForm.get();
        if (imagePath == null || imagePath.trim().isEmpty()) {
            message.set("Medicament image path cannot be empty.");
            return null;
        }

        float price = Float.parseFloat(medPriceForm.get());
        if (price <= 0) {
            message.set("Medicament price must be positive.");
            return null;
        }

        return new Medicament(id, name, Producer.valueOf(producerStr), price, imagePath);
    }

    //VALIDATOARE
    private Pharmacy validatePharmacyInput(String validationType) {
        int id = -1;
        if ("UPDATE".equals(validationType)) {
            id = Integer.parseInt(pharmacyIdForm.get());
            if (id <= 0) {
                message.set("Invalid Pharmacy ID for update.");
                return null;
            }
        }

        String name = pharmacyNameForm.get();
        if (name == null || name.isEmpty()) {
            message.set("Pharmacy name cannot be empty.");
            return null;
        }

        String address = pharmacyAddressForm.get();
        if (address == null || address.isEmpty()) {
            message.set("Pharmacy address cannot be empty.");
            return null;
        }

        return new Pharmacy(id, name, address);
    }




    //STOC_MEDICAMENT LIST TO STOC_MEDICAMENT_DTO LIST
    private List<StocMedicamentDTO> stocMedicamentToStocMedicamentDTO(List<StocMedicament> stocList) {
        if (stocList == null)
            return new ArrayList<>();

        return stocList.stream()
                .map(sm -> new StocMedicamentDTO(sm.getPharmacyId(), sm.getMedicamentId(), sm.getName(), sm.getProducer(), sm.getPrice(),
                        sm.getImagePath(), sm.getStock(), sm.getExpirationDate()))
                .collect(Collectors.toList());
    }


    //MEDICAMENT LIST TO MEDICAMENT_DTO LIST
    private List<MedicamentDTO> medicamentToMedicamentDTO(List<Medicament> medicaments) {
        if (medicaments == null)
            return new ArrayList<>();

        return medicaments.stream()
                .map(m -> new MedicamentDTO(m.getId(), m.getName(), m.getProducer(), m.getPrice(), m.getImage()))
                .collect(Collectors.toList());
    }


    //PHARMACY LIST TO PHARMACY_DTO LIST
    private List<PharmacyDTO> pharmacyToPharmacyDTO(List<Pharmacy> pharmacies) {
        if (pharmacies == null)
            return new ArrayList<>();

        return pharmacies.stream()
                .map(p -> new PharmacyDTO(p.getId(), p.getName(), p.getAddress()))
                .collect(Collectors.toList());
    }


}