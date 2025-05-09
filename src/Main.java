import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("/Application/resources/fxmls/Welcome.fxml"));
            Scene scene = new Scene(root, 400, 700);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Shape Destroyer");
            primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
