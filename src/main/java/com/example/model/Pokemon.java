package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Model class representing a Pokemon entity.
 * Contains all Pokemon attributes and data.
 */
public class Pokemon {
    private String picture;
    private String name;
    private int evolutionStage;
    private String shortDescription;
    private int dexNumber;
    
    @JsonProperty("isLegendary")
    private boolean legendary;
    
    private List<String> types;

    // Getters and Setters
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEvolutionStage() {
        return evolutionStage;
    }

    public void setEvolutionStage(int evolutionStage) {
        this.evolutionStage = evolutionStage;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getDexNumber() {
        return dexNumber;
    }

    public void setDexNumber(int dexNumber) {
        this.dexNumber = dexNumber;
    }

    public boolean isLegendary() {
        return legendary;
    }

    public void setLegendary(boolean legendary) {
        this.legendary = legendary;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
