/*
 * Class Name:			StandardGen.java
 * Class Purpose:		Generate a standard dungeon (rooms linked by corridors)
 * Created by:			boris on 2011-10-21
 */
package name.bobnet.android.rl.core.gen;

import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.tiles.TileType.TileStyle;

public class StandardGen extends Generator {

	public StandardGen(Dungeon dungeon, TileStyle style) {
		super(dungeon, style);
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.gen.Generator#genFeatures()
	 */
	@Override
	protected void genFeatures() {
		// TODO Auto-generated method stub

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
