package name.bobnet.android.rl.test;

import name.bobnet.android.rl.core.GameEngine;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Activity used to test the engine
 * 
 * @author boris
 */
public class TestActivity extends Activity {

	GameEngine engine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// create an engine object
		engine = GameEngine.getEngine();
		
		Log.d("RL", "Created Enggine: " + engine.toString());
		
		engine.init();
		
		Log.d("RL", "Inited Engine");
	}
	
}
