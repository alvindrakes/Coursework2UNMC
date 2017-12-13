package com.neet.DiamondHunter.MapViewer;

import com.neet.DiamondHunter.Main.Game;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.ResourceBundle;

//JavaFX libraries
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;

public class MapController implements Initializable {

    private TileInformation[][] tileInfo;

    //The application pane and map pane
    @FXML
    private AnchorPane mapPane;
    @FXML
    private Canvas mapCanvas;
    @FXML
    private StackPane mapStack;
    @FXML
    private TextArea infoText;
    @FXML
    private GridPane tileMapping;

    //For map loading
    private MapPane mp;
    private GraphicsContext gc;

    //Boolean counter of game launched
    boolean isGameLaunched;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isGameLaunched = false;
        // MapPane contains all map loading commands
        mp = new MapPane();

        infoText.setEditable(false);
        infoText.setText("Welcome to Map Viewer!\nJust drag and drop the axe or boat to any legal location you wish!\nThe location is saved automatically!");
        initMapCanvas();
        initTileMapping();

        // Initialises the size of the StackPane that contains the map in canvas
        mapStack.relocate(20, 20);
        mapStack.setPrefSize((double) (mp.getNumRows() * mp.getTileSize()),
                (double) (mp.getNumCols() * mp.getTileSize()));

        /*
         * All MapViewer classes are linked to MapController by the AnchorPane of MapViewer SceneBuilder
         * To set the minimum size (fitting all elements in the window), getPref has minimum width and height.
         */
        mapPane.setMinSize(mapStack.getPrefWidth(), mapStack.getPrefHeight());

    }


    /**
     * This class loads the map tiles with associated tile image, using snapshot commands, and linked to the mapCanvas
     * set in MapViewer SceneBuilder.
     */
    private void initMapCanvas() {
        mapCanvas.setWidth((double) MapPane.WIDTH);
        mapCanvas.setHeight((double) MapPane.HEIGHT);

        mp.loadTiles("/Tilesets/testtileset.gif");
        mp.loadMap("/Maps/testmap.map");
        gc = mapCanvas.getGraphicsContext2D();
        mp.drawImage(gc);

        mapCanvas.getGraphicsContext2D().drawImage(gc.getCanvas().snapshot(new SnapshotParameters(), null), 0, 0);
    }

    private void initTileMapping() {
        tileInfo = new TileInformation[mp.getNumRows()][mp.getNumCols()];
        for (int i = 0; i < tileInfo.length; i++) {
            tileMapping.getColumnConstraints().add(new ColumnConstraints((double) (mp.getTileSize())));
            tileMapping.getRowConstraints().add(new RowConstraints((double) (mp.getTileSize())));
        }

        for (int row = 0; row < mp.getNumRows(); row++) {
            for (int col = 0; col < mp.getNumCols(); col++) {
                tileInfo[row][col] = new TileInformation(mp.getTileImageFromMap(row, col), row, col);
                addTile(col, row);
            }
        }
    }

    private void addTile(int colIndex, int rowIndex) {

        Label label = new Label();
        label.setMinSize(mp.getTileSize(), mp.getTileSize());
        String tileText = "Coordinate: " + Integer.toString(rowIndex) + " x " + Integer.toString(colIndex);
        final String tt = tileText;

        label.setOnMouseEntered(e -> {
            infoText.setText(tt);
        });


        tileMapping.add(label, colIndex, rowIndex);

    }

    /**
     * Play button: Check if game is launched before
     */
    @FXML
    private void playGame() {
        if(isGameLaunched == false) {
            Game.runGame();
            isGameLaunched = true;
        }
    }

    /**
     * Exit button
     */
    @FXML
    private void exitGame() {
        System.exit(0);
    }




}