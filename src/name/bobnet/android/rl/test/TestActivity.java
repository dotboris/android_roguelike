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

	private GameEngine engine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("RL", "Creating engine");
		
		// create an engine object
		engine = GameEngine.getEngine();
		
		Log.d("RL", "Created Engine: " + engine.toString());

		engine.init(getResources());
		
		Log.d("RL", "Inited Engine");
		
		setContentView(new TestView(this));
		
		Log.d("RL", "loaded the test view");
	}
	
}
