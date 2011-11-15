/*
 * Class Name:			Feature.java
 * Class Purpose:		A dungeon feature
 * Created by:			boris on 2011-10-21
 */
package name.bobnet.android.rl.core.gen.feat;

import java.util.Random;

import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.tiles.TileType.TileStyle;

public abstract class Feature {

	// variables
	protected Dungeon d;
	protected Random rnd;
	protected TileStyle style;

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
	 * @param rnd
	 *            the random number generator used to generate the dungeon
	 * @param style
	 *            the style of the feature
	 */
	public Feature(Dungeon d, Random rnd, TileStyle style) {
		this.d = d;
		this.rnd = rnd;
		this.style = style;
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
	 * Generate the items in the feature
	 * 
	 * @param itemNum
	 *            the current count of items
	 * @param itemMax
	 *            the maximum number of items
	 * @return the number of items created
	 */
	public abstract int genItems(int itemNum, int itemMax);

	/**
	 * Generate the creatures in the feature
	 * 
	 * @param creatureNum
	 *            the current count of creatures
	 * @param creatureMax
	 *            the maximum number of creatures
	 * @return the number of creatures created
	 */
	public abstract int genCreatures(int creatureNum, int creatureMax);

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

	/**
	 * Get a random x coordinate for a wall
	 * 
	 * @param side
	 *            the side of the wall
	 * @return the random x value of a point on said side
	 */
	public abstract int getRndWallX(Side side);

	/**
	 * Get a random y coordinate for a wall
	 * 
	 * @param side
	 *            the side of the wall
	 * @return the random y value of a point on said side
	 */
	public abstract int getRndWallY(Side side);

}
