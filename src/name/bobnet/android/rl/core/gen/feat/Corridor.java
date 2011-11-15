package name.bobnet.android.rl.core.gen.feat;

import java.util.Random;

import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.tiles.Floor;
import name.bobnet.android.rl.core.ents.tiles.TileType.TileStyle;

/**
 * Creates a hallway/corridor
 * 
 * @author boris
 */
public class Corridor extends Feature {

	// constants
	public static final int L_MIN = 3;
	public static final int L_MAX = 10;

	// variables
	private int x, y, l;
	private boolean vert;

	public Corridor(Dungeon d, Random rnd, TileStyle style) {
		super(d, rnd, style);
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.feat.Feature#generate(int, int, name.bobnet.android.rl.core.gen.feat.Feature.Side)
	 */
	@Override
	public void generate(int x, int y, Side s) {
		// generate random length
		l = L_MIN + rnd.nextInt(L_MAX - L_MIN);

		// determine x, y and vert
		switch (s) {
		case NORTH:
			this.x = x;
			this.y = y;
			vert = true;
			break;
		case SOUTH:
			this.x = x;
			this.y = y - (l - 1);
			vert = true;
			break;
		case EAST:
			this.x = x - (l - 1);
			this.y = y;
			vert = false;
			break;
		case WEST:
			this.x = x;
			this.y = y;
			vert = false;
			break;
		}

	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.feat.Feature#check()
	 */
	@Override
	public boolean check() {
		try {
			// check the line
			for (int j = -1; j <= 1; j++)
				for (int i = vert ? y : x; i <= (vert ? y : x) + (l - 1); i++)
					if (d.getTile(vert ? x + j : i, vert ? i : y + j)
							.isGenUsed())
						return false;
		} catch (Exception e) {
			// probably out of bounds
			return false;
		}

		// all were good
		return true;
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.feat.Feature#place()
	 */
	@Override
	public void place() {
		d.fillRect(x, y, vert ? 1 : l, vert ? l : 1, new Floor(style), true);
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.feat.Feature#getRndWallX(name.bobnet.android.rl.core.gen.feat.Feature.Side)
	 */
	@Override
	public int getRndWallX(Side side) {
		if (vert) {
			// vertical line
			switch (side) {
			case NORTH:
			case SOUTH:
				return x;
			case EAST:
				return x + 1;
			case WEST:
				return x - 1;
			}
		} else {
			// horizontal line
			switch (side) {
			case NORTH:
			case SOUTH:
				return x + rnd.nextInt(l);
			case EAST:
				return x + l;
			case WEST:
				return x - 1;
			}
		}

		return -1;
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.feat.Feature#getRndWallY(name.bobnet.android.rl.core.gen.feat.Feature.Side)
	 */
	@Override
	public int getRndWallY(Side side) {
		if (vert) {
			// vertical line
			switch (side) {
			case NORTH:
				return y - 1;
			case SOUTH:
				return y + l;
			case EAST:
			case WEST:
				return y + rnd.nextInt(l);
			}
		} else {
			// horizontal line
			switch (side) {
			case NORTH:
				return y + 1;
			case SOUTH:
				return y - 1;
			case EAST:
			case WEST:
				return y;
			}
		}

		return -1;
	}

	/**
	 * @return the vert
	 */
	public boolean isVert() {
		return vert;
	}

	@Override
	public int genItems(int itemNum, int itemMax) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int genCreatures(int creatureNum, int creatureMax) {
		// TODO Auto-generated method stub
		return 0;
	}
}
