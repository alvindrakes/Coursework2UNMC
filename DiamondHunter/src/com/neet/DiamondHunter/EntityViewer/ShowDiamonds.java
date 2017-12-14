package com.neet.DiamondHunter.EntityViewer;

import com.neet.DiamondHunter.MapViewer.WriteCoord;

import javafx.scene.image.WritableImage;

/**
 * Handles diamond entity in the GridPane
 *
 */
public class ShowDiamonds implements EntityDisplay{

    private int[] coordinate;
    private static int coordCount;

    public ShowDiamonds(){
        getEntityPosition();
    }

    @Override
    public void getEntityPosition() {
        //3 indicates line 3 which is the Diamonds's coordinate
        coordinate = WriteCoord.getCoord(3);
        coordCount = 0;
    }

    @Override
    public WritableImage getEntity(int type) {
        return new SpriteLoader().getDiamond(0, 0);
    }

    @Override
    public boolean compareCoordinates(int row, int col, int type) {
        if(coordCount < coordinate.length - 1) {
            for(int i = 0; i < coordinate.length; i += 2) {
                if(row == coordinate[i] && col == coordinate[i + 1]) {
                    coordCount += 2;
                    return true;
                }
            }
        }
        return false;
    }

    public void updateEntityPosition() {
        String coords = Integer.toString(coordinate[0]) + "," + Integer.toString(coordinate[1]);
        WriteCoord.overwriteFile(coords,2);
    }

}
