package com.example.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for the Pokedex application.
 * Manages the Pokemon data, current state, and business logic for data operations.
 */
public class PokedexModel {
    private List<Pokemon> pokedexList;
    private int currentIndex;
    private boolean isRevealed;
    private boolean isPoweredOn;

    public PokedexModel() {
        this.pokedexList = new ArrayList<>();
        this.currentIndex = 0;
        this.isRevealed = false;
        this.isPoweredOn = false;
    }

    /**
     * Loads Pokemon data from JSON file
     * @param inputStream The input stream to the JSON file
     * @return true if loading was successful, false otherwise
     */
    public boolean loadData(InputStream inputStream) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (inputStream == null) {
                return false;
            }
            pokedexList = mapper.readValue(inputStream, new TypeReference<List<Pokemon>>() {});
            return !pokedexList.isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Navigate to the next Pokemon in the list (wraps around)
     */
    public void nextPokemon() {
        if (pokedexList.isEmpty()) return;
        currentIndex = (currentIndex < pokedexList.size() - 1) ? currentIndex + 1 : 0;
    }

    /**
     * Navigate to the previous Pokemon in the list (wraps around)
     */
    public void previousPokemon() {
        if (pokedexList.isEmpty()) return;
        currentIndex = (currentIndex > 0) ? currentIndex - 1 : pokedexList.size() - 1;
    }

    /**
     * Toggle the reveal state
     */
    public void toggleReveal() {
        isRevealed = !isRevealed;
    }

    /**
     * Power on the Pokedex
     */
    public void powerOn() {
        isPoweredOn = true;
        currentIndex = 0;
        isRevealed = false;
    }

    /**
     * Power off the Pokedex
     */
    public void powerOff() {
        isPoweredOn = false;
        isRevealed = false;
    }

    // Getters
    public Pokemon getCurrentPokemon() {
        if (pokedexList.isEmpty() || currentIndex >= pokedexList.size()) {
            return null;
        }
        return pokedexList.get(currentIndex);
    }

    public List<Pokemon> getPokedexList() {
        return pokedexList;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public boolean isPoweredOn() {
        return isPoweredOn;
    }

    public boolean hasData() {
        return !pokedexList.isEmpty();
    }
}
