package sample;

import calculate.KochFractal;
import calculate.KochManager;
import calculate.kochObserver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        kochObserver ko = new kochObserver();
        KochFractal kf = new KochFractal();

        kf.addObserver(ko);

        kf.setLevel(2);
        kf.generateLeftEdge();
        kf.generateBottomEdge();
        kf.generateRightEdge();
    }


}
