package Application.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.application.Platform;

import java.net.UnknownHostException;
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
}
