/*
 * Class Name:			Dungeon.java
 * Class Purpose:		Dungeon containing the tiles
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core.ents;

import name.bobnet.android.rl.core.ents.tiles.TileType;
import name.bobnet.android.rl.core.message.Message;

public class Dungeon implements Entity {

	// constants
	public final static int D_WIDTH = 80;
	public final static int D_HEIGHT = 80;

	// variables
	private Tile[][] tiles;

	public Dungeon() {
		// create the tiles array
		tiles = new Tile[D_WIDTH][D_HEIGHT];

		// create the tiles
		for (int x = 0; x < D_WIDTH; x++)
			for (int y = 0; y < D_HEIGHT; y++) {
				tiles[x][y] = new Tile(x, y);
			}
	}

	/**
	 * Get the tile relative to the provided one using the provided offset
	 * 
	 * @param t
	 *            The tile that is the starting point
	 * @param offsetX
	 *            the x offset
	 * @param offsetY
	 *            the y offset
	 * @throws IndexOutOfBoundsException
	 *             thrown when the new tile doesn't exist
	 * @throws NullPointerException
	 *             thrown when the origin tile is null
	 */
	public Tile getRelativeTile(Tile t, int offsetX, int offsetY) {
		return tiles[t.getX() + offsetX][t.getY() + offsetY];
	}

	/**
	 * Get a tile with its position
	 * 
	 * @param x
	 *            the x position of the tile
	 * @param y
	 *            the y position of the tile
	 */
	public Tile getTile(int x, int y) {
		return tiles[x][y];
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
	 * @param genFlag
	 *            whether or not to set the generation flag on the affected
	 *            tiles
	 */
	public void fillRect(int x, int y, int w, int h, TileType tileType,
			boolean genFlag) {
		// cycle through the cells
		for (int cx = x; cx < x + w; cx++)
			for (int cy = y; cy < y + h; cy++) {
				// set the tile type of the cell
				this.getTile(cx, cy).setTileType((TileType) tileType.clone());
				
				// set the generation flag
				if (genFlag)
					getTile(cx, cy).setGenUsed(true);
			}
	}

	@Override
	public void tick() {
		// tick all children
		for (int x = 0; x < D_WIDTH; x++)
			for (int y = 0; y < D_HEIGHT; y++)
				tiles[x][y].tick();
	}

	@Override
	public void processMessage(Message message) {
		// send message to all children
		for (int x = 0; x < D_WIDTH; x++)
			for (int y = 0; y < D_HEIGHT; y++)
				tiles[x][y].processMessage(message);

	}

}
