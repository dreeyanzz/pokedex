package com.example.controller;

import java.io.InputStream;

import com.example.model.PokedexModel;
import com.example.model.Pokemon;
import com.example.view.PokedexView;

/**
 * Controller class for the Pokedex application.
 * Handles user interactions and coordinates between Model and View.
 */
public class PokedexController {
    private final PokedexModel model;
    private final PokedexView view;
    private final Class<?> resourceClass;

    public PokedexController(PokedexModel model, PokedexView view, Class<?> resourceClass) {
        this.model = model;
        this.view = view;
        this.resourceClass = resourceClass;

        initializeEventHandlers();
    }

    /**
     * Set up event handlers for all buttons
     */
    private void initializeEventHandlers() {
        view.getBtnReveal().setOnAction(e -> handleToggleReveal());
        view.getBtnOn().setOnAction(e -> handlePowerOn());
        view.getBtnOff().setOnAction(e -> handlePowerOff());
        view.getBtnLeft().setOnAction(e -> handlePreviousPokemon());
        view.getBtnRight().setOnAction(e -> handleNextPokemon());
    }

    /**
     * Load Pokemon data from JSON file
     */
    public void loadData() {
        InputStream is = resourceClass.getResourceAsStream("/pokedex/kanto_pokemon.json");
        if (is == null) {
            view.showError("Error: JSON missing");
            return;
        }

        boolean success = model.loadData(is);
        if (!success) {
            view.showError("Error loading data");
            return;
        }

        if (model.hasData()) {
            updateDisplay();
        }
    }

    /**
     * Handle power on button
     */
    private void handlePowerOn() {
        if (!model.isPoweredOn()) {
            model.powerOn();
            view.showBootMessage();
            updateDisplay();
        }
    }

    /**
     * Handle power off button
     */
    private void handlePowerOff() {
        model.powerOff();
        view.clearDisplay();
    }

    /**
     * Handle reveal/hide toggle button
     */
    private void handleToggleReveal() {
        if (!model.isPoweredOn()) {
            return;
        }

        model.toggleReveal();
        updateDisplay();
    }

    /**
     * Handle next Pokemon navigation
     */
    private void handleNextPokemon() {
        if (!model.isPoweredOn()) {
            return;
        }

        model.nextPokemon();
        updateDisplay();
    }

    /**
     * Handle previous Pokemon navigation
     */
    private void handlePreviousPokemon() {
        if (!model.isPoweredOn()) {
            return;
        }

        model.previousPokemon();
        updateDisplay();
    }

    /**
     * Update the view based on current model state
     */
    private void updateDisplay() {
        Pokemon currentPokemon = model.getCurrentPokemon();

        if (currentPokemon == null) {
            view.showError("No Pokemon data");
            return;
        }

        if (model.isRevealed()) {
            view.displayPokemonRevealed(currentPokemon);
        } else {
            view.displayPokemonHidden(currentPokemon);
        }
    }

    /**
     * Initialize the application to powered off state
     */
    public void initializePoweredOff() {
        handlePowerOff();
    }
}
