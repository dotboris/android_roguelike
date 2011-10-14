/*
 * Class Name:			GameEngine.java
 * Class Purpose:		Basic game engine handling most game operations
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core;

import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.tiles.Floor;
import name.bobnet.android.rl.core.ents.tiles.TileType.TileStyle;
import name.bobnet.android.rl.core.message.MessageManager;

public class GameEngine {

	// engine singleton
	private static GameEngine engine;

	// variables
	private MessageManager messageManager;
	private Dungeon currentDungeon;

	private GameEngine() {
		// create a message manager
		messageManager = new MessageManager();
	}

	/**
	 * Starts the game
	 */
	public void init() {
		// TODO: Init engine

		// create a new dungeon (for testing)
		currentDungeon = new Dungeon();

		// set a few floor tiles
		for (int i = 50; i < 100; i++)
			for (int j = 50; j < 100; j++)
				currentDungeon.getTile(i, j).setTileType(
						new Floor(TileStyle.ROCK));
	}

	/**
	 * @return the currentDungeon
	 */
	public Dungeon getCurrentDungeon() {
		return currentDungeon;
	}

	/**
	 * @return the game engine singleton
	 */
	public static GameEngine getEngine() {
		if (engine == null)
			engine = new GameEngine();

		return engine;
	}

}
