package com.neet.DiamondHunter.EntityViewer;

import com.neet.DiamondHunter.MapViewer.WriteCoord;

import javafx.scene.image.WritableImage;

/**
 * Handles axe and ship entity for the GridPane.
 *
 */
public class ShowAxeShip extends AxeShip {

    private int[] coordinates;

    //items
    public static final int BOAT = 0;
    public static final int AXE = 1;

    public ShowAxeShip() {
        coordinates = new int[4];
        getEntityPosition();
    }

    @Override
    public void getEntityPosition() {
        /*
         * 0 indicates line 0 which is the axe and boat coordinates.
         * Array: axe_xaxis, axe_yaxis, boat_xaxis, boat_yaxis
         */
        coordinates = WriteCoord.getCoord(1);
    }

    @Override
    public WritableImage getEntity(int type) {
        if(type == BOAT){
            return new SpriteLoader().getItems(1, 0);
        }
        else if(type == AXE){
            return new SpriteLoader().getItems(1, 1);
        }

        return null;
    }

    @Override
    public boolean compareCoordinates(int row, int col, int type) {
        if(type == BOAT) {
            return (row == coordinates[2] && col == coordinates[3]) ? true : false;
        }
        else if(type == AXE) {
            return (row == coordinates[0] && col == coordinates[1]) ? true : false;
        }

        return false;
    }

    @Override
    public void updateEntityPosition(int ar, int ac, int br, int bc) {
        coordinates[0] = ar;
        coordinates[1] = ac;
        coordinates[2] = br;
        coordinates[3] = bc;

        String coords = Integer.toString(ar) + "," + Integer.toString(ac) + ","
                + Integer.toString(br) + "," + Integer.toString(bc);
        WriteCoord.overwriteFile(coords,1);
    }

}

