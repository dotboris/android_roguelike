/*
 * Class Name:			Dungeon.java
 * Class Purpose:		Dungeon containing the tiles
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core.ents;

public class Dungeon extends Entity {

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
				tiles[x][y] = new Tile();
			}
	}

	@Override
	public void tick() {
		// tick all children
		for (int x = 0; x < D_WIDTH; x++)
			for (int y = 0; y < D_HEIGHT; y++)  
				tiles[x][y].tick();
	}

}
