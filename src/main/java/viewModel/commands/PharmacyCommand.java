package viewModel.commands;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class PharmacyCommand  extends SimpleObjectProperty<EventHandler<ActionEvent>>{

    public PharmacyCommand(Runnable action) {
        super(event -> action.run());
    }

}