package name.bobnet.android.rl.core.gen.feat;

import java.util.Random;

import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.tiles.Floor;
import name.bobnet.android.rl.core.ents.tiles.TileType.TileStyle;

/**
 * Basic square room
 * 
 * @author boris
 */
public class SquareRoom extends Feature {

	// constants
	public static final int MAX_WIDTH = 50;
	public static final int MIN_WIDTH = 2;
	public static final int MAX_HEIGHT = 50;
	public static final int MIN_HEIGHT = 2;

	// variables
	private int x1, x2, y1, y2;

	public SquareRoom(Dungeon d, Random rnd, TileStyle style) {
		super(d, rnd, style);
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.feat.Feature#generate(int, int, name.bobnet.android.rl.core.gen.feat.Feature.Side)
	 */
	@Override
	public void generate(int x, int y, Side s) {
		// variables
		int w, h;

		// determine the width and height
		w = MIN_WIDTH + rnd.nextInt(MAX_WIDTH - MIN_WIDTH + 1);
		h = MIN_HEIGHT + rnd.nextInt(MAX_HEIGHT - MIN_HEIGHT + 1);

		// determine the x and y coordinates
		switch (s) {
		case NORTH:
			x1 = x - w / 2;
			x2 = x + w / 2;
			y1 = y;
			y2 = y + h;
			break;
		case SOUTH:
			x1 = x - w / 2;
			x2 = x + w / 2;
			y1 = y - h;
			y2 = y;
			break;
		case EAST:
			x1 = x - w;
			x2 = x;
			y1 = y - h / 2;
			y2 = y + h / 2;
			break;
		case WEST:
			x1 = x;
			x2 = x + w;
			y1 = y - h / 2;
			y2 = y + h / 2;
			break;
		}
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.feat.Feature#check()
	 */
	@Override
	public boolean check() {
		try {
			// check all the tiles to see if the generator used it
			for (int x = x1; x <= x2; x++)
				for (int y = y1; y <= y2; y++) {
					if (d.getTile(x, y).isGenUsed()) {
						// it's not a wall get out
						return false;
					}
				}
		} catch (Exception e) {
			// some error, probably out of bounds we can't place the feature
			return false;
		}

		// all unused we're good
		return true;
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.feat.Feature#place()
	 */
	@Override
	public void place() {
		// place all the floor tiles
		d.fillRect(x1, y1, x2 - x1, y2 - y1, new Floor(style), true);
	}

	@Override
	public int getRndWallX(Side side) {
		switch (side) {
		case NORTH:
		case SOUTH:
			return rnd.nextInt(x2 - x1 + 1) + x1;
		case WEST:
			return x1 - 1;
		case EAST:
			return x2 + 1;
		default:
			return -1;
		}
	}

	@Override
	public int getRndWallY(Side side) {
		switch (side) {
		case NORTH:
			return y1 - 1;
		case SOUTH:
			return y2 + 1;
		case WEST:
		case EAST:
			return rnd.nextInt(y2 - y1 + 1) + y1;
		default:
			return -1;
		}
	}
}
