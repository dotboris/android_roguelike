/*
 * Class Name:			StandardGen.java
 * Class Purpose:		Generate a standard dungeon (rooms linked by corridors)
 * Created by:			boris on 2011-10-21
 */
package name.bobnet.android.rl.core.gen;

import android.util.Log;
import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.tiles.TileType.TileStyle;
import name.bobnet.android.rl.core.gen.feat.Corridor;
import name.bobnet.android.rl.core.gen.feat.Feature;
import name.bobnet.android.rl.core.gen.feat.SquareRoom;
import name.bobnet.android.rl.core.gen.feat.Feature.Side;

public class StandardGen extends Generator {

	// constants
	public static final int F_MAX = 100;
	public static final int LOOP_NUM = 1000;
	public static final int I_MAX = 100;
	public static final int C_MAX = 150;

	// variables
	private Feature features[];

	public StandardGen(Dungeon dungeon, TileStyle style) {
		super(dungeon, style);
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.Generator#genFeatures()
	 */
	@Override
	protected void genFeatures() {
		// create the features array
		features = new Feature[F_MAX];

		// hold a temp feature
		Feature tFeature = new SquareRoom(dungeon, rnd, style);

		// feature count
		int fCount = 1;

		// create a first static room
		tFeature.generate(Dungeon.D_WIDTH / 2, Dungeon.D_HEIGHT / 2, Side.NORTH);

		// add it to the features array
		features[0] = tFeature;

		// place the room
		tFeature.place();

		// loop a whole lot
		for (int i = 0; i < LOOP_NUM && fCount < F_MAX; i++) {
			// pick a random feature
			Feature oldFeat = features[rnd.nextInt(fCount)];

			// determine what feature to place
			if (oldFeat instanceof SquareRoom)
				tFeature = new Corridor(dungeon, rnd, style);
			else if (rnd.nextInt(101) > 25) {
				tFeature = new SquareRoom(dungeon, rnd, style);
			} else
				tFeature = new Corridor(dungeon, rnd, style);

			// pick random position and side
			Side rSide = Side.values()[rnd.nextInt(4)];

			// determine the opposite side
			Side oSide = Side.NORTH;
			switch (rSide) {
			case NORTH:
				oSide = Side.SOUTH;
				break;
			case SOUTH:
				oSide = Side.NORTH;
				break;
			case EAST:
				oSide = Side.WEST;
				break;
			case WEST:
				oSide = Side.EAST;
				break;
			}

			// get the position
			int x = oldFeat.getRndWallX(oSide);
			int y = oldFeat.getRndWallY(oSide);

			// generate the feature
			tFeature.generate(x, y, rSide);

			// check if the feature can be placed
			if (tFeature.check()) {
				// add it to the features array
				features[fCount] = tFeature;

				// update counter
				fCount++;

				// place the feature
				tFeature.place();
			}
		}

		Log.d("RL", "Generated features: " + fCount);

	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.Generator#genItems()
	 */
	@Override
	protected void genItems() {
		// variables
		int itemCount = 0;

		// tell all the features to gen items
		for (Feature f : features) {
			if (f != null) {
				// create items
				itemCount += f.genItems(itemCount, I_MAX);

				// check if we hit the limit
				if (itemCount >= I_MAX)
					break;
			} else
				// we started hitting nulls, give up
				break;
		}
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.Generator#genMobs()
	 */
	@Override
	protected void genMobs() {
		// variables
		int creatureCount = 0;

		// tell all the features to gen creatures
		for (Feature f : features) {
			if (f != null) {
				// create creatures
				creatureCount += f.genCreatures(creatureCount, C_MAX);

				// check if we hit the limit
				if (creatureCount >= C_MAX)
					break;
			} else
				// we started hitting nulls, give up
				break;
		}
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.Generator#checkDungeon()
	 */
	@Override
	protected boolean checkDungeon() {
		// It should always be good
		return true;
	}

}
