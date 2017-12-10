package com.neet.DiamondHunter.EntityViewer;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.neet.DiamondHunter.Manager.Content;

import javafx.scene.image.WritableImage;

public class SpriteLoader {
	private WritableImage[][] player = load("/Sprites/playersprites.gif", 16, 16);
	private WritableImage[][] diamond = load("/Sprites/diamond.gif", 16, 16);

	private WritableImage[][] items = load("/Sprites/items.gif", 16, 16);

	private WritableImage[][] load(String s, int w, int h) {
		WritableImage[][] sprite;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			sprite = new WritableImage[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					sprite[i][j] = new ImageConversion(spritesheet.getSubimage(j * w, i * h, w, h)).getWrImg();
				}
			}
			return sprite;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public WritableImage getPlayer(int col, int row) {
		return player[col][row];
	}

	public WritableImage getDiamond(int col, int row) {
		return diamond[col][row];
	}

	public WritableImage getItems(int col, int row) {
		return items[col][row];
	}
}
