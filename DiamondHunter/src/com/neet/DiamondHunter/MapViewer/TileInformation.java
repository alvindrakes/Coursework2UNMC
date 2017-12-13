package com.neet.DiamondHunter.MapViewer;

public class TileInformation {

    private int tileImageType;
    private boolean isEntity;
    private boolean isNormal;
    private int entityType;

    //tile coordinate
    private int row;
    private int col;

    //tile type
    public static final boolean NORMAL = true;
    public static final boolean BLOCKED = false;

    //tile image, obstacles
    public static final int GREENTREE = 20;
    public static final int DEADTREE = 21;
    public static final int WATER = 22;

    //tile image, walkable
    public static final int GRASS = 1;
    public static final int BUSH = 2;
    public static final int FLOWER = 3;

    //Type of entity on tile
    public static final int NOENTITY = -1;
    public static final int BOAT = 0;
    public static final int AXE = 1;
    public static final int DIAMOND = 2;
    public static final int PLAYER = 3;

    public TileInformation(int tileImageType, int row, int col) {
        this.tileImageType = tileImageType;
        this.isNormal = (tileImageType == GRASS || tileImageType == BUSH || tileImageType == FLOWER) ? true : false;
        this.row = row;
        this.col = col;
        this.entityType = NOENTITY;
        this.isEntity = false;
    }

    public TileInformation(int tileImageType, int row, int col, int entityType) {
        this.tileImageType = tileImageType;
        this.isNormal = (tileImageType == GRASS || tileImageType == BUSH || tileImageType == FLOWER) ? true : false;
        this.row = row;
        this.col = col;
        this.entityType = entityType;
        this.isEntity = (entityType != -1) ? true : false;
    }

    private void setEntity(boolean isEntity) {
        this.isEntity = isEntity;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
        setEntity((this.entityType != -1) ? true : false);
    }

    public boolean isEntity() {
        return isEntity;
    }

    public int getTileImageType() {
        return tileImageType;
    }

    public boolean isNormal() {
        return isNormal;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getEntityType() {
        return entityType;
    }


}
