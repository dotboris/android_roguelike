package name.bobnet.android.rl.core.gen.feat;

import java.util.Random;

import name.bobnet.android.rl.core.ents.Creature;
import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.Item;
import name.bobnet.android.rl.core.ents.factory.ContentLoader;
import name.bobnet.android.rl.core.ents.factory.EntityFactory;
import name.bobnet.android.rl.core.ents.tiles.Floor;
import name.bobnet.android.rl.core.ents.tiles.TileType.TileStyle;

/**
 * Basic square room
 * 
 * @author boris
 */
public class SquareRoom extends Feature {

	// constants
	public static final int MAX_WIDTH = 10;
	public static final int MIN_WIDTH = 3;
	public static final int MAX_HEIGHT = 10;
	public static final int MIN_HEIGHT = 3;
	public static final int I_MAX = 8;
	public static final int C_MAX = 4;

	// variables
	private int x1, x2, y1, y2, w, h;
	private Side side;

	public SquareRoom(Dungeon d, Random rnd, TileStyle style) {
		super(d, rnd, style);
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.feat.Feature#generate(int, int, name.bobnet.android.rl.core.gen.feat.Feature.Side)
	 */
	@Override
	public void generate(int x, int y, Side s) {
		// set the side
		this.side = s;

		// determine the width and height
		w = MIN_WIDTH + rnd.nextInt(MAX_WIDTH - MIN_WIDTH);
		h = MIN_HEIGHT + rnd.nextInt(MAX_HEIGHT - MIN_HEIGHT);

		// determine the x and y coordinates
		switch (s) {
		case NORTH:
			x1 = x - (w - 1) / 2;
			x2 = x + (w - 1) / 2;
			y1 = y;
			y2 = y + (h - 1);
			break;
		case SOUTH:
			x1 = x - (w - 1) / 2;
			x2 = x + (w - 1) / 2;
			y1 = y - (h - 1);
			y2 = y;
			break;
		case EAST:
			x1 = x - (w - 1);
			x2 = x;
			y1 = y - (h - 1) / 2;
			y2 = y + (h - 1) / 2;
			break;
		case WEST:
			x1 = x;
			x2 = x + (w - 1);
			y1 = y - (h - 1) / 2;
			y2 = y + (h - 1) / 2;
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
			for (int x = x1 - (side == Side.WEST ? 0 : 1); x <= x2
					+ (side == Side.EAST ? 0 : 1); x++)
				for (int y = y1 - (side == Side.NORTH ? 0 : 1); y <= y2
						+ (side == Side.SOUTH ? 0 : 1); y++) {
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
		d.fillRect(x1, y1, w, h, new Floor(style), true);
	}

	@Override
	public int getRndWallX(Side side) {
		switch (side) {
		case NORTH:
		case SOUTH:
			return rnd.nextInt(w) + x1;
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
			return rnd.nextInt(h) + y1;
		default:
			return -1;
		}
	}

	@Override
	public int genItems(int itemNum, int itemMax) {
		// variable
		int count = 0;

		// determine how many items we make
		for (int i = 0; i < rnd.nextInt(I_MAX + 1); i++) {
			// check if we can place a creature
			if (itemNum + count < itemMax) {
				// pick a random position
				int x = rnd.nextInt(w) + x1;
				int y = rnd.nextInt(h) + y1;

				// get a random creature
				Item item = (Item) EntityFactory.getEntityFactory()
						.getRndEntity(ContentLoader.P_ITEMS, rnd);

				// check if we actually got a creature
				if (item != null) {
					// place it
					d.getTile(x, y).addItem(item);

					// update the counter
					count++;
				}

			}
		}

		// return the count
		return count;
	}

	@Override
	public int genCreatures(int creatureNum, int creatureMax) {
		// variable
		int count = 0;

		// determine how many creatures we make
		for (int i = 0; i < rnd.nextInt(C_MAX + 1); i++) {
			// check if we can place a creature
			if (creatureNum + count < creatureMax) {
				// pick a random position
				int x = rnd.nextInt(w) + x1;
				int y = rnd.nextInt(h) + y1;

				// check if we can place it
				if (d.getTile(x, y).getMob() == null) {
					// get a random creature
					Creature c = (Creature) EntityFactory.getEntityFactory()
							.getRndEntity(ContentLoader.P_CREATURES, rnd);

					// check if we actually got a creature
					if (c != null) {
						// place it
						d.getTile(x, y).setMob(c);

						// update the counter
						count++;
					}
				}
			}
		}

		// return the count
		return count;
	}
}
