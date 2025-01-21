package Application.controller;

import Application.controller.gameController.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WelcomeController {

    @FXML
    private Label userChecker;
    @FXML
    private Button submitButton;
    @FXML
    private TextField userTextField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static final String URL = "jdbc:mysql://b3p5m8nhncgiqy1daeus-mysql.services.clever-cloud.com:3306/b3p5m8nhncgiqy1daeus";
    private static final String USER = "urowmyrks8sa1rih";
    private static final String PASSWORD = "RsKRqsn19uGmkebas6WR";

    public void submitUser(ActionEvent event) {
        String userName = userTextField.getText();
        if (userName.isEmpty()) {
            userChecker.setText("Please enter a username!");
            return;
        } else {
            userChecker.setText("Processing...");
            Task<Boolean> dbTask = new Task<>() {

                @org.jetbrains.annotations.NotNull
                @Override
                protected Boolean call() throws Exception {
                    try {
                        return addPlayer(userName);
                    } catch (SQLException e) {
                        if (e.getMessage().contains("Communications link failure")) {
                            throw new Exception("No Internet! Please check your connection.");
                        } else if (e.getSQLState().equals("23000")) {
                            throw new Exception("Username already exists. Try another.");
                        } else {
                            throw new Exception("Database error: " + e.getMessage());
                        }
                    } catch (Exception e) {
                        throw new Exception("Unexpected error: " + e.getMessage());
                    }
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    boolean success = getValue(); // need to know why this
                    if (success) {
                        userChecker.setText("Player added successfully!");
                    } else {
                        userChecker.setText("Username isn't available. Try another.");
                    }
                }

                @Override
                protected void failed() {
                    super.failed();
                    Throwable exception = getException();
                    userChecker.setText(exception.getMessage());
                }
            };

            new Thread(dbTask).start();
        }
    }

    public boolean addPlayer(String playerName) throws SQLException {
        String insertQuery = "INSERT INTO players (name) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, playerName);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw e;
        }
    }





    @FXML
    public void easyMode(ActionEvent event) {
        String mode  = "Easy";
        loadGameScene(1.0, mode);
    }

    @FXML
    public void mediumMode(ActionEvent event) {
        String mode  = "Medium";
        loadGameScene(3.0, mode);
    }

    @FXML
    public void hardMode(ActionEvent event) {
        String mode  = "Hard";
        loadGameScene(4.5, mode);
    }

    private void loadGameScene(double fallingSpeed, String str) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Application/resources/fxmls/gameUi.fxml"));
            root = loader.load();

            gameController Obj = loader.getController();  // Get GameController instance
            Obj.setFallingSpeed(fallingSpeed);
            Obj.setMode(str);

            stage = (Stage) userTextField.getScene().getWindow();
            scene = new Scene(root, 400, 700);
            stage.setScene(scene);
            stage.setTitle("ShapeDestroyer");

            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
