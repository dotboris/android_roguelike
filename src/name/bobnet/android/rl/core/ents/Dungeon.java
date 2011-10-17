/*
 * Class Name:			Dungeon.java
 * Class Purpose:		Dungeon containing the tiles
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core.ents;

import name.bobnet.android.rl.core.message.Message;

public class Dungeon implements Entity {

	// constants
	public final static int D_WIDTH = 255;
	public final static int D_HEIGHT = 255;

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
