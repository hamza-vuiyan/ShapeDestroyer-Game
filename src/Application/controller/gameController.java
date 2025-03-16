package Application.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Optional;
import java.util.Random;

public class gameController {

    @FXML
    private Label modeLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Pane gamePane;


    public double fallingSpeed;

    public void setFallingSpeed(double fallingSpeed) {
        this.fallingSpeed = fallingSpeed;
    }

    public String mode;

    public void setMode(String str) {
        this.mode = str;
        if (modeLabel != null) {
            modeLabel.setText("Mode: " + mode);
        }
    }

    private int score = 0; // Keep track of the score
    private Timeline createShapesTimeline;
    private Timeline moveTimeline;

    public void initialize() {
        startGame();
    }

    private void startGame() {
        // Reset the score
        score = 0;
        scoreLabel.setText("Score: 0");
        modeLabel.setText("Mode: " + mode);

        // Create shapes periodically
        createShapesTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> createRandomShape()));
        createShapesTimeline.setCycleCount(Timeline.INDEFINITE);
        createShapesTimeline.play();

        // Animate shapes falling
        animateShapes();
    }

    private void createRandomShape() {
        Random random = new Random();
        javafx.scene.shape.Shape shape;

        // Create either a circle or rectangle
        if (random.nextBoolean()) {
            shape = new Circle(random.nextInt(50) + 20);
            ((Circle) shape).setFill(Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble()));
        } else {
            shape = new Rectangle(random.nextInt(50) + 20, random.nextInt(50) + 20);
            ((Rectangle) shape).setFill(Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble()));
        }

        // Position the shape below the labels
        shape.setLayoutX(random.nextInt((int) gamePane.getWidth() - 50));
        shape.setLayoutY(60); // Start just below the labels
        shape.setOnMouseClicked(event -> {
            gamePane.getChildren().remove(shape);
            score+=5;
             scoreLabel.setText(String.valueOf(score));
        });

        gamePane.getChildren().add(shape);
    }

    private void animateShapes() {
        moveTimeline = new Timeline(new KeyFrame(Duration.seconds(0.016), e -> {
            for (javafx.scene.Node node : gamePane.getChildren()) {
                if (node instanceof javafx.scene.shape.Shape) {
                    node.setLayoutY(node.getLayoutY() + fallingSpeed+1);
                    if (node.getLayoutY() > gamePane.getHeight()) {
                        System.out.println("Shape reached bottom");
                        stopGame();
                        return; // Exit immediately
                    }
                }
            }

        }));
        moveTimeline.setCycleCount(Timeline.INDEFINITE); // Timeline will repeat indefinitely
        moveTimeline.play(); // Start the timeline
    }

    private void stopGame() {
        System.out.println("stopGame() called"); // Debug log
        if (createShapesTimeline != null) {
            createShapesTimeline.stop();
        }
        if (moveTimeline != null) {
            moveTimeline.stop();
        }

        gamePane.getChildren().clear(); // Optionally clear shapes
        // Restart or reload the game as needed
    }

}
