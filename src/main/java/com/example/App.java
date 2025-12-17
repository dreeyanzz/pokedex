package com.example;

import com.example.controller.PokedexController;
import com.example.model.PokedexModel;
import com.example.view.PokedexView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the JavaFX Pokedex.
 * Follows the MVC (Model-View-Controller) architectural pattern.
 * 
 * This class is responsible for:
 * - Initializing the MVC components
 * - Creating the JavaFX Stage and Scene
 * - Starting the application
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Initialize MVC components
        PokedexModel model = new PokedexModel();
        PokedexView view = new PokedexView(getClass());
        PokedexController controller = new PokedexController(model, view, getClass());

        // Load data
        controller.loadData();

        // Create and configure the scene
        Scene scene = new Scene(
                view.getRoot(),
                PokedexView.getWindowWidth(),
                PokedexView.getWindowHeight());

        stage.setTitle("Pokedex");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // Initialize to powered off state
        controller.initializePoweredOff();
    }

    public static void main(String[] args) {
        launch(args);
    }
}