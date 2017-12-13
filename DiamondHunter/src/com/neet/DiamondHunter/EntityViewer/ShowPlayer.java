package com.neet.DiamondHunter.EntityViewer;

import com.neet.DiamondHunter.MapViewer.WriteCoord;

import javafx.scene.image.WritableImage;

/**
 * Handles player entity in the GridPane
 *
 */
public class ShowPlayer implements EntityDisplay {

    private int[] coordinate;

    public ShowPlayer(){
        coordinate = new int[2];
        getEntityPosition();
//        updateEntityPosition();
    }

    @Override
    public void getEntityPosition() {
        //2 indicates line 2 which is the player's coordinate
        coordinate = WriteCoord.getCoord(2);
    }

    @Override
    public WritableImage getEntity(int type) {
        return new SpriteLoader().getPlayer(0, 0);
    }

    @Override
    public boolean compareCoordinates(int row, int col, int type) {
        return (row == coordinate[0] && col == coordinate[1]) ? true : false;
    }

    public void updateEntityPosition() {
        String coords = Integer.toString(coordinate[0]) + "," + Integer.toString(coordinate[1]);
        WriteCoord.overwriteFile(coords,1);
    }

}