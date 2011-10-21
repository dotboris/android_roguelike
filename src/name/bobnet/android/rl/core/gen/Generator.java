/*
 * Class Name:			Generator.java
 * Class Purpose:		Generates a dungeon
 * Created by:			boris on 2011-10-21
 */
package name.bobnet.android.rl.core.gen;

import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.tiles.TileType;
import name.bobnet.android.rl.core.ents.tiles.TileType.TileStyle;

public abstract class Generator {

	// variables
	protected Dungeon dungeon;
	protected TileStyle style;

	public enum DungeonType {
		STANDARD
	}

	protected Generator(Dungeon dungeon, TileStyle style) {
		// set fields
		this.style = style;
		this.dungeon = dungeon;
	}

	/**
	 * Fills a rectangle in the dungeon with a certain tiletype
	 * 
	 * @param tileType
	 *            the tile to fill the dungeon with
	 * @param x
	 *            the x position of the rectangle
	 * @param y
	 *            the y position of the rectangle
	 * @param w
	 *            the width of the rectangle
	 * @param h
	 *            the height of the rectangle
	 */
	protected void dFillRect(int x, int y, int w, int h, TileType tileType) {
		// cycle through the cells
		for (int cx = x; cx < x + w; cx++)
			for (int cy = y; cy < y + h; cy++) {
				// set the tile type of the cell
				dungeon.getTile(cx, cy)
						.setTileType((TileType) tileType.clone());
			}
	}

	/**
	 * Generate a dungeon given the style and type
	 * 
	 * @param type
	 *            the type of the dungeon
	 * @param style
	 *            the style of the dungeon
	 * @return the generated dungeon
	 */
	public static Dungeon GenerateDungeon(DungeonType type, TileStyle style) {
		// variables
		Generator gen;
		boolean genGood;
		Dungeon dungeon;

		do {
			// create a new dungeon
			dungeon = new Dungeon();

			// determine what kind of dungeon to create
			switch (type) {
			case STANDARD:
			default:
				gen = new StandardGen(dungeon, style);
				break;
			}

			// call the right methods to generate the dungeon
			gen.genFeatures();
			gen.genItems();
			gen.genMobs();

			// check the dungeon
			genGood = gen.checkDungeon();

		} while (genGood);

		// return the generated dungeon
		return dungeon;
	}

	/**
	 * Generate dungeon features (rooms, corridors, etc.)
	 */
	protected abstract void genFeatures();

	/**
	 * generate items laying on the dungeon floor
	 */
	protected abstract void genItems();

	/**
	 * Spawn monsters around the dungeon
	 */
	protected abstract void genMobs();

	/**
	 * Verify that the dungeon is valid (Player can reach all tiles, etc)
	 */
	protected abstract boolean checkDungeon();

}
