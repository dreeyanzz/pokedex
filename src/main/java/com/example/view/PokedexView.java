package com.example.view;

import com.example.model.Pokemon;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.List;

/**
 * View class for the Pokedex application.
 * Handles all UI components, layout, and rendering.
 */
public class PokedexView {
    // UI Constants
    private static final double WIN_W = 548;
    private static final double WIN_H = 400;
    private static final double SCREEN_X = 128, SCREEN_Y = 125;
    private static final double SCREEN_W = 104, SCREEN_H = 104;
    private static final double POKEMON_NUMBER_X = 79, POKEMON_NUMBER_Y = 140;
    private static final double DESC_X = 77, DESC_Y = 314, DESC_W = 111, DESC_H = 43;
    
    // Button hitboxes
    private static final double BTN_REVEAL_X = 30, BTN_REVEAL_Y = 272, BTN_REVEAL_SIZE = 37;
    private static final double BTN_LEFT_X = 205, BTN_LEFT_Y = 309, NAV_W = 17, NAV_H = 17;
    private static final double BTN_RIGHT_X = 244, BTN_RIGHT_Y = 309;
    private static final double BTN_ON_X = 127.72, BTN_ON_Y = 14.34, BTN_POWER_SIZE = 16;
    private static final double BTN_OFF_X = 84.65, BTN_OFF_Y = 14.34;

    // UI Components
    private final Pane root;
    private ImageView mainScreenImage;
    private Label descriptionLabel;
    private Label pokemonNumberLabel;
    private Label detailsLabel1;
    private Label detailsLabel2;
    private Label detailsLabel3;
    private ColorAdjust silhouetteEffect;
    
    // Buttons
    private Button btnReveal;
    private Button btnOn;
    private Button btnOff;
    private Button btnLeft;
    private Button btnRight;

    private final Class<?> resourceClass;

    public PokedexView(Class<?> resourceClass) {
        this.resourceClass = resourceClass;
        this.root = new Pane();
        this.silhouetteEffect = new ColorAdjust();
        this.silhouetteEffect.setBrightness(-1.0);
        
        initializeUI();
    }

    /**
     * Initialize all UI components
     */
    private void initializeUI() {
        setupBackground();
        setupMainScreen();
        setupLabels();
        setupButtons();
    }

    private void setupBackground() {
        try {
            ImageView background = new ImageView(
                new Image(resourceClass.getResourceAsStream("/pokedex_bg.png"))
            );
            background.setFitWidth(WIN_W);
            background.setFitHeight(WIN_H);
            root.getChildren().add(background);
        } catch (Exception e) {
            System.err.println("Error: Could not find /pokedex_bg.png");
        }
    }

    private void setupMainScreen() {
        mainScreenImage = new ImageView();
        mainScreenImage.setLayoutX(SCREEN_X);
        mainScreenImage.setLayoutY(SCREEN_Y);
        mainScreenImage.setFitWidth(SCREEN_W);
        mainScreenImage.setFitHeight(SCREEN_H);
        mainScreenImage.setPreserveRatio(true);
        mainScreenImage.setEffect(silhouetteEffect);
        
        root.getChildren().add(mainScreenImage);
    }

    private void setupLabels() {
        // Pokemon Number
        pokemonNumberLabel = createLabel(POKEMON_NUMBER_X, POKEMON_NUMBER_Y, 12, "#000000");

        // Description
        descriptionLabel = createLabel(DESC_X, DESC_Y, 6, "#084036");
        descriptionLabel.setPrefSize(DESC_W, DESC_H);
        descriptionLabel.setWrapText(true);

        // Details (Name, Type, Evolution)
        detailsLabel1 = createLabel(337, 128, 12, "#32EE25");
        detailsLabel2 = createLabel(349, 146, 10, "#32EE25");
        detailsLabel3 = createLabel(339, 191, 8, "#32EE25");
        detailsLabel3.setPrefWidth(172);

        root.getChildren().addAll(
            pokemonNumberLabel, 
            descriptionLabel, 
            detailsLabel1, 
            detailsLabel2, 
            detailsLabel3
        );
    }

    private void setupButtons() {
        btnReveal = createInvisibleButton(BTN_REVEAL_X, BTN_REVEAL_Y, BTN_REVEAL_SIZE, BTN_REVEAL_SIZE);
        btnOn = createInvisibleButton(BTN_ON_X, BTN_ON_Y, BTN_POWER_SIZE, BTN_POWER_SIZE);
        btnOff = createInvisibleButton(BTN_OFF_X, BTN_OFF_Y, BTN_POWER_SIZE, BTN_POWER_SIZE);
        btnLeft = createInvisibleButton(BTN_LEFT_X, BTN_LEFT_Y, NAV_W, NAV_H);
        btnRight = createInvisibleButton(BTN_RIGHT_X, BTN_RIGHT_Y, NAV_W, NAV_H);

        root.getChildren().addAll(btnReveal, btnOn, btnOff, btnLeft, btnRight);
    }

    private Label createLabel(double x, double y, double fontSize, String colorHex) {
        Label lbl = new Label();
        lbl.setLayoutX(x);
        lbl.setLayoutY(y);
        lbl.autosize();
        
        try {
            Font f = Font.loadFont(
                resourceClass.getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 
                fontSize
            );
            if (f != null) {
                lbl.setFont(f);
            }
        } catch (Exception e) {
            System.out.println("Font not found");
        }
        
        lbl.setStyle("-fx-text-fill: " + colorHex + ";");
        return lbl;
    }

    private Button createInvisibleButton(double x, double y, double w, double h) {
        Button btn = new Button();
        btn.setLayoutX(x);
        btn.setLayoutY(y);
        btn.setPrefSize(w, h);
        btn.setOpacity(0);
        return btn;
    }

    /**
     * Display a Pokemon in revealed state
     */
    public void displayPokemonRevealed(Pokemon pokemon) {
        if (pokemon == null) return;

        // Remove silhouette effect
        mainScreenImage.setEffect(null);

        // Update labels with Pokemon data
        descriptionLabel.setText(pokemon.getShortDescription());
        pokemonNumberLabel.setText(String.format("#%03d", pokemon.getDexNumber()));
        pokemonNumberLabel.setStyle("-fx-text-fill: #000000;");

        detailsLabel1.setText(pokemon.getName().toUpperCase());
        detailsLabel2.setText(formatTypes(pokemon.getTypes()));
        detailsLabel3.setText("Evolution Stage:" + pokemon.getEvolutionStage());

        // Load and display image
        loadPokemonImage(pokemon.getPicture());
    }

    /**
     * Display a Pokemon in hidden/silhouette state
     */
    public void displayPokemonHidden(Pokemon pokemon) {
        if (pokemon == null) return;

        // Apply silhouette effect
        mainScreenImage.setEffect(silhouetteEffect);

        // Show mystery text
        pokemonNumberLabel.setText("Who's that\nPokemon?");
        pokemonNumberLabel.setStyle("-fx-text-fill: #c42020ff;");
        descriptionLabel.setText("Identify the silhouette!");
        detailsLabel1.setText("?????");
        detailsLabel2.setText("Type???");
        detailsLabel3.setText("Evolution Stage: ??");

        // Load image (with silhouette effect)
        loadPokemonImage(pokemon.getPicture());
    }

    /**
     * Clear all display when powered off
     */
    public void clearDisplay() {
        mainScreenImage.setImage(null);
        mainScreenImage.setEffect(null);
        descriptionLabel.setText("");
        pokemonNumberLabel.setText("");
        detailsLabel1.setText("");
        detailsLabel2.setText("");
        detailsLabel3.setText("");
    }

    /**
     * Show boot message
     */
    public void showBootMessage() {
        descriptionLabel.setText("Booting...");
    }

    /**
     * Show error message
     */
    public void showError(String message) {
        descriptionLabel.setText(message);
    }

    private void loadPokemonImage(String pictureFilename) {
        String imgPath = "/pokedex/" + pictureFilename;
        try {
            InputStream imgStream = resourceClass.getResourceAsStream(imgPath);
            if (imgStream != null) {
                mainScreenImage.setImage(new Image(imgStream));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatTypes(List<String> types) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.size(); i++) {
            sb.append("Type").append(i + 1).append(": ").append(types.get(i));
            if (i < types.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    // Getters for components
    public Pane getRoot() {
        return root;
    }

    public Button getBtnReveal() {
        return btnReveal;
    }

    public Button getBtnOn() {
        return btnOn;
    }

    public Button getBtnOff() {
        return btnOff;
    }

    public Button getBtnLeft() {
        return btnLeft;
    }

    public Button getBtnRight() {
        return btnRight;
    }

    public static double getWindowWidth() {
        return WIN_W;
    }

    public static double getWindowHeight() {
        return WIN_H;
    }
}
