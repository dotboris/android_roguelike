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

		// flags
		// boolean corridor = true;

		// create a first static room
		tFeature.generate(127, 127, Side.NORTH);

		// add it to the features array
		features[0] = tFeature;

		// place the room
		tFeature.place();

		// loop a whole lot
		for (int i = 0; i < LOOP_NUM && fCount < F_MAX; i++) {
			// pick a random feature
			Feature oldFeat = features[rnd.nextInt(fCount)];

			// create a feature
			// if (oldFeat instanceof SquareRoom) {
			// if (!corridor)
			// continue;
			// tFeature = new Corridor(dungeon, rnd, style);
			// } else {
			// if (corridor)
			// continue;
			// tFeature = new SquareRoom(dungeon, rnd, style);
			// }
			if(oldFeat instanceof SquareRoom)
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

				// flip corridor flag
				// corridor = !corridor;
			}
		}

		Log.d("RL", "Generated features: " + fCount);

	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.Generator#genItems()
	 */
	@Override
	protected void genItems() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.Generator#genMobs()
	 */
	@Override
	protected void genMobs() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.Generator#checkDungeon()
	 */
	@Override
	protected boolean checkDungeon() {
		// TODO Auto-generated method stub
		return false;
	}

}
