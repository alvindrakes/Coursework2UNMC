package com.neet.DiamondHunter.MapViewer;

import com.neet.DiamondHunter.Main.Game;

import java.net.URL;
import java.util.ResourceBundle;

//JavaFX libraries
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class MapController implements Initializable {

    //The application pane and map pane
    @FXML
    private AnchorPane mapPane;
    @FXML
    private Canvas mapCanvas;
    @FXML
    private StackPane mapStack;

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

        initMapCanvas();

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
