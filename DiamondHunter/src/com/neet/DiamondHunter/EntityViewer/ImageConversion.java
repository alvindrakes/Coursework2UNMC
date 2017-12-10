package com.neet.DiamondHunter.EntityViewer;

import java.awt.image.BufferedImage;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * Utility class to load image into Map Viewer application.
 * This class is able to convert BufferedImage or WritableImage to output in SwingJavaFX hybrid or vanilla JavaFX.
 * A list of images or a single image is also supported.
 * This class is generally used to process BufferedImage to JavaFX and vice versa.
 *
 */
public class ImageConversion {

	private BufferedImage buffImg;
	
	/**
	 * Single BufferedImage conversion constructor
	 * @param buffImg The BufferedImage to be processed
	 */
	public ImageConversion(BufferedImage buffImg) {
		this.buffImg = buffImg;
	}
	
	/**
	 * Converts BufferedImage into WritableImage
	 * @return WritableImage
	 */
	public WritableImage getWrImg() {
		WritableImage wrImg = null;
		if (buffImg != null) {
			wrImg = new WritableImage(buffImg.getWidth(), buffImg.getHeight());
			PixelWriter pw1 = wrImg.getPixelWriter();
			for (int x = 0; x < buffImg.getWidth(); x++) {
				for (int y = 0; y < buffImg.getHeight(); y++) {
					pw1.setArgb(x, y, buffImg.getRGB(x, y));
				}
			}
		}
		return wrImg;
	}

}
