package com.neet.DiamondHunter.EntityViewer;

/**
 * Axe and boat are coordinates that are required to be updated by the Map Viwer.
 * Adds the updating functionality to the axe and boat.
 *
 */
public abstract class AxeShip implements EntityDisplay{
	
	/**
	 * Gets the updated coordinates and overwrite the Entity-Coordinate.txt file first line with the updated coordinates.
	 * @param ar Axe row index
	 * @param ac Axe column index
	 * @param br Boat row index
	 * @param bc Boat column index
	 */
	public abstract void updateEntityPosition(int ar, int ac, int br, int bc);
	
}