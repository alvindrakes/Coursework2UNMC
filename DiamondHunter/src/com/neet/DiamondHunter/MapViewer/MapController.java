package com.neet.DiamondHunter.MapViewer;

import java.net.URL;
import java.util.ResourceBundle;

import com.neet.DiamondHunter.EntityViewer.AxeShip;
import com.neet.DiamondHunter.EntityViewer.ShowAxeShip;
import com.neet.DiamondHunter.EntityViewer.EntityDisplay;
import com.neet.DiamondHunter.EntityViewer.ShowDiamonds;
import com.neet.DiamondHunter.EntityViewer.ShowPlayer;
import com.neet.DiamondHunter.Main.Game;

import com.neet.DiamondHunter.TileMap.Tile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 * Main controller for the interface. Any interaction in the
 * MapViewInterface.fxml is computed here.
 *
 */
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
    @FXML    private AnchorPane mapPane;
    @FXML    private Canvas mapCanvas;
    @FXML    private GridPane tileMapping;
    @FXML    private StackPane mapStack;
    @FXML    private Label currentCoord;
    @FXML    private Label msgBoxChanged;

    //Buttons
    @FXML    private Button playButton;
    @FXML    private Button exitButton;
    @FXML    private Button resetButton;

    @FXML    public Label axeCoord, boatCoord;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WriteCoord.checkExist();
        // At launch of Map Viewer, the game itself is never launched
        isLaunchedMainGame = false;
        // MapPane has all the loaders for the map
        mp = new MapPane();


        //Button load images
        Image playImage = new Image(getClass().getResourceAsStream("/Sprites/playbutton.gif"));
        playButton.setGraphic(new ImageView(playImage));
        Image exitImage = new Image(getClass().getResourceAsStream("/Sprites/exitbutton.gif"));
        exitButton.setGraphic(new ImageView(exitImage));
        Image resetImage = new Image(getClass().getResourceAsStream("/Sprites/resetbutton.gif"));
            resetButton.setGraphic(new ImageView(resetImage));

        //Begin all entity coordinate configuration
        as = new ShowAxeShip();
        sp = new ShowPlayer();
        sd = new ShowDiamonds();

        initMapCanvas();

        // Initialises the size of the StackPane that contains the map in canvas
        // and GridPane
        mapStack.relocate(20, 20);
        mapStack.setPrefSize((double) (mp.getNumRows() * mp.getTileSize()),
                (double) (mp.getNumCols() * mp.getTileSize()));

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
    private void initTileMapping(){
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
    private void addTile(int colIndex, int rowIndex){

        String StoreCoord;
        Label label = new Label();
        label.setMinSize(mp.getTileSize(), mp.getTileSize());
        StoreCoord = Integer.toString(rowIndex)+ "," + Integer.toString(colIndex);

        displayDefaultCoord();

		itemsDisplayOnMap(label, colIndex, rowIndex);

        label.setUserData(tileInfo[rowIndex][colIndex]);
        dropTarget(label, tileInfo[rowIndex][colIndex]);

        label.setOnMouseEntered(e -> {
            currentCoord.setText(StoreCoord);
            axeCoord.setText(ShowAxeShip.storeAxeCoord);
            boatCoord.setText(ShowAxeShip.storeBoatCoord);
        });

        tileMapping.add(label, colIndex, rowIndex);
    }

    private void dragSource(Label source, String item) {
        source.setOnDragDetected((MouseEvent e) -> {
            Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(((ImageView) (source.getGraphic())).getImage());
            db.setContent(content);

            itemType = item;
            e.consume();
        });

        source.setOnDragDone(e -> {
            if (e.getTransferMode() == TransferMode.MOVE)
                source.setGraphic(null);
            e.consume();
        });
    }

    /**
     * Method to drop axe/boat on any good tile
     * @param target The label where the dragging object is currently on
     * @param ti The tile information of every tile in the map
     */
    private void dropTarget(Label target, TileInformation ti) {

        target.setOnDragOver(e -> {
            if (e.getGestureSource() != target) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        target.setOnDragEntered(e -> {
            if (e.getGestureSource() != target && e.getDragboard().hasContent(DataFormat.IMAGE)) {
                itemsDropTileCheck(target, ti);
            }
            e.consume();
            printMouseCoordDrag(target);
        });

        target.setOnDragExited(e -> {
            target.setStyle(null);
        });

        itemsSuccessDrop(target, ti);
    }

    /** display the mouse coordination while dragging
     *
     * @param target
     */
    private void printMouseCoordDrag(Label target) {
        TileInformation targetTile = (TileInformation)(target.getUserData());
        currentCoord.setText(targetTile.getRow() + "," + targetTile.getCol());
    }


    private void displayDefaultCoord() {
        currentCoord.setText("-");
        axeCoord.setText("26,37");
        boatCoord.setText("12,4");
    }

    private void itemsDisplayOnMap(Label label, int colIndex, int rowIndex) {
        //display boat on top of tile
        if(as.compareCoordinates(rowIndex, colIndex, ShowAxeShip.BOAT)){
            label.setGraphic(new ImageView(as.getEntity(ShowAxeShip.BOAT)));
            tileInfo[rowIndex][colIndex].setEntityType(TileInformation.BOAT);
            itemType = "Boat";
            tmpCoords[2] = rowIndex;
            tmpCoords[3] = colIndex;
            dragSource(label, itemType);
        }
        //display axe on top of tile
        else if(as.compareCoordinates(rowIndex, colIndex, ShowAxeShip.AXE)){
            label.setGraphic(new ImageView(as.getEntity(ShowAxeShip.AXE)));
            tileInfo[rowIndex][colIndex].setEntityType(TileInformation.AXE);
            itemType = "Axe";
            tmpCoords[0] = rowIndex;
            tmpCoords[1] = colIndex;
            dragSource(label, itemType);
        }
        //display player initial position on map
        else if(sp.compareCoordinates(rowIndex, colIndex, EntityDisplay.UNIQUE)){
            label.setGraphic(new ImageView(sp.getEntity(EntityDisplay.UNIQUE)));
            tileInfo[rowIndex][colIndex].setEntityType(TileInformation.PLAYER);
        }
        // display diamonds initial position on map
        else if(sd.compareCoordinates(rowIndex, colIndex, EntityDisplay.UNIQUE)) {
            label.setGraphic(new ImageView(sd.getEntity(EntityDisplay.UNIQUE)));
            tileInfo[rowIndex][colIndex].setEntityType(TileInformation.DIAMOND);
        }

    }


    /** check whether items can be placed
     *
     * @param target
     * @param ti
     */
    private void itemsDropTileCheck(Label target, TileInformation ti) {
        // if the tile has items on it or is blocked, set colour to red
        if (ti.isEntity() || !ti.isNormal()) {
            target.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5)");
            msgBoxChanged.setText("You can't place the item here!");  //tell user that items can't be placed here
        } else {
            target.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
            msgBoxChanged.setText("Use your mouse to move the " +
                    "\naxe and boat on the map. " +
                    "\n\nFunction of buttons on the right:" +
                    "\n1. Play game\n2. Exit game\n3. Reset items position");  //if can, display default message
        }
    }

    /** Set the graphic and data needed if drop succeed
     *
     * @param target
     * @param ti
     */
    private void itemsSuccessDrop(Label target, TileInformation ti) {
        if (!ti.isEntity() && ti.isNormal()) {
            target.setOnDragDropped((DragEvent e) -> {

                Dragboard db = e.getDragboard();
                boolean flag = false;
                if (db.hasContent(DataFormat.IMAGE)) {
                    TileInformation targetTile = (TileInformation)(target.getUserData());

                    target.setGraphic(new ImageView(((Image) db.getContent(DataFormat.IMAGE))));
                    flag = true;
                    //update the new coordinates of axe or boat
                    if(itemType == "Axe"){
                        tmpCoords[0] = targetTile.getRow();
                        tmpCoords[1] = targetTile.getCol();
                    }else if(itemType == "Boat"){
                        tmpCoords[2] = targetTile.getRow();
                        tmpCoords[3] = targetTile.getCol();
                    }
                    saveCoor();
                    updateGridPane();
                }
                e.setDropCompleted(flag);
                e.consume();
            });
        }
    }

    /**
     * Refreshes the GridPane every time a drag and drop action is completed successfully.
     * Saves the changed coordinates of the moved item as well.
     */
    private void updateGridPane() {
        as.getEntityPosition();
        sp.getEntityPosition();
        sd.getEntityPosition();
        tileMapping.getChildren().clear();
        tileInfo = new TileInformation[mp.getNumRows()][mp.getNumCols()];

        for (int row = 0; row < mp.getNumRows(); row++) {
            for (int col = 0; col < mp.getNumCols(); col++) {
                tileInfo[row][col] = new TileInformation(mp.getTileImageFromMap(row, col), row, col);
                addTile(col, row);
            }
        }
    }

    /**
     * Saves the coordinates of all items.
     * Switches the overwrite to true.
     */
    private void saveCoor() {
        WriteCoord.toOverwrite = true;
        as.updateEntityPosition(tmpCoords[0], tmpCoords[1], tmpCoords[2], tmpCoords[3]);
    }

    /**
     * Launched the game if the game has never been launched during the
     * lifecycle of Map Viewer application. If the game is launched at most
     * once, the game will be kept alive throughout the lifecycle of the Map
     * Viewer application. Resources are not released throughout the lifecycle.
     */

    @FXML
    private void resetButton() {
        tmpCoords[0] = 26;
        tmpCoords[1] = 37;
        tmpCoords[2] = 12;
        tmpCoords[3] = 4;
        saveCoor();
        updateGridPane();
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