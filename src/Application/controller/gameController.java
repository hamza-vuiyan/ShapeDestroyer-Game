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
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
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

        animateShapes();
    }
    private Color getRandomColor() {
        Random random = new Random();
        double r, g, b;

        do {
            r = random.nextDouble();
            g = random.nextDouble();
            b = random.nextDouble();
        } while (r > 1 && g > 1 && b > 1); // Avoid near-white colors

        return Color.color(r, g, b);
    }


    private void createRandomShape() {

        Random random = new Random();
        javafx.scene.shape.Shape shape;

        double minShapeSize = 30;
        double maxShapeSize = 70;

        if (random.nextBoolean()) {
            double radius = minShapeSize + random.nextDouble() * (maxShapeSize- minShapeSize);

            shape = new Circle(radius);
            ((Circle) shape).setFill(getRandomColor());
        } else {
            double width = minShapeSize + random.nextDouble() * (maxShapeSize-minShapeSize);
            double height = minShapeSize + random.nextDouble() * (maxShapeSize-minShapeSize);

            shape = new Rectangle(width, height);
            ((Rectangle) shape).setFill(getRandomColor());
        }

        double minX = 10; // Small gap from the left
        double maxX = gamePane.getWidth() - shape.getBoundsInLocal().getWidth() - minX;

        shape.setLayoutX(minX + random.nextDouble()*maxX);
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
                        return; 
                    }
                }
            }

        }));
        moveTimeline.setCycleCount(Timeline.INDEFINITE); // Timeline will repeat indefinitely
        moveTimeline.play(); // Start the timeline
    }

    private void stopGame() {
        System.out.println("stopGame() called");
        if (createShapesTimeline != null) {
            createShapesTimeline.stop();
        }
        if (moveTimeline != null) {
            moveTimeline.stop();
        }
        gamePane.getChildren().clear();
        // Restart or reload the game as needed
    }

}
