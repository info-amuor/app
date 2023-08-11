import com.sun.xml.internal.bind.v2.model.core.ID;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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


    private static final String DATABASE_URL="jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME="customer_db";
    private static final String DATABASE_USER="root";
    private static final String DATABASE_PASSWORD="1234";

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
            try{
                Connection connection = DriverManager.getConnection(DATABASE_URL+DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
                String sql = "INSERT INTO customer VALUES(?,?,?,?)";
                PreparedStatement stm = connection.prepareStatement(sql);
                stm.setObject(1,txtId.getText());
                stm.setObject(2,txtName.getText());
                stm.setObject(3,txtAddress.getText());
                stm.setObject(4,Double.parseDouble(txtSalary.getText()));

                if (stm.executeUpdate()>0){
                    new Alert(Alert.AlertType.INFORMATION, "Saved!").show();
                    clearData();
                }else{
                    new Alert(Alert.AlertType.ERROR, "Try Again!").show();
                }

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Try Again!").show();
                throw new RuntimeException(e);
            }

        }else{
            new Alert(Alert.AlertType.WARNING, "Please insert valid data!").show();
        }

    }

    private void clearData(){
        txtId.clear();
        txtName.clear();
        txtSalary.clear();
        txtAddress.clear();

        txtId.requestFocus();
    }

    public void btnPrintAll(ActionEvent actionEvent) {
    }

    public void btnBackupData(ActionEvent actionEvent) {
    }
}
