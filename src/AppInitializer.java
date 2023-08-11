import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AppInitializer extends Application {

    private static final String DATABASE_URL="jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME="customer_db";
    private static final String DATABASE_USER="root";
    private static final String DATABASE_PASSWORD="1234";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        initializeDatabaseIfNotExists();

        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("MainForm.fxml"))));
        primaryStage.show();

    }

    private void initializeDatabaseIfNotExists() {
        try(Connection connection = DriverManager.getConnection(DATABASE_URL,DATABASE_USER,DATABASE_PASSWORD)){
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS "+DATABASE_NAME);
            statement.executeUpdate("USE "+DATABASE_NAME);

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS customer("+"id VARCHAR(45) PRIMARY KEY,"+"name VARCHAR(45) NOT NULL,"+"address VARCHAR(45) NOT NULL,"+"salary DOUBLE)");

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
