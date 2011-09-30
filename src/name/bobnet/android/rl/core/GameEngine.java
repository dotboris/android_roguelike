/*
 * Class Name:			GameEngine.java
 * Class Purpose:		Basic game engine handling most game operations
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core;

public class GameEngine {
	
	// engine singleton
	private static GameEngine engine;
	
	public static GameEngine getEngine() {
		if (engine == null)
			engine = new GameEngine();
		
		return engine;
	}

}
