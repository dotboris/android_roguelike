/*
 * Class Name:			DungeonManager.java
 * Class Purpose:		Manages all the dungeons
 * Created by:			boris on 2011-10-28
 */
package name.bobnet.android.rl.core;

import name.bobnet.android.rl.core.ents.Dungeon;

/**
 * Manages all the dungeon.
 * 
 * <p>
 * This class will:
 * <ul>
 * <li>Determine if a dungeon should be generated or loaded from file</li>
 * <li>Generate Dungeons</li>
 * <li>Save dungeons to file</li>
 * <li>Load dungeons from file</li>
 * </ul>
 * </p>
 * 
 * @author boris
 */
public class DungeonManager {

	// constants
	public static final String D_PATH = "dungeons/";
	public static final String D_PREFIX = "dun_";

	// variables
	private int cLevel;
	private Dungeon cDungeon;
	
	// singleton
	private static DungeonManager dungeonManager;

	/**
	 * Get the {@link DungeonManager} Singleton
	 * 
	 * @return the {@link DungeonManager} singleton
	 */
	public static DungeonManager getDungeonManager() {
		// create the singleton instance
		if (dungeonManager == null)
			dungeonManager = new DungeonManager();

		// return the singleton
		return dungeonManager;
	}

	private DungeonManager() {
		// set the current level
		cLevel = -1;
	}

	/**
	 * Get the dungeon at a specific level
	 * 
	 * @param level
	 *            the level of the dungeon to get
	 * @return the dungeon
	 */
	public Dungeon getDungeon(int level) {
		// TODO: getDungeon() Method
		return null;
	}
}
