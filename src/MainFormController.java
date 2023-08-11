import com.sun.xml.internal.bind.v2.model.core.ID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
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

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory("salary"));
        loadAllCustomers();
    }

    private void loadAllCustomers() {
        try{
            Connection connection = DriverManager.getConnection(DATABASE_URL+DATABASE_NAME,DATABASE_USER,DATABASE_PASSWORD);
            String sql = "SELECT * FROM customer";
            PreparedStatement stm = connection.prepareStatement(sql);

            ResultSet resultSet = stm.executeQuery();

            ObservableList<CustomerTM> list = FXCollections.observableArrayList();

            while (resultSet.next()){
                list.add(
                        new CustomerTM(resultSet.getString(1),resultSet.getString(2),
                                resultSet.getString(3),resultSet.getDouble(4))
                );
            }
            tableCustomer.setItems(list);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Try Again!").show();
            throw new RuntimeException(e);
        }

    }


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
                    loadAllCustomers();
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

    public void btnBackupData(ActionEvent actionEvent) throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("My Backup File");
        File file = chooser.showSaveDialog(null);

        if (file!=null){
            String backupTempData = "mysqldump -u root -p1234 customer_db";
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe","/c",backupTempData);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();


            try(FileWriter fileWriter = new FileWriter(file)){
                int readBytesData;
                byte[] bytes = new byte[1024];
                while ((readBytesData=process.getInputStream().read(bytes))!=-1){
                    fileWriter.write(new String(bytes,0,readBytesData));
                }
            }

            System.out.println(file.getAbsolutePath());

        }
    }
}
