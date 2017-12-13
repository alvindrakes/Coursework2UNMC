package com.neet.DiamondHunter.MapViewer;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.neet.DiamondHunter.EntityViewer.ImageConversion;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;

/**
 * Loads the tiles and maps to be drawn onto the map canvas.
 * Class specific to handle the game map only.
 *
 */
public class MapPane {

    // position
    private int x;
    private int y;

    // map
    private int[][] map;

    private int tileSize;
    private int numRows;
    private int numCols;

    // tileset
    private BufferedImage tileset;
    private int numTilesAcross;
    private WritableImage[][] tiles;

    // dimensions
    // HEIGHT is the playing area size
    public static final int WIDTH = 640;
    public static final int HEIGHT = 640;

    public MapPane() {
        //The number of pixels of each tile
        tileSize = 16;
    }

    /**
     * Load the tiles and store a reference over each tile.
     * @param s The tile image URI
     */
    public void loadTiles(String s) {
        try {
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            numTilesAcross = tileset.getWidth() / tileSize;
            tiles = new WritableImage[2][numTilesAcross];
            BufferedImage subimage1, subimage2;
            for (int col = 0; col < numTilesAcross; col++) {
                subimage1 = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
                subimage2 = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
                if (subimage1 != null && subimage2 != null) {
                    tiles[0][col] = new ImageConversion(subimage1).getWrImg();;
                    tiles[1][col] = new ImageConversion(subimage2).getWrImg();;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadMap(String s) {
        try {

            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());
            map = new int[numRows][numCols];
            for (int row = 0; row < numRows; row++) {
                String line = br.readLine();
                if(line != null) {
                    String[] tokens = line.split("\\s+");
                    for(int col = 0; col < numCols; col++) {
                        map[row][col] = Integer.parseInt(tokens[col]);
                    }
                }
                else {
                    System.err.println("Invalid or no URI input. Terminating program...");
                    System.exit(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the map based on the mapping data loaded from {@link com.neet.DiamondHunter.MapViewer.MapPane#loadMap(String)}.
     * @param gc The graphic context of the canvas.
     */
    public void drawImage(GraphicsContext gc) {

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (map[row][col] == 0)
                    continue;

                int rc = map[row][col];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;
                gc.save();
                gc.drawImage(tiles[r][c], x + col * tileSize, y + row * tileSize);
            }
        }
    }

    /**
     * Retrieves the size of each tile.
     * Used to uniform the GridPane layout.
     * @return The number of pixels of each tile
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * @return Number of rows of the map
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * @return Number of columns of the map
     */
    public int getNumCols() {
        return numCols;
    }

    public int getTileImageFromMap(int r, int c) {
        return map[r][c];
    }
}
