package Application.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane; // Import AnchorPane
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;

public class gameController {
    public double fallingSpeed;

    public void setFallingSpeed(double fallingSpeed) {
        this.fallingSpeed = fallingSpeed;
    }

    @FXML
    private AnchorPane anchorPane;  // Link to the AnchorPane in your FXML file

    public void initialize() {
        // Generate random shapes every 1 second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> createRandomShape()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        animateShapes();
    }

    // Create random shapes (Circles or Rectangles)
    private void createRandomShape() {
        Random random = new Random();

        // Randomly choose Circle or Rectangle
        boolean isCircle = random.nextBoolean();
        javafx.scene.shape.Shape shape;
        if (isCircle) {
            shape = new Circle(random.nextInt(50) + 20);  // Random circle size
            ((Circle) shape).setFill(Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble()));
        } else {
            shape = new Rectangle(random.nextInt(50) + 20, random.nextInt(50) + 20);  // Random rectangle size
            ((Rectangle) shape).setFill(Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble()));
        }

        // Position the shape at the top (random X position)
        shape.setLayoutX(random.nextInt((int) anchorPane.getWidth() - 50));
        shape.setLayoutY(-shape.getBoundsInLocal().getHeight());  // Position off-screen at the top

        // Add shape to the AnchorPane
        anchorPane.getChildren().add(shape);
    }

    // Animate shapes from top to bottom
// Animate shapes from top to bottom
    private void animateShapes() {
        Timeline moveTimeline = new Timeline();
        moveTimeline.setCycleCount(Timeline.INDEFINITE);
        moveTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.016), e -> {
            for (javafx.scene.Node node : anchorPane.getChildren()) {
                if (node instanceof javafx.scene.shape.Shape) {
                    // Use fallingSpeed to control how much the shapes move
                    node.setLayoutY(node.getLayoutY() + fallingSpeed);
                    if (node.getLayoutY() > anchorPane.getHeight()) {
                        anchorPane.getChildren().remove(node);  // Remove shape if it goes off-screen
                    }
                }
            }
        }));
        moveTimeline.play();
    }

}
