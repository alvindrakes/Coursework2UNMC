package com.neet.DiamondHunter.EntityViewer;

import javafx.scene.image.WritableImage;

/**
 * Displays any entity that are shown in the GridPane.
 * An entity can be axe, boat, diamonds or player.
 * @see com.neet.DiamondHunter.Entity
 *
 */
public interface EntityDisplay {
	
	int UNIQUE = -1;
	
	/**
	 * Retrieves the entity position from the text file generated.
	 * @see com.neet.DiamondHunter.MapViewer.WriteCoord#getCoord(int)
	 */
	void getEntityPosition();
	
	/**
	 * Draws the entity and returns the image.
	 * @param type The entity flag. Defaults to -1 if entity is unique.
	 * @return The image of the entity
	 */
	WritableImage getEntity(int type);
	
	/**
	 * Compare the coordinates of the current GridPane index with the entity coordinates.
	 * This must be called after {@link com.neet.DiamondHunter.EntityViewer.EntityDisplay#getEntityPosition()}.
	 * @param row The row index of the GridPane
	 * @param col The column index of the GridPane
	 * @param type The entity flag. Defaults to -1 if entity is unique.
	 * @return True if the entity is to be drawn on the (col, row) of GridPane. False otherwise.
	 */
	boolean compareCoordinates(int row, int col, int type);
	
	
}
