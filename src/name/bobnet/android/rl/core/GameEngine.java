/*
 * Class Name:			GameEngine.java
 * Class Purpose:		Basic game engine handling most game operations
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core;

import name.bobnet.android.rl.core.message.MessageManager;

public class GameEngine {
	
	// engine singleton
	private static GameEngine engine;
	
	// variables
	private MessageManager messageManager;
	
	private GameEngine() {
		// create a message manager
		messageManager = new MessageManager();
	}
	
	/**
	 * Starts the game
	 */
	public void init() {
		// TODO: Init engine
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
