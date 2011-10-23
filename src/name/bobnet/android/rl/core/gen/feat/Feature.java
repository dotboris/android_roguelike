/*
 * Class Name:			Feature.java
 * Class Purpose:		A dungeon feature
 * Created by:			boris on 2011-10-21
 */
package name.bobnet.android.rl.core.gen.feat;

import name.bobnet.android.rl.core.ents.Dungeon;

public abstract class Feature {

	// variables
	protected Dungeon d;

	/**
	 * The possible sides of a feature
	 * 
	 * @author boris
	 */
	public enum Side {
		NORTH, SOUTH, EAST, WEST
	}

	/**
	 * Create the feature with a dungeon to work with
	 * 
	 * @param d
	 *            the dungeon to work with
	 */
	public Feature(Dungeon d) {
		this.d = d;
	}

	/**
	 * Generate the random feature with random dimensions
	 * 
	 * The generator doesn't whether or not the feature will fit in the dungeon
	 * 
	 * @param x
	 *            the x coordinate of the room entrance
	 * @param y
	 *            the y coordinate of the room entrance
	 * @param s
	 *            what side of the room the entrance is on
	 */
	public abstract void generate(int x, int y, Side s);

	/**
	 * Determine if the room can be placed inside the dungeon
	 * 
	 * @return true if the feature can be placed false otherwise
	 */
	public abstract boolean check();

	/**
	 * Place the feature in the dungeon
	 */
	public abstract void place();

}
