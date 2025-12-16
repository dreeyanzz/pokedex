package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    // --- UI COORDINATES ---
    private static final double WIN_W = 548;
    private static final double WIN_H = 400;

    private static final double SCREEN_X = 128, SCREEN_Y = 125;
    private static final double SCREEN_W = 104, SCREEN_H = 104;

    private static final double POKEMON_NUMBER_X = 79, POKEMON_NUMBER_Y = 140;

    private static final double DESC_X = 77, DESC_Y = 314, DESC_W = 111, DESC_H = 43;

    // Hitboxes
    private static final double BTN_REVEAL_X = 30, BTN_REVEAL_Y = 272, BTN_REVEAL_SIZE = 37;
    private static final double BTN_LEFT_X = 205, BTN_LEFT_Y = 309, NAV_W = 17, NAV_H = 17;
    private static final double BTN_RIGHT_X = 244, BTN_RIGHT_Y = 309;

    // --- STATE ---
    private List<Pokemon> pokedexList = new ArrayList<>();
    private int currentIndex = 0;
    private boolean isRevealed = false;
    private boolean isPoweredOn = true;

    // --- UI NODES ---
    private ImageView mainScreenImage;
    private Label descriptionLabel;
    private Label pokemonNumberLabel;
    private ColorAdjust silhouetteEffect;
    private Label detailsLabel1;
    private Label detailsLabel2;
    private Label detailsLabel3;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();

        // 1. Setup Background
        setupBackground(root);

        // 2. Setup Main Screen
        setupMainScreen(root);

        // 3. Setup All Labels (Using the new helper function)
        setupLabels(root);

        // 4. Setup Buttons
        setupButtons(root);

        // 5. Load Data & Launch
        loadData();

        Scene scene = new Scene(root, WIN_W, WIN_H);
        stage.setTitle("JavaFX Pokedex");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // Turn off initially
        turnOff();
    }

    // --- NEW SETUP METHODS (Cleaning up start) ---

    private void setupBackground(Pane root) {
        try {
            ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/pokedex_bg.png")));
            background.setFitWidth(WIN_W);
            background.setFitHeight(WIN_H);
            root.getChildren().add(background);
        } catch (Exception e) {
            System.err.println("Error: Could not find /pokedex_bg.png");
        }
    }

    private void setupMainScreen(Pane root) {
        mainScreenImage = new ImageView();
        mainScreenImage.setLayoutX(SCREEN_X);
        mainScreenImage.setLayoutY(SCREEN_Y);
        mainScreenImage.setFitWidth(SCREEN_W);
        mainScreenImage.setFitHeight(SCREEN_H);
        mainScreenImage.setPreserveRatio(true);

        silhouetteEffect = new ColorAdjust();
        silhouetteEffect.setBrightness(-1.0);
        mainScreenImage.setEffect(silhouetteEffect);

        root.getChildren().add(mainScreenImage);
    }

    private void setupLabels(Pane root) {
        // Pokemon Number
        pokemonNumberLabel = createLabel(POKEMON_NUMBER_X, POKEMON_NUMBER_Y, 12, "#000000");

        // Description
        descriptionLabel = createLabel(DESC_X, DESC_Y, 6, "#084036");
        descriptionLabel.setPrefSize(DESC_W, DESC_H);
        descriptionLabel.setWrapText(true);

        // Details (Name, Type, Evo)
        int fix = 0;
        detailsLabel1 = createLabel(337, 128 - fix, 12, "#32EE25");
        detailsLabel2 = createLabel(349, 146 - fix, 10, "#32EE25");
        detailsLabel3 = createLabel(339, 191 - fix, 8, "#32EE25");
        detailsLabel3.setPrefWidth(172); // 162 + widthFix

        root.getChildren().addAll(pokemonNumberLabel, descriptionLabel, detailsLabel1, detailsLabel2, detailsLabel3);
    }

    private void setupButtons(Pane root) {
        Button btnReveal = createInvisibleButton(BTN_REVEAL_X, BTN_REVEAL_Y, BTN_REVEAL_SIZE, BTN_REVEAL_SIZE);
        btnReveal.setOnAction(e -> toggleReveal());

        Button btnOn = createInvisibleButton(127.72, 14.34, 16, 16);
        btnOn.setOnAction(e -> turnOn());

        Button btnOff = createInvisibleButton(84.65, 14.34, 16, 16);
        btnOff.setOnAction(e -> turnOff());

        Button btnLeft = createInvisibleButton(BTN_LEFT_X, BTN_LEFT_Y, NAV_W, NAV_H);
        btnLeft.setOnAction(e -> prevPokemon());

        Button btnRight = createInvisibleButton(BTN_RIGHT_X, BTN_RIGHT_Y, NAV_W, NAV_H);
        btnRight.setOnAction(e -> nextPokemon());

        root.getChildren().addAll(btnReveal, btnOn, btnOff, btnLeft, btnRight);
    }

    // --- HELPER FUNCTIONS ---

    private Label createLabel(double x, double y, double fontSize, String colorHex) {
        Label lbl = new Label();
        lbl.setLayoutX(x);
        lbl.setLayoutY(y);
        lbl.autosize();
        try {
            Font f = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), fontSize);
            if (f != null)
                lbl.setFont(f);
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

    // --- LOGIC ---

    private void turnOff() {
        isPoweredOn = false;
        mainScreenImage.setImage(null);
        mainScreenImage.setEffect(null);

        descriptionLabel.setText("");
        pokemonNumberLabel.setText("");
        detailsLabel1.setText("");
        detailsLabel2.setText("");
        detailsLabel3.setText("");
    }

    private void turnOn() {
        if (!isPoweredOn) {
            isPoweredOn = true;
            descriptionLabel.setText("Booting...");
            currentIndex = 0;
            // Load the pokemon, but ensure we start revealed
            updateUI();
            isRevealed = true;
            toggleReveal(); // This will trigger the display
        }
    }

    private void loadData() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream("/pokedex/kanto_pokemon.json")) {
            if (is == null) {
                descriptionLabel.setText("Error: JSON missing");
                return;
            }
            pokedexList = mapper.readValue(is, new TypeReference<List<Pokemon>>() {
            });
            if (!pokedexList.isEmpty())
                updateUI();
        } catch (IOException e) {
            e.printStackTrace();
            descriptionLabel.setText("Error loading data");
        }
    }

    // New helper to fill text labels (called by updateUI and toggleReveal)
    private void updateLabels(Pokemon p) {
        // 1. Strings
        String typeStr = "";
        StringBuilder sb = new StringBuilder();
        List<String> types = p.getTypes();
        for (int i = 0; i < types.size(); i++) {
            sb.append("Type").append(i + 1).append(": ").append(types.get(i));
            if (i < types.size() - 1)
                sb.append("\n");
        }
        typeStr = sb.toString();

        // 2. Set Text
        descriptionLabel.setText(p.getShortDescription());
        pokemonNumberLabel.setText(String.format("#%03d", p.getDexNumber()));
        pokemonNumberLabel.setStyle("-fx-text-fill: #000000;");

        detailsLabel1.setText(p.getName().toUpperCase());
        detailsLabel2.setText(typeStr);
        detailsLabel3.setText("Evolution Stage:" + p.getEvolutionStage());
    }

    private void updateUI() {
        Pokemon p = pokedexList.get(currentIndex);

        // Update Text
        if (isRevealed)
            updateLabels(p);

        // Update Image
        String imgPath = "/pokedex/" + p.getPicture();
        try {
            InputStream imgStream = getClass().getResourceAsStream(imgPath);
            if (imgStream != null) {
                mainScreenImage.setImage(new Image(imgStream));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toggleReveal() {
        if (!isPoweredOn)
            return;

        isRevealed = !isRevealed;

        if (isRevealed) {
            // REVEALED: Remove effect, show real data
            mainScreenImage.setEffect(null);
            // Reuse the helper method! No more duplicate code.
            updateLabels(pokedexList.get(currentIndex));
        } else {
            // HIDDEN: Add effect, show "???" data
            mainScreenImage.setEffect(silhouetteEffect);

            pokemonNumberLabel.setText("Who's that\nPokemon?");
            pokemonNumberLabel.setStyle("-fx-text-fill: #c42020ff;");

            descriptionLabel.setText("Identify the silhouette!");
            detailsLabel1.setText("?????");
            detailsLabel2.setText("Type???");
            detailsLabel3.setText("Evolution Stage: ??");
        }
    }

    private void nextPokemon() {
        if (!isPoweredOn)
            return;
        currentIndex = (currentIndex < pokedexList.size() - 1) ? currentIndex + 1 : 0;
        updateUI();
    }

    private void prevPokemon() {
        if (!isPoweredOn)
            return;
        currentIndex = (currentIndex > 0) ? currentIndex - 1 : pokedexList.size() - 1;
        updateUI();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // --- DATA MODEL ---
    public static class Pokemon {
        private String picture;
        private String name;
        private int evolutionStage;
        private String shortDescription;
        private int dexNumber;
        private boolean isLegendary;
        private List<String> types;

        @JsonProperty("isLegendary")
        private boolean legendary;

        // Getters/Setters
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
}