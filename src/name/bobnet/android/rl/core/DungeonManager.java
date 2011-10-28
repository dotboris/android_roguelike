/*
 * Class Name:			DungeonManager.java
 * Class Purpose:		Manages all the dungeons
 * Created by:			boris on 2011-10-28
 */
package name.bobnet.android.rl.core;

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
		// TODO: DungeonManager constructor
	}
}
