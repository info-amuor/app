import com.sun.xml.internal.bind.v2.model.core.ID;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class MainFormController {
    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TableView<CustomerTM> tableCustomer;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colSalary;

    private boolean validateFields(TextField field, Pattern pattern){
        if (pattern.matcher(field.getText()).matches()){
            field.setStyle("");
            return true;
        }else{
            field.setStyle("-fx-border-color: red;");
            return false;
        }
    }
    private boolean validate() {
        final String ID_REG = "\\D-\\d"; //D-1***
        final String NAME_REG = ".{3,}";
        final String ADDRESS_REG = ".+";
        final String SALARY_REG = "\\d+(\\.\\d{1,2})?";

        Pattern idP=Pattern.compile(ID_REG);
        Pattern nameP=Pattern.compile(NAME_REG);
        Pattern addressP=Pattern.compile(ADDRESS_REG);
        Pattern salaryP=Pattern.compile(SALARY_REG);

        if (
                validateFields(txtId,idP) &&
                        validateFields(txtName,nameP) &&
                        validateFields(txtAddress, addressP) &&
                        validateFields(txtSalary, salaryP)
        ){
            return true;
        }else{
            return false;
        }

    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if (validate()){
            // save
        }else{
            new Alert(Alert.AlertType.WARNING, "Please insert valid data!").show();
        }

    }

    public void btnPrintAll(ActionEvent actionEvent) {
    }
}
