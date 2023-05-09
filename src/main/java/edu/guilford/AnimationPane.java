package edu.guilford;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class AnimationPane extends Pane {
    // We choose Pane so that we can place and move objects anywhere we want

    // attributes for the size of the Scene where this will be displayed
    private int width;
    private int height;
    private Circle circEvent;
    private int dx = 2;
    private int dy = 3;

    // constructor
    public AnimationPane(int width, int height) {
        this.width = width;
        this.height = height;

        // Let's add a Circle to the Pane at a random location
        Circle circle = new Circle(50, Color.TOMATO);
        circle.setCenterX(Math.random() * width);
        circle.setCenterY(Math.random() * height);
        this.getChildren().add(circle);

        // JavaFX animations are based on transitions: a starting point, an ending
        // point, and a duration
        // Example: fade the circle from 1.0 (opaque) to 0.0 (transparent) over 2
        // seconds
        // Build a FadeTransition object (from the animation package) with the circle as
        // the target
        FadeTransition ft = new FadeTransition(Duration.seconds(2), circle);
        // Set the properties of the transition
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        // we'll do this multiple times
        ft.setCycleCount(4);
        // we'll reverse at the end of each cycle
        ft.setAutoReverse(true);
        // start the animation
        // ft.play();

        // Let's move the Circle around on the screen using a specified Path
        // Now it's a PathTransition object
        // We'll use a Path object to specify the path
        Path path = new Path();
        // We add specific directives (elements) to the Path
        path.getElements().add(new MoveTo(200, 300));
        path.getElements().add(new LineTo(300, 200));
        path.getElements().add(new CubicCurveTo(100, 100, 200, 200, 50, 50));
        // Now we can build a PathTransition object with this path and the circle as the
        // target
        PathTransition pt = new PathTransition(Duration.seconds(3), path, circle);
        pt.setCycleCount(2);
        pt.setAutoReverse(true);
        // pt.play();

        // KeyFrame animation: Set particular goals to be attained at particular times
        // That's done with a KeyFrame and one or more KeyValues
        // And the KeyFrame objects are put in a Timeline object
        // Here are KeyValue objects to put the circle at a particular location
        KeyValue kvx = new KeyValue(circle.centerXProperty(), 600);
        KeyValue kvy = new KeyValue(circle.centerYProperty(), 100);
        // take 2 seconds to get there
        KeyFrame kf = new KeyFrame(Duration.seconds(2), kvx, kvy);
        // Add another KeyFrame to change the radius of the circle
        KeyValue kvr = new KeyValue(circle.radiusProperty(), 200);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(2), kvr);
        // Now we can build a Timeline object with these KeyFrames
        Timeline tl = new Timeline(kf, kf2);

        // By default, all transitions are done in parallel, but we can do them
        // sequentially instead
        // We'll use a SequentialTransition object
        SequentialTransition seqT = new SequentialTransition();
        // We add the transitions to the sequence
        seqT.getChildren().addAll(ft, pt, tl);
        seqT.play();

        // Let's add a Circle to the Pane at a random location
        circEvent = new Circle(100, Color.THISTLE);
        getChildren().add(circEvent);

        circEvent.setCenterX(Math.random() * width);
        circEvent.setCenterY(Math.random() * height);
        Timeline tlCirc = new Timeline();
        KeyFrame kfCirc = new KeyFrame(Duration.millis(10), new CircAnimate());
        tlCirc.getKeyFrames().add(kfCirc);
        tlCirc.setCycleCount(Timeline.INDEFINITE);
        tlCirc.play();
    }

    // inner class to animate the circle: it's an EventHandler interface
    // specifically for ActionEvents
    private class CircAnimate implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent arg0) {
            double cx = circEvent.getCenterX();
            double cy = circEvent.getCenterY();
            double r = circEvent.getRadius();

            // check if the circle is the near the edge of the pane, and if it is, reverse
            // the velocity
            if (cx + r > width || cx - r < 0) {
                dx = -dx;
            }
            if (cy + r > height || cy - r < 0) {
                dy = -dy;
            }
            // move the circle
            circEvent.setCenterX(cx + dx);
            circEvent.setCenterY(cy + dy);

        }

    }

}
