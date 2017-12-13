package com.neet.DiamondHunter.MapViewer;

import java.net.URL;
import java.util.ResourceBundle;

import com.neet.DiamondHunter.EntityViewer.AxeShip;
import com.neet.DiamondHunter.EntityViewer.ShowAxeShip;
import com.neet.DiamondHunter.EntityViewer.EntityDisplay;
import com.neet.DiamondHunter.EntityViewer.ShowDiamonds;
import com.neet.DiamondHunter.EntityViewer.ShowPlayer;
import com.neet.DiamondHunter.Main.Game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;


public class MapController implements Initializable {

    //All entity instantiation
    private AxeShip as;
    private EntityDisplay sp;
    private EntityDisplay sd;

    //The map
    private MapPane mp;
    private GraphicsContext gc;

    //The information of each tile loaded
    private TileInformation[][] tileInfo;

    //Check if the Diamond  Hunter game has already been launched
    boolean isLaunchedMainGame;

    //Temporary store for the updated coordinates
    private int[] tmpCoords = new int[4];
    private String itemType = "";

    //The application pane and map pane
    @FXML
    private AnchorPane mapPane;
    @FXML
    private Canvas mapCanvas;
    @FXML
    private GridPane tileMapping;
    @FXML
    private StackPane mapStack;

    @FXML
    private TextArea infoText;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WriteCoord.checkExist();
        // At launch of Map Viewer, the game itself is never launched
        isLaunchedMainGame = false;
        // MapPane has all the loaders for the map
        mp = new MapPane();

        //Begin all entity coordinate configuration
        as = new ShowAxeShip();
        sp = new ShowPlayer();
        sd = new ShowDiamonds();

        // The tile information display box is never editable
        infoText.setEditable(false);
        infoText.setText("Welcome to Map Viewer!\nJust drag and drop the axe or boat to any legal location you wish!\nThe location is saved automatically!");
        initMapCanvas();

        // Initialises the size of the StackPane that contains the map in canvas
        // and GridPane
        mapStack.relocate(20, 20);
        mapStack.setPrefSize((double) (mp.getNumRows() * mp.getTileSize()),
                (double) (mp.getNumCols() * mp.getTileSize()));

        mapPane.setOnMouseEntered(e -> {
            infoText.setText("Welcome to Map Viewer!\nJust drag and drop the axe or boat to any legal location you wish!\nThe location is saved automatically!");
        });;
        /*
         * This is the base size of the entire application. The AnchorPain (main
         * window) is adjusted based on the size of the map. However, this is
         * FXML overridden if the application grows.
         */
        mapPane.setMinSize(mapStack.getPrefWidth(), mapStack.getPrefHeight());

        initTileMapping();
    }

    /**
     * Initialises the map in a non-FXML canvas and draws onto FXML canvas as a
     * whole.
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
     * Initialises the grid on top of the map that handles input validation and
     * movement of boat and axe.
     */
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

    /**
     * Adds a label to each tile that contains information of the current tile.
     *
     * @param colIndex The column index of the GridPane.
     * @param rowIndex The row index of the GridPane.
     */
    private void addTile(int colIndex, int rowIndex) {

        Label label = new Label();
        label.setMinSize(mp.getTileSize(), mp.getTileSize());
        String tileText = "Coordinate: " + Integer.toString(rowIndex) + " x " + Integer.toString(colIndex);

        //display boat on top of tile
        if(as.compareCoordinates(rowIndex, colIndex, ShowAxeShip.BOAT)){
            label.setGraphic(new ImageView(as.getEntity(ShowAxeShip.BOAT)));
            tileInfo[rowIndex][colIndex].setEntityType(TileInformation.BOAT);
            tileText += "\nA boat!";
            itemType = "Boat";
            tmpCoords[2] = rowIndex;
            tmpCoords[3] = colIndex;
            //dragSource(label, itemType);
        }
        //display axe on top of tile
        else if(as.compareCoordinates(rowIndex, colIndex, ShowAxeShip.AXE)){
            label.setGraphic(new ImageView(as.getEntity(ShowAxeShip.AXE)));
            tileInfo[rowIndex][colIndex].setEntityType(TileInformation.AXE);
            tileText += "\nAn axe!";
            itemType = "Axe";
            tmpCoords[0] = rowIndex;
            tmpCoords[1] = colIndex;
            //dragSource(label, itemType);
        }
        //display player initial position on map
        else if(sp.compareCoordinates(rowIndex, colIndex, EntityDisplay.UNIQUE)){
            label.setGraphic(new ImageView(sp.getEntity(EntityDisplay.UNIQUE)));
            tileInfo[rowIndex][colIndex].setEntityType(TileInformation.PLAYER);
            tileText += "\nYou are here!";
        }
        // display diamonds initial position on map
        else if(sd.compareCoordinates(rowIndex, colIndex, EntityDisplay.UNIQUE)) {
            label.setGraphic(new ImageView(sd.getEntity(EntityDisplay.UNIQUE)));
            tileInfo[rowIndex][colIndex].setEntityType(TileInformation.DIAMOND);
            tileText += "\nA diamond!";
        }


        label.setUserData(tileInfo[rowIndex][colIndex]);
        //dropTarget(label, tileInfo[rowIndex][colIndex]);

        final String tt = tileText;

        label.setOnMouseEntered(e -> {
            infoText.setText(tt);
        });


        tileMapping.add(label, colIndex, rowIndex);

    }

    @FXML
    private void playGame() {
        if(isLaunchedMainGame == false) {
            Game.runGame();
            isLaunchedMainGame = true;
        }
    }

    /**
     * Exits the game and let's the garbage collector release the resources held
     * by the application.
     */
    @FXML
    private void exitGame() {
        System.exit(0);
    }
}